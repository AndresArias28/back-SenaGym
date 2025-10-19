package com.gym.gym_ver2.rutinaRealizada.service;

import com.gym.gym_ver2.aplicaction.service.RutinaRealizadaService;
import com.gym.gym_ver2.domain.model.entity.RutinaRealizada;
import com.gym.gym_ver2.domain.model.dto.IniciarRutinaRequest;
import com.gym.gym_ver2.infraestructure.exceptions.RecursoNoEncontradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class iniciarRutinaTest {

    @Mock
    private RutinaRealizadaService rutinaRealizadaService;

    @InjectMocks
    private iniciarRutinaTest testInstance;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIniciarRutinaSuccess() {
        IniciarRutinaRequest request = new IniciarRutinaRequest(1, 2);
        List<RutinaRealizada> expectedRutinas = new ArrayList<>();
        expectedRutinas.add(new RutinaRealizada());

        when(rutinaRealizadaService.iniciarRutina(request)).thenReturn(expectedRutinas);

        List<RutinaRealizada> actualRutinas = rutinaRealizadaService.iniciarRutina(request);

        assertEquals(expectedRutinas, actualRutinas);
    }

    @Test
    void testIniciarRutinaNotFound() {
        IniciarRutinaRequest request = new IniciarRutinaRequest(1, 2);

        when(rutinaRealizadaService.iniciarRutina(request)).thenThrow(new RecursoNoEncontradoException("Desafío no encontrado"));

        Exception exception = assertThrows(RecursoNoEncontradoException.class, () -> {
            rutinaRealizadaService.iniciarRutina(request);
        });

        assertEquals("Desafío no encontrado", exception.getMessage());
    }
}
