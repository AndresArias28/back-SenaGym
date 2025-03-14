package com.gym.gym_ver2.infraestructure.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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


import org.springframework.http.HttpHeaders;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    //atributos
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    //constructor
    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override// se ejecuta en cada peticion
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
       // final String uri = request.getRequestURI();
        final String token = getTokenFromRequest(request);//obtener token
        final String userEmail;

        if (token == null) { //validar si el token es nulo
            System.out.println("Token no encontrado en la solicitud");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            userEmail = jwtService.extractUsername(token); //extraer el correo del token
            System.out.println("Correo del token desde el filtro: " + userEmail);
            //validar token y correo
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
                System.out.println("UserDetails cargado: " + userDetails.getUsername());

                if (jwtService.validateToken(token, userDetails)) {
                    Claims claims = Jwts.parserBuilder()// Construir el token mediante la librería Jwts
                            .setSigningKey(jwtService.getKey()) // Clave secreta usada para firmar
                            .build()
                            .parseClaimsJws(token)
                            .getBody();

                    String roles = claims.get("rol", String.class); // Obtener roles como String
                    // Convertir roles en una lista de autoridades
                    List<GrantedAuthority> authorities = (roles != null) ? Collections.singletonList(new SimpleGrantedAuthority(roles)) : List.of();
                    System.out.println("Roles del token: " + roles);
                    System.out.println("Autoridades generadas filtro: " + authorities);
                    // Validar la url de la petición
                    if (request.getRequestURI().startsWith("/user/obtenereUsarios")) {
                        boolean esAdministrador = authorities.stream()
                                .anyMatch(auth -> auth.getAuthority().equals("ROLE_Administrador"));
                        boolean esUsuario = authorities.stream()
                                .anyMatch(auth -> auth.getAuthority().equals("ROLE_Usuario"));
                        boolean esSuperusuario = authorities.stream()
                                .anyMatch(auth -> auth.getAuthority().equals("ROLE_Superusuario"));
                        // Validar que el usuario tenga el rol usuario para denegarle el permiso
                        if (esUsuario) {
                            System.err.println("Access Denied: Required Superusuario or admin role");
                            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied: Requiere Administrador or Superusuario role");
                            return;
                        }
                    }
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    System.out.println("Autenticación establecida en el SecurityContextHolder");
                }
            }
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            System.err.println("El token ha expirado: " + e.getMessage());
        } catch (io.jsonwebtoken.SignatureException e) {
            System.err.println("Firma del token no válida: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error al validar el token: " + e.getMessage());
        }
        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);//obtener el item de autenticacion
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);//el token se encuentra despues de la palabra Bearer
        }
        return null;
    }

}