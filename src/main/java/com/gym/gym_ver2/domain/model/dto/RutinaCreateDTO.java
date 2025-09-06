package com.gym.gym_ver2.domain.model.dto;

import com.gym.gym_ver2.domain.model.entity.Dificultad;
import com.gym.gym_ver2.domain.model.entity.Enfoque;
import jakarta.persistence.Transient;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class RutinaCreateDTO {
    private Integer idRutina;
    private String nombre;
    private String descripcion;
    @Transient
    private MultipartFile fotoRutina;
    private Enfoque enfoque;
    private Dificultad dificultad;
    private Integer puntuajeRutina;
    private List<RutinaEjercicioDTO> ejercicios;

    @Data
    @Builder
    public static class RutinaEjercicioDTO {
        private Integer idEjercicio;
        private String nombre;
        private String descripcion;
        private String musculos;
        private Integer series;
        private Integer repeticion;
        private Integer carga;
        private Integer duracion;
        private Integer orden;
        private Integer tiempoDescanso;
        private Boolean asignacion;
    }

}

