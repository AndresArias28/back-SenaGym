package com.gym.gym_ver2.domain.model.requestModels;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SerieAvanceRequest {
    private Integer idDesafioRealizado;
    private Integer idRutinaEjercicio;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserResponse {
        String message;
    }

}
