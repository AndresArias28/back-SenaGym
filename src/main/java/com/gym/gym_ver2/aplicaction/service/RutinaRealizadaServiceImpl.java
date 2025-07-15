package com.gym.gym_ver2.aplicaction.service;

import com.gym.gym_ver2.domain.model.dto.SerieAvanceRequest;
import com.gym.gym_ver2.domain.model.dto.SerieAvanceResponse;
import com.gym.gym_ver2.domain.model.entity.DesafioRealizado;
import com.gym.gym_ver2.domain.model.entity.RutinaRealizada;
import com.gym.gym_ver2.infraestructure.exceptions.RecursoNoEncontradoException;
import com.gym.gym_ver2.infraestructure.persistence.repository.DesafiosRealizadosRepository;
import com.gym.gym_ver2.infraestructure.persistence.repository.RutinaEjerciciosRepository;
import com.gym.gym_ver2.infraestructure.persistence.repository.RutinaRealizadaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RutinaRealizadaServiceImpl implements  RutinaRealizadaService {

    private final RutinaRealizadaRepository rutinaRealizadaRepository;
    private final DesafiosRealizadosRepository desafiRealizadoRepository;
    private final RutinaEjerciciosRepository rutinaEjerciciosRepository;

    @Override
    public SerieAvanceResponse avanzarSerie(SerieAvanceRequest request) {
        RutinaRealizada progreso = rutinaRealizadaRepository
                .findByDesafioRealizado_IdDesafioRealizadoAndRutinaEjercicio_IdRutinaEjercicio(
                        request.getIdDesafioRealizado(),
                        request.getIdRutinaEjercicio()
                )
                .orElseThrow(() -> new RecursoNoEncontradoException("Progreso no encontrado"));
        // Incrementar serie
        progreso.setSeries(progreso.getSeries() + 1);
        // Verificar si completó el ejercicio
        int objetivo = progreso.getRutinaEjercicio().getSeries();
        boolean ejercicioCompletado = progreso.getSeries() >= objetivo;
        if (ejercicioCompletado) {
            progreso.setEstado("Finalizado");
        }
        rutinaRealizadaRepository.save(progreso);

        // Verificar si todos los ejercicios de la rutina ya están completados
        List<RutinaRealizada> ejercicios = rutinaRealizadaRepository
                .findAllByDesafioRealizado_IdDesafioRealizado(request.getIdDesafioRealizado());

        boolean rutinaFinalizada = ejercicios.stream()
                .allMatch(e -> e.getSeries() >= e.getRutinaEjercicio().getSeries());

        if (rutinaFinalizada) {
            DesafioRealizado desafio = desafiRealizadoRepository.findById(request.getIdDesafioRealizado())
                    .orElseThrow(() -> new RuntimeException("Desafío no encontrado"));
            desafio.setEstadoDesafio("Finalizado");
            desafio.setFechaFinDesafio(LocalDateTime.now());
            desafiRealizadoRepository.save(desafio);
        }
        return new SerieAvanceResponse(
                progreso.getSeries(),
                objetivo,
                ejercicioCompletado,
                rutinaFinalizada
        );
    }

    @Override
    public String actualizarFechaInicio(Integer id) {
        DesafioRealizado desafioResgistrado = desafiRealizadoRepository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Desafio realizado no encontrado"));

        desafioResgistrado.setFechaInicioDesafio(LocalDateTime.now());

        desafiRealizadoRepository.save(desafioResgistrado);

        return "Fecha de inicio actualizada correctamente";
    }


}
