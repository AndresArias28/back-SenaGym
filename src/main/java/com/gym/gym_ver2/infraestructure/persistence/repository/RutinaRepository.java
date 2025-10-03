package com.gym.gym_ver2.infraestructure.persistence.repository;

import com.gym.gym_ver2.domain.model.dto.RutinaAprendizDTO;
import com.gym.gym_ver2.domain.model.entity.Rutina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RutinaRepository extends JpaRepository<Rutina, Integer> {

    @Query("""
        SELECT DISTINCT new com.gym.gym_ver2.domain.model.dto.RutinaAprendizDTO(
            r.nombre,
            r.dificultad,
            r.enfoque
        )
        FROM Aprendiz a
        JOIN DesafioRealizado dr ON a.idPersona = dr.aprendiz.idPersona
        JOIN RutinaRealizada rr ON dr.idDesafioRealizado = rr.desafioRealizado.idDesafioRealizado
        JOIN RutinaEjercicio re ON rr.rutinaEjercicio.idRutinaEjercicio = re.idRutinaEjercicio
        JOIN Rutina r ON re.rutina.idRutina = r.idRutina
        WHERE a.idPersona = :idAprendiz
    """)
    List<RutinaAprendizDTO> obtenerRutinasPorAprendiz(@Param("idAprendiz") Integer idAprendiz);

}
