package com.utec.repository;

import com.utec.model.Funcionalidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FuncionalidadRepository extends JpaRepository<Funcionalidad, Integer> {
    boolean existsByNombreIgnoreCase(String nombre);

    List<Funcionalidad> findByNombreContainingIgnoreCase(String nombre);

    List<Funcionalidad> findByEstado_Idestado(Integer idEstado);

    @Query(value = "SELECT * FROM funcionalidad WHERE id_estado = 1", nativeQuery = true)
    List<Funcionalidad> findAllActivas();

}
