package com.gym.gym_ver2.aplicaction.service;

import com.gym.gym_ver2.domain.model.dto.AsignacionResponse;
import com.gym.gym_ver2.domain.model.dto.AsignacionRutinaDTO;
import com.gym.gym_ver2.domain.model.entity.Aprendiz;
import com.gym.gym_ver2.domain.model.entity.AsignacionRutina;
import com.gym.gym_ver2.domain.model.entity.Rutina;
import com.gym.gym_ver2.domain.model.entity.RutinaEjercicio;
import com.gym.gym_ver2.infraestructure.exceptions.RecursoNoEncontradoException;
import com.gym.gym_ver2.infraestructure.persistence.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AsignacionRutinasServiceImpl implements  AsignacionRutinaService{

    private final AprendizRepository aprendizRepository;
    private final RutinaEjerciciosRepository rutinaEjerciciosRepository;
    private final AsignacionRutinaRepository asignacionRutinaRepository;
    private final RutinaRepository rutinaRepository;

    @Override
    @Transactional
    public AsignacionResponse asignarRutina(AsignacionRutinaDTO dto) {

        if (dto.getIdPersona() == null) {
            throw new RecursoNoEncontradoException("El ID del aprendiz no puede ser nulo");
        }
        if (dto.getIdRutina() == null) {
            throw new RecursoNoEncontradoException("El ID de la rutina no puede ser nulo");
        }
        Aprendiz aprendiz = aprendizRepository.findById(dto.getIdPersona())
                .orElseThrow(() -> new RecursoNoEncontradoException("Aprendiz no encontrado"));

        Rutina rutina = rutinaRepository.findById(dto.getIdRutina())
                .orElseThrow(() -> new RecursoNoEncontradoException("Rutina no encontrada de veras"));

        AsignacionRutina asignacionRutina = AsignacionRutina.builder()
                .aprendiz(aprendiz)
                .rutina(rutina)
                .observaciones(dto.getObservaciones())
                .fechaAsignacion(LocalDateTime.now())
                .fechaFinalizacion(null)
                .diasAsignado(dto.getDiasAsignado())
                .build();

        asignacionRutinaRepository.save(asignacionRutina);

        return AsignacionResponse.builder()
                .idAsignacion(asignacionRutina.getIdAsignacionRutina())
                .idPersona(aprendiz.getIdPersona())
                .idRutina(rutina.getIdRutina())
                .observaciones(asignacionRutina.getObservaciones())
                .fechaAsignacion(asignacionRutina.getFechaAsignacion())
                .diasAsignado(asignacionRutina.getDiasAsignado())
                .fechaFinalizacion(asignacionRutina.getFechaFinalizacion())
                .build();
    }
}
