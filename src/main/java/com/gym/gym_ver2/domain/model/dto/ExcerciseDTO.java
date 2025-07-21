package com.gym.gym_ver2.domain.model.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExcerciseDTO {
        private Integer idEjercicio;
        private String nombreEjercicio;
        private String descripcionEjercicio;
        private String musculos;
        private Double met;
}
