package com.gym.gym_ver2.infraestructure.controller;

import com.gym.gym_ver2.aplicaction.service.RutinaService;
import com.gym.gym_ver2.domain.model.dto.RutinaDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ObtenerRutinasTest {

    @Mock
    private RutinaService rutinaService;

    @InjectMocks
    private RutinaController rutinaController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testObtenerRutinas_Success() {
        List<RutinaDTO> expectedRutinas = new ArrayList<>();
        expectedRutinas.add(new RutinaDTO());
        expectedRutinas.add(new RutinaDTO());

        when(rutinaService.obtenerRutinas()).thenReturn(expectedRutinas);

        ResponseEntity<?> response = rutinaController.obtenerRutinas();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedRutinas, response.getBody());
        verify(rutinaService, times(1)).obtenerRutinas();
    }

    @Test
    void testObtenerRutinas_Exception() {
        when(rutinaService.obtenerRutinas()).thenThrow(new RuntimeException("Error al obtener rutinas"));

        ResponseEntity<?> response = rutinaController.obtenerRutinas();

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(rutinaService, times(1)).obtenerRutinas();
    }
}
