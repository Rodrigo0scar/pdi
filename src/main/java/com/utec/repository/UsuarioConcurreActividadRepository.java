package com.utec.repository;

import com.utec.dto.ReporteInscripcionDTO;
import com.utec.dto.ReporteInscripcionDetalladoDTO;
import com.utec.dto.ReportePorTipoActividadDTO;
import com.utec.dto.ReporteTipoActDTO;
import com.utec.model.UsuarioConcurreActividad;
import com.utec.model.UsuarioConcurreActividadId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UsuarioConcurreActividadRepository extends JpaRepository<UsuarioConcurreActividad, UsuarioConcurreActividadId> {
    boolean existsByUsuarioIdAndActividadId(Integer id_usuario,Integer id_actividad);
    Optional<UsuarioConcurreActividad> findByUsuarioIdAndActividadId(Integer id_usuario, Integer id_actividad);
    
    @Query("""
    SELECT new com.utec.dto.ReporteInscripcionDTO(
        CAST(SUM(CASE WHEN uca.asistencia = true THEN 1 ELSE 0 END) AS int),
        CAST(SUM(CASE WHEN uca.asistencia = false THEN 1 ELSE 0 END) AS int)
    )
    FROM UsuarioConcurreActividad uca
    JOIN uca.actividad a
    JOIN a.tipoActividad ta
    WHERE ta.tipo = :tipoActividad
""")
    ReporteInscripcionDTO obtenerReportePorTipoActividad(@Param("tipoActividad") String tipoActividad );

    @Query("""
    SELECT new com.utec.dto.ReportePorTipoActividadDTO(
        ta.tipo,
        CAST(SUM(CASE WHEN uca.asistencia = true THEN 1 ELSE 0 END) AS int),
        CAST(SUM(CASE WHEN uca.asistencia = false THEN 1 ELSE 0 END) AS int)
    )
    FROM UsuarioConcurreActividad uca
    JOIN uca.actividad a
    JOIN a.tipoActividad ta
    WHERE uca.fechCobro BETWEEN :fechaDesde AND :fechaHasta
      AND (:tiposActividad IS NULL OR ta.tipo IN :tiposActividad)
    GROUP BY ta.tipo
    ORDER BY ta.tipo
""")
    List<ReportePorTipoActividadDTO>obtenerReportePorTipoActividad(
            @Param("fechaDesde") LocalDate fechaDesde,
            @Param("fechaHasta") LocalDate fechaHasta,
            @Param("tiposActividad") List<String> tiposActividad
    );





    // Nueva consulta para reporte detallado por rango de fechas y actividades
    @Query("""
    SELECT new com.utec.dto.ReporteInscripcionDetalladoDTO$ReporteInscripcionPorActividadDTO(
        a.id,
        a.nombre,
        ta.tipo,
        CAST(SUM(CASE WHEN uca.asistencia = true THEN 1 ELSE 0 END) AS int),
        CAST(SUM(CASE WHEN uca.asistencia = false THEN 1 ELSE 0 END) AS int)
    )
    FROM UsuarioConcurreActividad uca
    JOIN uca.actividad a
    JOIN a.tipoActividad ta
    WHERE uca.fechCobro BETWEEN :fechaDesde AND :fechaHasta
    AND (:idActividades IS NULL OR a.id IN :idActividades)
    GROUP BY a.id, a.nombre, ta.tipo
    ORDER BY a.nombre
    """)
    List<ReporteInscripcionDetalladoDTO.ReporteInscripcionPorActividadDTO> obtenerReporteDetalladoPorFechas(
        @Param("fechaDesde") LocalDate fechaDesde,
        @Param("fechaHasta") LocalDate fechaHasta,
        @Param("idActividades") List<Integer> idActividades
    );
    
    // Consulta para obtener totales generales
    @Query("""
    SELECT new com.utec.dto.ReporteInscripcionDTO(
        CAST(SUM(CASE WHEN uca.asistencia = true THEN 1 ELSE 0 END) AS int),
        CAST(SUM(CASE WHEN uca.asistencia = false THEN 1 ELSE 0 END) AS int)
    )
    FROM UsuarioConcurreActividad uca
    JOIN uca.actividad a
    WHERE uca.fechCobro BETWEEN :fechaDesde AND :fechaHasta
    AND (:idActividades IS NULL OR a.id IN :idActividades)
    """)
    ReporteInscripcionDTO obtenerTotalesGenerales(
        @Param("fechaDesde") LocalDate fechaDesde,
        @Param("fechaHasta") LocalDate fechaHasta,
        @Param("idActividades") List<Integer> idActividades
    );
    
    // Consulta para obtener actividades disponibles
    @Query("""
    SELECT DISTINCT a.id
    FROM UsuarioConcurreActividad uca
    JOIN uca.actividad a
    ORDER BY a.id
    """)
    List<Integer> obtenerActividadesDisponibles();

}
