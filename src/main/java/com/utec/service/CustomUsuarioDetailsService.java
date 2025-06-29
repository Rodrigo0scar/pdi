package com.utec.service;

import com.utec.model.Perfil;
import com.utec.repository.PerfilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.utec.model.Usuario;
import com.utec.repository.UsuarioRepository;

import java.util.Collections;

@Service
public class CustomUsuarioDetailsService implements UserDetailsService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PerfilRepository perfilRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario user = usuarioRepository.findByCorreo(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        String role = "ROLE_" + (user.getPerfil().getNombre().equals("Administrador") ? "ADMIN" : "USER");
        
        return new User(
            user.getCorreo(),
            user.getContrasenia(),
            Collections.singletonList(new SimpleGrantedAuthority(role))
        );
    }
}
