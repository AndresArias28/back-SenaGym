package com.gym.gym_ver2.domain.model.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ValidacionRutinaResponse {
    private String mensaje;
    private Double caloriasTotales;
}
