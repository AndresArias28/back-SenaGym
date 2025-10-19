package com.gym.gym_ver2.aplicaction.service;

import com.gym.gym_ver2.domain.model.entity.Rutina;
import com.gym.gym_ver2.domain.model.entity.RutinaEjercicio;
import com.gym.gym_ver2.domain.model.entity.RutinaRealizada;
import com.gym.gym_ver2.infraestructure.exceptions.RecursoNoEncontradoException;
import com.gym.gym_ver2.infraestructure.persistence.repository.RutinaRealizadaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ObtenerRutinaDesdeDesafioRealizadoTest {

    @Mock
    private RutinaRealizadaRepository rutinaRealizadaRepository;

    @InjectMocks
    private AdminServiceImpl adminService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testObtenerRutinaDesdeDesafioRealizado_Success() {
        Integer idDesafioRealizado = 1;
        Rutina rutina = new Rutina();
        RutinaRealizada rutinaRealizada = mock(RutinaRealizada.class);
        RutinaEjercicio rutinaEjercicio = mock(RutinaEjercicio.class);

        when(rutinaRealizadaRepository.findFirstByDesafioRealizado_IdDesafioRealizado(idDesafioRealizado))
                .thenReturn(Optional.of(rutinaRealizada));
        when(rutinaRealizada.getRutinaEjercicio()).thenReturn(rutinaEjercicio);
        when(rutinaEjercicio.getRutina()).thenReturn(rutina);

        Rutina result = adminService.obtenerRutinaDesdeDesafioRealizado(idDesafioRealizado);

        assertNotNull(result);
        assertEquals(rutina, result);
        verify(rutinaRealizadaRepository, times(1))
                .findFirstByDesafioRealizado_IdDesafioRealizado(idDesafioRealizado);
    }

    @Test
    void testObtenerRutinaDesdeDesafioRealizado_NotFound() {
        Integer idDesafioRealizado = 1;

        when(rutinaRealizadaRepository.findFirstByDesafioRealizado_IdDesafioRealizado(idDesafioRealizado))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(RecursoNoEncontradoException.class, () ->
                adminService.obtenerRutinaDesdeDesafioRealizado(idDesafioRealizado));

        assertEquals("Rutina realizada no encontrada para el desafío", exception.getMessage());
        verify(rutinaRealizadaRepository, times(1))
                .findFirstByDesafioRealizado_IdDesafioRealizado(idDesafioRealizado);
    }
}
