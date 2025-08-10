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
@Table(name = "recorrido")
public class Recorrido {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "id_recorrido")
    private Long idRecorrido;

    @Column(name = "distancia_km", precision = 8, scale = 2)
    private Double distanciaKm;

    @Column(name = "tiempo_min", nullable = false)
    private Integer tiempoMin;

    @Column(name = "CO2", precision = 5, scale = 2)
    private Double co2;

    //medio_transporte
    @Column(name = "medio_transporte", nullable = false)
    private String medioTransporte;

    //relacino muchos auno con usuariuo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario", nullable = false)
    private Usuario usuario;

    //relacion muchos a uno con ruta
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_ruta", referencedColumnName = "id_ruta", nullable = false)
    private Ruta ruta;


}
