package com.gym.gym_ver2.domain.model.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DesafiosDeUsuarios {
    private Integer idDesafioRealizado;
    private Integer idDesafio;
    private String estado;
}
