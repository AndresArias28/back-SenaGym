package com.gym.gym_ver2.infraestructure.persistence.repository;

import com.gym.gym_ver2.domain.model.dto.DesafioRealizadoDao;
import com.gym.gym_ver2.domain.model.entity.DesafioRealizado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DesafioRealizadoRepository extends JpaRepository<DesafioRealizado,Integer> {
    @Query("SELECT new com.gym.gym_ver2.domain.model.dto.DesafioRealizadoDao(dr.idDesafioRealizado, dr.desafio.idDesafio, dr.estadoDesafio, " +
            "dr.fechaInicioDesafio, dr.fechaFinDesafio, dr.caloriasTotales, dr.puntosObtenidos) " +
            "FROM DesafioRealizado dr " +
            "WHERE dr.aprendiz.idPersona = :idPersona " +
            "ORDER BY dr.fechaInicioDesafio DESC")
    List<DesafioRealizadoDao> findAllByIdPersona(@Param("idPersona") Integer idPersona);
}
