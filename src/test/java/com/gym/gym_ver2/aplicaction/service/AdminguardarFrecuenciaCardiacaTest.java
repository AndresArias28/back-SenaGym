package com.gym.gym_ver2.aplicaction.service;

import com.gym.gym_ver2.domain.model.entity.Aprendiz;
import com.gym.gym_ver2.infraestructure.exceptions.RecursoNoEncontradoException;
import com.gym.gym_ver2.infraestructure.persistence.repository.AprendizRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminguardarFrecuenciaCardiacaTest {

    @Mock
    private AprendizRepository  aprendizRepository;

    @InjectMocks
    private AdminServiceImpl adminService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void guardarFrecuenciaCardiaca_ShouldUpdateFrecuencia_WhenAprendizExists() {

        Integer idPersona = 1;
        Integer frecuencia = 75;
        Aprendiz aprendiz = new Aprendiz();
        aprendiz.setIdPersona(idPersona);

        when(aprendizRepository.findById(idPersona)).thenReturn(Optional.of(aprendiz));
        when(aprendizRepository.save(aprendiz)).thenReturn(aprendiz);

        Aprendiz result = adminService.guardarFrecuenciaCardiaca(idPersona, frecuencia);

        assertEquals(frecuencia, result.getFrecuenciaCardiaca());
        verify(aprendizRepository, times(1)).save(aprendiz);
    }

    @Test
    void guardarFrecuenciaCardiaca_ShouldThrowException_WhenAprendizDoesNotExist() {
        // Arrange
        Integer idPersona = 1;
        Integer frecuencia = 75;

        when(aprendizRepository.findById(idPersona)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RecursoNoEncontradoException.class, () ->
                adminService.guardarFrecuenciaCardiaca(idPersona, frecuencia));

        assertEquals("Aprendiz no encontrado", exception.getMessage());
        verify(aprendizRepository, never()).save(any(Aprendiz.class));
    }
}
