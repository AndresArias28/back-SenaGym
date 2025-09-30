package com.gym.gym_ver2.aplicaction.service;

import com.gym.gym_ver2.domain.model.dto.AsignacionResponse;
import com.gym.gym_ver2.domain.model.dto.AsignacionRutinaDTO;
import com.gym.gym_ver2.domain.model.dto.AsignacionesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


public interface AsignacionRutinaService {

    AsignacionResponse asignarRutina(AsignacionRutinaDTO dto);

    AsignacionResponse obtenerRutinaPorPersona(Integer idPersona);

    List<AsignacionesResponse> obtenerAllAsignaciones();
}
