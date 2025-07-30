package com.gym.gym_ver2.aplicaction.service;

import com.gym.gym_ver2.domain.model.dto.EjercicioDTO;
import com.gym.gym_ver2.domain.model.dto.ExcerciseDTO;
import com.gym.gym_ver2.domain.model.dto.ExercisesCreateDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EjercicioService {

    List<EjercicioDTO> obtenerEjercicios();

    EjercicioDTO crearEjercicio(ExcerciseDTO datos, MultipartFile fotoEjercicio);

    EjercicioDTO actualizarEjercicio(Integer id, ExercisesCreateDTO datos);

    void eliminarEjercicio(Integer id);
}