package com.gym.gym_ver2.aplicaction.service;

import com.gym.gym_ver2.domain.model.dto.AprendizRanking;
import com.gym.gym_ver2.domain.model.entity.Aprendiz;
import com.gym.gym_ver2.infraestructure.persistence.repository.AprendizRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminObtenerTop20AprendicesTest {

    @Mock
    private AprendizRepository aprendizRepository;

    @InjectMocks
    private AdminServiceImpl adminService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void obtenerTop20Aprendices_ShouldReturnTop20Aprendices() {
        // Arrange
        Aprendiz aprendiz1 = Aprendiz.builder()
                .idPersona(1)
                .nombres("Juan")
                .apellidos("Pérez")
                .puntosAcumulados(50)
                .horasAcumuladas(100)
                .build();

        Aprendiz aprendiz2 = Aprendiz.builder()
                .idPersona(2)
                .nombres("Ana")
                .apellidos("Gómez")
                .puntosAcumulados(70)
                .horasAcumuladas(80)
                .build();

        List<Aprendiz> aprendices = Arrays.asList(aprendiz1, aprendiz2);

        when(aprendizRepository.findTop20ByOrderByPuntosAcumuladosDesc()).thenReturn(aprendices);

        List<AprendizRanking> result = adminService.obtenerTop20Aprendices();

        assertEquals(2, result.size());
        assertEquals("Juan", result.get(0).getNombres());
        assertEquals("Ana", result.get(1).getNombres());
        verify(aprendizRepository, times(1)).findTop20ByOrderByPuntosAcumuladosDesc();
    }
}
