package com.gym.gym_ver2.infraestructure.persistence.repository;

import com.gym.gym_ver2.domain.model.entity.Rol;
import com.gym.gym_ver2.domain.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByEmailUsuario(String email);

    @Query("SELECT u FROM Usuario u WHERE u.idRol = :rol")
    List<Usuario> findAllByRol(@Param("rol") Rol rol);

    @Transactional
    @Modifying
    @Query("UPDATE Usuario u SET u.nombreUsuario = :nombreUsuario, u.emailUsuario = :emailUsuario WHERE u.idRol = :id")
    void updateUser(@Param("id") Integer id,
                    @Param("nombreUsuario") String nombreUsuario,
                    @Param("emailUsuario") String emailUsuario);

}
