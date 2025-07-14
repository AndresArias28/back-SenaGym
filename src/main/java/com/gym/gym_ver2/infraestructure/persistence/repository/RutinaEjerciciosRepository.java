package com.gym.gym_ver2.infraestructure.persistence.repository;

import com.gym.gym_ver2.domain.model.entity.Rutina;
import com.gym.gym_ver2.domain.model.entity.RutinaEjercicio;
import com.gym.gym_ver2.domain.model.entity.RutinaRealizada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface RutinaEjerciciosRepository extends JpaRepository<RutinaEjercicio, Integer> {
    List<RutinaEjercicio> findByRutina(Rutina rutina);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "DELETE FROM rutina_ejercicio WHERE id_rutina = :idRutina", nativeQuery = true)
    void eliminarTodoPorRutina(@Param("idRutina") Integer idRutina);


    @Modifying
    @Transactional
    @Query("DELETE FROM RutinaEjercicio re WHERE re.rutina.idRutina = :idRutina")
    void eliminarPorRutinaId(@Param("idRutina") Integer idRutina);

    List<RutinaEjercicio> findAllByRutina_IdRutina(Integer idRutina);

}
