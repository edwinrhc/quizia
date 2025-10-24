package com.erhcdev.quizia.model;

import lombok.Data;

import java.util.List;

@Data
public class QuizRequest {
    private String topic;
    private int quantity;
}

