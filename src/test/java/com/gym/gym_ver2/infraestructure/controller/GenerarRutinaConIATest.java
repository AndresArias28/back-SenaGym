package com.gym.gym_ver2.infraestructure.controller;

import com.gym.gym_ver2.aplicaction.service.RutinaService;
import com.gym.gym_ver2.domain.model.dto.SolicitudRutinaDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GenerarRutinaConIATest {

    @Mock
    private RutinaService rutinaService;

    @InjectMocks
    private RutinaController rutinaController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerarRutinaConIA_Success() {
        SolicitudRutinaDTO datos = new SolicitudRutinaDTO();
        String expectedRutina = "Rutina generada con éxito";

        when(rutinaService.generarRutinaConIA(datos)).thenReturn(expectedRutina);

        ResponseEntity<?> response = rutinaController.generarRutina(datos);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedRutina, response.getBody());
        verify(rutinaService, times(1)).generarRutinaConIA(datos);
    }

    @Test
    void testGenerarRutinaConIA_Exception() {
        SolicitudRutinaDTO datos = new SolicitudRutinaDTO();

        when(rutinaService.generarRutinaConIA(datos)).thenThrow(new RuntimeException("Error al generar rutina"));

        ResponseEntity<?> response = rutinaController.generarRutina(datos);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(rutinaService, times(1)).generarRutinaConIA(datos);
    }
}
