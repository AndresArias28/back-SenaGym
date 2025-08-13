package com.gym.gym_ver2.domain.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RankingResponse {
    private Integer idUsuario;
    private String nombreUsuario;
    private Double puntosTotales;


    public RankingResponse(List<RankingResponse> lista) {
    }
}
