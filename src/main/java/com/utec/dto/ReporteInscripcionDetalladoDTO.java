package com.utec.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReporteInscripcionDetalladoDTO {

    private Integer totalInscripciones;
    private Integer totalCancelaciones;
    private List<ReporteInscripcionPorActividadDTO> reportePorActividad;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReporteInscripcionPorActividadDTO {
        private Integer idActividad;
        private String nombreActividad;
        private String tipoActividad;
        private Integer cantidadInscripciones;
        private Integer cantidadCancelaciones;
    }
} 