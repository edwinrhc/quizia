package com.erhcdev.quizia.controller;

import com.erhcdev.quizia.dto.QuestionDto;
import com.erhcdev.quizia.dto.QuestionView;
import com.erhcdev.quizia.model.QuizRequest;
import com.erhcdev.quizia.service.AiService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/quiz")
@RequiredArgsConstructor
public class QuizController {


    private final AiService aiService;

    private int letterToIndex(String letra) {
        return switch (letra == null ? "" : letra.trim().toUpperCase()) {
            case "A" -> 0; case "B" -> 1; case "C" -> 2; case "D" -> 3; default -> -1;
        };
    }

    private String cleanOption(String s) {
        // quita prefijos tipo "A) ", "B. ", etc.
        return s == null ? "" : s.replaceFirst("^[A-D][)\\.]\\s*", "").trim();
    }

    @PostMapping("/generate")
    public ResponseEntity<List<QuestionView>> generateQuiz(@RequestBody QuizRequest request) {
        // 1) Pídele a la IA la lista de QuestionDto
        List<QuestionDto> raw = aiService.generateQuiz(request.getTopic(), request.getQuantity());

        // 2) Normaliza
        List<QuestionView> view = raw.stream().map(q -> {
            List<String> opcionesLimpias = q.getOpciones() == null ? List.of() :
                    q.getOpciones().stream()
                            .map(this::cleanOption)
                            .limit(4) // garantiza 4
                            .toList();

            // si por error vinieran menos de 4, rellena para no romper el front (opcional)
            while (opcionesLimpias.size() < 4) {
                opcionesLimpias = new java.util.ArrayList<>(opcionesLimpias);
                opcionesLimpias.add("N/A");
            }

            String letter = (q.getRespuesta_correcta() == null ? "A" : q.getRespuesta_correcta().trim().toUpperCase());
            int idx = letterToIndex(letter);
            if (idx < 0 || idx >= 4) idx = 0; // fallback defensivo

            return new QuestionView(q.getPregunta(), opcionesLimpias, letter, idx);
        }).toList();

        return ResponseEntity.ok(view);
    }


}
