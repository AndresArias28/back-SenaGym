package com.gym.gym_ver2.aplicaction.service;

import com.gym.gym_ver2.domain.model.dto.IniciarRutinaRequest;
import com.gym.gym_ver2.domain.model.entity.RutinaRealizada;
import java.util.List;


public interface ProgresoService {
//    Map<String, Object> registrarOActualizarProgreso(ProgresoRequest progresoRequest);

    List<RutinaRealizada> iniciarRutina(IniciarRutinaRequest request);
}
