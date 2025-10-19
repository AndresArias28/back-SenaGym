package com.gym.gym_ver2.infraestructure.auth;
//patrones utilizados: builder, singleton,  inyeccion de dependencias. fachada, observerr, Cadena de Responsabilidad

import com.gym.gym_ver2.aplicaction.service.CloudinaryService;
import com.gym.gym_ver2.aplicaction.service.PasswordResetService;
import com.gym.gym_ver2.aplicaction.service.UsuarioService;
import com.gym.gym_ver2.domain.model.dto.RegisterRequestDTO;
import com.gym.gym_ver2.domain.model.entity.Aprendiz;
import com.gym.gym_ver2.domain.model.entity.Rol;
import com.gym.gym_ver2.domain.model.entity.Usuario;
import com.gym.gym_ver2.infraestructure.config.CustomUserDetailsService;
import com.gym.gym_ver2.infraestructure.exceptions.RecursoNoEncontradoException;
import com.gym.gym_ver2.infraestructure.jwt.JwtService;
import com.gym.gym_ver2.infraestructure.persistence.repository.AprendizRepository;
import com.gym.gym_ver2.infraestructure.persistence.repository.PersonaRepository;
import com.gym.gym_ver2.infraestructure.persistence.repository.RolRepository;
import com.gym.gym_ver2.infraestructure.persistence.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements  AuthService {

    private final UsuarioRepository userRepository;
    private final AprendizRepository aprendizRepository;
    private final PersonaRepository personaRepository;
    private final RolRepository rolRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final PasswordResetService passwordResetService;
    private final UsuarioService usuarioService;
    private final JavaMailSender mailSender;
    private final UserDetailsService userDetailsService;
    private  final CustomUserDetailsService customUserDetailsService;
    private final CloudinaryService cloudinaryService;

    public AuthResponse login(LoginRequest rq) {

        if (rq.getEmailUsuario() == null || rq.getEmailUsuario().isEmpty()) {

            throw new RecursoNoEncontradoException("El email no puede estar vacío");
        }
        if (rq.getContrasenaUsuario() == null || rq.getContrasenaUsuario().isEmpty()) {

            throw new RecursoNoEncontradoException("La contraseña no puede estar vacía");
        }

        try {
            System.out.println("Autenticando usuario: " + rq.getEmailUsuario());

            // Autenticar usuario
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            rq.getEmailUsuario(),
                            rq.getContrasenaUsuario()
                    )
            );
            System.out.println("Usuario autenticado correctamente.");

            // Cargar detalles del usuario
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(rq.getEmailUsuario());

            // Buscar usuario completo
            Optional<Usuario> optionalUsuario = userRepository.findByEmailUsuario(rq.getEmailUsuario());

            if (optionalUsuario.isEmpty()) {
                System.out.println(" Usuario no encontrado en la BD.");
                throw new RuntimeException("Usuario no encontrado");
            }

            Usuario usuario = optionalUsuario.get();
            System.out.println("Usuario encontrado en BD. ID: " + usuario.getIdUsuario());

            HashMap<String, Object> tokenExtraClaim = new HashMap<>();
            tokenExtraClaim.put("sub", usuario.getEmailUsuario());
            tokenExtraClaim.put("id_usuario", usuario.getIdUsuario());
            tokenExtraClaim.put("nombre_usuario", usuario.getNombreUsuario());
            tokenExtraClaim.put("foto", usuario.getFotoPerfil());

            if (usuario.getPersona() != null) {
                Integer idPersona = usuario.getPersona().getIdPersona();
                tokenExtraClaim.put("id_persona", idPersona);
            } else {
                System.out.println("Usuario no tiene persona asociada.");
            }

            String token = jwtService.generateToken(tokenExtraClaim, userDetails);
            System.out.println("Token generado correctamente: " + token);

            // Retornar respuesta
            return AuthResponse.builder().token(token).build();

        } catch (BadCredentialsException e) {
            System.out.println("Credenciales inválidas: " + e.getMessage());
            throw new RecursoNoEncontradoException("Usuario o contraseña incorrectos");
        } catch (Exception e) {
            System.out.println("Error inesperado durante el login: " + e.getMessage());
            e.printStackTrace();
            throw new RecursoNoEncontradoException("Error durante el inicio de sesión");
        }
    }


    public AuthResponse register(RegisterRequestDTO rq) {
        String imageUrl = null;
        String imagePublicId = null;

        MultipartFile file = rq.getFotoPerfil();

        if (file != null && !file.isEmpty()) {
            try{
                var result = cloudinaryService.uploadImage(file, "usuarios");
                imageUrl = result.get("url");
                imagePublicId = result.get("public_id");
            }catch (Exception e){
                throw new RuntimeException("Error al subir la imagen: " + e.getMessage());
            }

        }else{
            imageUrl = "default.png";
            imagePublicId = "default";
        }

        if (rq.getEmailUsuario() == null || rq.getEmailUsuario().isEmpty()) {
            throw new IllegalArgumentException("El email no puede estar vacío");
        }
        if (rq.getContrasenaUsuario() == null || rq.getContrasenaUsuario().isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía");
        }
        // Verificar si el usuario ya existe
        if (userRepository.findByEmailUsuario(rq.getEmailUsuario()).isPresent()) {
            throw new RuntimeException("El usuario ya existe");
        }
        // obtener el rol de aprendiz  desde la base de datos
        Rol rol = rolRepository.findById(3).orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        // crear y guardar al aprendiz (persona concreta)
        Aprendiz aprendiz = Aprendiz.builder()// crear un aprendiz con la informacion del usuario
                .nombres(rq.getNombres())
                .apellidos(rq.getApellidos())
                .identificacion(rq.getIdentificacion())
                .telefono(rq.getTelefono())
                .fechaNacimiento(rq.getFechaNacimiento())
                .sexo(rq.getSexo())
                .ficha(rq.getFicha())
                .jornada(rq.getJornada())
                .estatura(rq.getEstatura())
                .peso(rq.getPeso())
                .nivelFisico(rq.getNivelFisico())
                .frecuenciaCardiaca(rq.getPresionSanguinea())
                .puntosAcumulados(0)
                .horasAcumuladas(0)
                .build();

        aprendiz = aprendizRepository.save(aprendiz);

        Usuario usuario = Usuario.builder()// mediante el patron builder se crea un usuario con la informacion del request
                .persona(aprendiz)
                .idRol(rol)//por defecto se asigna el rol de usuario
                .nombreUsuario(rq.getNombreUsuario())
                .emailUsuario(rq.getEmailUsuario())
                .contrasenaUsuario(passwordEncoder.encode(rq.getContrasenaUsuario()))//codificar la contraseña
                .fotoPerfil(imageUrl)
                .imagePublicId(imagePublicId)
                .estado("Activo")
                .build();

        userRepository.save(usuario);

        return AuthResponse.builder().token(jwtService.createToken(usuario)).build();
    }

//    public Usuario getUsuarioActual(String email) {
//        return userRepository.findByEmailUsuario(email).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
//    }

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

    @Override
    public String recoverPassword(String newPassword, String token) {
        String email = passwordResetService.validatePasswordResetToken(token);
        usuarioService.updatePassword(email, newPassword);
        return "Contraseña actualizada correctamente";
    }

}
