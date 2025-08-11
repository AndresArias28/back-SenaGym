package com.gym.gym_ver2.infraestructure.controller;

import com.gym.gym_ver2.domain.model.dto.EmailRequestForgotPass;
import com.gym.gym_ver2.infraestructure.auth.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/me")
    public Map<String, Object> me(org.springframework.security.oauth2.core.oidc.user.OidcUser principal) {
            // OidcUser trae claims como sub, email, name, picture, etc.
            Map<String, Object> info = new java.util.HashMap<>();
            info.put("name", principal.getFullName());
            info.put("email", principal.getEmail());
            info.put("picture", principal.getPicture());
            info.put("claims", principal.getClaims());
            return info;
    }
}
