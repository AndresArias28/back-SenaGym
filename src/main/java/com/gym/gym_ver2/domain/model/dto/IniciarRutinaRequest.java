package com.gym.gym_ver2.domain.model.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IniciarRutinaRequest {
    private Integer idRutina;
    private Integer idDesafioRealizado;
}
