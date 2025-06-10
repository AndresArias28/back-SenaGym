package com.gym.gym_ver2.infraestructure.controller;

import com.gym.gym_ver2.aplicaction.service.RutinaService;
import com.gym.gym_ver2.domain.model.dto.RutinaDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Rutina Controller", description = "Endpoints para gestionar rutinas de ejercicios")
@RequestMapping("/rutina")
@RestController
@RequiredArgsConstructor
public class RutinaController {

    private final RutinaService rutinaService;

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/crear")
    public ResponseEntity<RutinaDTO> crearRutina(@RequestBody RutinaDTO rutinaDTO) {
        try {
            RutinaDTO nuevaRutina = rutinaService.crearRutina(rutinaDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaRutina);
        } catch (Exception e) {
             e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/obtenerRutinas")
    public ResponseEntity<?> obtenerRutinas() {
        try {
            List<RutinaDTO> rutinas = rutinaService.obtenerRutinas();
            return ResponseEntity.ok(rutinas);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @DeleteMapping("/eliminarRutinas/{id}")
    public ResponseEntity<?> eliminarRutina(@PathVariable Integer id) {
        try {
            rutinaService.eliminarRutina(id);
            return ResponseEntity.ok("✅ Rutina eliminada exitosamente.");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ en controller Rutina no encontrada con ID: " + id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("❌ Error al eliminar la rutina.");
        }
    }


    @CrossOrigin(origins = "http://localhost:4200")
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<RutinaDTO> actualizarRutina(@PathVariable Integer id, @RequestBody RutinaDTO rutinaDTO) {
        try {
            RutinaDTO rutinaActualizada = rutinaService.actualizarRutina(id, rutinaDTO);
            return ResponseEntity.ok(rutinaActualizada);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
