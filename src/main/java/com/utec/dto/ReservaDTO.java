package com.utec.dto;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservaDTO {

private Integer idReserva;

    private Integer idUsuario;

    @NotNull(message = "El ID espacio es obligatorio")
    private Integer idEspacio;

    @NotNull(message = "La fecha y hora de reserva es obligatoria")
    private LocalDateTime fechHoraReserva;

    @NotNull(message = "La duración es obligatoria")
    @Min(value = 1, message = "La duración mínima es 1 hora")
    @Max(value = 5, message = "La duración máxima es 5 horas")
    private Integer duracion;

    @NotNull(message = "La cantidad de personas es obligatoria")
    @Min(value = 1, message = "Debe haber al menos 1 persona")
    @Max(value = 99, message = "El máximo permitido es 99 personas")
    private Integer cantPersonas;

   // @NotNull(message = "El ID del estado es obligatorio")
    private Integer idEstado;

  //  @NotNull(message = "El importe total es obligatorio")
   // @DecimalMin(value = "0.0", inclusive = true, message = "El importe debe ser mayor o igual a 0")
    private BigDecimal impoTotal;


    private LocalDate fechPagoSenia;

   // @DecimalMin(value = "0.0", message = "El importe de seña debe ser mayor o igual a 0")
    private BigDecimal impoSeniPagado;

   // @NotNull(message = "La fecha de cobro es obligatoria")
    private LocalDateTime fechCobro;

    private LocalDate fechVtoSenia;

    @NotNull(message = "El ID del modo de pago es obligatorio")
    @Min(value = 1, message = "El ID del modo de pago debe ser positivo")
    private Integer idModoPago;
}
