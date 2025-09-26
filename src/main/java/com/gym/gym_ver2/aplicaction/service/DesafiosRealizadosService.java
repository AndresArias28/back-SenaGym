package com.gym.gym_ver2.aplicaction.service;

import com.gym.gym_ver2.domain.model.dto.DesafioRealizadoDao;
import com.gym.gym_ver2.domain.model.dto.DesafiosUsuarioDAO;
import com.gym.gym_ver2.domain.model.dto.responseDTO.DesafiosDeUsuarios;

import java.util.List;

public interface DesafiosRealizadosService {
    DesafiosUsuarioDAO obtenerDesafioActuaPorUsuario( Integer idUsuario);

    List<DesafiosDeUsuarios> obtenerDesafiosPorUsuario(Integer idUsuario);

    List<DesafioRealizadoDao> listDesafiosRealizadosByUsuarioId(Integer idUsuario);
}
