package com.gym.gym_ver2.rutinaRealizada.service;

import com.gym.gym_ver2.aplicaction.service.RutinaRealizadaService;
import com.gym.gym_ver2.domain.model.entity.DesafioRealizado;
import com.gym.gym_ver2.infraestructure.exceptions.RecursoNoEncontradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class actualizarFechaInicioTest {

    @Mock
    private RutinaRealizadaService rutinaRealizadaService;

    @InjectMocks
    private actualizarFechaInicioTest testInstance;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testActualizarFechaInicioSuccess() {
        Integer id = 1;
        String expectedMessage = "Fecha de inicio actualizada correctamente";

        when(rutinaRealizadaService.actualizarFechaInicio(id)).thenReturn(expectedMessage);

        String actualMessage = rutinaRealizadaService.actualizarFechaInicio(id);

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void testActualizarFechaInicioNotFound() {
        Integer id = 1;

        when(rutinaRealizadaService.actualizarFechaInicio(id)).thenThrow(new RecursoNoEncontradoException("Desafio realizado no encontrado"));

        Exception exception = assertThrows(RecursoNoEncontradoException.class, () -> {
            rutinaRealizadaService.actualizarFechaInicio(id);
        });

        assertEquals("Desafio realizado no encontrado", exception.getMessage());
    }
}
