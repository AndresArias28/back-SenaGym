package com.gym.gym_ver2.domain.model.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class EjercicioDTO {
    private Integer idEjercicio;
    private String nombreEjercicio;
    private String descripcionEjercicio;
    private String fotoEjercicio;
    private String musculos;

}


