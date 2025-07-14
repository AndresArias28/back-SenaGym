package com.gym.gym_ver2.infraestructure.controller;

import com.gym.gym_ver2.aplicaction.service.ProgresoService;
import com.gym.gym_ver2.domain.model.dto.IniciarRutinaRequest;
import com.gym.gym_ver2.domain.model.dto.ProgresoRequest;
import com.gym.gym_ver2.domain.model.entity.RutinaRealizada;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;


@Tag(name = "Progreso Controller", description = "Endpoints para gestionar el progreso de cada usuario")
@RequestMapping("/progreso")
@RestController
@RequiredArgsConstructor
public class ProgresoController {

    private final ProgresoService progresoService;



    @PostMapping("/iniciar")
    public ResponseEntity<?> iniciarRutina(@RequestBody IniciarRutinaRequest request) {
        try{

            List<RutinaRealizada> creadas = progresoService.iniciarRutina(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "mensaje", "Rutina iniciada correctamente",
                    "registros_creados", creadas.size()
            ));
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        }
    }
}
