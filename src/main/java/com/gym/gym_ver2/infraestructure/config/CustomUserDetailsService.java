package com.gym.gym_ver2.infraestructure.config;

import com.gym.gym_ver2.domain.model.entity.Usuario;
import com.gym.gym_ver2.infraestructure.persistence.repository.UsuarioRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import java.util.List;

// Clase que implementa la interfaz UserDetailsService de Spring Security, para cargar un usuario por su email y devolver un objeto UserDetails
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository userRepository;//patron singleton

    public CustomUserDetailsService(UsuarioRepository userRepository1) {
        this.userRepository = userRepository1;
    }

    @Override // Metodo que carga un usuario por su email y devuelve un objeto UserDetails
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = userRepository.findByEmailUsuario(email)
                .orElseThrow(() ->   new UsernameNotFoundException("Usuario no encontrado con email: " + email));
        System.out.println("Usuario encontrado: " + usuario.getEmailUsuario());

        if (usuario.getContrasenaUsuario() == null || usuario.getContrasenaUsuario().isBlank()) {
            throw new RuntimeException("El usuario tiene una contraseña vacía o nula");
        }

        if (usuario.getEmailUsuario() == null || usuario.getEmailUsuario().isBlank()) {
            throw new RuntimeException("El usuario no tiene un correo válido");
        }
        // Asignar roles al usuario y devolver un objeto UserDetails
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(usuario.getIdRol().getNombreRol()));
        usuario.setAuthorities(authorities);
        System.out.println("Authorities: " + authorities);
        return  usuario;
    }
}
