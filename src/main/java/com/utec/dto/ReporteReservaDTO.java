package com.utec.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReporteReservaDTO {
  //  private Integer idReserva;
    private LocalDate fecha;
    private Long cantidadReservas;
    private Long cantidadCanceladas;
    private Integer idEspacio;

}
