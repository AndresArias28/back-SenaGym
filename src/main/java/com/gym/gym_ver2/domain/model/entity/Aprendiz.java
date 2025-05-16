package com.gym.gym_ver2.domain.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.math.BigDecimal;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "aprendiz")
@PrimaryKeyJoinColumn(name = "id_persona")
public class Aprendiz extends Persona {

    @Column(name = "ficha")
    private Integer ficha;

    @Column(name = "jornada")
    private String jornada;

    @Column(name = "estatura")
    private BigDecimal estatura;

    @Column(name = "peso")
    private BigDecimal peso;

    @Column(name = "puntos_acumulados")
    private Integer puntosAcumulados;

    @Column(name = "horas_acumuladas")
    private Integer horasAcumuladas;

    @Column(name = "nivel_fisico")
    private String nivelFisico;

}
