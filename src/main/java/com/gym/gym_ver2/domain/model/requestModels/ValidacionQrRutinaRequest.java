package com.gym.gym_ver2.domain.model.requestModels;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ValidacionQrRutinaRequest {
    private String codigoQR;
    private Integer idDesafioRealizado;
}
