package com.utec.repository;

import com.utec.dto.TelefonoDTO;
import com.utec.model.Telefono;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TelefonoRepository extends JpaRepository<Telefono,Integer> {

    @Query(value = """
SELECT new com.utec.dto.TelefonoDTO
        (t.idTelefono,t.numTelefono)
 FROM Telefono t
 WHERE t.usuario.id = :idUsuario
 """)
    List<TelefonoDTO> findAllByIdUser(@Param("idUsuario") int idUsuario);
}
