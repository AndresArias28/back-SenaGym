package com.gym.gym_ver2.aplicaction.service;

import com.gym.gym_ver2.domain.model.dto.*;
import com.gym.gym_ver2.domain.model.entity.RutinaRealizada;
import com.gym.gym_ver2.domain.model.requestModels.SerieAvanceRequest;

import java.util.List;

public interface RutinaRealizadaService {

    SerieAvanceResponse avanzarSerie(SerieAvanceRequest serieAvanceRq);

    String actualizarFechaInicio(Integer idRutinaRealizada);

    List<RutinaRealizada> iniciarRutina(IniciarRutinaRequest request);
}
