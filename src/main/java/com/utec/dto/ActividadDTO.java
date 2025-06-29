package com.utec.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
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
public class ActividadDTO {

   @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer id;

    @NotNull
    @Size(min = 1, max = 50)
    private String nombre;

    @NotBlank(message = "La descripción no puede estar vacía")
    @Size(min = 1, max = 250)
    @Pattern(regexp = "^[A-Za-z0-9ÁÉÍÓÚáéíóúÑñü., \\-]+$", message = "La descripción contiene caracteres inválidos")
    private String descripcion;

    @NotBlank(message = "Este campo no puede estar vacío")
    @Size(min = 1, max = 100)
    @Pattern(regexp = "^[A-Za-z0-9ÁÉÍÓÚáéíóúÑñü.,\\- ]+$", message = "El campo contiene caracteres inválidos")
    private String objetivo;

    @NotNull(message = "La fecha y hora no puede ser nula")
    @Future(message = "La fecha y hora debe ser futura")
    private LocalDateTime fech_hora_actividad;

    @NotNull(message = "La duracion")
    @Min(value = 1,message = "La duración debe ser mayor a 0")
    private  Integer duracion;

    private boolean requ_inscripcion;

    @NotNull(message = "La fecha de apertura no puede ser nula")
    private LocalDate fech_apertura_inscripcion;

    @NotNull
    @Min(value = 0, message = "El costo no puede ser negativo")
    private BigDecimal costo;

    @NotBlank(message = "Las observaciones no pueden estar vacías")
    @Pattern(regexp = "^[A-Za-z0-9ÁÉÍÓÚáéíóúÑñü.,\\- ]+$", message = "El campo contiene caracteres inválidos")
    private String observaciones;

    @NotNull(message = "El tipo de actividad es obligatorio")
    private Integer idTipoActividad;

    @NotNull(message = "El espacio es obligatorio")
    private Integer idEspacio;

    @NotNull(message = "El modo de pago es obligatorio")
    private Integer idModoPago;

    @NotNull(message = "El estado es obligatorio")
    private Integer idEstado;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonIgnore
    private Integer idUsuario;

}
