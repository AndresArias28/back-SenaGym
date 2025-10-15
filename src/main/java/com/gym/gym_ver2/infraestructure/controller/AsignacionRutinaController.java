package com.gym.gym_ver2.infraestructure.controller;

import com.gym.gym_ver2.aplicaction.service.AsignacionRutinaService;
import com.gym.gym_ver2.domain.model.dto.AsignacionResponse;
import com.gym.gym_ver2.domain.model.dto.AsignacionRutinaDTO;
import com.gym.gym_ver2.domain.model.dto.AsignacionesResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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

    @GetMapping("/rutina/{idPersona}")
    public ResponseEntity<List<AsignacionResponse>> obtenerRutinaPorPersona(@PathVariable Integer idPersona) {
        List<AsignacionResponse> asignaciones = asignacionRutinaService.obtenerRutinaPorPersona(idPersona);
        return ResponseEntity.ok(asignaciones);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/obttenerAll")
    public ResponseEntity<List<AsignacionesResponse>> obtenerAllRutinas() {
        List<AsignacionesResponse> asignaciones = asignacionRutinaService.obtenerAllAsignaciones();
        return ResponseEntity.ok(asignaciones);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PutMapping("/actualizar/{idAsignacion}")
    public ResponseEntity <AsignacionResponse> actualizarAsignacion(
            @PathVariable Integer idAsignacion,
            @RequestBody AsignacionRutinaDTO dto) {

            AsignacionResponse asignacionActualizada = asignacionRutinaService.actualizarAsignacion(idAsignacion, dto);
            return ResponseEntity.ok(asignacionActualizada);

    }



}
