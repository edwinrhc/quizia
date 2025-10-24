package com.erhcdev.quizia.model;

import lombok.Data;

import java.util.List;

@Data
public class QuizResult {
    private String topic;
    private List<QuestionResult> questions;
}
