package com.gym.gym_ver2.rutinaRealizada.service;

import com.gym.gym_ver2.aplicaction.service.RutinaRealizadaService;
import com.gym.gym_ver2.domain.model.requestModels.SerieAvanceRequest;
import com.gym.gym_ver2.domain.model.dto.SerieAvanceResponse;
import com.gym.gym_ver2.infraestructure.exceptions.RecursoNoEncontradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class avanzarSerieTest {

    @Mock
    private RutinaRealizadaService rutinaRealizadaService;

    @InjectMocks
    private avanzarSerieTest testInstance;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAvanzarSerieSuccess() {
        SerieAvanceRequest request = new SerieAvanceRequest(1, 2);
        SerieAvanceResponse expectedResponse = new SerieAvanceResponse(3, 15);

        when(rutinaRealizadaService.avanzarSerie(request)).thenReturn(expectedResponse);

        SerieAvanceResponse actualResponse = rutinaRealizadaService.avanzarSerie(request);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void testAvanzarSerieNotFound() {
        SerieAvanceRequest request = new SerieAvanceRequest(1, 2);

        when(rutinaRealizadaService.avanzarSerie(request)).thenThrow(new RecursoNoEncontradoException("Progreso no encontrado"));

        Exception exception = assertThrows(RecursoNoEncontradoException.class, () -> {
            rutinaRealizadaService.avanzarSerie(request);
        });

        assertEquals("Progreso no encontrado", exception.getMessage());
    }
}
