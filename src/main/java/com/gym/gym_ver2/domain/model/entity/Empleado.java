package com.gym.gym_ver2.domain.model.entity;

import lombok.*;
import jakarta.persistence.*;
import lombok.experimental.SuperBuilder;


@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "empleado")
@PrimaryKeyJoinColumn(name = "id_persona")
public class Empleado extends Persona {

    @Column(name = "codigo_qr")
    private String codigoQr;

    @Column(name = "cargo")
    private String cargo;

}
