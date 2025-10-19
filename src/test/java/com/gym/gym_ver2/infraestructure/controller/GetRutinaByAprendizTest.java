package com.gym.gym_ver2.infraestructure.controller;

import com.gym.gym_ver2.aplicaction.service.RutinaService;
import com.gym.gym_ver2.domain.model.dto.RutinaAprendizDTO;
import com.gym.gym_ver2.domain.model.entity.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetRutinaByAprendizTest {

    @Mock
    private RutinaService rutinaService;

    @InjectMocks
    private RutinaController rutinaController;

    @Mock
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetRutinaByAprendiz_Success() {
        int idUsuario = 1;
        when(usuario.getIdUsuario()).thenReturn(idUsuario);

        List<RutinaAprendizDTO> expectedRutinas = new ArrayList<>();
        expectedRutinas.add(new RutinaAprendizDTO());
        expectedRutinas.add(new RutinaAprendizDTO());

        when(rutinaService.getRutinaByAprendiz(idUsuario)).thenReturn(expectedRutinas);

        ResponseEntity<List<RutinaAprendizDTO>> response = rutinaController.getRutinaByAprendiz(usuario);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedRutinas, response.getBody());
        verify(rutinaService, times(1)).getRutinaByAprendiz(idUsuario);
    }

    @Test
    void testGetRutinaByAprendiz_EmptyList() {
        int idUsuario = 1;
        when(usuario.getIdUsuario()).thenReturn(idUsuario);

        List<RutinaAprendizDTO> expectedRutinas = new ArrayList<>();
        when(rutinaService.getRutinaByAprendiz(idUsuario)).thenReturn(expectedRutinas);

        ResponseEntity<List<RutinaAprendizDTO>> response = rutinaController.getRutinaByAprendiz(usuario);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
        verify(rutinaService, times(1)).getRutinaByAprendiz(idUsuario);
    }
}
