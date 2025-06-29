package com.utec.PDI;

import com.utec.model.Perfil;
import com.utec.model.Usuario;
import com.utec.repository.PerfilRepository;
import com.utec.repository.UsuarioRepository;
import com.utec.service.CustomUsuarioDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomUsuarioDetailsServicesTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PerfilRepository perfilRepository;

    @InjectMocks
    private CustomUsuarioDetailsService customUsuarioDetailsService;


    //cargar usuario con rol admin de forma correcta
    @Test
    void testCargarUsuConRolAdmin() {

        Perfil perfilAdmin = new Perfil();
        perfilAdmin.setNombre("Administrador");

        Usuario usuarioAdmin = new Usuario();
        usuarioAdmin.setCorreo("admin@utec.edu.uy");
        usuarioAdmin.setContrasenia("123456");
        usuarioAdmin.setPerfil(perfilAdmin);

        when(usuarioRepository.findByCorreo("admin@utec.edu.uy")).thenReturn(Optional.of(usuarioAdmin));

        //act
        UserDetails userDetails = customUsuarioDetailsService.loadUserByUsername("admin@utec.edu.uy");

        //assert
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo("admin@utec.edu.uy");
        assertThat(userDetails.getPassword()).isEqualTo("123456");
        assertThat(userDetails.getAuthorities().toString()).contains("ROLE_ADMIN");

    }


    //test para el role user
    @Test
    void testCargarUsuConRolUser(){
        Perfil perfilUser = new Perfil();
        perfilUser.setNombre("Usuario");

        Usuario usuario = new Usuario();
        usuario.setCorreo("user@utec.edu.uy");
        usuario.setContrasenia("123456");
        usuario.setPerfil(perfilUser);

        when(usuarioRepository.findByCorreo("user@utec.edu.uy"))
        .thenReturn(Optional.of(usuario));

        UserDetails userDetails = customUsuarioDetailsService.loadUserByUsername("user@utec.edu.uy");

        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo("user@utec.edu.uy");
        assertThat(userDetails.getAuthorities())
                .extracting("authority")
                .contains("ROLE_USER");
    }



    //test si el user noe existe
    @Test
    void testUsuarioNoEncontradoExcepcion(){
        when(usuarioRepository.findByCorreo("nouser@utec.edu.uy"))
        .thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            customUsuarioDetailsService.loadUserByUsername("nouser@utec.edu.uy");
        });
    }
}
