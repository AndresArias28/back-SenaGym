package com.gym.gym_ver2.aplicaction.service;

import com.gym.gym_ver2.domain.model.dto.AprendicesDashDTO;
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

class AdminGetAprendicesDashTest {

    @Mock
    private AprendizRepository aprendizRepository;

    @InjectMocks
    private AdminServiceImpl adminService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAprendicesDash_ShouldReturnListOfAprendicesDashDTO() {
        // Arrange
        AprendicesDashDTO aprendiz1 = new AprendicesDashDTO(1, "Juan", 12345, "Avanzado", 50, 100);
        AprendicesDashDTO aprendiz2 = new AprendicesDashDTO(2, "Ana", 12346, "Intermedio", 40, 80);

        List<AprendicesDashDTO> aprendices = Arrays.asList(aprendiz1, aprendiz2);

        when(aprendizRepository.obtenerAprendicesParaDashboard()).thenReturn(aprendices);

        List<AprendicesDashDTO> result = adminService.getAprendicesDash();

        assertEquals(2, result.size());
        assertEquals("Juan", result.get(0).getNombre());
        assertEquals("Ana", result.get(1).getNombre());
        verify(aprendizRepository, times(1)).obtenerAprendicesParaDashboard();
    }
}
