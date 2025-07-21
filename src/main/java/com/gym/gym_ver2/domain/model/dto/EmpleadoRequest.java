package com.gym.gym_ver2.domain.model.dto;

import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmpleadoRequest {
    private Integer idPersona;
    private String identificacion;
    private String nombres;
    private String apellidos;
    private String email;
    private String telefono;
    private String fechaNacimiento;
    private String cargo;
    private String contrasenaUsuario;
    private String nombreUsuario;
    private String emailUsuario;
}
