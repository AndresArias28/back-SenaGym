package com.gym.gym_ver2.infraestructure.controller;

import com.gym.gym_ver2.aplicaction.service.AsignacionRutinaService;
import com.gym.gym_ver2.domain.model.dto.AsignacionResponse;
import com.gym.gym_ver2.domain.model.dto.AsignacionRutinaDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/asignaciones")
@RequiredArgsConstructor
public class AsignacionRutinaController {

    private final AsignacionRutinaService asignacionRutinaService;

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/asignar")
    public ResponseEntity<AsignacionResponse> asignarRutina(@RequestBody AsignacionRutinaDTO dto) {
            AsignacionResponse nuevaAsignacion = asignacionRutinaService.asignarRutina(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaAsignacion);
    }
}
