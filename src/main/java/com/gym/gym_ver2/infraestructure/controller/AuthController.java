package com.gym.gym_ver2.infraestructure.controller;

import com.gym.gym_ver2.domain.model.dto.EmailRequestForgotPass;
import com.gym.gym_ver2.infraestructure.auth.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "auth  Controller", description = "Endpoints para la autenticación y restablecimiento de contraseñas")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;//instancia para acceder a los metodos y a su vez al token

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping(value="/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest rq) {
        return ResponseEntity.ok(authService.login(rq));
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUsers(@RequestBody RegisterRequest rq) {
        return ResponseEntity.ok(authService.register(rq));
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody EmailRequestForgotPass request) {
        if(request.getEmailUsuario() == null || request.getEmailUsuario().isEmpty()) {
            throw new IllegalArgumentException("El email no puede estar vacío");
        }
        return ResponseEntity.ok(authService.forgotPassword(request.getEmailUsuario()));
    }


}
