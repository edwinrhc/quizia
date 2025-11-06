package com.erhcdev.quizia.dto;

import lombok.Data;

import java.util.List;

@Data
public class QuestionDto {

    private String pregunta;
    private List<String> opciones;
    private String respuesta_correcta;

}
