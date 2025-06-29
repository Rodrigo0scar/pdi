package com.utec.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FiltroReporteReservaDTO {

    private LocalDate fechaDesde;
    private LocalDate fechaHasta;
    private LinkedList<Integer> idEspacios;

    //tipo de operacion a filtrar
    private String tipoOperacion; //"INSCRIPCION", "CANCELACION" o "AMBAS"
}
