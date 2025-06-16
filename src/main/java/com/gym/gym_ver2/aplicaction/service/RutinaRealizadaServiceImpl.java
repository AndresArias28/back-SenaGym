package com.gym.gym_ver2.aplicaction.service;

import com.gym.gym_ver2.domain.model.dto.RutinaRealizadaDTO;
import com.gym.gym_ver2.domain.model.entity.DesafioRealizado;
import com.gym.gym_ver2.domain.model.entity.RutinaEjercicio;
import com.gym.gym_ver2.domain.model.entity.RutinaRealizada;
import com.gym.gym_ver2.infraestructure.repository.DesafiosRealizadosRepository;
import com.gym.gym_ver2.infraestructure.repository.RutinaEjerciciosRepository;
import com.gym.gym_ver2.infraestructure.repository.RutinaRealizadaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RutinaRealizadaServiceImpl implements  RutinaRealizadaService {

    private final RutinaRealizadaRepository rutinaRealizadaRepository;
    private final DesafiosRealizadosRepository desafioUsuarioRepository;
    private final RutinaEjerciciosRepository rutinaEjerciciosRepository;

    @Override
    @Transactional
    public RutinaRealizadaDTO crearRutina(RutinaRealizadaDTO rutinaRealizadaDTO) {

        DesafioRealizado desafio = desafioUsuarioRepository.findById(rutinaRealizadaDTO.getDesafioRealizado().getIdDesafioRealizado())
                .orElseThrow(() -> new RuntimeException("Desafío no encontrado"));

        RutinaEjercicio rutinaEjercicio = rutinaEjerciciosRepository.findById(rutinaRealizadaDTO.getRutinaEjercicio().getIdRutinaEjercicio())
                .orElseThrow(() -> new RuntimeException("Rutina de ejercicio no encontrada"));

        RutinaRealizada rutinaRealizada = RutinaRealizada.builder()
                .desafioRealizado(desafio)
                .rutinaEjercicio(rutinaEjercicio)
                .series(rutinaRealizadaDTO.getSeriesRealizadas())
                .repeticiones(rutinaRealizadaDTO.getRepeticionesRealizadas())
                .carga(rutinaRealizadaDTO.getCargaRealizada())
                .estado(rutinaRealizadaDTO.getEstado())
                .build();

        RutinaRealizada nuevaRutina = rutinaRealizadaRepository.save(rutinaRealizada);

        return RutinaRealizadaDTO.builder()
                .desafioRealizado(nuevaRutina.getDesafioRealizado())
                .rutinaEjercicio(nuevaRutina.getRutinaEjercicio())
                .seriesRealizadas(nuevaRutina.getSeries())
                .repeticionesRealizadas(nuevaRutina.getRepeticiones())
                .cargaRealizada(nuevaRutina.getCarga())
                .estado(nuevaRutina.getEstado())
                .build();
    }


}
