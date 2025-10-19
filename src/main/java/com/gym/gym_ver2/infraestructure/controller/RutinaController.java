package com.gym.gym_ver2.infraestructure.controller;

import com.gym.gym_ver2.aplicaction.service.RutinaService;
import com.gym.gym_ver2.domain.model.dto.RutinaAprendizDTO;
import com.gym.gym_ver2.domain.model.dto.RutinaCreateDTO;
import com.gym.gym_ver2.domain.model.dto.RutinaDTO;
import com.gym.gym_ver2.domain.model.dto.SolicitudRutinaDTO;
import com.gym.gym_ver2.domain.model.entity.Usuario;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "Rutina Controller", description = "Endpoints para gestionar rutinas de ejercicios")
@RequestMapping("/rutina")
@RestController
@RequiredArgsConstructor
public class RutinaController {

    private final RutinaService rutinaService;

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping(value = "/crear", consumes = "multipart/form-data")
    public ResponseEntity<RutinaDTO> crearRutina(
            @RequestPart("datos") RutinaCreateDTO datos,
            @RequestPart("fotoRutina") MultipartFile fotoRutina
    ) {
        datos.setFotoRutina(fotoRutina);
        try {
            RutinaDTO nuevaRutina = rutinaService.crearRutina(datos);
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
    public ResponseEntity<Map<String, String>> eliminarRutina(@PathVariable Integer id) {
        Map<String, String> response = new HashMap<>();
        try {
            rutinaService.eliminarRutina(id);
            response.put("mensaje", "Rutina eliminada exitosamente.");
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex) {
            response.put("error", "Rutina no encontrada con ID: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", "Error al eliminar la rutina.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PutMapping(value = "/actualizar/{id}", consumes = "multipart/form-data")
    public ResponseEntity<RutinaDTO> actualizarRutina(
            @PathVariable Integer id,
            @RequestPart("datos") RutinaCreateDTO datos,
            @RequestPart(value = "fotoRutina", required = false) MultipartFile fotoRutina
    ) {
        try {
            // Solo se actualiza la foto si viene una nueva
            if (fotoRutina != null && !fotoRutina.isEmpty()) {
                datos.setFotoRutina(fotoRutina);
            }

            RutinaDTO rutinaActualizada = rutinaService.actualizarRutina(id, datos);
            return ResponseEntity.ok(rutinaActualizada);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(value = "/obtenerRutina/{id}")
    public ResponseEntity<RutinaDTO> obtenerRutina(@PathVariable Integer id) {
        try {
            RutinaDTO rutina = rutinaService.obtenerRutinaPorId(id);
            if (rutina != null) {
                return ResponseEntity.ok(rutina);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/generar")
    public ResponseEntity<?> generarRutina(@RequestBody SolicitudRutinaDTO datos) {
        try {
            String resultado = rutinaService.generarRutinaConIA(datos);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al generar rutina");
        }
    }

    @GetMapping("/porAprendiz")
    public ResponseEntity<List<RutinaAprendizDTO>> getRutinaByAprendiz(@AuthenticationPrincipal Usuario usuario) {
    int idUsuario = usuario.getIdUsuario();
        List<RutinaAprendizDTO> rutinas = rutinaService.getRutinaByAprendiz(idUsuario);
        return ResponseEntity.ok(rutinas);
    }

}
