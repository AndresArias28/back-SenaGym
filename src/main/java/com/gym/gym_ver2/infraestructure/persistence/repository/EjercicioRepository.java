package com.gym.gym_ver2.infraestructure.persistence.repository;

import com.gym.gym_ver2.domain.model.entity.Ejercicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EjercicioRepository extends JpaRepository<Ejercicio, Integer> {
    // Aquí puedes agregar métodos personalizados si es necesario
    // Por ejemplo, para buscar ejercicios por nombre o tipo
}
