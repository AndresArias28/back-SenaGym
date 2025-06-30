package com.gym.gym_ver2.aplicaction.service;

import com.gym.gym_ver2.domain.model.dto.EjercicioDTO;
import com.gym.gym_ver2.domain.model.dto.ExercisesCreateDTO;

import java.util.List;

public interface EjercicioService {

    List<EjercicioDTO> obtenerEjercicios();

    EjercicioDTO crearEjercicio(ExercisesCreateDTO datos);
}
