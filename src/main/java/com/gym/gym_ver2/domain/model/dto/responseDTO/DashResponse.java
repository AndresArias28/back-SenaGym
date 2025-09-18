package com.gym.gym_ver2.domain.model.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DashResponse {
    private String nombre;
    private String apelldido;
    private String email;
    private String fotoPerfil;
    private Integer totalRutinasCompletadas;
    private Integer totalDesafiosCompletados;
    private Integer totalCaloriasQuemadas;
    private Integer numeroFicha;
}
