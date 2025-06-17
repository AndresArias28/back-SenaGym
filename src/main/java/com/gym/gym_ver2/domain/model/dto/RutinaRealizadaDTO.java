package com.gym.gym_ver2.domain.model.dto;

import com.gym.gym_ver2.domain.model.entity.DesafioRealizado;
import com.gym.gym_ver2.domain.model.entity.RutinaEjercicio;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class RutinaRealizadaDTO {
    private Integer desafioRealizado;
    private Integer rutinaEjercicio;
    private Integer seriesRealizadas;
    private Integer repeticionesRealizadas;
    private Integer cargaRealizada;
    private String estado;

}
