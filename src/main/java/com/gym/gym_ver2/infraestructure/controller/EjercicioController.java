package com.gym.gym_ver2.infraestructure.controller;

import com.gym.gym_ver2.aplicaction.service.EjercicioService;
import com.gym.gym_ver2.domain.model.dto.EjercicioDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@Tag(name = "Ejercicio Controller", description = "Endpoints para gestionar ejercicios")
@RequestMapping("/ejercicio")
@RestController
@RequiredArgsConstructor
public class EjercicioController {
    private final EjercicioService ejercicioService;

    @GetMapping(value = "/obtenerEjercicios")
    public ResponseEntity<?> obtenerEjercicios() {
        try {
            List<EjercicioDTO> ejercicios = ejercicioService.obtenerEjercicios();
            return ResponseEntity.ok(ejercicios);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
