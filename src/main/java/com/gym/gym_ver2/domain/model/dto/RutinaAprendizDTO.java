package com.gym.gym_ver2.domain.model.dto;

import com.gym.gym_ver2.domain.model.entity.Dificultad;
import com.gym.gym_ver2.domain.model.entity.Enfoque;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RutinaAprendizDTO {
    private String nombre;
    private Dificultad dificultad;
    private Enfoque enfoque;
}
