package com.erhcdev.quizia.model;

import lombok.Data;

import java.util.List;

@Data
public class QuestionResult {

    private String question;
    private List<String> options;
    private String correctAnswer;
    private String selectedAnswer;

}
