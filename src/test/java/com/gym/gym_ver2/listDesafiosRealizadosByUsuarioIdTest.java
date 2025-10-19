package com.gym.gym_ver2;

import com.gym.gym_ver2.aplicaction.service.DesafiosRealizadosService;
import com.gym.gym_ver2.domain.model.dto.DesafioRealizadoDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class listDesafiosRealizadosByUsuarioIdTest {

    @Mock
    private DesafiosRealizadosService desafiosRealizadosService;

    @InjectMocks
    private listDesafiosRealizadosByUsuarioIdTest testInstance;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListDesafiosRealizadosByUsuarioId() {
        Integer idUsuario = 1;
        List<DesafioRealizadoDao> expectedDesafios = Arrays.asList(
                new DesafioRealizadoDao(
                        1,                // idDesafioRealizado
                        10,               // idDesafio
                        "Completado",     // estado
                        LocalDateTime.now(), // fechaInicioDesafio
                        LocalDateTime.now().plusHours(1), // fechaFinDesafio
                        250.5,            // caloriasTotales
                        100               // puntosObtenidos
                ),
                new DesafioRealizadoDao(
                        2,                // idDesafioRealizado
                        11,               // idDesafio
                        "Completado",     // estado
                        LocalDateTime.now(), // fechaInicioDesafio
                        LocalDateTime.now().plusHours(1), // fechaFinDesafio
                        280.5,            // caloriasTotales
                        100               // puntosObtenidos
                )
        );

        when(desafiosRealizadosService.listDesafiosRealizadosByUsuarioId(idUsuario)).thenReturn(expectedDesafios);

        List<DesafioRealizadoDao> actualDesafios = desafiosRealizadosService.listDesafiosRealizadosByUsuarioId(idUsuario);

        assertEquals(expectedDesafios, actualDesafios);
    }
}
