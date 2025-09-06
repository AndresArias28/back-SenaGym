package com.gym.gym_ver2.infraestructure.persistence.repository;

import com.gym.gym_ver2.domain.model.entity.RutinaEjercicio;
import com.gym.gym_ver2.domain.model.entity.RutinaRealizada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RutinaRealizadaRepository extends JpaRepository<RutinaRealizada, Integer> {
    Optional<RutinaRealizada> findByDesafioRealizado_IdDesafioRealizadoAndRutinaEjercicio_IdRutinaEjercicio(Integer idDesafio, Integer  idEjercicio);
    List<RutinaRealizada> findAllByDesafioRealizado_IdDesafioRealizado(Integer idDesafio);
    Optional<RutinaRealizada> findFirstByDesafioRealizado_IdDesafioRealizado(Integer idDesafioRealizado);

}
