package com.gym.gym_ver2.domain.model.entity;

import jakarta.persistence.Entity;
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
@Table(name = "desafio")
public class Desafio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_desafio")
    private Integer idDesafio;

    @Column(name = "nombre_desafio")
    private String nombreDesafio;

    @Column(name = "numero_desafio")
    private Integer numeroDesafio;

    @OneToMany(mappedBy = "desafio", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<DesafioRealizado> desafiosUsuarios;




}
