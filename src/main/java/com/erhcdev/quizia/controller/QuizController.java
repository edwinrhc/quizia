package com.erhcdev.quizia.controller;

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

    @PostMapping("/generate")
    public ResponseEntity<List<Map<String, Object>>> generateQuiz(@RequestBody QuizRequest request){

        String response = aiService.generateQuiz(request.getTopic(), request.getQuantity());

        // Convertimos el texto JSON a una lista de mapas
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> preguntas = new ArrayList<>();

        try{
            preguntas = mapper.readValue(response, new TypeReference<List<Map<String, Object>>>() {});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(preguntas);
    }


}
