package com.gym.gym_ver2.infraestructure.controller;

import com.gym.gym_ver2.aplicaction.service.ProgresoService;
import com.gym.gym_ver2.domain.model.dto.ProgresoRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;


@Tag(name = "Progreso Controller", description = "Endpoints para gestionar el progreso de cada usuario")
@RequestMapping("/progreso")
@RestController
@RequiredArgsConstructor
public class ProgresoController {

    private final ProgresoService progresoService;

    @PostMapping("/registrarProgreso")
    public ResponseEntity<?> registrarProgreso(@RequestBody ProgresoRequest progresoRequest) {
        try{
            Map<String, Object> respuesta = progresoService.registrarOActualizarProgreso(progresoRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }
}
