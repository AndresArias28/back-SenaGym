package com.gym.gym_ver2.infraestructure.persistence.repository;

import com.gym.gym_ver2.domain.model.dto.AsignacionesResponse;
import com.gym.gym_ver2.domain.model.entity.AsignacionRutina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AsignacionRutinaRepository extends JpaRepository<AsignacionRutina,Integer> {
    List<AsignacionRutina> findByAprendiz_IdPersona(Integer idPersona);

    @Query("SELECT new com.gym.gym_ver2.domain.model.dto.AsignacionesResponse(" +
            "CONCAT(a.nombres, ' ', a.apellidos), a.idPersona, a.ficha, " +
            "ar.fechaAsignacion, a.nivelFisico, ar.observaciones, ar.diaAsignado, ar.rutina.nombre) " +
            "FROM AsignacionRutina ar " +
            "JOIN ar.aprendiz a " +
            "ORDER BY ar.fechaAsignacion DESC")
    List<AsignacionesResponse> listarAsignacionesConAprendiz();
}
