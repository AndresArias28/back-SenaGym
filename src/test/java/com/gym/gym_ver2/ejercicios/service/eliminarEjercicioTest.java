package com.gym.gym_ver2.ejercicios.service;

import com.gym.gym_ver2.aplicaction.service.EjercicioService;
import com.gym.gym_ver2.infraestructure.exceptions.RecursoNoEncontradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class eliminarEjercicioTest {

    @Mock
    private EjercicioService ejercicioService;

    @InjectMocks
    private eliminarEjercicioTest testInstance;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testEliminarEjercicioSuccess() {
        Integer id = 1;

        doNothing().when(ejercicioService).eliminarEjercicio(id);

        assertDoesNotThrow(() -> ejercicioService.eliminarEjercicio(id));

        verify(ejercicioService, times(1)).eliminarEjercicio(id);
    }

    @Test
    void testEliminarEjercicioNotFound() {
        Integer id = 1;

        doThrow(new RecursoNoEncontradoException("Ejercicio no encontrado con ID: " + id))
                .when(ejercicioService).eliminarEjercicio(id);

        Exception exception = assertThrows(RecursoNoEncontradoException.class, () -> {
            ejercicioService.eliminarEjercicio(id);
        });

        assertEquals("Ejercicio no encontrado con ID: " + id, exception.getMessage());
    }
}
