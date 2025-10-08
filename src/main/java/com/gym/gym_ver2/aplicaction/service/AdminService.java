package com.gym.gym_ver2.aplicaction.service;

import com.gym.gym_ver2.domain.model.dto.AdminDTO;
import com.gym.gym_ver2.domain.model.dto.AprendicesDashDTO;
import com.gym.gym_ver2.domain.model.dto.AprendizRanking;
import com.gym.gym_ver2.domain.model.dto.FrecuenciaAprendizDTO;
import com.gym.gym_ver2.domain.model.dto.responseDTO.ValidacionRutinaResponse;
import com.gym.gym_ver2.domain.model.entity.Aprendiz;
import com.gym.gym_ver2.domain.model.requestModels.RegisterAdminRequest;
import com.gym.gym_ver2.infraestructure.auth.AuthResponse;
import java.util.List;
import java.util.Map;

public interface  AdminService {

    List<AdminDTO> getAdmins();

    void registerQR(String     qr, Integer idAdmin);

    ValidacionRutinaResponse validarQr(String codigoQR, Integer idDesafioRealizado);

    AuthResponse registerAdmin(RegisterAdminRequest adminRequest);

    Aprendiz guardarFrecuenciaCardiaca(Integer idPersona, Integer frecuencia);

    List<AprendizRanking> obtenerTop20Aprendices();


    FrecuenciaAprendizDTO obtenerFrecuenciaCardiaca(int idPersona);

    List<AprendicesDashDTO> getAprendicesDash();
}
