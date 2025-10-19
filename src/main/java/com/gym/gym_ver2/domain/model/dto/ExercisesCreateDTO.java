package com.gym.gym_ver2.domain.model.dto;

import jakarta.persistence.Transient;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ExercisesCreateDTO {
    private Integer idEjercicio;
    private String nombreEjercicio;
    private String descripcionEjercicio;
    private String musculos;
    @Transient
    private MultipartFile fotoEjercicio;
    private Double met;

    //crear construtcor con 5 paraemtros> "Ejercicio Actualizado", "Descripción Actualizada", null, "Músculos Actualizados", 7.0
    public ExercisesCreateDTO( String nombreEjercicio, String descripcionEjercicio, MultipartFile fotoEjercicio, String musculos, Double met) {
        this.nombreEjercicio = nombreEjercicio;
        this.descripcionEjercicio = descripcionEjercicio;
        this.fotoEjercicio = fotoEjercicio;
        this.musculos = musculos;
        this.met = met;
    }

}
