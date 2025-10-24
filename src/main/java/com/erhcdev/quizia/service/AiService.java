package com.erhcdev.quizia.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AiService {

    @Value("${ai.api.key}")
    private String apiKey;

    @Value("${ai.api.url}")
    private String apiUrl;

    private final WebClient webClient = WebClient.create();

    public String generateQuiz(String topic, int numQuestions){
        String prompt = String.format("""
            Genera %d preguntas tipo test sobre %s.
            Devuelve un JSON con la estructura:
            [
              {"pregunta": "...", "opciones": ["A","B","C","D"], "respuesta_correcta": "B"}
            ]
        """, numQuestions, topic);

        Map<String, Object> body = Map.of(
                "contents", List.of(Map.of(
                        "parts", List.of(Map.of("text", prompt)))
                )
        );

        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(apiUrl)
                        .queryParam("key", apiKey)
                        .build())
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
