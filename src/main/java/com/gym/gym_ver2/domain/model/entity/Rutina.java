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
@Table(name = "rutina")
public class Rutina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rutina")
    private Integer idRutina;

    @OneToMany(mappedBy = "rutina", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RutinaEjercicio> rutinaEjercicios;

    @OneToMany(mappedBy = "rutina", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AsignacionRutina> asignacionRutinas;

    @Column(name = "nombre_rutina")
    private String nombre;

    @Column(name = "descripcion_rutina")
    private String descripcion;

    @Column(name = "foto_rutina")
    private String fotoRutina;

    @Enumerated(EnumType.STRING)
    @Column(name = "dificultad")
    private Dificultad dificultad;

    @Enumerated(EnumType.STRING)
    @Column(name = "enfoque")
    private Enfoque enfoque;

    @Column(name = "image_public_id")
    private String imagePublicId;

}
