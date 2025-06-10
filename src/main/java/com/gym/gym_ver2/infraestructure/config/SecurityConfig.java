package com.gym.gym_ver2.infraestructure.config;

import com.gym.gym_ver2.infraestructure.jwt.JwtAuthenticationFilter;
import com.gym.gym_ver2.infraestructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
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
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration//configurar objetos de spring
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor//inyectar dependencias
public class SecurityConfig { //obtener la cadena de filtros

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailsService userDetailsService;

    @Bean //configurar la cadena de filtros, se encarga de la seguridad
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                try {
                    return http
                            .csrf(AbstractHttpConfigurer::disable)//deshabilitar la proteccion csrf, no es necesario con JWT
                            .cors(cors -> cors.configurationSource(request -> {
                                CorsConfiguration config = new CorsConfiguration();
                                config.setAllowedOrigins(List.of("*")); // Ajusta según sea necesario
                                config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                                config.setAllowedHeaders(List.of("*"));
                                return config;
                            }))
                            .authorizeHttpRequests(authRequest -> authRequest//configurar las rutas que necesitan autenticacion
                                    .requestMatchers(
                                            "/auth/**",
                                            "/v3/api-docs/**",
                                            "/swagger-ui/**",
                                            "/swagger-ui.html"
                                    ).permitAll()
                                    .requestMatchers(HttpMethod.PUT).permitAll()
                                    .requestMatchers(HttpMethod.OPTIONS).permitAll()
                                    .requestMatchers(HttpMethod.POST).permitAll()
                                    .requestMatchers(HttpMethod.GET).permitAll()
                                    .requestMatchers(HttpMethod.DELETE).permitAll()
                                    .anyRequest().authenticated()
                            )
                    //configurar la sesion para que sea sin estado
                    .sessionManagement(sessionManagement ->
                            sessionManagement
                            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .authenticationProvider(authenticationProvider())//configurar el proveedor de autenticacion
                    //configurar el filtro de autenticacion JWT antes del filtro estándar de Spring Security.
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
