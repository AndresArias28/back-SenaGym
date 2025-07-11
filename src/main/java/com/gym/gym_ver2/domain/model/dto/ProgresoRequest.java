package com.gym.gym_ver2.domain.model.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ProgresoRequest {
    private Integer idDesafioRealizado;
    private Integer idRutinaEjercicio;
    private Integer repeticiones;
    private Integer carga;
}
