package com.utec.auditoria;

import com.utec.model.Auditoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditoriaRepository extends JpaRepository<Auditoria, Integer> {

/*    @Query("SELECT a FROM Auditoria a " +
            "WHERE (:usuario IS NULL OR a.usuario = :usuario) " +
            "AND (:fechaDesde IS NULL OR a.fechaHora >= :fechaDesde) " +
            "AND (:fechaHasta IS NULL OR a.fechaHora <= :fechaHasta) " +
            "AND (:operacion IS NULL OR LOWER(a.operacion) LIKE LOWER(CONCAT('%', :operacion, '%'))) " +
            "ORDER BY a.fechaHora DESC")*/
@Query(value = "SELECT * FROM auditoria a " +
        "WHERE (:usuario IS NULL OR a.usuario = :usuario) " +

        "AND DATE(a.fecha_hora) >= DATE(:fechaDesde) " +
        "AND DATE(a.fecha_hora) <= DATE(:fechaHasta) " +
        "AND (:operacion IS NULL OR a.operacion ILIKE CONCAT('%', CAST(:operacion AS VARCHAR), '%')) " +
        "ORDER BY a.fecha_hora DESC", nativeQuery = true)
    List<Auditoria> buscarPorFiltros(@Param("usuario") String usuario,
                                     @Param("fechaDesde") LocalDateTime fechaDesde,
                                     @Param("fechaHasta") LocalDateTime fechaHasta,
                                     @Param("operacion") String operacion);

}

