package com.utec.repository;

import com.utec.model.PerfilAccedeFuncionalidad;
import com.utec.model.PerfilAccedeFuncionalidadId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PerfilAccedeFuncionalidadRepository extends JpaRepository<PerfilAccedeFuncionalidad, Integer> {
    boolean existsByFuncionalidad_Id(Integer idFuncionalidad);
    boolean existsById_IdPerfilAndId_IdFuncionalidad(Integer idPerfil, Integer idFuncionalidad);
    boolean existsByFuncionalidad_IdAndEstado_Idestado(Integer idFuncionalidad, Integer idEstado);

    Optional<PerfilAccedeFuncionalidad> findById(PerfilAccedeFuncionalidadId id);

    @Query(value = """
     SELECT paf.id_funcionalidad FROM perfil_accede_funcionalidad paf WHERE paf.id_perfil = :idPerfil and paf.id_estado=1
     """, nativeQuery = true)
    List<Integer> findIdsFuncionalidadesByPerfil(@Param("idPerfil") Integer idPerfil);

}
