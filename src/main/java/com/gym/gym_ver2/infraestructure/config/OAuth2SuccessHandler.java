package com.gym.gym_ver2.infraestructure.config;

import com.gym.gym_ver2.aplicaction.service.UsuarioService;
import com.gym.gym_ver2.infraestructure.jwt.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
public class OAuth2SuccessHandler implements   org.springframework.security.web.authentication.AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UsuarioService userService;

    @Value("${app.oauth2.redirect-url:http://localhost:5173/login/success}")
    private String redirectUrl;

    public OAuth2SuccessHandler(JwtService jwtService, UsuarioService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        // 1) obtener datos del usuario (tolerando OIDC y OAuth2)
        String email = null;
        String name  = null;
        Object principal = authentication.getPrincipal();
        if (principal instanceof OidcUser oidc) {
            email = oidc.getEmail();
            name  = oidc.getFullName();
        } else if (principal instanceof OAuth2User oauth2) {
            Map<String, Object> attrs = oauth2.getAttributes();
            email = asString(attrs.get("email"));
            name  = asString(attrs.get("name"));
        }

        if (email == null || email.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No se pudo obtener el email del proveedor OAuth2");
            return;
        }
       userService.upsertFromGoogle(email, name);

        // 2) Crear un token JWT para el usuario
        UserDetails usuario = org.springframework.security.core.userdetails.User
                .withUsername(email)
                .password("N/A")
                .authorities("Usuario")
                .build();

        String token = jwtService.createToken(usuario);

        String next = redirectUrl + "?token=" + URLEncoder.encode(token, StandardCharsets.UTF_8);

        response.sendRedirect(next);
    }

    private static String asString(Object o) {
        return (o == null) ? null : String.valueOf(o);
    }

}
