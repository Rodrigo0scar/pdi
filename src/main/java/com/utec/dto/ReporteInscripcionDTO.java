package com.utec.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReporteInscripcionDTO {

    public ReporteInscripcionDTO(Integer cantidadInscriptos, Integer cantidadCancelados) {
        this.cantidadInscriptos = cantidadInscriptos;
        this.cantidadCancelados = cantidadCancelados;
    }

    private Integer cantidadInscriptos;
    private Integer cantidadCancelados;
}
