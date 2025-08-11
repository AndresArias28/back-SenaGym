package com.gym.gym_ver2.infraestructure.config;

import com.gym.gym_ver2.infraestructure.jwt.JwtService;
import jakarta.servlet.FilterChain;
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
import static org.hibernate.engine.config.spi.StandardConverters.asString;

@Component
public class OAuth2SuccessHandler implements   org.springframework.security.web.authentication.AuthenticationSuccessHandler {

    private final JwtService jwtService;

    @Value("${app.oauth2.redirect-url:http://localhost:5173/login/success}")
    private String redirectUrl;

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

        // 1) Obtener datos del usuario (tolerando OIDC y OAuth2)
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
        // TODO: upsert en tu BD (usuarios / oauth_cuentas)
        // userService.upsertFromGoogle(email, name, principal.getSubject(), principal.getPicture());

        // Construye un UserDetails mínimo para firmar el JWT
        UserDetails usuario = org.springframework.security.core.userdetails.User
                .withUsername(email)
                .password("N/A")
                .authorities("ROLE_USER")
                .build();

        String token = jwtService.createToken(usuario);

        String next = redirectUrl + "?token=" + URLEncoder.encode(token, StandardCharsets.UTF_8);


        // Opción rápida (para pruebas): redirigir con token en query
        response.sendRedirect(next);

    }
    private static String asString(Object o) {
        return (o == null) ? null : String.valueOf(o);
    }

}
