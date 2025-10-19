package com.gym.gym_ver2.ejercicios.service;

import com.gym.gym_ver2.aplicaction.service.EjercicioService;
import com.gym.gym_ver2.domain.model.dto.EjercicioDTO;
import com.gym.gym_ver2.infraestructure.exceptions.RecursoNoEncontradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class obtenerEjerciciosTest {

    @Mock
    private EjercicioService ejercicioService;

    @InjectMocks
    private obtenerEjerciciosTest testInstance;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testObtenerEjerciciosSuccess() {
        List<EjercicioDTO> expectedEjercicios = Arrays.asList(
                new EjercicioDTO(1, "Ejercicio 1", "Descripción 1", "foto1.jpg", "Músculos 1", 5.0),
                new EjercicioDTO(2, "Ejercicio 2", "Descripción 2", "foto2.jpg", "Músculos 2", 6.0)
        );

        when(ejercicioService.obtenerEjercicios()).thenReturn(expectedEjercicios);

        List<EjercicioDTO> actualEjercicios = ejercicioService.obtenerEjercicios();

        assertEquals(expectedEjercicios, actualEjercicios);
    }

    @Test
    void testObtenerEjerciciosNotFound() {
        when(ejercicioService.obtenerEjercicios()).thenThrow(new RecursoNoEncontradoException("No se encontraron ejercicios"));

        Exception exception = assertThrows(RecursoNoEncontradoException.class, () -> {
            ejercicioService.obtenerEjercicios();
        });

        assertEquals("No se encontraron ejercicios", exception.getMessage());
    }
}
