package com.gym.gym_ver2.infraestructure.controller;

import com.gym.gym_ver2.aplicaction.service.RutasService;
import com.gym.gym_ver2.domain.model.dto.RutasRequest;
import com.gym.gym_ver2.domain.model.dto.RutasResponse;
import com.gym.gym_ver2.domain.model.entity.Ruta;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("rutas")
public class RutasController {

    private final RutasService rutasService;

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping(value = "guardarRuta")
    public ResponseEntity<RutasResponse> guardarRuta(@RequestBody RutasRequest ruta){
        return ResponseEntity.ok(rutasService.save(ruta));
    }
}
