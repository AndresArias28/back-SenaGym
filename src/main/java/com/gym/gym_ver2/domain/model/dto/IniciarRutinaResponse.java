package com.gym.gym_ver2.domain.model.dto;

import lombok.Data;

@Data
public class IniciarRutinaResponse {
    private boolean success;
    private String mensaje;
    private int registrosCreados;
    private RutinaRealizadaDTO rutinaRealizada;
}
