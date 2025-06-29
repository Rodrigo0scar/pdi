package com.utec.repository;

import com.utec.model.Administrador;
import com.utec.model.TipoDocumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdministradorRepository extends JpaRepository<Administrador, Integer> {
    Optional<Administrador> findByTipoDocumentoAndNumeDocumento(TipoDocumento tipoDocumento, String numeDocumento);
    Optional<Administrador> findByCorreo(String correo);
}

