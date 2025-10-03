package com.gym.gym_ver2.domain.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "desafio_realizado")
public class DesafioRealizado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_desafio_realizado")
    private Integer idDesafioRealizado;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_desafio", referencedColumnName = "id_desafio")
    private Desafio desafio;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_persona", referencedColumnName = "id_persona")
    private Aprendiz aprendiz;

    @OneToMany(mappedBy = "desafioRealizado", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<RutinaRealizada> rutinasRealizadas;

    @Column(name = "fecha_inicio_desafio")
    private LocalDateTime fechaInicioDesafio;

    @Column(name = "fecha_fin_desafio")
    private LocalDateTime fechaFinDesafio;

    @Column(name = "estado")
    private String estadoDesafio;

    @Column(name = "caloriasTotales")
    private Double caloriasTotales;

    @Column(name = "puntaje" )
    private Integer puntosObtenidos;

}
