package com.gym.gym_ver2.domain.model.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class SerieAvanceResponse {
    private Integer seriesRealizadas;
    private Integer seriesObjetivo;
    private boolean ejercicioCompletado;
    private boolean rutinaCompletada;

    //crear contrucotr con 2 paarmetros
    public SerieAvanceResponse(Integer seriesRealizadas, Integer seriesObjetivo) {
        this.seriesRealizadas = seriesRealizadas;
        this.seriesObjetivo = seriesObjetivo;
    }
}
