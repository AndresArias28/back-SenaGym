package com.gym.gym_ver2.domain.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "recompensa")
public class Recompensa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_recompensa")
    private Long idRecompensa;

    @ManyToMany(mappedBy = "recompensas")
    private List<Usuario> usuarios = new ArrayList<>();

    @Column(name = "nombre_recompensa", nullable = false)
    private String nombreRecompensa;

    @Column(name = "puntos_necesarios", nullable = false)
    private Integer puntosNecesarios;

    @Column(name = "tipo_recompensa", nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoRecompensa tipoRecompensa;

}

