package com.gym.gym_ver2.aplicaction.service;

import com.gym.gym_ver2.domain.model.dto.AsignacionResponse;
import com.gym.gym_ver2.domain.model.dto.AsignacionRutinaDTO;
import com.gym.gym_ver2.domain.model.entity.Aprendiz;
import com.gym.gym_ver2.domain.model.entity.Rutina;
import com.gym.gym_ver2.domain.model.entity.AsignacionRutina;
import com.gym.gym_ver2.infraestructure.exceptions.RecursoNoEncontradoException;
import com.gym.gym_ver2.infraestructure.persistence.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDateTime;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AsignacionRutinasServiceImplTest {
    @Mock
    private AprendizRepository aprendizRepository;
    @Mock
    private RutinaEjerciciosRepository rutinaEjerciciosRepository;
    @Mock
    private AsignacionRutinaRepository asignacionRutinaRepository;
    @Mock
    private RutinaRepository rutinaRepository;

    @InjectMocks
    private AsignacionRutinasServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void asignarRutina_deberiaAsignarRutinaCorrectamente() {
        // Arrange
        Integer idPersona = 1;
        Integer idRutina = 2;
        String observaciones = "Observaciones de prueba";
        String diasAsignado = "Lunes";
        AsignacionRutinaDTO dto = AsignacionRutinaDTO.builder()
                .idPersona(idPersona)
                .idRutina(idRutina)
                .observaciones(observaciones)
                .diasAsignado(diasAsignado)
                .build();

        Aprendiz aprendiz = Aprendiz.builder().idPersona(idPersona).build();
        Rutina rutina = Rutina.builder().idRutina(idRutina).build();
        AsignacionRutina asignacionRutina = AsignacionRutina.builder()
                .idAsignacionRutina(10)
                .aprendiz(aprendiz)
                .rutina(rutina)
                .observaciones(observaciones)
                .fechaAsignacion(LocalDateTime.of(2025, 10, 15, 0, 0))
                .fechaFinalizacion(null)
                .diaAsignado(diasAsignado)
                .build();

        when(aprendizRepository.findById(idPersona)).thenReturn(Optional.of(aprendiz));
        when(rutinaRepository.findById(idRutina)).thenReturn(Optional.of(rutina));
        when(asignacionRutinaRepository.save(any(AsignacionRutina.class))).thenAnswer(invocation -> {
            AsignacionRutina arg = invocation.getArgument(0);
            arg.setIdAsignacionRutina(10); // Simula el ID generado
            return arg;
        });

        // Act
        AsignacionResponse response = service.asignarRutina(dto);

        // Assert
        assertNotNull(response);
        assertEquals(10, response.getIdAsignacion());
        assertEquals(idPersona, response.getIdPersona());
        assertEquals(idRutina, response.getIdRutina());
        assertEquals(observaciones, response.getObservaciones());
        assertEquals(diasAsignado, response.getDiasAsignado());
        assertNull(response.getFechaFinalizacion());
        assertNotNull(response.getFechaAsignacion());
        verify(asignacionRutinaRepository, times(1)).save(any(AsignacionRutina.class));
    }

    @Test
    void asignarRutina_deberiaLanzarExcepcionSiIdPersonaEsNull() {
        AsignacionRutinaDTO dto = AsignacionRutinaDTO.builder()
                .idPersona(null)
                .idRutina(1)
                .build();
        assertThrows(RecursoNoEncontradoException.class, () -> service.asignarRutina(dto));
    }

    @Test
    void asignarRutina_deberiaLanzarExcepcionSiIdRutinaEsNull() {
        AsignacionRutinaDTO dto = AsignacionRutinaDTO.builder()
                .idPersona(1)
                .idRutina(null)
                .build();
        assertThrows(RecursoNoEncontradoException.class, () -> service.asignarRutina(dto));
    }

    @Test
    void asignarRutina_deberiaLanzarExcepcionSiAprendizNoExiste() {
        when(aprendizRepository.findById(1)).thenReturn(Optional.empty());
        AsignacionRutinaDTO dto = AsignacionRutinaDTO.builder()
                .idPersona(1)
                .idRutina(2)
                .build();
        assertThrows(RecursoNoEncontradoException.class, () -> service.asignarRutina(dto));
    }

    @Test
    void asignarRutina_deberiaLanzarExcepcionSiRutinaNoExiste() {
        Aprendiz aprendiz = Aprendiz.builder().idPersona(1).build();
        when(aprendizRepository.findById(1)).thenReturn(Optional.of(aprendiz));
        when(rutinaRepository.findById(2)).thenReturn(Optional.empty());
        AsignacionRutinaDTO dto = AsignacionRutinaDTO.builder()
                .idPersona(1)
                .idRutina(2)
                .build();
        assertThrows(RecursoNoEncontradoException.class, () -> service.asignarRutina(dto));
    }
}

