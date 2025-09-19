package com.gym.gym_ver2.infraestructure.auth;

import com.gym.gym_ver2.domain.model.entity.Usuario;
import org.springframework.data.repository.Repository;

interface UsuarioRepository extends Repository<Usuario, Integer> {
}
