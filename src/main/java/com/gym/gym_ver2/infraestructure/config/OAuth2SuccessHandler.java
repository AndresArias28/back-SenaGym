package com.gym.gym_ver2.infraestructure.config;

import com.gym.gym_ver2.infraestructure.jwt.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;

@Component
public class OAuth2SuccessHandler implements   org.springframework.security.web.authentication.AuthenticationSuccessHandler {

    private final JwtService jwtService;

    public OAuth2SuccessHandler(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, FilterChain chain,
                                        Authentication authentication) throws IOException, ServletException {

    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OidcUser principal = (OidcUser) authentication.getPrincipal();
        String email = principal.getEmail();
        String name  = principal.getFullName();

        // TODO: upsert en tu BD (usuarios / oauth_cuentas)
        // userService.upsertFromGoogle(email, name, principal.getSubject(), principal.getPicture());

        // Construye un UserDetails mínimo para firmar el JWT
        UserDetails usuario = org.springframework.security.core.userdetails.User
                .withUsername(email)
                .password("N/A")
                .authorities("ROLE_USER")
                .build();

        String token = jwtService.createToken(usuario);

        // Opción rápida (para pruebas): redirigir con token en query
        response.sendRedirect("http://localhost:5173/login/success?token=" + URLEncoder.encode(token, java.nio.charset.StandardCharsets.UTF_8));

    }
}
