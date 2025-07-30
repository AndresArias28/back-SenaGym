package com.gym.gym_ver2.infraestructure.auth;

import com.gym.gym_ver2.domain.model.dto.RegisterRequestDTO;

public interface AuthService  {
    String login(LoginRequest rq);

    AuthResponse register(RegisterRequestDTO rq);

    String forgotPassword(String email);

    String recoverPassword(String newPassword, String token);
}
