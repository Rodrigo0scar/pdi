package com.utec.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FiltroReporteAuditoriaDTO{
private String usuario;
private LocalDateTime fechaDesde;
private LocalDateTime fechaHasta;
private String operacion;
}
