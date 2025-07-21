package com.gym.gym_ver2.infraestructure.controller;

import com.gym.gym_ver2.aplicaction.service.EjercicioService;
import com.gym.gym_ver2.domain.model.dto.EjercicioDTO;
import com.gym.gym_ver2.domain.model.dto.ExcerciseDTO;
import com.gym.gym_ver2.domain.model.dto.ExercisesCreateDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Ejercicio Controller", description = "Endpoints para gestionar ejercicios")
@RequestMapping("/ejercicio")
@RestController
@RequiredArgsConstructor
public class EjercicioController {

    private final EjercicioService ejercicioService;

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(value = "/obtenerEjercicios")
    public ResponseEntity<?> obtenerEjercicios() {
        List<EjercicioDTO> ejercicios = ejercicioService.obtenerEjercicios();
        return ResponseEntity.ok(ejercicios);
    }


    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping(value = "/crearEjercicio", consumes = "multipart/form-data")
    public ResponseEntity<EjercicioDTO> crearEjercicio(
            @RequestPart("datos") ExcerciseDTO datos,
            @RequestPart(value = "fotoEjercicio", required = false) MultipartFile fotoEjercicio
    ) {
        try {
            EjercicioDTO nuevoEjercicio = ejercicioService.crearEjercicio(datos, fotoEjercicio);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoEjercicio);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PutMapping(value = "/actualizarEjercicio/{id}", consumes = "multipart/form-data")
    public ResponseEntity<EjercicioDTO> actualizarEjercicio(
            @PathVariable Integer id,
            @RequestPart("datos") ExercisesCreateDTO datos,
            @RequestPart(value = "fotoEjercicio", required = false) MultipartFile fotoEjercicio
    ) {
        datos.setFotoEjercicio(fotoEjercicio);

        try {
            EjercicioDTO ejercicioActualizado = ejercicioService.actualizarEjercicio(id, datos);
            return ResponseEntity.ok(ejercicioActualizado);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @DeleteMapping("/eliminarEjercicio/{id}")
    public ResponseEntity<Void> eliminarEjercicio(@PathVariable Integer id) {
        try {
            ejercicioService.eliminarEjercicio(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
