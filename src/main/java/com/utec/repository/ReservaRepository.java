package com.utec.repository;

import com.utec.dto.ReporteReservaDTO;
import com.utec.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {

    List<Reserva> findByEspacio_IdEspacio(Integer idEspacio);

    boolean existsByEspacio_IdEspacioAndEstado_Idestado(Integer idEspacio, int i);

    @Query(value="""
        SELECT 
        DATE(r.fech_hora_reserva) AS fecha,
        SUM(CASE WHEN r.id_estado = 1 THEN 1 ELSE 0 END) AS cantidad_reservas,
        SUM(CASE WHEN r.id_estado = 2 THEN 1 ELSE 0 END) AS cantidad_cancelaciones,
        r.id_Espacio
        FROM Reserva r
        WHERE r.fech_hora_reserva BETWEEN :desde AND :hasta
          AND r.id_Espacio IN (:idsEspacios)
          GROUP BY DATE(r.fech_hora_reserva),r.id_Espacio
          ORDER BY r.id_Espacio
    """, nativeQuery = true)
    List<Object[]> obtenerReporteAgrupadoPorEspacio(@Param("desde") LocalDate fechaDesde, @Param ("hasta")LocalDate fechaHasta, @Param("idsEspacios") List<Integer> idsEspacios);

    @Query(value = """
    SELECT 
        DATE(r.fech_hora_reserva) AS fecha,
        SUM(CASE WHEN r.id_estado = 1 THEN 1 ELSE 0 END) AS cantidad_reservas,
        SUM(CASE WHEN r.id_estado = 2 THEN 1 ELSE 0 END) AS cantidad_cancelaciones,
        r.id_espacio
    FROM reserva r
    WHERE r.fech_hora_reserva BETWEEN :desde AND :hasta
      AND r.id_espacio IN (:idsEspacios)
    GROUP BY DATE(r.fech_hora_reserva),r.id_Espacio
    ORDER BY DATE(r.fech_hora_reserva)
""", nativeQuery = true)
    List<Object[]> obtenerReporteAgrupadoPorFecha(
            @Param("desde") LocalDate desde,
            @Param("hasta") LocalDate hasta,
            @Param("idsEspacios") List<Integer> idsEspacios
    );

}
