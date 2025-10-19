package com.gym.gym_ver2.asignacionRutinas;

import com.gym.gym_ver2.aplicaction.service.AsignacionRutinasServiceImpl;
import com.gym.gym_ver2.domain.model.dto.AsignacionResponse;
import com.gym.gym_ver2.domain.model.entity.Aprendiz;
import com.gym.gym_ver2.domain.model.entity.AsignacionRutina;
import com.gym.gym_ver2.domain.model.entity.Rutina;
import com.gym.gym_ver2.infraestructure.exceptions.RecursoNoEncontradoException;
import com.gym.gym_ver2.infraestructure.persistence.repository.AsignacionRutinaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class AsignacionRutinaServiceTest {

    @Mock
    private AsignacionRutinaRepository asignacionRutinaRepository;

    @InjectMocks
    private AsignacionRutinasServiceImpl asignacionRutinasService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deberiaRecuperarTodasLasAsignacionesDeRutinas() {
        Aprendiz aprendiz = new Aprendiz();
        aprendiz.setIdPersona(1);

        Rutina rutina = new Rutina();
        rutina.setIdRutina(1);

        AsignacionRutina asignacion = new AsignacionRutina();
        asignacion.setIdAsignacionRutina(5);
        asignacion.setAprendiz(aprendiz);
        asignacion.setRutina(rutina);
        asignacion.setObservaciones("Observaciones de prueba");
        asignacion.setDiaAsignado("Lunes");
        asignacion.setFechaFinalizacion(LocalDateTime.now());

        when(asignacionRutinaRepository.findByAprendiz_IdPersona(1)).thenReturn(List.of(asignacion));

        List<AsignacionResponse> resultado = asignacionRutinasService.obtenerRutinaPorPersona(1);

        assertEquals(1, resultado.size());
        assertEquals("Lunes", resultado.get(0).getDiasAsignado());
    }

    @Test
    void deberiaLanzarExcepcionCuandoNoSeEncuentraAsignaciones() {
        when(asignacionRutinaRepository.findByAprendiz_IdPersona(1)).thenReturn(List.of());

        assertThrows(RecursoNoEncontradoException.class, () -> asignacionRutinasService.obtenerRutinaPorPersona(1));
    }
}
