package com.gym.gym_ver2.domain.model.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class DesafioRequest {
    private Integer idDesafio;
    private String nombreDesafio;
    private Integer numeroDesafio;
    private LocalDateTime fechaFinDesafio;
    private LocalDateTime fechaInicioDesafio;
    private String estadoDesafio;
    private Integer puntosAcumulados;
}
