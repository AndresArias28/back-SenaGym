package com.gym.gym_ver2.infraestructure.auth;
//patrones utilizados: builder, singleton,  inyeccion de dependencias. fachada, observerr, Cadena de Responsabilidad

import com.gym.gym_ver2.aplicaction.service.PasswordResetService;
import com.gym.gym_ver2.domain.model.entity.Rol;
import com.gym.gym_ver2.domain.model.entity.Usuario;
import com.gym.gym_ver2.infraestructure.config.CustomUserDetailsService;
import com.gym.gym_ver2.infraestructure.exceptions.RecursoNoEncontradoException;
import com.gym.gym_ver2.infraestructure.jwt.JwtService;
import com.gym.gym_ver2.infraestructure.repository.RolRepository;
import com.gym.gym_ver2.infraestructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements  AuthService {

    private final UsuarioRepository userRepository;
    private final RolRepository rolRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final PasswordResetService passwordResetService;
    private final JavaMailSender mailSender;
    private final UserDetailsService userDetailsService;
    private  final CustomUserDetailsService customUserDetailsService;

    public AuthResponse login(LoginRequest rq) {
        // Validar que el email y la contraseña no estén vacíos
        if (rq.getEmailUsuario() == null || rq.getEmailUsuario().isEmpty()) {
            throw new IllegalArgumentException("El email no puede estar vacío");
        }
        if (rq.getContrasenaUsuario() == null || rq.getContrasenaUsuario().isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía");
        }
        try {//patron Cadena de Responsabilidad
            authenticationManager.authenticate(// autentica que el usuario y la contraseña sean correctos
                    new UsernamePasswordAuthenticationToken(rq.getEmailUsuario(), rq.getContrasenaUsuario())//este metodo se encarga de validar el usuario y la contraseña si ya existen en la base de datos
            );
            //recuperar el usuario de la BD
            System.out.println("Email del usuario: " + rq.getEmailUsuario());
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(rq.getEmailUsuario());//cargar el usuario de la base de datos
            System.out.println("detalles del usuario: " + userDetails.getUsername());
            HashMap<String, Object> tokenExtraClaim = new HashMap<>(); //crear un objeto de tipo HashMap
            tokenExtraClaim.put("sub", rq.getEmailUsuario());//agregar el email del usuario al token
            String token = jwtService.generateToken(tokenExtraClaim, userDetails);// generar el token segun el email del usuario
            System.out.println("Token generado: " + token);
            return AuthResponse.builder().token(token).build();//crear la respuesta con el token y retornarla
        } catch (Exception e) {
            throw new RuntimeException("Usuario o contraseña incorrectos");
        }
    }

    public AuthResponse register(RegisterRequest rq) {

        // Validar que el email y la contraseña no estén vacíos
        if (rq.getEmailUsuario() == null || rq.getEmailUsuario().isEmpty()) {
            throw new RecursoNoEncontradoException("El email no puede estar vacío");
        }
        if (rq.getContrasenaUsuario() == null || rq.getContrasenaUsuario().isEmpty()) {
            throw new RecursoNoEncontradoException("La contraseña no puede estar vacía");
        }

        if (userRepository.findByEmailUsuario(rq.getEmailUsuario()).isPresent()) {
            throw new RuntimeException("El usuario ya existe");
        }

        Rol rol = rolRepository.findById(2)
                .orElseThrow(() -> new RecursoNoEncontradoException("Rol no encontrado"));

        Usuario usuario = Usuario.builder()// mediante el patron builder se crea un usuario con la informacion del request
                .idRol(rol)
                .nombreUsuario(rq.getNombreUsuario())
                .emailUsuario(rq.getEmailUsuario())
                .apellidos(rq.getApellidos())
                .nombres(rq.getNombres())
                .identificacion(rq.getIdentificacion())
                .contrasenaUsuario(passwordEncoder.encode(rq.getContrasenaUsuario()))//codificar la contraseña
                .build();

        //guardar el usuario en la base de datos
        userRepository.save(usuario);

        UserDetails  userDetails = org.springframework.security.core.userdetails.User
                .withUsername(usuario.getEmailUsuario())
                .password(usuario.getContrasenaUsuario())
                .authorities(usuario.getIdRol().getNombreRol())
                .build();

        return AuthResponse.builder().token(jwtService.createToken(userDetails)).build();
    }


    public Usuario getUsuarioActual(String email) {
        return userRepository.findByEmailUsuario(email).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public String forgotPassword(String email) {
        System.out.println("Email del usuario: " + email);// buscar al usuario por email
        Usuario usuario = userRepository.findByEmailUsuario(email).orElseThrow(() -> new RuntimeException("No se encontró ningún usuario con este email"));
        String token = UUID.randomUUID().toString();// Generar un token único
        passwordResetService.createResetTokenForUser(usuario, token); // Guardar el token en la base de datos
        // enviar correo electrónico con el token
        String resetLink = "http://localhost:6090/auth/reset-password?token=" + token;
        sendPasswordResetEmail(usuario.getEmailUsuario(), resetLink);

        return "Se ha enviado un correo con instrucciones para restablecer tu contraseña.";
    }

    private void sendPasswordResetEmail(String email, String resetLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Recuperación de Contraseña");
        message.setText("Haz clic en el siguiente enlace para restablecer tu contraseña: " + resetLink);
        mailSender.send(message);
    }



}
