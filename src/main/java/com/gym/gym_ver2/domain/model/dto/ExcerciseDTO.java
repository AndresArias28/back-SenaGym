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

        //crear construtcor con 4 paraemtros
        public ExcerciseDTO( String nombreEjercicio, String descripcionEjercicio, String musculos, Double met) {
            this.nombreEjercicio = nombreEjercicio;
            this.descripcionEjercicio = descripcionEjercicio;
            this.musculos = musculos;
            this.met = met;
        }
}
