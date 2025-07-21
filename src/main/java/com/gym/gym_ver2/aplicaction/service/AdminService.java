package com.gym.gym_ver2.aplicaction.service;

import com.gym.gym_ver2.domain.model.dto.AdminDTO;
import com.gym.gym_ver2.domain.model.dto.CodigoQRRequest;
import com.gym.gym_ver2.domain.model.entity.Usuario;
import com.gym.gym_ver2.infraestructure.auth.AuthResponse;
import java.util.List;

public interface  AdminService {

    List<AdminDTO> getAdmins();

    void registerQR(String     qr, Integer idAdmin);

    boolean validarQr(String codigoQR);
}
