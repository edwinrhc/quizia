package com.erhcdev.quizia.controller;

import com.erhcdev.quizia.model.QuizRequest;
import com.erhcdev.quizia.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/quiz")
@RequiredArgsConstructor
public class QuizController {

    private final AiService aiService;

    @PostMapping("/generate")
    public ResponseEntity<String> generateQuiz(@RequestBody QuizRequest request){
        String response = aiService.generateQuiz(request.getTopic(), request.getQuantity());
        return ResponseEntity.ok(response);
    }

}
