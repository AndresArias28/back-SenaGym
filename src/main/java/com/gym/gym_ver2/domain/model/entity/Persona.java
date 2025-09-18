package com.gym.gym_ver2.domain.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "persona")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Persona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_persona")
    private Integer idPersona;

    @Column(name = "identificacion", unique = true)
    private String identificacion;

    @Column(name = "nombres")
    private String nombres;

    @Column(name = "apellidos")
    private String apellidos;

    @Column(name="fecha_nacimiento")
    private LocalDateTime fechaNacimiento;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "sexo")
    private String sexo;

}
