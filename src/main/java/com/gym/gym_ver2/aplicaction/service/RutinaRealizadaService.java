package com.gym.gym_ver2.aplicaction.service;

import com.gym.gym_ver2.domain.model.dto.RutinaRealizadaDTO;
import com.gym.gym_ver2.domain.model.dto.SerieAvanceRequest;
import com.gym.gym_ver2.domain.model.dto.SerieAvanceResponse;

public interface RutinaRealizadaService {

    RutinaRealizadaDTO crearRutina(RutinaRealizadaDTO rutinaRealizadaDTO);

    SerieAvanceResponse avanzarSerie(SerieAvanceRequest serieAvanceRq);
}
