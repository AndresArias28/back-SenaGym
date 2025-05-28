package com.gym.gym_ver2.infraestructure.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {
    @Value("${SECRET_KEY}")
    private String secreteKey;

    public String getSecretKey() {
        return secreteKey;
    }
}
