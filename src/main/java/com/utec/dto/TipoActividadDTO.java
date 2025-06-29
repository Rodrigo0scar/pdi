package com.utec.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoActividadDTO {

    @Pattern(regexp = "^[A-Za-z0-9ÁÉÍÓÚáéíóúÑñü,.\\- ]+$")
    @NotNull(message = "El tipo de actividad no puede ser nulo.")
    @NotBlank(message = "El tipo de actividad no puede estar en blanco.")
    @Size(min = 1,max = 30,message = "El tipo de actividad debe tener entre 1 y 30 caracteres.")
    private String tipo;

    @Pattern(regexp = "^[A-Za-z0-9ÁÉÍÓÚáéíóúÑñü,.\\- ]+$")
    @NotBlank(message = "El tipo de actividad no puede estar en blanco.")
    @Size(min = 1,max = 50,message = "La descripcion del tipo de actividad debe tener entre 1 y 50 caracteres.")
    private String descripcion;

    private LocalDate fechaBaja;

    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñü,.\\- ]+$")
    //@NotBlank(message = "La razon de baja no puede estar en blanco.")
    @Size(min = 1,max = 50,message = "La razon de baja debe contener entre 1 y 50 caracteres.")
    private String razonBaja;

    private String comentariosBaja;

    private Integer idUsuarioBaja;

    //@NotNull(message = "El tipo de actividad debe tener estado.")
    private Integer idEstado;

}
