package com.gym.gym_ver2.aplicaction.service;

import com.gym.gym_ver2.domain.model.dto.PuntosRequest;
import com.gym.gym_ver2.domain.model.dto.RankingResponse;
import com.gym.gym_ver2.domain.model.entity.Usuario;

import java.util.List;

public interface UsuarioService {

    Usuario upsertFromGoogle(String email, String name);

    String registerPuntos(Integer idUsuario, PuntosRequest puntos);

    List<RankingResponse> getRanking();
}
