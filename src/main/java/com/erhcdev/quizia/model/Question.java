package com.erhcdev.quizia.model;

import lombok.Data;

import java.util.List;

@Data
public class Question{
    private String pregunta;
    private List<String> opciones;
    private String respuesta_correcta;
}
