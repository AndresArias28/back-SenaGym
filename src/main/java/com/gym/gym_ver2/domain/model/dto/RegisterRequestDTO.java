package com.gym.gym_ver2.domain.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDTO {
    String nombreUsuario;
    String emailUsuario;
    String contrasenaUsuario;
    String estado;
    MultipartFile fotoPerfil;

    String apellidos;
    String nombres;
    String telefono;
    String identificacion;
    LocalDateTime fechaNacimiento;
    String sexo;

    Double estatura;
    Double peso;
    Integer ficha;
    String jornada;
    Integer horasAcumuladas;
    Integer puntosAcumulados;
    String nivelFisico;
    Integer presionSanguinea;
}
