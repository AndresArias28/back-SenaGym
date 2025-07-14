package com.gym.gym_ver2.aplicaction.service;

import com.gym.gym_ver2.domain.model.dto.IniciarRutinaRequest;
import com.gym.gym_ver2.domain.model.dto.ProgresoRequest;
import com.gym.gym_ver2.domain.model.entity.DesafioRealizado;
import com.gym.gym_ver2.domain.model.entity.RutinaEjercicio;
import com.gym.gym_ver2.domain.model.entity.RutinaRealizada;
import com.gym.gym_ver2.infraestructure.persistence.repository.DesafiosRealizadosRepository;
import com.gym.gym_ver2.infraestructure.persistence.repository.RutinaEjerciciosRepository;
import com.gym.gym_ver2.infraestructure.persistence.repository.RutinaRealizadaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ProgresoServiceImpl implements ProgresoService {

    private final RutinaRealizadaRepository rutinaRealizadaRepo;
    private final RutinaEjerciciosRepository rutinaEjerciciosRepo;
    private  final DesafiosRealizadosRepository desafiosRealizadosRepo;

//    @Override
//    public Map<String, Object> registrarOActualizarProgreso(ProgresoRequest progresoRequest) {
//        // Verificar si ya existe un progreso para el desafío y rutina de ejercicio
//        Optional<RutinaRealizada> existente = rutinaRealizadaRepo.findByDesafioRealizado_IdDesafioRealizadoAndRutinaEjercicio_IdRutinaEjercicio(progresoRequest.getIdDesafioRealizado(), progresoRequest.getIdRutinaEjercicio());
//
//        RutinaEjercicio  rutinaEjercicio = rutinaEjerciciosRepo.findById(progresoRequest.getIdRutinaEjercicio())
//                .orElseThrow(() -> new RuntimeException("Rutina Ejercicio no encontrado"));
//
//        int metaSeries = rutinaEjercicio.getSeries();
//
//        Map<String, Object> respuesta = new HashMap<>();
//
//        if (existente.isEmpty()) {
//            // Primera vez: crear progreso
//            RutinaRealizada nueva = new RutinaRealizada();
//            nueva.setDesafioRealizado(desafiosRealizadosRepo.findById(progresoRequest.getIdDesafioRealizado())
//                    .orElseThrow(() -> new RuntimeException("Desafío no encontrado")));  nueva.setRutinaEjercicio(rutinaEjercicio);
//            nueva.setRutinaEjercicio(rutinaEjercicio);
//            nueva.setSeries(1);
//            nueva.setRepeticiones(progresoRequest.getRepeticiones());
//            nueva.setCarga(rutinaEjercicio.getCarga());
//            nueva.setEstado("En Progreso");
//
//            rutinaRealizadaRepo.save(nueva);
//            respuesta.put("avanzar", (1 >= metaSeries));
//        } else {
//            // Ya existe, actualizar
//            RutinaRealizada progreso = existente.get();
//            progreso.setSeries(progreso.getSeries() + 1);
//            progreso.setRepeticiones(progresoRequest.getRepeticiones());
//            progreso.setCarga(progresoRequest.getCarga());
//            if (progreso.getSeries() >= metaSeries) {
//                progreso.setEstado("Completado");
//            }
//            rutinaRealizadaRepo.save(progreso);
//            respuesta.put("avanzar", progreso.getSeries() >= metaSeries);
//        }
//
//        List<RutinaRealizada> listaProgreso = rutinaRealizadaRepo.findAllByDesafioRealizado_IdDesafioRealizado(progresoRequest.getIdDesafioRealizado());
//
//        boolean rutinaTerminada = listaProgreso.stream()
//                .allMatch(r -> r.getSeries() >= r.getRutinaEjercicio().getSeries());
//
//        if (rutinaTerminada) {
//            DesafioRealizado desafio = desafiosRealizadosRepo.findById(progresoRequest.getIdDesafioRealizado())
//                    .orElseThrow(() -> new RuntimeException("Desafío no encontrado"));
//            desafio.setEstadoDesafio("Completado");
//            desafiosRealizadosRepo.save(desafio);
//        }
//        respuesta.put("rutinaFinalizada", rutinaTerminada);
//        return respuesta;
//    }

    @Override
    public List<RutinaRealizada> iniciarRutina(IniciarRutinaRequest request) {
        List<RutinaEjercicio> ejercicios = rutinaEjerciciosRepo.findAllByRutina_IdRutina(request.getIdRutina());

        DesafioRealizado desafio = desafiosRealizadosRepo.findById(request.getIdDesafioRealizado())
                .orElseThrow(() -> new RuntimeException("Desafío no encontrado"));

        List<RutinaRealizada> registrosCreados = new ArrayList<>();

        for (RutinaEjercicio ejercicio : ejercicios) {
            boolean yaExiste = rutinaRealizadaRepo
                    .findByDesafioRealizado_IdDesafioRealizadoAndRutinaEjercicio_IdRutinaEjercicio(request.getIdDesafioRealizado(), ejercicio.getIdRutinaEjercicio())
                    .isPresent();

            if (!yaExiste) {
                RutinaRealizada nueva = new RutinaRealizada();
                nueva.setDesafioRealizado(desafio);
                nueva.setRutinaEjercicio(ejercicio);
                nueva.setSeries(0);
                nueva.setRepeticiones(0);
                nueva.setEstado("En Progreso");

                registrosCreados.add(rutinaRealizadaRepo.save(nueva));
            }
        }

        return registrosCreados;
    }



}
