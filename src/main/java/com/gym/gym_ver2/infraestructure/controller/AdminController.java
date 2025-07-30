package com.gym.gym_ver2.infraestructure.controller;

import com.gym.gym_ver2.aplicaction.service.AdminService;
import com.gym.gym_ver2.domain.model.dto.AdminDTO;
import com.gym.gym_ver2.domain.model.dto.CodigoQRRequest;
import com.gym.gym_ver2.domain.model.entity.Empleado;
import com.gym.gym_ver2.domain.model.entity.Usuario;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;


import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Tag(name = "Admin Controller", description = "Endpoints para gestion de admins")

@RequestMapping("/admin")
@RestController
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @Operation(hidden = true)
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/obtenerAdmins")
    public ResponseEntity<List<AdminDTO>> obtenerAdmins() {
        try {
            List<AdminDTO> admins = adminService.getAdmins();
            return ResponseEntity.ok(admins);
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/register/qr")
    public ResponseEntity<Map> registrarQR(@AuthenticationPrincipal Usuario usuario, @RequestBody CodigoQRRequest rq) {
//        int idAdmin = usuario.getIdUsuario().intValue();
        int idPersona = usuario.getPersona().getIdPersona().intValue();
        String codigoQR = rq.getCodigoQR();
        adminService.registerQR(codigoQR, idPersona);
        return ResponseEntity.ok((Map.of("mensaje", "QR guardado")));
    }


    @PostMapping("/validarQR")
    public ResponseEntity<?> validarQR(@RequestBody CodigoQRRequest rq) {
        boolean esValido = adminService.validarQr(rq.getCodigoQR());

        if (esValido) {
            return ResponseEntity.ok(Map.of("mensaje", "QR valido"));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("mensaje", "QR invalido"));
        }
    }

}