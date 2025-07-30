package com.gym.gym_ver2.infraestructure.persistence.repository;

import com.gym.gym_ver2.domain.model.entity.AsignacionRutina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AsignacionRutinaRepository extends JpaRepository<AsignacionRutina,Integer> {
    List<AsignacionRutina> findByAprendiz_IdPersona(Integer idPersona);
}
