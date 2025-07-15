package com.gym.gym_ver2.infraestructure.controller;

import com.gym.gym_ver2.aplicaction.service.ProgresoService;
import com.gym.gym_ver2.domain.model.dto.ActualizarFechaInicioRequest;
import com.gym.gym_ver2.domain.model.dto.IniciarRutinaRequest;
import com.gym.gym_ver2.domain.model.dto.IniciarRutinaResponse;
import com.gym.gym_ver2.domain.model.dto.ProgresoRequest;
import com.gym.gym_ver2.domain.model.entity.RutinaRealizada;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;


@Tag(name = "Progreso Controller", description = "Endpoints para gestionar el progreso de cada usuario")
@RequestMapping("/progreso")
@RestController
@RequiredArgsConstructor
public class ProgresoController {

    private final ProgresoService progresoService;

    @PostMapping("/RegistrarProgreso")
    public ResponseEntity<IniciarRutinaResponse> iniciarRutina(@RequestBody IniciarRutinaRequest request) {
        IniciarRutinaResponse response = new IniciarRutinaResponse();

            List<RutinaRealizada> creadas = progresoService.iniciarRutina(request);
            response.setSuccess(true);
            response.setMensaje("Rutina iniciada correctamente");
            response.setRegistrosCreados(creadas.size());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }
}
