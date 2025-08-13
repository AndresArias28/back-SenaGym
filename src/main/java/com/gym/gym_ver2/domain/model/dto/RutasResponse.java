package com.gym.gym_ver2.domain.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RutasResponse {
    private Long idRuta;
    private String origen;
    private String destino;
    private Double distancia;



}
