package com.utec.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EspacioDTO {
    private Integer idEspacio;
    @NotBlank
    @Size(min = 1, max = 50)
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñü ]+$", message = "Nombre contiene caracteres inválidos")
    private String nombre;

    @NotNull
    @Min(1)
    @Max(999)
    private Integer capacidadMaxima;

    @NotNull
    @DecimalMin(value = "1.00")
    @DecimalMax(value = "99999.00")
    private BigDecimal precioReservaSocio;

    @NotNull
    @DecimalMin(value = "1.00")
    @DecimalMax(value = "99999.00")
    private BigDecimal precioReservaNoSocio;

    @NotNull
    private LocalDate fechaVigenciaPrecio;

    @Size(max = 250)
    @Pattern(regexp = "^[A-Za-z0-9ÁÉÍÓÚáéíóúÑñü,\\.\\- ]*$", message = "Observaciones debe contener letras y números")
    private String observaciones;

    private Integer idEstado;
}
