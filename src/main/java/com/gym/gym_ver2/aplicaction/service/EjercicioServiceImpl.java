package com.gym.gym_ver2.aplicaction.service;

import com.gym.gym_ver2.domain.model.dto.EjercicioDTO;
import com.gym.gym_ver2.domain.model.entity.Ejercicio;
import com.gym.gym_ver2.infraestructure.repository.EjercicioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EjercicioServiceImpl implements EjercicioService {

    private final EjercicioRepository ejercicioRepository;

    @Override
    @Transactional(readOnly = true)
    public List<EjercicioDTO> obtenerEjercicios() {

        List<Ejercicio> ejercicios = ejercicioRepository.findAll();

        return ejercicios.stream().map(ejercicio -> new EjercicioDTO(
                ejercicio.getIdEjercicio(),
                ejercicio.getNombreEjercicio(),
                ejercicio.getDescripcionEjercicio(),
                ejercicio.getFotoEjercicio(),
                ejercicio.getFotoEjercicio()

        )).toList();

    }
}
