package com.gym.gym_ver2.infraestructure.controller;

import com.gym.gym_ver2.aplicaction.service.RutinaRealizadaService;
import com.gym.gym_ver2.domain.model.dto.IniciarRutinaRequest;
import com.gym.gym_ver2.domain.model.dto.IniciarRutinaResponse;
import com.gym.gym_ver2.domain.model.requestModels.SerieAvanceRequest;
import com.gym.gym_ver2.domain.model.dto.SerieAvanceResponse;
import com.gym.gym_ver2.domain.model.entity.RutinaRealizada;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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

    @PatchMapping("/desafio/{idRutinaRealizada}")
    public ResponseEntity<String> iniciarRutina(@PathVariable Integer idRutinaRealizada) {
        String mensaje = rutinaRealizadaService.actualizarFechaInicio(idRutinaRealizada);
        return ResponseEntity.ok(mensaje);
    }

    @PostMapping("/RegistrarProgreso")
    public ResponseEntity<IniciarRutinaResponse> iniciarRutina(@RequestBody IniciarRutinaRequest request) {
        IniciarRutinaResponse response = new IniciarRutinaResponse();
        List<RutinaRealizada> creadas = rutinaRealizadaService.iniciarRutina(request);
        response.setSuccess(true);
        response.setMensaje("Rutina iniciada correctamente");
        response.setRegistrosCreados(creadas.size());


        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
