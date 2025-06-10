package com.gym.gym_ver2.domain.model.entity;

public enum Enfoque {
//    FULL_BODY, TREN_SUPERIOR, TREN_INFERIOR
    FULL_BODY("Full Body"),
    TREN_SUPERIOR("Tren Superior"),
    TREN_INFERIOR("Tren Inferior");

    private final String valor;
    Enfoque(String valor) {
        this.valor = valor;
    }
    public String getValor() {
        return valor;
    }
    public static Enfoque from(String input) {
        for (Enfoque e : values()) {
            if (e.valor.equalsIgnoreCase(input) || e.name().equalsIgnoreCase(input)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Enfoque no válido: " + input);
    }
}
