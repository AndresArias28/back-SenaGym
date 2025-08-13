package com.gym.gym_ver2.infraestructure.controller;

import com.gym.gym_ver2.aplicaction.service.UsuarioService;
import com.gym.gym_ver2.domain.model.dto.PuntosRequest;
import com.gym.gym_ver2.domain.model.dto.RankingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService userService;

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/registerPuntos/{idUsuario}")
    public ResponseEntity<String> registerPuntos(@PathVariable Integer idUsuario, @RequestBody PuntosRequest puntos) {
        String resultado = userService.registerPuntos(idUsuario, puntos);
        return ResponseEntity.ok(resultado);
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/ranking")
    public ResponseEntity<List<RankingResponse>> getRanking() {
        return ResponseEntity.ok(userService.getRanking());
    }
}
