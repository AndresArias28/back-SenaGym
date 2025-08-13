package com.gym.gym_ver2.domain.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RutasRequest {
    private String origen;
    private String destino;
    private Double distancia;
}
