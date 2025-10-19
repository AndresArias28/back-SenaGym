package com.gym.gym_ver2.ejercicios.service;

import com.gym.gym_ver2.aplicaction.service.EjercicioService;
import com.gym.gym_ver2.domain.model.dto.EjercicioDTO;
import com.gym.gym_ver2.domain.model.dto.ExcerciseDTO;
import com.gym.gym_ver2.infraestructure.exceptions.RecursoNoEncontradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class crearEjercicioTest {

    @Mock
    private EjercicioService ejercicioService;

    @Mock
    private MultipartFile mockFile;

    @InjectMocks
    private crearEjercicioTest testInstance;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCrearEjercicioSuccess() {
        ExcerciseDTO datos = new ExcerciseDTO("Ejercicio 1", "Descripción 1", "Músculos 1", 5.0);
        EjercicioDTO expectedEjercicio = new EjercicioDTO(1, "Ejercicio 1", "Descripción 1", "foto1.jpg", "Músculos 1", 5.0);

        when(ejercicioService.crearEjercicio(datos, mockFile)).thenReturn(expectedEjercicio);

        EjercicioDTO actualEjercicio = ejercicioService.crearEjercicio(datos, mockFile);

        assertEquals(expectedEjercicio, actualEjercicio);
    }

    @Test
    void testCrearEjercicioImageUploadError() {
        ExcerciseDTO datos = new ExcerciseDTO("Ejercicio 1", "Descripción 1", "Músculos 1", 5.0);

        when(ejercicioService.crearEjercicio(datos, mockFile)).thenThrow(new RuntimeException("Error al subir la imagen"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            ejercicioService.crearEjercicio(datos, mockFile);
        });

        assertEquals("Error al subir la imagen", exception.getMessage());
    }
}
