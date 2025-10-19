package com.gym.gym_ver2.infraestructure.auth;

import com.gym.gym_ver2.domain.model.entity.Aprendiz;
import com.gym.gym_ver2.domain.model.entity.Usuario;
import com.gym.gym_ver2.infraestructure.config.CustomUserDetailsService;
import com.gym.gym_ver2.infraestructure.exceptions.RecursoNoEncontradoException;
import com.gym.gym_ver2.infraestructure.jwt.JwtService;
import com.gym.gym_ver2.infraestructure.persistence.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceImplTest {
    @Mock
    private UsuarioRepository userRepository;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtService jwtService;
    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void login_deberiaRetornarTokenCorrectamente() {
        // Arrange
        LoginRequest rq = new LoginRequest();
        rq.setEmailUsuario("test@email.com");
        rq.setContrasenaUsuario("password");

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);

        UserDetails userDetails = mock(UserDetails.class);
        when(customUserDetailsService.loadUserByUsername(rq.getEmailUsuario())).thenReturn(userDetails);

        Usuario usuario = Usuario.builder()
                .idUsuario(1)
                .emailUsuario(rq.getEmailUsuario())
                .nombreUsuario("TestUser")
                .fotoPerfil("foto.png")
                .persona(Aprendiz.builder().idPersona(2).nombres("Juan").apellidos("Perez").build())
                .build();
        when(userRepository.findByEmailUsuario(rq.getEmailUsuario())).thenReturn(Optional.of(usuario));
        when(jwtService.generateToken(anyMap(), eq(userDetails))).thenReturn("token123");
        // Act
        AuthResponse response = authService.login(rq);
        // Assert
        assertNotNull(response);
        assertEquals("token123", response.getToken());
    }

    @Test
    void login_deberiaLanzarExcepcionSiEmailEsVacio() {
        LoginRequest rq = new LoginRequest();
        rq.setEmailUsuario("");
        rq.setContrasenaUsuario("password");
        assertThrows(RecursoNoEncontradoException.class, () -> authService.login(rq));
    }

    @Test
    void login_deberiaLanzarExcepcionSiContrasenaEsVacia() {
        LoginRequest rq = new LoginRequest();
        rq.setEmailUsuario("test@email.com");
        rq.setContrasenaUsuario("");
        assertThrows(RecursoNoEncontradoException.class, () -> authService.login(rq));
    }

    @Test
    void login_deberiaLanzarExcepcionSiCredencialesIncorrectas() {
        LoginRequest rq = new LoginRequest();
        rq.setEmailUsuario("test@email.com");
        rq.setContrasenaUsuario("password");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Credenciales inválidas"));
        assertThrows(RecursoNoEncontradoException.class, () -> authService.login(rq));
    }

    @Test
    void login_deberiaLanzarExcepcionSiUsuarioNoExiste() {
        LoginRequest rq = new LoginRequest();
        rq.setEmailUsuario("test@email.com");
        rq.setContrasenaUsuario("password");
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        UserDetails userDetails = mock(UserDetails.class);
        when(customUserDetailsService.loadUserByUsername(rq.getEmailUsuario())).thenReturn(userDetails);
        when(userRepository.findByEmailUsuario(rq.getEmailUsuario())).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> authService.login(rq));
    }
}

