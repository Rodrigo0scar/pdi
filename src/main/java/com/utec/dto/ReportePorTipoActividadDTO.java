package com.utec.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportePorTipoActividadDTO {
    private String tipoActividad;
    private Integer cantidadInscriptos;
    private Integer cantidadCancelados;

}

