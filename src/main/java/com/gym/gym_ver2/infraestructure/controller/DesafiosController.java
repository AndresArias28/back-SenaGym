package com.gym.gym_ver2.infraestructure.controller;

import com.gym.gym_ver2.aplicaction.service.DesafiosRealizadosService;
import com.gym.gym_ver2.domain.model.dto.DesafioRealizadoDao;
import com.gym.gym_ver2.domain.model.dto.DesafiosUsuarioDAO;
import com.gym.gym_ver2.domain.model.dto.responseDTO.DesafiosDeUsuarios;
import com.gym.gym_ver2.domain.model.entity.Usuario;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "Desafios  Controller", description = "Endpoints para gestionar los desafios usuarios")
@RestController
@RequestMapping("/desafios")
@RequiredArgsConstructor
public class DesafiosController {

    private final DesafiosRealizadosService desafiosServices;

    @GetMapping("/obtenerDesafioActual")
    public ResponseEntity<DesafiosUsuarioDAO> obtenerDesafios(@AuthenticationPrincipal Usuario usuario) {
            Integer idUsuario = usuario.getIdUsuario().intValue();
            DesafiosUsuarioDAO desafios = desafiosServices.obtenerDesafioActuaPorUsuario(idUsuario);
            return ResponseEntity.ok(desafios);
    }

    @GetMapping("/porUsuario")
    public ResponseEntity<List<DesafiosDeUsuarios>> obtenerDesafiosPorUsuario(@AuthenticationPrincipal Usuario usuario) {
            Integer idUsuario = usuario.getIdUsuario().intValue();
            List<DesafiosDeUsuarios> desafios = desafiosServices.obtenerDesafiosPorUsuario(idUsuario);
            return ResponseEntity.ok(desafios);
    }

    @GetMapping("/desafiosRealizadosPorUsuario")
    public ResponseEntity<List<DesafioRealizadoDao>> obtenerDesafiosPorUsuarioId(@AuthenticationPrincipal Usuario usuario) {
            Integer idUsuario = usuario.getIdUsuario().intValue();
            List<DesafioRealizadoDao> desafios = desafiosServices.listDesafiosRealizadosByUsuarioId(idUsuario);
            return ResponseEntity.ok(desafios);
    }

}
