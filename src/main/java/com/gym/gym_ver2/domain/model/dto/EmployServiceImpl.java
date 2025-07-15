package com.gym.gym_ver2.domain.model.dto;

import com.gym.gym_ver2.aplicaction.service.EmployService;
import com.gym.gym_ver2.domain.model.entity.Empleado;
import com.gym.gym_ver2.infraestructure.exceptions.RecursoNoEncontradoException;
import com.gym.gym_ver2.infraestructure.persistence.repository.EmployRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployServiceImpl implements EmployService {

    private final EmployRepository employRepository;


    @Override
    public void registroQR(Integer idEmpleado, String codigoQR) {
        Empleado employ = employRepository.findById(idEmpleado)
                .orElseThrow(() -> new RecursoNoEncontradoException("Empleado no encontrado con ID: " + idEmpleado));
        employ.setCodigoQr(codigoQR);
        employRepository.save(employ);
    }
}
