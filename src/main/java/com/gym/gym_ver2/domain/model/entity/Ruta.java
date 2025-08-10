package com.gym.gym_ver2.domain.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ruta", schema = "public")
public class Ruta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ruta")
    private Long idRuta;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario", nullable = false)
    private Usuario usuario;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "ruta", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<Recorrido> recorridos;

    @NotBlank
    @Column(name = "origen", nullable = false, columnDefinition = "TEXT")
    private String origen;

    @NotBlank
    @Column(name = "destino", nullable = false, columnDefinition = "TEXT")
    private String destino;

    // Distancia sugerida por el planificador (opcional)
    @Column(name = "distancia_km")
    private Double distanciaKm;

    // Tiempo estimado sugerido, en minutos (simple)
    @Column(name = "tiempo_estimado_min")
    private Integer tiempoEstimadoMin;

    @Column(name = "trazado_polyline")
    private String trazadoPolyline;

    @Column(name = "medio_transporte")
    @Enumerated(EnumType.STRING)
    private Medio medioTransporte;



}
