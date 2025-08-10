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
@Table(name = "oauth_cuentas")
public class Oauth_cuentas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_outh_cuenta")
    private Long idOauthCuenta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "proveedor")
    private String proveedor;

    @Column(name = "exter_id")
    private String exterId;

    //emailprobveedor
    @Column(name = "email_proveedor")
    private String emailProveedor;

    //avatarurl
    @Column(name = "avatar_url")
    private String avatarUrl;


}
