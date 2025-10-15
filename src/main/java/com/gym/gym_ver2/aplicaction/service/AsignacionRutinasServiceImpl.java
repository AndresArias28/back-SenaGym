package com.gym.gym_ver2.aplicaction.service;

import com.gym.gym_ver2.domain.model.dto.AsignacionResponse;
import com.gym.gym_ver2.domain.model.dto.AsignacionRutinaDTO;
import com.gym.gym_ver2.domain.model.dto.AsignacionesResponse;
import com.gym.gym_ver2.domain.model.entity.Aprendiz;
import com.gym.gym_ver2.domain.model.entity.AsignacionRutina;
import com.gym.gym_ver2.domain.model.entity.Rutina;
import com.gym.gym_ver2.infraestructure.exceptions.RecursoNoEncontradoException;
import com.gym.gym_ver2.infraestructure.persistence.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

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
                .diaAsignado(dto.getDiasAsignado())
                .build();

        asignacionRutinaRepository.save(asignacionRutina);

        return AsignacionResponse.builder()
                .idAsignacion(asignacionRutina.getIdAsignacionRutina())
                .idPersona(aprendiz.getIdPersona())
                .idRutina(rutina.getIdRutina())
                .observaciones(asignacionRutina.getObservaciones())
                .fechaAsignacion(asignacionRutina.getFechaAsignacion())
                .diasAsignado(asignacionRutina.getDiaAsignado())
                .fechaFinalizacion(asignacionRutina.getFechaFinalizacion())
                .build();
    }

    @Override
    public List<AsignacionResponse> obtenerRutinaPorPersona(Integer idPersona) {
        List<AsignacionRutina> asignaciones = asignacionRutinaRepository.findByAprendiz_IdPersona(idPersona);

        if (asignaciones.isEmpty()) {
            throw new RecursoNoEncontradoException("No se encontraron asignaciones de rutina para el aprendiz con ID: " + idPersona);
        }
        return asignaciones.stream()
                .map(asignacion -> AsignacionResponse.builder()
                        .idAsignacion(asignacion.getIdAsignacionRutina())
                        .idPersona(asignacion.getAprendiz().getIdPersona())
                        .idRutina(asignacion.getRutina().getIdRutina())
                        .observaciones(asignacion.getObservaciones())
                        .fechaAsignacion(asignacion.getFechaAsignacion())
                        .diasAsignado(asignacion.getDiaAsignado())
                        .fechaFinalizacion(asignacion.getFechaFinalizacion())
                        .build()
                )
                .toList();
    }

    @Override
    public List<AsignacionesResponse> obtenerAllAsignaciones() {
        return asignacionRutinaRepository.listarAsignacionesConAprendiz();
    }

    @Override
    @Transactional
    public AsignacionResponse actualizarAsignacion(Integer idAsignacion, AsignacionRutinaDTO dto) {
        AsignacionRutina asignacion = asignacionRutinaRepository.findById(idAsignacion)
                .orElseThrow(() -> new RecursoNoEncontradoException("Asignación no encontrada con ID: " + idAsignacion));

        asignacion.setObservaciones(dto.getObservaciones());

            if (dto.getIdRutina() == null) {
                asignacion.setRutina(null);
                asignacion.setFechaFinalizacion(LocalDateTime.now());
            }else{
            Rutina rutina = rutinaRepository.findById(dto.getIdRutina())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Rutina no encontrada con ID: " + dto.getIdRutina()));
            asignacion.setRutina(rutina);
            asignacion.setFechaFinalizacion(null);
        }

        // 🔹 Si quieres, puedes también actualizar el día asignado
        if (dto.getDiasAsignado() != null) {
            asignacion.setDiaAsignado(dto.getDiasAsignado());
        }

        asignacionRutinaRepository.save(asignacion);

        return AsignacionResponse.builder().idAsignacion(asignacion.getIdAsignacionRutina())
                .idPersona(asignacion.getAprendiz().getIdPersona())
                .idRutina(asignacion.getRutina() != null ? asignacion.getRutina().getIdRutina() : null)
                .observaciones(asignacion.getObservaciones())
                .fechaAsignacion(asignacion.getFechaAsignacion())
                .diasAsignado(asignacion.getDiaAsignado())
                .fechaFinalizacion(asignacion.getFechaFinalizacion())
                .build();
    }

}
