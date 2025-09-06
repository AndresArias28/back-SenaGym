package com.gym.gym_ver2.infraestructure.jwt;

import com.gym.gym_ver2.infraestructure.config.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;


import org.springframework.http.HttpHeaders;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    //atributos
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final CustomUserDetailsService customUserDetailsService;
    private static final Set<String> PUBLIC_PREFIXES = Set.of(
            "/auth/",
            "/oauth2/",
            "/login/",
            "/v3/api-docs/",
            "/swagger-ui/"
    );

    //constructor
    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService, CustomUserDetailsService customUserDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override// se ejecuta en cada peticion
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String path = request.getServletPath();

        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }
        // 2) Rutas públicas: sin validación de JWT
        if (isPublicPath(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3) Leer token del header Authorization
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = authHeader.substring(7);
        if (token == null || token.isBlank()) {
            writeUnauthorized(response, "Token vacío");
            return;
        }

        try {
            final String userEmail = jwtService.extractUsername(token); //extraer el correo del token
            System.out.println("Correo del token desde el filtro: " + userEmail);
            //validar token y correo
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(userEmail);// cargar los detalles del usuario usando el correo extraído del token
                System.out.println("UserDetails cargado: " + userDetails.getUsername());

                // Validar el token con los detalles del usuario
                if (jwtService.validateToken(token, userDetails)) {

                    // Construir el token mediante la librería Jwts
                    Claims claims = Jwts.parserBuilder()
                            .setSigningKey(jwtService.getKey()) // Clave secreta usada para firmar
                            .build()
                            .parseClaimsJws(token)
                            .getBody();

                    // Obtener los roles del token
                    String roles = claims.get("rol", String.class);
                    // Convertir roles en una lista de autoridades
                    List<GrantedAuthority> authorities = (roles != null) ? Collections.singletonList(new SimpleGrantedAuthority(roles)) : List.of();
                    System.out.println("Roles del token: " + roles);
                    System.out.println("Autoridades generadas filtro: " + authorities);

                    System.out.println("userDetails.getUsername(): " + userDetails.getUsername());
                    System.out.println("userDetails.getAuthorities(): " + userDetails.getAuthorities());

                    // Establecer la autenticación en el SecurityContextHolder para  el usuario autenticado
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    System.out.println("Autenticación establecida en el SecurityContextHolder");
                }else {
                    writeUnauthorized(response, "Token no válido");
                    return;
                }
            }
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            System.err.println("El token ha expirado: " + e.getMessage());
        } catch (io.jsonwebtoken.SignatureException e) {
            System.err.println("Firma del token no válida: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error al validar el token: " + e.getMessage());
        }
        // continuar con la cadena de filtros
        filterChain.doFilter(request, response);
    }

    // obtener el token del encabezado Authorization de la solicitud
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);//obtener el item de autenticacion
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);//el token se encuentra despues de la palabra Bearer
        }
        return null;
    }

    private void writeUnauthorized(HttpServletResponse response, String message) throws IOException {
        if (!response.isCommitted()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":true,\"mensaje\":\"" + message + "\"}");
        }
    }

    private boolean isPublicPath(String path) {
        if (path == null) return true;
        if ("/".equals(path) || "/ping".equals(path)) return true;
        for (String prefix : PUBLIC_PREFIXES) {
            if (path.startsWith(prefix)) return true;
        }
        return false;
    }

}