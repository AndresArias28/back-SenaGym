package com.gym.gym_ver2.aplicaction.service;

import com.gym.gym_ver2.domain.model.dto.PuntosRequest;
import com.gym.gym_ver2.domain.model.dto.RankingResponse;
import com.gym.gym_ver2.domain.model.entity.Rol;
import com.gym.gym_ver2.domain.model.entity.Usuario;
import com.gym.gym_ver2.infraestructure.exceptions.RecursoNoEncontradoException;
import com.gym.gym_ver2.infraestructure.repository.PuntosHistorial;
import com.gym.gym_ver2.infraestructure.repository.RolRepository;
import com.gym.gym_ver2.infraestructure.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioServiceImpl implements  UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;


    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, RolRepository rolRepository) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
    }

    @Transactional
    @Override
    public Usuario upsertFromGoogle(String email, String nombre) {
       Rol rolUser = rolRepository.findById(2)
                .orElseThrow(() -> new RuntimeException("Rol 'usuario' no encontrado"));

        return usuarioRepository.findByEmailUsuario(email)
                .map(usuarioExistente -> {
                    // Si el nombre cambió, lo actualizamos
                    if (nombre != null && !nombre.equals(usuarioExistente.getNombreUsuario())) {
                        usuarioExistente.setNombreUsuario(nombre);
                    }
                    return usuarioRepository.save(usuarioExistente);
                })
                .orElseGet(() -> {
                    // Crear usuario nuevo para login con Google
                    Usuario nuevo = new Usuario();
                    nuevo.setEmailUsuario(email);
                    nuevo.setNombreUsuario(nombre);
                    nuevo.setContrasenaUsuario("");
                    nuevo.setIdRol(rolUser);
                    return usuarioRepository.save(nuevo);
                });
    }

    @Transactional
    @Override
    public String registerPuntos(Integer idUsuario, PuntosRequest puntos) {
        Double puntosDouble = puntos.getPuntos();
        Usuario usuario = usuarioRepository.findById(idUsuario).orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));
        // Aquí podrías agregar la lógica para registrar puntos, por ejemplo:
        usuario.setPuntos(puntosDouble);     // ← reemplaza el valor
        usuarioRepository.save(usuario);     // ← PERSISTE el cambio
        return " Puntos registrados para el usuario: " + usuario.getNombreUsuario() + " con ID: " + idUsuario;
    }

    @Override
    public List<RankingResponse> getRanking() {
        return usuarioRepository.findAll().stream()
                .map(u -> new RankingResponse(
                        u.getIdUsuario(),
                        u.getNombreUsuario(),
                        u.getPuntos() == null ? 0.0 : u.getPuntos()   // null-safe
                ))
                .sorted((a, b) -> Double.compare(
                        b.getPuntosTotales(), a.getPuntosTotales()     // DESC
                ))
                .toList();
    }

}
