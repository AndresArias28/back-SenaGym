package com.gym.gym_ver2.infraestructure.persistence.repository;

import com.gym.gym_ver2.domain.model.entity.Desafio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DesafioRepository extends JpaRepository<Desafio, Integer> {
    Optional<Desafio> findByNumeroDesafio(Integer numeroDesafio);
}
