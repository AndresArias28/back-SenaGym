package com.gym.gym_ver2.infraestructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PuntosHistorial extends JpaRepository<com.gym.gym_ver2.domain.model.entity.PuntosHistorial,Integer> {

    Optional<PuntosHistorial> findByUsuario_IdUsuario(Integer idUsuario);
}
