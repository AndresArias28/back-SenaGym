package com.gym.gym_ver2.aplicaction.service;

import com.gym.gym_ver2.domain.model.dto.responseDTO.DashResponse;
import com.gym.gym_ver2.domain.model.entity.*;
import com.gym.gym_ver2.domain.model.requestModels.SerieAvanceRequest;
import com.gym.gym_ver2.domain.model.dto.UsuarioDTO;
import com.gym.gym_ver2.infraestructure.exceptions.RecursoNoEncontradoException;
import com.gym.gym_ver2.infraestructure.persistence.repository.AprendizRepository;
import com.gym.gym_ver2.infraestructure.persistence.repository.DesafioRealizadoRepository;
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
    private final AprendizRepository aprendizRepository;
    private final DesafioRealizadoRepository desafioRealizadoRepository;

    @Autowired
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, AprendizRepository aprendizRepository, DesafioRealizadoRepository desafioRealizadoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.aprendizRepository = aprendizRepository;
        this.desafioRealizadoRepository = desafioRealizadoRepository;
    }

//    @PreAuthorize("hasAnyAuthority('ROLE_Administrador', 'ROLE_Superusuario')")
    @Override
    @Transactional(readOnly = true)
    public List<UsuarioDTO> getUsers() {

            List<Usuario> usuarios = usuarioRepository.findAll();

            return usuarios.stream().map(usr -> {

                Persona persona = usr.getPersona();
                Rol rol = usr.getIdRol();
                String nombres = (persona != null) ? persona.getNombres() : null;
                String apellidos = (persona != null) ? persona.getApellidos() : null;
                Integer idRol = (rol != null) ? rol.getIdRol() : null;
                return new UsuarioDTO(
                                persona.getIdPersona(),
                                nombres,
                                usr.getNombreUsuario(),
                                usr.getEmailUsuario(),
                                persona.getIdentificacion(),
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
                usuario.getPersona().getIdPersona(),
                usuario.getNombreUsuario(),
                usuario.getEmailUsuario(),
                usuario.getPersona().getNombres(),
                usuario.getPersona().getIdentificacion(),
                usuario.getIdRol().getIdRol()

        );
    }

    @Transactional
    @Override
    public SerieAvanceRequest.UserResponse actualizarUsuario(UsuarioDTO userRequest) {
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
        return new SerieAvanceRequest.UserResponse("Usuario actualizado correctamente");
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

//    @Override
//    @Transactional(readOnly = true)
//    public DashResponse getUserDashboardData(int idUsuario) {
//
//        Usuario usuario = usuarioRepository.
//                findById(idUsuario).orElseThrow(() -> new RecursoNoEncontradoException("recurso no encontrado"));
//
//        Persona persona = usuario.getPersona();
//        if(persona == null) {
//            throw new RecursoNoEncontradoException("Persona no encontrada para el usuario con ID: " + idUsuario);
//        }
//
//        int idAprendiz = persona.getIdPersona();
//
//        Aprendiz aprendiz  = aprendizRepository.findById(idAprendiz)
//                .orElseThrow(() -> new RecursoNoEncontradoException("Aprendiz no encontrado para la persona con ID: " + idAprendiz));
//
//        int numeroFicha = aprendiz.getFicha();
//        if (numeroFicha == 0) {
//            throw new RecursoNoEncontradoException("Número de ficha no encontrado para el aprendiz con ID: " + idAprendiz);
//        }
//
//        // Buscar el DesafioRealizado más reciente o activo
//        Optional<DesafioRealizado> desafioActualOpt = desafioRealizadoRepository
//                .findTopByAprendiz_IdPersonaOrderByFechaInicioDesc(aprendiz.getIdPersona());
//
//
//        return new DashResponse();
//    }
}
