package com.gym.gym_ver2.aplicaction.service;

import com.gym.gym_ver2.domain.model.dto.DesafiosUsuarioDAO;

import java.util.List;

public interface DesafiosRealizadosService {
    public DesafiosUsuarioDAO obtenerDesafioActuaPorUsuario( Integer idUsuario);
}
