package com.gym.gym_ver2.aplicaction.service;

import com.gym.gym_ver2.domain.model.dto.DesafioRealizadoResponse;
import com.gym.gym_ver2.domain.model.dto.DesafiosUsuarioDAO;
import com.gym.gym_ver2.domain.model.entity.Aprendiz;
import com.gym.gym_ver2.domain.model.entity.Desafio;
import com.gym.gym_ver2.domain.model.entity.DesafioRealizado;
import com.gym.gym_ver2.domain.model.entity.Usuario;
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

         //obtener Usuario logueado
        Usuario user = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        //obtener idPersona
        Integer IdPersona = user.getPersona().getIdPersona();

        //obtener aprendiz por idPersona
        Aprendiz aprendiz = aprendizRepository.findById(IdPersona)
                .orElseThrow(() -> new RuntimeException("Aprendiz no encontrado"));

        List<DesafioRealizado> desafiosRealizados  =  aprendiz.getDesafiosRealizados();

        // 4. Buscar si hay uno en progreso
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
                    .orElseThrow(() -> new RuntimeException("Desafio no encontrado para el número: " + numeroSiguiente));

            desafioRealizado = new DesafioRealizado();
            desafioRealizado.setDesafio(desafio);
            desafioRealizado.setAprendiz(aprendiz);
            desafioRealizado.setEstadoDesafio("En progreso");
            desafioRealizado.setFechaInicioDesafio(desafioRealizado.getFechaInicioDesafio());
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

        desafiosUsuarioDAO.setIdDesafio(desafioRealizado.getDesafio().getIdDesafio());
        desafiosUsuarioDAO.setNombreDesafio(desafioRealizado.getDesafio().getNombreDesafio());
        desafiosUsuarioDAO.setNumeroDesafio(desafioRealizado.getDesafio().getNumeroDesafio());
        desafiosUsuarioDAO.setEstadoDesafio(desafioRealizado.getEstadoDesafio());
        desafiosUsuarioDAO.setFechaInicioDesafio(desafioRealizado.getFechaInicioDesafio());
        desafiosUsuarioDAO.setFechaFinDesafio(desafioRealizado.getFechaFinDesafio());
        desafiosUsuarioDAO.setPuntosAcumulados(puntos);
        return  desafiosUsuarioDAO;
    }

    @Override
    public DesafioRealizadoResponse crearDesafioRealizado(Integer idAprendiz, Integer idDesafio) {
        if (idDesafio == null || idAprendiz == null) {
            throw new IllegalArgumentException("ID del desafío o del aprendiz no puede ser null");
        }

        Desafio desafio = desafioRepository.findById(idDesafio)
                .orElseThrow(() -> new RuntimeException("Desafío no encontrado"));

        Aprendiz aprendiz = aprendizRepository.findById(idAprendiz)
                .orElseThrow(() -> new RuntimeException("Aprendiz no encontrado"));

        DesafioRealizado nuevo = DesafioRealizado.builder()
                .desafio(desafio)
                .aprendiz(aprendiz)
                .fechaInicioDesafio(LocalDateTime.now())
                .estadoDesafio("En Progreso")
                .build();

        DesafioRealizado guardado = desafioRealizadoRepository.save(nuevo);

        return new DesafioRealizadoResponse(
                guardado.getIdDesafioRealizado(),
                guardado.getEstadoDesafio()
        );
    }

}
