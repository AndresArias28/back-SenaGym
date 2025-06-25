package com.gym.gym_ver2.aplicaction.service;

import com.gym.gym_ver2.domain.model.entity.Persona;

import com.gym.gym_ver2.domain.model.entity.Rol;
import com.gym.gym_ver2.domain.model.entity.Usuario;
import com.gym.gym_ver2.domain.model.pojos.UserResponse;
import com.gym.gym_ver2.domain.model.dto.UsuarioDTO;
import com.gym.gym_ver2.infraestructure.persistence.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

//    @PreAuthorize("hasAnyAuthority('ROLE_Administrador', 'ROLE_Superusuario')")
    @Override
    @Transactional(readOnly = true)
    public List<UsuarioDTO> getUsers() {

            // Obtener todos los usuarios desde el repositorio
            List<Usuario> usuarios = usuarioRepository.findAll();

            return usuarios.stream().map(usr -> {

                Persona persona = usr.getPersona();
                Rol rol = usr.getIdRol();

                // Validaciones
                String nombres = (persona != null) ? persona.getNombres() : null;
                String apellidos = (persona != null) ? persona.getApellidos() : null;
                Integer idRol = (rol != null) ? rol.getIdRol() : null;
                return new UsuarioDTO(
                                nombres,
                                apellidos,
                                usr.getNombreUsuario(),
                                usr.getEmailUsuario(),
                                idRol
                );
            }).toList();
    }

    @Override
    public UsuarioDTO getUser(Integer idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElse(null);
        assert usuario != null;
        return new UsuarioDTO(
                usuario.getNombreUsuario(),
                usuario.getEmailUsuario(),
                usuario.getPersona().getNombres(),
                usuario.getPersona().getIdentificacion(),
                usuario.getIdRol().getIdRol()

        );
    }

    @Transactional
    @Override
    public UserResponse actualizarUsuario(UsuarioDTO userRequest) {
//        Optional<Usuario> usuario = usuarioRepository.findById(userRequest.getIdPersona().getIdPersona());
//        if (usuario.isEmpty()) {
//            return new UserResponse("Usuario no encontrado");
//        }
//        // Validar campos de entrada
//        if (userRequest.getNombreUsuario() == null || userRequest.getNombreUsuario().isEmpty() ||
//                userRequest.getEmailUsuario() == null || userRequest.getEmailUsuario().isEmpty()) {
//            return new UserResponse("Datos inválidos: nombre o email vacío");
//        }
//        usuarioRepository.updateUser(    // Actualizar el usuario -- patron repository
//                userRequest.getIdPersona().getIdPersona(),
//                userRequest.getNombreUsuario(),
//                userRequest.getEmailUsuario()
//        );
        return new UserResponse("Usuario actualizado correctamente");
    }

    @Override
    public void updatePassword(String email, String newPassword) {
        Optional<Usuario> usuario = usuarioRepository.findByEmailUsuario(email);
        if (usuario.isEmpty()) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
        Usuario user = usuario.get();
        user.setContrasenaUsuario(passwordEncoder.encode(newPassword));
        usuarioRepository.save(user);
    }
}
