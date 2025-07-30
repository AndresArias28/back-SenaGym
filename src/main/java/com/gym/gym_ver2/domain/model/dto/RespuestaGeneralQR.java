package com.gym.gym_ver2.domain.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class RespuestaGeneralQR {
    private String estado;
    private String mensaje;
    private LocalDateTime timestamp;
}
