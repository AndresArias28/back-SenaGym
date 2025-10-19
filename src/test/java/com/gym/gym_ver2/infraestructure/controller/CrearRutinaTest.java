package com.gym.gym_ver2.infraestructure.controller;

import com.gym.gym_ver2.aplicaction.service.RutinaService;
import com.gym.gym_ver2.domain.model.dto.RutinaCreateDTO;
import com.gym.gym_ver2.domain.model.dto.RutinaDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CrearRutinaTest {

    @Mock
    private RutinaService rutinaService;

    @InjectMocks
    private RutinaController rutinaController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCrearRutina_Success() {
        RutinaCreateDTO datos = new RutinaCreateDTO();
        MultipartFile fotoRutina = mock(MultipartFile.class);
        datos.setFotoRutina(fotoRutina);

        RutinaDTO expectedRutina = new RutinaDTO();
        when(rutinaService.crearRutina(datos)).thenReturn(expectedRutina);

        ResponseEntity<RutinaDTO> response = rutinaController.crearRutina(datos, fotoRutina);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedRutina, response.getBody());
        verify(rutinaService, times(1)).crearRutina(datos);
    }

    @Test
    void testCrearRutina_Exception() {
        RutinaCreateDTO datos = new RutinaCreateDTO();
        MultipartFile fotoRutina = mock(MultipartFile.class);
        datos.setFotoRutina(fotoRutina);

        when(rutinaService.crearRutina(datos)).thenThrow(new RuntimeException("Error al crear rutina"));

        ResponseEntity<RutinaDTO> response = rutinaController.crearRutina(datos, fotoRutina);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(rutinaService, times(1)).crearRutina(datos);
    }
}
