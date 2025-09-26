package com.gym.gym_ver2.domain.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DesafioRealizadoDao {
    private Integer idDesafioRealizado;
    private Integer idDesafio;
    private String estado;
    private LocalDateTime fechaInicioDesafio;
    private LocalDateTime fechaFinDesafio;
    private Double caloriasTotales;
    private Integer puntosObtenidos;
}
