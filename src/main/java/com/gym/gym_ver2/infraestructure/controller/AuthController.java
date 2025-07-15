package com.gym.gym_ver2.infraestructure.controller;

import com.gym.gym_ver2.domain.model.dto.EmailRequestForgotPass;
import com.gym.gym_ver2.domain.model.dto.RegisterRequestDTO;
import com.gym.gym_ver2.infraestructure.auth.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@Tag(name = "Auth  Controller", description = "Endpoints para la autenticación y restablecimiento de contraseñas")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping(value="/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest rq) {
        return ResponseEntity.ok(authService.login(rq));
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping(value="/register", consumes = "multipart/form-data")
    public ResponseEntity<AuthResponse> registerUsers(@ModelAttribute RegisterRequestDTO rq) {
        return ResponseEntity.ok(authService.register(rq));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody EmailRequestForgotPass request) {
        if(request.getEmailUsuario() == null || request.getEmailUsuario().isEmpty()) {
            throw new IllegalArgumentException("El email no puede estar vacío");
        }
        return ResponseEntity.ok(authService.forgotPassword(request.getEmailUsuario()));
    }

    @PutMapping("/reset-password")
    public ResponseEntity<String> resetPassword( @RequestBody Map<String, String> request) {
        String token = request.get("token");
        String newPassword = request.get("password");
        return ResponseEntity.ok(authService.recoverPassword(newPassword, token));
    }

}
