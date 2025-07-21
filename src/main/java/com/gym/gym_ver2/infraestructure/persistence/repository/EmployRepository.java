package com.gym.gym_ver2.infraestructure.persistence.repository;

import com.gym.gym_ver2.domain.model.entity.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface EmployRepository extends JpaRepository<Empleado, Integer> {
    Optional<Empleado> findByCodigoQr(String codigoQR);
}
