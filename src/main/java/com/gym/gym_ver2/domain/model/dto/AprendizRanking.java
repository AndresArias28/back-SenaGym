package com.gym.gym_ver2.domain.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AprendizRanking {
    private Integer idPersona;
    private String nombres;
    private String apellidos;
    private Integer horasAcumuladas;
    private Integer puntosAcumulados;
}
