package com.gym.gym_ver2.infraestructure.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleException( IllegalArgumentException e) {

        return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntime( RuntimeException e) {
        return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<?> manejarRecursoNoEncontrado(RecursoNoEncontradoException ex) {
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("error", true);
        respuesta.put("mensaje", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> manejarErroresGenerales(Exception ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("mensaje", "Error interno: " + ex.getMessage());
        error.put("error", true);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
