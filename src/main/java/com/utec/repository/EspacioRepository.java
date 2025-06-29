package com.utec.repository;

import com.utec.model.Espacio;
import com.utec.model.TipoDocumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface EspacioRepository  extends JpaRepository<Espacio, Integer> {
    Optional<Espacio> findByNombre(String nombre);
    Optional<Espacio> findById(Integer id);

    List<Espacio> findByEstado_Idestado(Integer idestado);

    List<Espacio>  findByNombreContainingIgnoreCase(String nombre);


   @Query(value = """
    SELECT * FROM espacio e
    WHERE e.id_estado = 1
       AND e.capa_maxima >= ?3
      AND NOT EXISTS (
        SELECT 1 FROM reserva r
        WHERE r.id_espacio = e.id_espacio
          AND r.id_estado = 1
          AND (
            (?1 < r.fech_hora_reserva + INTERVAL '1 hour' * r.duracion)
            AND (?2 > r.fech_hora_reserva)
          )
      )
      AND NOT EXISTS (
        SELECT 1 FROM actividad a
        WHERE a.id_espacio = e.id_espacio
          AND a.id_estado = 1
          AND (
            (?1 < a.fech_hora_actividad + INTERVAL '1 hour' * a.duracion)
            AND (?2 > a.fech_hora_actividad)
          )
      )
""", nativeQuery = true)
   List<Espacio> buscarEspaciosDisponibles(LocalDateTime nuevaInicio, LocalDateTime nuevaFin,   Integer capacidadSolicitada);


}
