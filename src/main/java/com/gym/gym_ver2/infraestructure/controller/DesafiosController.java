package com.gym.gym_ver2.infraestructure.controller;

import com.gym.gym_ver2.aplicaction.service.DesafiosRealizadosService;
import com.gym.gym_ver2.domain.model.dto.DesafiosUsuarioDAO;
import com.gym.gym_ver2.infraestructure.jwt.JwtConfig;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@Tag(name = "Desafios  Controller", description = "Endpoints para gestionar los desafios usuarios")
@RestController
@RequestMapping("/desafios")
@RequiredArgsConstructor
public class DesafiosController {


    private final DesafiosRealizadosService desafiosServices;
    private final JwtConfig jwtConfig;


    @GetMapping("/obtenerDesafioACtual")
    public ResponseEntity<?> obtenerDesafios(@RequestHeader("Authorization") String token) {
        try{
            Integer idUsuario = jwtConfig.extraerIdUsuarioDesdeToken(token);
            DesafiosUsuarioDAO desafios = desafiosServices.obtenerDesafioActuaPorUsuario(idUsuario);

            return ResponseEntity.ok(desafios);
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }
}
