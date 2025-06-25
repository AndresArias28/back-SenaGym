package com.gym.gym_ver2.infraestructure.exceptions;

public class RecursoNoEncontradoException extends RuntimeException  {
    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
