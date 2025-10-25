package com.erhcdev.quizia.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AiService {

    @Value("${ai.api.key}")
    private String apiKey;

    @Value("${ai.api.url}")
    private String apiUrl;

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://openrouter.ai")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
            .build();

    public String generateQuiz(String topic, int numQuestions){
        String prompt = String.format("""
                Generate %d preguntas tipo test sobre %s. Devuelve SOLO un SJON válido con la estructura:
                [
                {"pregunta": "...", "opciones": ["A","B","C","D"], "respuesta_correcta:"C"}
                ]
                """, numQuestions, topic);
        Map<String, Object> body = Map.of(
                "model","gpt-4o-mini",
                "messages",List.of(Map.of(
                        "role", "user",
                        "content", prompt
        ))
        );
        try{
            System.out.println("Enviando solicitud a OpenRouter...");
            String rawResponse = webClient.post()
                    .uri("/api/v1/chat/completions")
                    .header("Authorization","Bearer " + apiKey)
                    .header("HTTP-Referer", "https://erhc-dev.com")
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block(Duration.ofSeconds(20));

            System.out.println("Respuesta cruda recibida");

            // Extraer el campo "content" del JSON
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(rawResponse);
            String content = root.path("choices").get(0).path("message").path("content").asText();

            content = content.replaceAll("```json", "")
                    .replaceAll("```", "")
                    .trim();

            System.out.println("✅ JSON limpio:");
            System.out.println(content);

            return content;
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\": \"" + e.getMessage() + "\"}";
        }

    }
}
