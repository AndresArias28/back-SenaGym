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
@Table(name = "asignacion_rutina")
public class AsignacionRutina {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_asignacion_rutina")
    private Integer idAsignacionRutina;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_persona", referencedColumnName = "id_persona")
    private Aprendiz aprendiz;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_rutina", referencedColumnName = "id_rutina")
    private Rutina rutina;

    @Column(name = "fecha_asignacion")
    private LocalDateTime fechaAsignacion;

    @Column(name = "fecha_finalizacion")
    private LocalDateTime fechaFinalizacion;

    @Column(name = "dias_asignado")
    private String diaAsignado;

    @Column(name = "observaciones")
    private  String observaciones;

}
