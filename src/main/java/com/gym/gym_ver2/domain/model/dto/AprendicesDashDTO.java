package com.gym.gym_ver2.domain.model.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AprendicesDashDTO {
    private Integer id;
    private String nombre;
    private Integer ficha;
    private String nivel;
    private Integer horas;
    private Integer puntos;
}
