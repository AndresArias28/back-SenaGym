package com.gym.gym_ver2.infraestructure.persistence.repository;

import com.gym.gym_ver2.domain.model.dto.AprendicesDashDTO;
import com.gym.gym_ver2.domain.model.entity.Aprendiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface    AprendizRepository extends JpaRepository<Aprendiz, Integer> {
    List<Aprendiz> findTop20ByOrderByPuntosAcumuladosDesc();

    @Query("""
        SELECT  new com.gym.gym_ver2.domain.model.dto.AprendicesDashDTO(
            p.idPersona,
            CONCAT(p.nombres, ' ',p.apellidos),
            apre.ficha,
            apre.nivelFisico,
            apre.horasAcumuladas,
            apre.puntosAcumulados
        )
        FROM Persona p
        JOIN Aprendiz apre ON p.idPersona = apre.idPersona
        ORDER BY apre.puntosAcumulados DESC
    """)
    List<AprendicesDashDTO> obtenerAprendicesParaDashboard();

}
