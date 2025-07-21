package com.gym.gym_ver2.aplicaction.service;

import com.gym.gym_ver2.domain.model.requestModels.SerieAvanceRequest;
import com.gym.gym_ver2.domain.model.dto.UsuarioDTO;
import java.util.List;

public interface UsuarioService {

    List<UsuarioDTO> getUsers();

    UsuarioDTO getUser(Integer idPersona);

    SerieAvanceRequest.UserResponse actualizarUsuario(UsuarioDTO userRequest);

    void updatePassword(String email, String newPassword);
}
