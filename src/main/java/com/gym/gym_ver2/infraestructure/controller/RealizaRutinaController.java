package com.gym.gym_ver2.infraestructure.controller;

import com.gym.gym_ver2.aplicaction.service.RutinaRealizadaService;
import com.gym.gym_ver2.domain.model.dto.SerieAvanceRequest;
import com.gym.gym_ver2.domain.model.dto.SerieAvanceResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Realizar Rutina Controller", description = "Endpoints para empezar las rutinas por usuarios y puedne realizar ejercicios")
@RequestMapping("/rutina-realizada")
@RestController
@RequiredArgsConstructor
public class RealizaRutinaController {

    private final RutinaRealizadaService rutinaRealizadaService;

    @PatchMapping("/serie")
    public ResponseEntity<SerieAvanceResponse> avanzarSerie(@RequestBody SerieAvanceRequest serieAvanceRq) {
            SerieAvanceResponse avance = rutinaRealizadaService.avanzarSerie(serieAvanceRq);
            return ResponseEntity.ok(avance);
    }

    @PatchMapping("/desafio/{idDesafio}")
    public ResponseEntity<String> iniciarRutina(@PathVariable Integer idDesafio) {
        String mensaje = rutinaRealizadaService.actualizarFechaInicio(idDesafio);
        return ResponseEntity.ok(mensaje);
    }

}
