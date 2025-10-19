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

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ActualizarRutinaTest {

    @Mock
    private RutinaService rutinaService;

    @InjectMocks
    private RutinaController rutinaController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testActualizarRutina_Success() throws IOException {
        Integer id = 1;
        RutinaCreateDTO datos = new RutinaCreateDTO();
        MultipartFile fotoRutina = mock(MultipartFile.class);
        datos.setFotoRutina(fotoRutina);

        RutinaDTO expectedRutina = new RutinaDTO();
        when(rutinaService.actualizarRutina(id, datos)).thenReturn(expectedRutina);

        ResponseEntity<RutinaDTO> response = rutinaController.actualizarRutina(id, datos, fotoRutina);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedRutina, response.getBody());
        verify(rutinaService, times(1)).actualizarRutina(id, datos);
    }

    @Test
    void testActualizarRutina_Exception() throws IOException {
        Integer id = 1;
        RutinaCreateDTO datos = new RutinaCreateDTO();
        MultipartFile fotoRutina = mock(MultipartFile.class);
        datos.setFotoRutina(fotoRutina);

        when(rutinaService.actualizarRutina(id, datos)).thenThrow(new RuntimeException("Error al actualizar rutina"));

        ResponseEntity<RutinaDTO> response = rutinaController.actualizarRutina(id, datos, fotoRutina);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(rutinaService, times(1)).actualizarRutina(id, datos);
    }
}
