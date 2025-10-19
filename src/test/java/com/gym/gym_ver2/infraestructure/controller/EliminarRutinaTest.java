package com.gym.gym_ver2.infraestructure.controller;

import com.gym.gym_ver2.aplicaction.service.RutinaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EliminarRutinaTest {

    @Mock
    private RutinaService rutinaService;

    @InjectMocks
    private RutinaController rutinaController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testEliminarRutina_Success() {
        Integer id = 1;

        ResponseEntity<Map<String, String>> response = rutinaController.eliminarRutina(id);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().containsKey("mensaje"));
        assertEquals("Rutina eliminada exitosamente.", response.getBody().get("mensaje"));
        verify(rutinaService, times(1)).eliminarRutina(id);
    }

    @Test
    void testEliminarRutina_NotFound() {
        Integer id = 1;
        doThrow(new RuntimeException("Rutina no encontrada"))
                .when(rutinaService).eliminarRutina(id);

        ResponseEntity<Map<String, String>> response = rutinaController.eliminarRutina(id);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().containsKey("error"));
        assertEquals("Rutina no encontrada con ID: 1", response.getBody().get("error"));
        verify(rutinaService, times(1)).eliminarRutina(id);
    }


}
