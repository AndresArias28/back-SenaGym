package com.gym.gym_ver2.aplicaction.service;

import com.gym.gym_ver2.domain.model.dto.AsignacionResponse;
import com.gym.gym_ver2.domain.model.dto.AsignacionRutinaDTO;
import com.gym.gym_ver2.domain.model.dto.AsignacionesResponse;
import java.util.List;

public interface AsignacionRutinaService {

    AsignacionResponse asignarRutina(AsignacionRutinaDTO dto);

    List<AsignacionResponse> obtenerRutinaPorPersona(Integer idPersona);

    List<AsignacionesResponse> obtenerAllAsignaciones();

    AsignacionResponse actualizarAsignacion(Integer idAsignacion, AsignacionRutinaDTO dto);

}
