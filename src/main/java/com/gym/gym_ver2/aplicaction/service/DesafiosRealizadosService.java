package com.gym.gym_ver2.aplicaction.service;

import com.gym.gym_ver2.domain.model.dto.DesafioRealizadoResponse;
import com.gym.gym_ver2.domain.model.dto.DesafiosUsuarioDAO;

import java.util.List;

public interface DesafiosRealizadosService {
    DesafiosUsuarioDAO obtenerDesafioActuaPorUsuario( Integer idUsuario);

//    DesafioRealizadoResponse crearDesafioRealizado(Integer idAprendiz, Integer idDesafio);
}
