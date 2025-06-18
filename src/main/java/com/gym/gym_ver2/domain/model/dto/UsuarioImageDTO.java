package com.gym.gym_ver2.domain.model.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class UsuarioImageDTO {
     private String nombreCompleto;
     private String nombreUsuario;
     private String emailUsuario;
     private String identificacion;
     private MultipartFile rol;

}

