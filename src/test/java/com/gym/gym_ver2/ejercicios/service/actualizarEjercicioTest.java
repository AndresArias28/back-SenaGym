package com.gym.gym_ver2.ejercicios.service;

import com.gym.gym_ver2.aplicaction.service.EjercicioService;
import com.gym.gym_ver2.domain.model.dto.EjercicioDTO;
import com.gym.gym_ver2.domain.model.dto.ExercisesCreateDTO;
import com.gym.gym_ver2.infraestructure.exceptions.RecursoNoEncontradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class actualizarEjercicioTest {

    @Mock
    private EjercicioService ejercicioService;

    @InjectMocks
    private actualizarEjercicioTest testInstance;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testActualizarEjercicioSuccess() {
        Integer id = 1;
        ExercisesCreateDTO datos = new ExercisesCreateDTO("Ejercicio Actualizado", "Descripción Actualizada", null, "Músculos Actualizados", 7.0);
        EjercicioDTO expectedEjercicio = new EjercicioDTO(1, "Ejercicio Actualizado", "Descripción Actualizada", "fotoActualizada.jpg", "Músculos Actualizados", 7.0);

        when(ejercicioService.actualizarEjercicio(id, datos)).thenReturn(expectedEjercicio);

        EjercicioDTO actualEjercicio = ejercicioService.actualizarEjercicio(id, datos);

        assertEquals(expectedEjercicio, actualEjercicio);
    }

    @Test
    void testActualizarEjercicioNotFound() {
        Integer id = 1;
        ExercisesCreateDTO datos = new ExercisesCreateDTO("Ejercicio Actualizado", "Descripción Actualizada", null, "Músculos Actualizados", 7.0);

        when(ejercicioService.actualizarEjercicio(id, datos)).thenThrow(new RecursoNoEncontradoException("Ejercicio no encontrado con ID: " + id));

        Exception exception = assertThrows(RecursoNoEncontradoException.class, () -> {
            ejercicioService.actualizarEjercicio(id, datos);
        });

        assertEquals("Ejercicio no encontrado con ID: " + id, exception.getMessage());
    }

    @Test
    void testActualizarEjercicioImageUploadError() {
        Integer id = 1;
        ExercisesCreateDTO datos = new ExercisesCreateDTO("Ejercicio Actualizado", "Descripción Actualizada", null, "Músculos Actualizados", 7.0);

        when(ejercicioService.actualizarEjercicio(id, datos)).thenThrow(new RuntimeException("Error al subir la imagen"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            ejercicioService.actualizarEjercicio(id, datos);
        });

        assertEquals("Error al subir la imagen", exception.getMessage());
    }
}
