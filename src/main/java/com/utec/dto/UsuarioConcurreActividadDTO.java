package com.utec.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Schema(description = "DTO para registrar el pago de una actividad por parte de un usuario")
public class UsuarioConcurreActividadDTO {

   private Date fechCobro;
   private BigDecimal montCobrado;

   private Boolean asistencia;    //cambio para pagos de boolean a Boolean(wrapper)
   private Boolean pagoTicket;   //cambio para pagos de boolean a Boolean(wrapper)

   @Schema(description = "ID del usuario", example = "1", required = true)
   private Integer idUsuario;

   @Schema(description = "ID de la actividad", example = "4", required = true)
   private Integer idActividad;
   private Integer idModoPago;

}
