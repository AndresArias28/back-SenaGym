package com.gym.gym_ver2.infraestructure.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtConfig {

    @Value("${SECRET_KEY}")
    private String secretKey;

    public String getSecretKey() {
        return secretKey;
    }
}

