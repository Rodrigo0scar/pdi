package com.utec.repository;

import com.utec.dto.UsuarioDTO;
import com.utec.model.Estado;
import com.utec.model.Perfil;
import com.utec.model.TipoDocumento;
import com.utec.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByTipoDocumentoAndNumeDocumento(TipoDocumento tipoDocumento, String numeDocumento);
    Optional<Usuario> findByCorreo(String correo);
    List<Usuario> findByPrimNombre(String primNombre);
    List<Usuario> findByPrimApellido(String primApellido);
    List<Usuario> findByEstado(Estado estado);
    Usuario findByNumeDocumento(String numeDocumento);
    List<Usuario> findByPerfil(Perfil perfil);

}
