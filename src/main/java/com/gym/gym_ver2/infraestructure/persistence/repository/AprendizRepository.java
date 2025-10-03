package com.gym.gym_ver2.infraestructure.persistence.repository;

import com.gym.gym_ver2.domain.model.entity.Aprendiz;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AprendizRepository extends JpaRepository<Aprendiz, Integer> {
    List<Aprendiz> findTop20ByOrderByPuntosAcumuladosDesc();

}
