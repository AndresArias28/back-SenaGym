package com.gym.gym_ver2.aplicaction.service;

import com.gym.gym_ver2.domain.model.dto.ProgresoRequest;

import java.util.Map;

public interface ProgresoService {
    Map<String, Object> registrarOActualizarProgreso(ProgresoRequest progresoRequest);
}
