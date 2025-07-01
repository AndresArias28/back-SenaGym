package com.gym.gym_ver2.domain.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ejercicio")
public class Ejercicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ejercicio")
    private Integer idEjercicio;

    @OneToMany(mappedBy = "ejercicio", fetch =  FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RutinaEjercicio> rutinaEjercicios;

    @Column(name = "nombre_ejercicio")
    private String nombreEjercicio;

    @Column(name = "descripcion_ejercicio")
    private String descripcionEjercicio;

    @Column(name = "foto_ejercicio")
    private String fotoEjercicio;

    @Column(name = "musculos")
    private String musculos;

    @Column(name = "met", nullable = false, precision = 1)
    private Double met;

    @Column(name = "image_public_id")
    private String imagePublicId;

}
