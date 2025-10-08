package com.gym.gym_ver2.domain.model.dto;

import lombok.*;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FrecuenciaAprendizDTO {
    Integer idAprendiz;
    Integer frecuenciaCardiaca;
}
