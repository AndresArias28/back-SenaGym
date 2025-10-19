package com.gym.gym_ver2.aplicaction.service;

import com.gym.gym_ver2.domain.model.entity.Empleado;
import com.gym.gym_ver2.infraestructure.exceptions.RecursoNoEncontradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminServiceImplTest {

    @Mock
    private com.gym.gym_ver2.infraestructure.persistence.repository.EmployRepository  employRepository;

    @InjectMocks
    private AdminServiceImpl adminService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerQR_ShouldUpdateEmployeeQR_WhenEmployeeExists() {
        // Arrange
        String qrCode = "testQR";
        Integer adminId = 1;
        Empleado empleado = new Empleado();
        empleado.setIdPersona(adminId);
        when(employRepository.findById(adminId)).thenReturn(Optional.of(empleado));

        adminService.registerQR(qrCode, adminId);
        assertEquals(qrCode, empleado.getCodigoQr());
        verify(employRepository, times(1)).save(empleado);
    }

    @Test
    void registerQR_ShouldThrowException_WhenEmployeeDoesNotExist() {
        // Arrange
        String qrCode = "testQR";
        Integer adminId = 1;

        when(employRepository.findById(adminId)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RecursoNoEncontradoException.class, () ->
                adminService.registerQR(qrCode, adminId));

        assertEquals("Empleado no encontrado", exception.getMessage());
        verify(employRepository, never()).save(any(Empleado.class));
    }
}
