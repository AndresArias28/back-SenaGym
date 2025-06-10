package com.gym.gym_ver2.aplicaction.service;

import com.gym.gym_ver2.domain.model.dto.RutinaDTO;
import org.springframework.stereotype.Service;

import java.util.List;

public interface RutinaService {

    RutinaDTO crearRutina(RutinaDTO rutinaDTO);

    List<RutinaDTO> obtenerRutinas();

    void eliminarRutina(Integer id);

    RutinaDTO actualizarRutina(Integer id, RutinaDTO rutinaDTO);
}
