package com.gym.gym_ver2.aplicaction.service;

import com.gym.gym_ver2.domain.model.dto.AdminDTO;
import com.gym.gym_ver2.domain.model.dto.CodigoQRRequest;
import com.gym.gym_ver2.domain.model.entity.Empleado;
import com.gym.gym_ver2.domain.model.entity.Rol;
import com.gym.gym_ver2.domain.model.entity.Usuario;
import com.gym.gym_ver2.domain.model.requestModels.RegisterAdminRequest;
import com.gym.gym_ver2.infraestructure.auth.AuthResponse;
import com.gym.gym_ver2.infraestructure.exceptions.RecursoNoEncontradoException;
import com.gym.gym_ver2.infraestructure.jwt.JwtService;
import com.gym.gym_ver2.infraestructure.persistence.repository.EmployRepository;
import com.gym.gym_ver2.infraestructure.persistence.repository.RolRepository;
import com.gym.gym_ver2.infraestructure.persistence.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements  AdminService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final EmployRepository employRepository;
    private final JwtService jwtService;

    @Override
    @Transactional
    public List<AdminDTO> getAdmins() {
        Rol rol = rolRepository.findById(2).orElseThrow(() -> new IllegalArgumentException("Rol no encontrado"));
        List<Usuario> usuariosAdmins = usuarioRepository.findAllByRol(rol);
        return usuariosAdmins.stream()
                .map(usr -> new AdminDTO(
                        usr.getPersona().getIdPersona(),
                        usr.getNombreUsuario(),
                        usr.getEmailUsuario(),
                        usr.getIdRol().getIdRol()
                ))
                .toList();
    }

    @Override
    public void registerQR(String qr, Integer idAdmin) {
        Empleado empleado = employRepository.findById(idAdmin)
                .orElseThrow(() -> new RecursoNoEncontradoException("Empleado no encontrado"));
        empleado.setCodigoQr(qr);
        employRepository.save(empleado);
    }

    @Override
    public boolean validarQr(String codigoQR) {
        if (codigoQR == null || codigoQR.isEmpty()) {
            throw new RecursoNoEncontradoException("El código QR no puede ser nulo o vacío");
        }
        System.out.println("codigoQR: " + codigoQR);
        return employRepository.findByCodigoQr(codigoQR).isPresent();
    }

    @Override
    public AuthResponse registerAdmin(RegisterAdminRequest adminRequest) {
        Rol rol = rolRepository.findById(2)
                .orElseThrow(() -> new RecursoNoEncontradoException("Rol no encontrado"));
        Empleado admin = new Empleado();
        admin.setNombres(adminRequest.getNombreAdmin());
        admin.setApellidos(adminRequest.getApellidoAdmin());
        admin.setIdentificacion(adminRequest.getCedulaAdmin());

        admin = employRepository.save(admin);

        Usuario savedUser = Usuario.builder()
                .persona(admin)
                .idRol(rol)
                .nombreUsuario(adminRequest.getNombreAdmin())
                .emailUsuario(adminRequest.getEmailAdmin())
                .contrasenaUsuario(adminRequest.getContrasenaAdmin())
                .build();
        usuarioRepository.save(savedUser);
        System.out.println("Admin registrado: " + savedUser.getNombreUsuario() + ", ID: " + admin.getIdPersona());
        return AuthResponse.builder().token(jwtService.createToken(savedUser)).build();
    }

}
