package com.gym.gym_ver2.domain.model.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class AsignacionRutinaDTO {
    private Integer idPersona;
    private Integer idRutina;
    private String observaciones;
    private LocalDateTime fechaAsignacion;
    private LocalDateTime fechaFinalizacion;
}

