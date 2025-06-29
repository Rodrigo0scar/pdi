package com.utec.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ReservaPagoSeniaDTO {

    @NotNull(message = "El ID de la reserva es obligatorio")
    @Schema(description = "ID de la reserva", example = "1")
    private Integer idReserva;


    @NotNull(message = "La fecha de pago de la seña es obligatoria")
    @Schema(description = "Fecha en la que se paga la seña", example = "2025-06-27")
    private LocalDate fechPagoSenia;

    @NotNull(message = "El importe de la seña pagada es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El importe de la seña no puede ser negativo")
    private BigDecimal impoSeniPagado;


    @NotNull(message = "La fecha de cobro es obligatoria")
    @Schema(description = "Fecha y hora del cobro registrado", example = "2025-06-27T14:30:00")
    private LocalDateTime fechCobro;
}
