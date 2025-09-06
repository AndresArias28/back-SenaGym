package com.gym.gym_ver2.aplicaction.service;

import com.gym.gym_ver2.domain.model.dto.AdminDTO;
import com.gym.gym_ver2.domain.model.dto.responseDTO.ValidacionRutinaResponse;
import com.gym.gym_ver2.domain.model.requestModels.RegisterAdminRequest;
import com.gym.gym_ver2.infraestructure.auth.AuthResponse;
import java.util.List;

public interface  AdminService {

    List<AdminDTO> getAdmins();

    void registerQR(String     qr, Integer idAdmin);

    ValidacionRutinaResponse validarQr(String codigoQR, Integer idDesafioRealizado);

    AuthResponse registerAdmin(RegisterAdminRequest adminRequest);
}
