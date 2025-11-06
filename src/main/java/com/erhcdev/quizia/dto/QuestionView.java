package com.erhcdev.quizia.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class QuestionView {
    private String pregunta;
    private List<String> opciones;
    private String answerLetter;
    private int correctIndex;


}
