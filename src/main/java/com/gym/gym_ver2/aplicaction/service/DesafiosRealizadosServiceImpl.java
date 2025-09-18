package com.gym.gym_ver2.aplicaction.service;

import com.gym.gym_ver2.domain.model.dto.DesafioRealizadoResponse;
import com.gym.gym_ver2.domain.model.dto.DesafiosUsuarioDAO;
import com.gym.gym_ver2.domain.model.entity.Aprendiz;
import com.gym.gym_ver2.domain.model.entity.Desafio;
import com.gym.gym_ver2.domain.model.entity.DesafioRealizado;
import com.gym.gym_ver2.domain.model.entity.Usuario;
import com.gym.gym_ver2.infraestructure.exceptions.RecursoNoEncontradoException;
import com.gym.gym_ver2.infraestructure.persistence.repository.AprendizRepository;
import com.gym.gym_ver2.infraestructure.persistence.repository.DesafioRealizadoRepository;
import com.gym.gym_ver2.infraestructure.persistence.repository.DesafioRepository;
import com.gym.gym_ver2.infraestructure.persistence.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DesafiosRealizadosServiceImpl implements  DesafiosRealizadosService {

    private final UsuarioRepository usuarioRepository;
    private final AprendizRepository aprendizRepository;
    private final DesafioRepository desafioRepository;
    private final DesafioRealizadoRepository desafioRealizadoRepository;

    @Override
    public DesafiosUsuarioDAO obtenerDesafioActuaPorUsuario( Integer idUsuario) {

        Usuario user = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));

        Integer IdPersona = user.getPersona().getIdPersona();

        Aprendiz aprendiz = aprendizRepository.findById(IdPersona)
                .orElseThrow(() -> new RecursoNoEncontradoException("Aprendiz no encontrado"));

        List<DesafioRealizado> desafiosRealizados  =  aprendiz.getDesafiosRealizados();

        // buscar si hay uno en progreso
        Optional<DesafioRealizado> enProgreso = desafiosRealizados.stream()
                .filter(d -> "En progreso".equalsIgnoreCase(d.getEstadoDesafio()))
                .findFirst();

        DesafioRealizado desafioRealizado;

        if (enProgreso.isPresent() ) {
            desafioRealizado = enProgreso.get();
        } else {
            // Todos finalizados, se debe iniciar uno nuevo
            int numeroSiguiente = desafiosRealizados.size() + 1;

            Desafio desafio = desafioRepository.findByNumeroDesafio(numeroSiguiente)
                    .orElseThrow(() -> new RecursoNoEncontradoException("Desafio no encontrado para el número: " + numeroSiguiente));

            desafioRealizado = new DesafioRealizado();
            desafioRealizado.setDesafio(desafio);
            desafioRealizado.setAprendiz(aprendiz);
            desafioRealizado.getDesafio().setNumeroDesafio(numeroSiguiente);
            desafioRealizado.setEstadoDesafio("En progreso");
            desafioRealizado.setFechaInicioDesafio(LocalDateTime.now());
            desafioRealizado.setFechaFinDesafio(null);
            desafioRealizado = desafioRealizadoRepository.save(desafioRealizado);
        }

        //obtener puntos del aprendiz
        int puntos = desafioRealizado.getRutinasRealizadas() != null
                ? desafioRealizado.getRutinasRealizadas().stream()
                .mapToInt(r -> r.getDesafioRealizado().getAprendiz().getPuntosAcumulados() != null ? r.getDesafioRealizado().getAprendiz().getPuntosAcumulados() : 0)
                .sum()
                : 0;

        DesafiosUsuarioDAO desafiosUsuarioDAO = new DesafiosUsuarioDAO();

        desafiosUsuarioDAO.setIdDesafioRealiado(desafioRealizado.getIdDesafioRealizado());
        desafiosUsuarioDAO.setIdDesafio(desafioRealizado.getDesafio().getIdDesafio());
        desafiosUsuarioDAO.setNombreDesafio(desafioRealizado.getDesafio().getNombreDesafio());
        desafiosUsuarioDAO.setNumeroDesafio(desafioRealizado.getDesafio().getNumeroDesafio());
        desafiosUsuarioDAO.setEstadoDesafio(desafioRealizado.getEstadoDesafio());
        desafiosUsuarioDAO.setFechaInicioDesafio(desafioRealizado.getFechaInicioDesafio());
        desafiosUsuarioDAO.setFechaFinDesafio(desafioRealizado.getFechaFinDesafio());
        desafiosUsuarioDAO.setPuntosAcumulados(puntos);
        return  desafiosUsuarioDAO;
    }

}
