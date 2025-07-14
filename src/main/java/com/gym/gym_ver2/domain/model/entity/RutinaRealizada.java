package com.gym.gym_ver2.domain.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rutina_realizada")
public class RutinaRealizada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rutina_realizada")
    private Integer idRutinaRealizada;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rutina_ejercicio", referencedColumnName = "id_rutina_ejercicio")
    private RutinaEjercicio rutinaEjercicio;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "desafio_realizado", referencedColumnName = "id_desafio_realizado")
    private DesafioRealizado desafioRealizado;

    @Column(name = "repeticiones")
    private Integer repeticiones;

    @Column(name = "series")
    private Integer series;

    @Column(name = "estado")
    private String estado;

}
