package com.gym.gym_ver2.infraestructure.config;

import com.gym.gym_ver2.infraestructure.jwt.JwtAuthenticationFilter;
import com.gym.gym_ver2.infraestructure.repository.UsuarioRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration//configurar objetos de spring
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor//inyectar dependencias
public class SecurityConfig { //obtener la cadena de filtros
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailsService userDetailsService;

    @Bean //configurar la cadena de filtros, se encarga de la seguridad
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                try {
                    return http
                            // CSRF off (JWT / APIs)
                            .csrf(AbstractHttpConfigurer::disable)

                            // CORS (usa el bean de abajo)
                            .cors(Customizer.withDefaults())

                            // Rutas públicas y protegidas
                            .authorizeHttpRequests(auth -> auth
                                    // Preflight
                                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                                    // Públicas
                                    .requestMatchers(
                                            "/",
                                            "/ping",
                                            "/auth/**",
                                            "/oauth2/**",
                                            "/login/**",
                                            "/v3/api-docs/**",
                                            "/swagger-ui/**",
                                            "/swagger-ui.html"
                                    ).permitAll()

                                    .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                                    .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()

                                    // Todo lo demás autenticado
                                    .anyRequest().authenticated()
                            )

                            // Manejo de excepciones: 401 JSON controlado
                            .exceptionHandling(ex -> ex.authenticationEntryPoint((req, res, e) -> {
                                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                res.setContentType("application/json");
                                res.getWriter().write("{\"error\":true,\"mensaje\":\"No autorizado\"}");
                            }))

                            // Sesión: IF_REQUIRED porque usas oauth2Login para el handshake
                            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))

                            // Google OAuth2
                            .oauth2Login(oauth -> oauth
                                    .loginPage("/oauth2/authorization/google")
                                    .successHandler(oAuth2SuccessHandler)
                            )

                            // Logout
                            .logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/me"))

                            // Proveedor de autenticación (si lo usas para login con credenciales)
                            .authenticationProvider(authenticationProvider())

                            // Filtro JWT antes del estándar
                            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                            .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Bean // configurar cors para permitir peticiones de angular
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOriginPatterns("*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }

//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration c = new CorsConfiguration();
//        // Ajusta tus orígenes
//        c.setAllowedOrigins(List.of(
//                "http://localhost:3000",      // React local
//                "https://tu-frontend.com"     // producción (si aplica)
//        ));
//        c.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//        c.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With"));
//        c.setExposedHeaders(List.of("Authorization")); // si devuelves token en header
//        // Si usas cookies (withCredentials) descomenta:
//        // c.setAllowCredentials(true);
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", c);
//        return source;
//    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
