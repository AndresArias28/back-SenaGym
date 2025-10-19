package com.gym.gym_ver2.domain.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "aprendiz", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AsignacionRutina> asignacionesRutina;

    @OneToMany(mappedBy = "aprendiz", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DesafioRealizado> desafiosRealizados;

    @Column(name = "jornada")
    private String jornada;

    @Column(name = "estatura")
    private Double estatura;

    @Column(name = "peso")
    private Double peso;

    @Column(name = "nivel_fisico")
    private String nivelFisico;

    @Column(name = "puntos_acumulados")
    private Integer puntosAcumulados;

    @Column(name = "horas_acumuladas")
    private Integer horasAcumuladas;

    @Column(name = "frecuencia_cardiaca")
    private Integer frecuenciaCardiaca;
}
