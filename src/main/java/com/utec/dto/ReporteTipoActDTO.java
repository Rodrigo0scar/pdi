package com.utec.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReporteTipoActDTO {
    private Integer totalInscriptos;
    private Integer totalCancelados;
    private List<ReportePorTipoActividadDTO> reportePorTipoActividad;


}
