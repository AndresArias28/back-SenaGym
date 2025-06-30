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

}
