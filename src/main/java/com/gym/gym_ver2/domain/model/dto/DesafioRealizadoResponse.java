package com.gym.gym_ver2.domain.model.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class DesafioRealizadoResponse {
    private Integer idDesafioRealizado;
    private String estado;
}
