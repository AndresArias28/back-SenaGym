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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ObtenerRutinaPorIdTest {

    @Mock
    private RutinaService rutinaService;

    @InjectMocks
    private RutinaController rutinaController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testObtenerRutinaPorId_Success() {
        Integer id = 1;
        RutinaDTO expectedRutina = new RutinaDTO();

        when(rutinaService.obtenerRutinaPorId(id)).thenReturn(expectedRutina);

        ResponseEntity<RutinaDTO> response = rutinaController.obtenerRutina(id);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedRutina, response.getBody());
        verify(rutinaService, times(1)).obtenerRutinaPorId(id);
    }

    @Test
    void testObtenerRutinaPorId_NotFound() {
        Integer id = 1;

        when(rutinaService.obtenerRutinaPorId(id)).thenReturn(null);

        ResponseEntity<RutinaDTO> response = rutinaController.obtenerRutina(id);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(rutinaService, times(1)).obtenerRutinaPorId(id);
    }

    @Test
    void testObtenerRutinaPorId_Exception() {
        Integer id = 1;

        when(rutinaService.obtenerRutinaPorId(id)).thenThrow(new RuntimeException("Error al obtener rutina"));

        ResponseEntity<RutinaDTO> response = rutinaController.obtenerRutina(id);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(rutinaService, times(1)).obtenerRutinaPorId(id);
    }
}
