package com.gym.gym_ver2.infraestructure.controller;

import com.gym.gym_ver2.aplicaction.service.EmployService;
import com.gym.gym_ver2.domain.model.dto.CodigoQRRequest;
import com.gym.gym_ver2.domain.model.dto.RespuestaGeneralQR;
import com.gym.gym_ver2.domain.model.entity.Usuario;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Tag(name = "Realizar gestiones del QR y del empleado", description = "Endpoints para alamcenar los QRs generados por el admin")
@RequestMapping("/empleado")
@RestController
@RequiredArgsConstructor
public class EmployController {

    private final EmployService employService;

    @PostMapping("/registrarQR/{id}")
    public ResponseEntity<RespuestaGeneralQR> asignarCodigoQR(
            @AuthenticationPrincipal Usuario user, @RequestBody CodigoQRRequest request
            ){
       int  idEmpleado = user.getIdUsuario().intValue();
       String codigoQR = request.getCodigoQR();
        employService.registroQR(idEmpleado, codigoQR);
        RespuestaGeneralQR respuesta = new RespuestaGeneralQR(
                "exito",
                "El QR fue asignado correctamente al empleado",
                LocalDateTime.now()
        );
        return  ResponseEntity.ok(respuesta);
    }
}
