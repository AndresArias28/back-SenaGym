package com.gym.gym_ver2.infraestructure.controller;

import com.gym.gym_ver2.aplicaction.service.RutinaRealizadaService;
import com.gym.gym_ver2.domain.model.dto.RutinaRealizadaDTO;
import com.gym.gym_ver2.domain.model.dto.SerieAvanceRequest;
import com.gym.gym_ver2.domain.model.dto.SerieAvanceResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Realizar Rutina Controller", description = "Endpoints para empezar las rutinas por usuarios y puedne realizar ejercicios")
@RequestMapping("/rutina-realizada")
@RestController
@RequiredArgsConstructor
public class RealizaRutinaController {

    private final RutinaRealizadaService rutinaRealizadaService;

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/registrar-rutina")
    public ResponseEntity<RutinaRealizadaDTO> registrarRutina(@RequestBody RutinaRealizadaDTO  rutinaRealizadaDTO) {
        try {
            RutinaRealizadaDTO nuevaRutina = rutinaRealizadaService.crearRutina(rutinaRealizadaDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaRutina);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping("/serie")
    public ResponseEntity<SerieAvanceResponse> avanzarSerie(@RequestBody SerieAvanceRequest serieAvanceRq) {

            SerieAvanceResponse avance = rutinaRealizadaService.avanzarSerie(serieAvanceRq);
            return ResponseEntity.ok(avance);

    }




}
