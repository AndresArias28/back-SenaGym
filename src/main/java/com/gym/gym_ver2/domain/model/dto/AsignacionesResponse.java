package com.gym.gym_ver2.domain.model.dto;

import lombok.*;

import java.time.LocalDateTime;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AsignacionesResponse {
    private String nombreAprendiz;
    private Integer idPersona;
    private Integer ficha;
    private LocalDateTime fechaCreacion;
    private String nivelFisico;
    private String obsevaciones;
    private String diasAsignado;
    private String nombreRutina;
}
