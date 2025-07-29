package com.gym.gym_ver2.domain.model.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Data
public class SolicitudRutinaDTO {
    private String sexo;
    private int edad;
    private String objetivo; // Ej: "perder peso y ganar músculo"
    private String lesiones; // Ej: "rodilla", puede ser una lista o texto
    private String nivel; // "principiante", "intermedio", "avanzado"
    private int altura; // cm
    private int peso; // kg
    private int frecuencia; // Ej: "3 veces por semana"
    private String ubicacion; // "casa" o "gimnasio"
    private int frecuenciaCardio; // Ej: "2 veces por semana"

}
