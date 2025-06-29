package com.utec.repository;

import com.utec.model.*;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SocioRepository extends JpaRepository<Socio, Integer> {
    Optional<Socio> findByTipoDocumentoAndNumeDocumento(TipoDocumento tipoDocumento, String numeDocumento);
    Optional<Socio> findByCorreo(String correo);

    @Modifying
    @Transactional
    @Query(value = """
INSERT INTO socio (id_usuario, id_categoria, dif_auditiva, uso_leng_senia, id_estado, paga_desde, paga_hasta, id_subcomision) 
VALUES (:usuario, :categoria, :difAud, :lengSen, :estado, :pagaDesde, :pagaHasta, :subcomision)
""",nativeQuery = true)
    void crearSocioDesdeUsuario(@Param("usuario") Integer usuario,
                                @Param("categoria") Integer categoria,
                                @Param("difAud") boolean difAud,
                                @Param("lengSen") boolean lengSen,
                                @Param("estado") Integer estado,
                                @Param("pagaDesde") Integer pagaDesde,
                                @Param("pagaHasta") Integer pagaHasta,
                                @Param("subcomision") Integer subcomision);
}
