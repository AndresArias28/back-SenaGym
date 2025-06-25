package com.gym.gym_ver2.infraestructure.persistence.repository;

import com.gym.gym_ver2.domain.model.entity.Persona;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonaRepository extends JpaRepository<Persona, Integer> {

}
