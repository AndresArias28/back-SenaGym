package com.gym.gym_ver2.aplicaction.service;

import com.gym.gym_ver2.domain.model.dto.*;
import com.gym.gym_ver2.domain.model.requestModels.SerieAvanceRequest;
import com.gym.gym_ver2.domain.model.dto.SerieAvanceResponse;
import com.gym.gym_ver2.domain.model.entity.DesafioRealizado;
import com.gym.gym_ver2.domain.model.entity.RutinaEjercicio;
import com.gym.gym_ver2.domain.model.entity.RutinaRealizada;
import com.gym.gym_ver2.infraestructure.exceptions.RecursoNoEncontradoException;
import com.gym.gym_ver2.infraestructure.persistence.repository.DesafiosRealizadosRepository;
import com.gym.gym_ver2.infraestructure.persistence.repository.RutinaEjerciciosRepository;
import com.gym.gym_ver2.infraestructure.persistence.repository.RutinaRealizadaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RutinaRealizadaServiceImpl implements  RutinaRealizadaService {

    private final RutinaRealizadaRepository rutinaRealizadaRepository;
    private final DesafiosRealizadosRepository desafioUsuarioRepository;
    private final RutinaEjerciciosRepository rutinaEjerciciosRepository;
    private final DesafiosRealizadosRepository desafiosRealizadosRepository;

    @Override
    public SerieAvanceResponse avanzarSerie(SerieAvanceRequest request) {
        RutinaRealizada progreso = rutinaRealizadaRepository
                .findByDesafioRealizado_IdDesafioRealizadoAndRutinaEjercicio_IdRutinaEjercicio(
                        request.getIdDesafioRealizado(),
                        request.getIdRutinaEjercicio()
                )
                .orElseThrow(() -> new RecursoNoEncontradoException("Progreso no encontrado"));

        int nuevasSeries = progreso.getSeries() + 1;
        progreso.setSeries(nuevasSeries);
        int repeticionesEsperadas = progreso.getRutinaEjercicio().getRepeticiones();
        int repeticionesActuales = progreso.getRepeticiones();
        progreso.setRepeticiones(repeticionesEsperadas + repeticionesActuales);

        // Verificar si completó el ejercicio
        int objetivoSeries = progreso.getRutinaEjercicio().getSeries();
        boolean ejercicioCompletado = nuevasSeries >= objetivoSeries;

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
            DesafioRealizado desafio = desafioUsuarioRepository.findById(request.getIdDesafioRealizado())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Desafío no encontrado"));
            desafio.setEstadoDesafio("Finalizado");
            desafio.setFechaFinDesafio(LocalDateTime.now());
            desafioUsuarioRepository.save(desafio);
        }
        return new SerieAvanceResponse(
                progreso.getSeries(),
                objetivoSeries,
                ejercicioCompletado,
                rutinaFinalizada
        );
    }

    @Override
    public String actualizarFechaInicio(Integer id) {
        DesafioRealizado desafioResgistrado = desafioUsuarioRepository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Desafio realizado no encontrado"));

        desafioResgistrado.setFechaInicioDesafio(LocalDateTime.now());

        desafioUsuarioRepository.save(desafioResgistrado);

        return "Fecha de inicio actualizada correctamente";
    }


    @Override
    public List<RutinaRealizada> iniciarRutina(IniciarRutinaRequest request) {
        List<RutinaEjercicio> ejercicios = rutinaEjerciciosRepository.findAllByRutina_IdRutina(request.getIdRutina());

        DesafioRealizado desafio = desafiosRealizadosRepository.findById(request.getIdDesafioRealizado())
                .orElseThrow(() -> new RecursoNoEncontradoException("Desafío no encontrado"));

        List<RutinaRealizada> registrosCreados = new ArrayList<>();

        for (RutinaEjercicio ejercicio : ejercicios) {
            boolean yaExiste = rutinaRealizadaRepository
                    .findByDesafioRealizado_IdDesafioRealizadoAndRutinaEjercicio_IdRutinaEjercicio(request.getIdDesafioRealizado(), ejercicio.getIdRutinaEjercicio())
                    .isPresent();
            if (!yaExiste) {
                RutinaRealizada nueva = new RutinaRealizada();
                nueva.setDesafioRealizado(desafio);
                nueva.setRutinaEjercicio(ejercicio);
                nueva.setSeries(0);
                nueva.setRepeticiones(0);
                nueva.setEstado("En Progreso");
                registrosCreados.add(rutinaRealizadaRepository.save(nueva));
            }
        }
        return registrosCreados;
    }
}
