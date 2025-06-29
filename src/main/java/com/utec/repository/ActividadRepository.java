package com.utec.repository;

import com.utec.model.Actividad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ActividadRepository extends JpaRepository<Actividad, Integer> {
    @Query("SELECT a FROM Actividad a WHERE a.estado.id = 1 AND a.fech_hora_actividad > CURRENT_DATE")
    List<Actividad> findActividadesActivasParaInscripcion();

    @Query("SELECT DISTINCT a FROM Actividad a " +
    "JOIN a.estado " +
    "LEFT JOIN a.usuario " +
    "LEFT JOIN a.tipoActividad " +
    "LEFT JOIN a.espacio " +
    "LEFT JOIN a.modoPago " +
    "WHERE a.estado.idestado = 1 AND a.fech_hora_actividad > CURRENT_DATE")
List<Actividad> findActividadesActivasParaInscripcionWithRelations();
//    @Query("SELECT a FROM Actividad a WHERE a.estado.idestado = 1 AND a.fech_hora_actividad > CURRENT_DATE")
//    List<Actividad> findActividadesActivasParaInscripcion();


}
