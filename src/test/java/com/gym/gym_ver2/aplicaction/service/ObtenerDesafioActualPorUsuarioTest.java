package com.gym.gym_ver2.aplicaction.service;

import com.gym.gym_ver2.domain.model.dto.DesafiosUsuarioDAO;
import com.gym.gym_ver2.domain.model.entity.*;
import com.gym.gym_ver2.infraestructure.exceptions.RecursoNoEncontradoException;
import com.gym.gym_ver2.infraestructure.persistence.repository.AprendizRepository;
import com.gym.gym_ver2.infraestructure.persistence.repository.DesafioRealizadoRepository;
import com.gym.gym_ver2.infraestructure.persistence.repository.DesafioRepository;
import com.gym.gym_ver2.infraestructure.persistence.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ObtenerDesafioActualPorUsuarioTest {

    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private AprendizRepository aprendizRepository;
    @Mock
    private DesafioRepository desafioRepository;
    @Mock
    private DesafioRealizadoRepository desafioRealizadoRepository;

    @InjectMocks
    private DesafiosRealizadosServiceImpl desafiosRealizadosService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void cuandoUsuarioTieneDesafioEnProgreso_debeDevolverEseDesafio() {
        // Arrange
        Integer idUsuario = 1;
        Usuario usuario = new Usuario();

        Persona persona = new Aprendiz();
        persona.setIdPersona(1);
        usuario.setPersona(persona);

        persona.setIdPersona(1);
        usuario.setPersona(persona);

        Aprendiz aprendiz = new Aprendiz();
        DesafioRealizado desafioEnProgreso = new DesafioRealizado();
        desafioEnProgreso.setEstadoDesafio("En progreso");
        desafioEnProgreso.setFechaInicioDesafio(LocalDateTime.now());

        Desafio desafio = new Desafio();
        desafio.setIdDesafio(1);
        desafio.setNombreDesafio("Desafío de prueba");
        desafio.setNumeroDesafio(1);
        desafioEnProgreso.setDesafio(desafio);

        ArrayList<DesafioRealizado> desafiosRealizados = new ArrayList<>();
        desafiosRealizados.add(desafioEnProgreso);
        aprendiz.setDesafiosRealizados(desafiosRealizados);
        aprendiz.setPuntosAcumulados(100);

        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(usuario));
        when(aprendizRepository.findById(persona.getIdPersona())).thenReturn(Optional.of(aprendiz));

        // Act
        DesafiosUsuarioDAO resultado = desafiosRealizadosService.obtenerDesafioActuaPorUsuario(idUsuario);

        // Assert
        assertNotNull(resultado);
        assertEquals("En progreso", resultado.getEstadoDesafio());
        assertEquals(desafio.getIdDesafio(), resultado.getIdDesafio());
        assertEquals(desafio.getNombreDesafio(), resultado.getNombreDesafio());

        verify(usuarioRepository).findById(idUsuario);
        verify(aprendizRepository).findById(persona.getIdPersona());
    }

    @Test
    void cuandoNoHayDesafioEnProgreso_debeCrearNuevoDesafio() {
        // Arrange
        Integer idUsuario = 1;
        Usuario usuario = new Usuario();

        Persona persona = new Aprendiz();
        persona.setIdPersona(1);
        usuario.setPersona(persona);

        persona.setIdPersona(1);
        usuario.setPersona(persona);

        Aprendiz aprendiz = new Aprendiz();
        aprendiz.setDesafiosRealizados(new ArrayList<>());

        Desafio nuevoDesafio = new Desafio();
        nuevoDesafio.setIdDesafio(1);
        nuevoDesafio.setNombreDesafio("Nuevo Desafío");
        nuevoDesafio.setNumeroDesafio(1);

        DesafioRealizado nuevoDesafioRealizado = new DesafioRealizado();
        nuevoDesafioRealizado.setDesafio(nuevoDesafio);
        nuevoDesafioRealizado.setEstadoDesafio("En progreso");
        nuevoDesafioRealizado.setFechaInicioDesafio(LocalDateTime.now());

        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(usuario));
        when(aprendizRepository.findById(persona.getIdPersona())).thenReturn(Optional.of(aprendiz));
        when(desafioRepository.findByNumeroDesafio(1)).thenReturn(Optional.of(nuevoDesafio));
        when(desafioRealizadoRepository.save(any(DesafioRealizado.class))).thenReturn(nuevoDesafioRealizado);

        // Act
        DesafiosUsuarioDAO resultado = desafiosRealizadosService.obtenerDesafioActuaPorUsuario(idUsuario);

        // Assert
        assertNotNull(resultado);
        assertEquals("En progreso", resultado.getEstadoDesafio());
        assertEquals(nuevoDesafio.getIdDesafio(), resultado.getIdDesafio());
        assertEquals(nuevoDesafio.getNombreDesafio(), resultado.getNombreDesafio());

        verify(desafioRepository).findByNumeroDesafio(1);
        verify(desafioRealizadoRepository).save(any(DesafioRealizado.class));
    }

    @Test
    void cuandoUsuarioNoExiste_debeLanzarExcepcion() {
        // Arrange
        Integer idUsuario = 999;
        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RecursoNoEncontradoException.class, () -> {
            desafiosRealizadosService.obtenerDesafioActuaPorUsuario(idUsuario);
        });

        verify(usuarioRepository).findById(idUsuario);
        verifyNoInteractions(aprendizRepository, desafioRepository, desafioRealizadoRepository);
    }
}
