package com.utec.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetActividadDTO {
    public Integer id;
    public String nombre;
    public String tipoDeActividad;
    public String estado;
    public LocalDateTime fecha;
    public BigDecimal costo;
}
