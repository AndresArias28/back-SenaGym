package com.gym.gym_ver2.aplicaction.service;

import com.gym.gym_ver2.domain.model.dto.AsignacionResponse;
import com.gym.gym_ver2.domain.model.dto.AsignacionRutinaDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


public interface AsignacionRutinaService {

    AsignacionResponse asignarRutina(AsignacionRutinaDTO dto);

    AsignacionResponse obtenerRutinaPorPersona(Integer idPersona);
}
