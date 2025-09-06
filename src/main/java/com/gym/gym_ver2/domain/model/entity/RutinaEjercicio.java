package com.gym.gym_ver2.domain.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rutina_ejercicio")
public class RutinaEjercicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rutina_ejercicio")
    private Integer idRutinaEjercicio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_rutina", referencedColumnName = "id_rutina")
    private Rutina rutina;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_ejercicio", referencedColumnName = "id_ejercicio")
    private Ejercicio ejercicio;

    @OneToMany(mappedBy = "rutinaEjercicio", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<RutinaRealizada> rutinasRealizadas;

    @Column(name = "repeticiones")
    private Integer repeticiones;

    @Column(name = "series")
    private Integer series;

    @Column(name = "duracion")
    private Integer duracion;

    @Column(name = "carga")
    private Integer carga;

    @Column(name = "orden")
    private Integer orden;

    @Column(name = "tiempo_descanso")
    private Integer tiempoDescanso;

    @Column(name = "asignacion")
    private Boolean asignacion;

}
