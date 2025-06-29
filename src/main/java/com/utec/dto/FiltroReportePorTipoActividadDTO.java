package com.utec.dto;

import com.utec.enums.TipoReporteEnum;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FiltroReportePorTipoActividadDTO {
    @NotNull(message = "La fecha desde es obligatoria")
    private LocalDateTime fechaDesde;

    @NotNull(message = "La fecha hasta es obligatoria")
    private LocalDateTime fechaHasta;

    private TipoReporteEnum tipoReporte = TipoReporteEnum.AMBAS;

    private List<String> tiposActividad;
}