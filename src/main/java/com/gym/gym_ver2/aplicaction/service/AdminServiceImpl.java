package com.gym.gym_ver2.aplicaction.service;

import com.gym.gym_ver2.domain.model.dto.AdminDTO;
import com.gym.gym_ver2.domain.model.dto.AprendicesDashDTO;
import com.gym.gym_ver2.domain.model.dto.AprendizRanking;
import com.gym.gym_ver2.domain.model.dto.FrecuenciaAprendizDTO;
import com.gym.gym_ver2.domain.model.dto.responseDTO.ValidacionRutinaResponse;
import com.gym.gym_ver2.domain.model.entity.*;
import com.gym.gym_ver2.domain.model.requestModels.RegisterAdminRequest;
import com.gym.gym_ver2.infraestructure.auth.AuthResponse;
import com.gym.gym_ver2.infraestructure.exceptions.RecursoNoEncontradoException;
import com.gym.gym_ver2.infraestructure.jwt.JwtService;
import com.gym.gym_ver2.infraestructure.persistence.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements  AdminService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final EmployRepository employRepository;
    private final DesafiosRealizadosRepository desafiosRealizadosRepository;
    private final AprendizRepository aprendizRepository;
    private final RutinaRepository rutinaRepository;
    private final RutinaEjerciciosRepository rutinaEjerciciosRepository;
    private final RutinaRealizadaRepository rutinaRealizadaRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;


    @Override
    @Transactional
    public List<AdminDTO> getAdmins() {
        Rol rol = rolRepository.findById(2).orElseThrow(() -> new IllegalArgumentException("Rol no encontrado"));
        List<Usuario> usuariosAdmins = usuarioRepository.findAllByRol(rol);
        return usuariosAdmins.stream()
                .map(usr -> new AdminDTO(
                        usr.getPersona().getIdPersona(),
                        usr.getNombreUsuario(),
                        usr.getEmailUsuario(),
                        usr.getIdRol().getIdRol()
                ))
                .toList();
    }

    @Override
    public void registerQR(String qr, Integer idAdmin) {
        Empleado empleado = employRepository.findById(idAdmin)
                .orElseThrow(() -> new RecursoNoEncontradoException("Empleado no encontrado"));
        empleado.setCodigoQr(qr);
        employRepository.save(empleado);
    }

    @Override
    @Transactional
    public ValidacionRutinaResponse validarQr(String codigoQR, Integer idDesafioRealizado) {
        if (codigoQR == null || codigoQR.isEmpty()) {
            throw new RecursoNoEncontradoException("El código QR no puede ser nulo o vacío");
        }


        boolean existeQR = employRepository.findByCodigoQr(codigoQR).isPresent();
        if (!existeQR) {
            throw new RecursoNoEncontradoException("Código QR no válido");
        }

        DesafioRealizado desafioRealizado = desafiosRealizadosRepository.findById(idDesafioRealizado).orElseThrow(() -> new RecursoNoEncontradoException("Desafío no encontrado"));

        Aprendiz aprendiz = aprendizRepository.findById(desafioRealizado.getAprendiz().getIdPersona()).orElseThrow(() -> new RecursoNoEncontradoException("Aprendiz no encontrado"));

        log.info("Aprendiz encontrado: id={}, nombre={}, doc={}, peso={}",
                aprendiz.getIdPersona(),
                aprendiz.getNombres(),
                aprendiz.getIdentificacion(),
                aprendiz.getPeso()
        );

        Double pesoKg = aprendiz.getPeso();

        Rutina rutina = obtenerRutinaDesdeDesafioRealizado(idDesafioRealizado);

        log.info("rutina encontrada: nombre={}, id={}",
                rutina.getNombre(),
                rutina.getIdRutina()
        );

        List<RutinaEjercicio> ejercicios = rutinaEjerciciosRepository.findByRutina(rutina);
        if (ejercicios == null || ejercicios.isEmpty()) {
            throw new IllegalStateException("La rutina no tiene ejercicios asignados");
        }

        double promedioMets = ejercicios.stream()
                .mapToDouble(ej -> ej.getEjercicio().getMet())
                .average()
                .orElseThrow(() -> new IllegalStateException("No se encontraron ejercicios con METS válidos"));

        // 6. Calcular total en minutos
        long duracionTotalSegundos = ejercicios.stream()
                .mapToLong(ej -> ej.getSeries() * ej.getDuracion()) // duracion en seg por serie
                .sum();

        double duracionTotalMinutos = duracionTotalSegundos / 60.0;

        double calorias = (promedioMets * 3.5 * pesoKg * duracionTotalMinutos) / 200.0;

        String dif = Optional.ofNullable(rutina.getDificultad()).orElse(rutina.getDificultad()).toString();

        log.info("el nivel es: {}", dif);

        int puntosGanados;

        switch (dif) {
            case "PRINCIPIANTE" -> puntosGanados = 50;
            case "INTERMEDIO"   -> puntosGanados = 75;
            case "AVANZADO"     -> puntosGanados = 100;
            default             -> puntosGanados = 0;
        }

        int puntosPrevios = Optional.ofNullable(aprendiz.getPuntosAcumulados()).orElse(0);
        int horasPrevias  = Optional.ofNullable(aprendiz.getHorasAcumuladas()).orElse(0);

        int puntosTotales = puntosPrevios + puntosGanados;

        // Cada 100 puntos = 1 hora
        int horasPorPuntos = puntosTotales / 100;

        // Solo sumamos si hay nuevas horas adicionales
        int horasActualizadas = Math.max(horasPrevias, horasPorPuntos);

        desafioRealizado.setPuntosObtenidos(puntosGanados);
        desafioRealizado.setCaloriasTotales(calorias);
        desafioRealizado.setFechaFinDesafio(LocalDateTime.now());
        desafioRealizado.setEstadoDesafio("Finalizado");
        desafiosRealizadosRepository.save(desafioRealizado);

        aprendiz.setPuntosAcumulados(puntosTotales);
        aprendiz.setHorasAcumuladas(horasActualizadas);
        aprendizRepository.save(aprendiz);
        return new ValidacionRutinaResponse("Rutina validada con éxito", calorias);
    }

    public Rutina obtenerRutinaDesdeDesafioRealizado(Integer idDesafioRealizado) {
        RutinaRealizada rutinaRealizada = rutinaRealizadaRepository.findFirstByDesafioRealizado_IdDesafioRealizado(idDesafioRealizado)
                .orElseThrow(() -> new RecursoNoEncontradoException("Rutina realizada no encontrada para el desafío"));
        return rutinaRealizada.getRutinaEjercicio().getRutina();
    }

    @Override
    public AuthResponse registerAdmin(RegisterAdminRequest adminRequest) {
        Rol rol = rolRepository.findById(2)
                .orElseThrow(() -> new RecursoNoEncontradoException("Rol no encontrado"));
        Empleado admin = new Empleado();
        admin.setNombres(adminRequest.getNombreAdmin());
        admin.setApellidos(adminRequest.getApellidoAdmin());
        admin.setIdentificacion(adminRequest.getCedulaAdmin());

        admin = employRepository.save(admin);

        Usuario savedUser = Usuario.builder()
                .persona(admin)
                .idRol(rol)
                .nombreUsuario(adminRequest.getNombreAdmin())
                .emailUsuario(adminRequest.getEmailAdmin())
                .contrasenaUsuario(passwordEncoder.encode(adminRequest.getContrasenaAdmin()))
                .build();
        usuarioRepository.save(savedUser);
        System.out.println("Admin registrado: " + savedUser.getNombreUsuario() + ", ID: " + admin.getIdPersona());
        return AuthResponse.builder().token(jwtService.createToken(savedUser)).build();
    }

    @Override
    public Aprendiz guardarFrecuenciaCardiaca(Integer idPersona, Integer frecuencia) {
        Aprendiz aprendiz = aprendizRepository.findById(idPersona)
                .orElseThrow(() -> new RecursoNoEncontradoException("Aprendiz no encontrado"));
        aprendiz.setFrecuenciaCardiaca(frecuencia);
        return  aprendizRepository.save(aprendiz);
    }

    @Override
    public List<AprendizRanking> obtenerTop20Aprendices() {
        return aprendizRepository.findTop20ByOrderByPuntosAcumuladosDesc().stream().map(a -> new AprendizRanking(
                a.getIdPersona(),
                a.getNombres(),
                a.getApellidos(),
                a.getHorasAcumuladas(),
                a.getPuntosAcumulados()
        )).toList();
    }

    @Override
    public FrecuenciaAprendizDTO obtenerFrecuenciaCardiaca(int idPersona) {
        Aprendiz aprendiz = aprendizRepository.findById(idPersona)
                .orElseThrow(() -> new RecursoNoEncontradoException("Aprendiz no encontrado"));
        return new FrecuenciaAprendizDTO(
                aprendiz.getIdPersona(),
                Optional.ofNullable(aprendiz.getFrecuenciaCardiaca()).orElse(0)
        );
    }

    @Override
    public List<AprendicesDashDTO> getAprendicesDash(){
        List<AprendicesDashDTO> aprendices = aprendizRepository.obtenerAprendicesParaDashboard();
        return  aprendices;

    }


}
