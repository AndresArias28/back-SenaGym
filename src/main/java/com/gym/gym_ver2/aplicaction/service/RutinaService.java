package com.gym.gym_ver2.aplicaction.service;

import com.gym.gym_ver2.domain.model.dto.RutinaAprendizDTO;
import com.gym.gym_ver2.domain.model.dto.RutinaCreateDTO;
import com.gym.gym_ver2.domain.model.dto.RutinaDTO;
import com.gym.gym_ver2.domain.model.dto.SolicitudRutinaDTO;
import java.io.IOException;
import java.util.List;

public interface RutinaService {

    RutinaDTO crearRutina(RutinaCreateDTO rutinaDTO);

    List<RutinaDTO> obtenerRutinas();

    void eliminarRutina(Integer id);

    RutinaDTO actualizarRutina(Integer id, RutinaCreateDTO rutinaDTO) throws IOException;

    RutinaDTO obtenerRutinaPorId(Integer id);

    String generarRutinaConIA(SolicitudRutinaDTO datos);

    List<RutinaAprendizDTO> getRutinaByAprendiz(int idUsuario);
}
