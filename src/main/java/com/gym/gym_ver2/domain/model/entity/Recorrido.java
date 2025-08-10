package com.gym.gym_ver2.domain.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_recorrido")
    private Long idRecorrido;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario", nullable = false, referencedColumnName = "id_usuario")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "id_ruta", referencedColumnName = "id_ruta", nullable = true)
    private Ruta ruta;

    @Column(name = "distancia_km")
    private Double distanciaKm;

    @Column(name = "co2_ahorrado_kg")
    private Double co2AhorradoKg;

    @Column(name = "medio_transporte")
    @Enumerated(EnumType.STRING)
    private Medio medioTransporte;

}
