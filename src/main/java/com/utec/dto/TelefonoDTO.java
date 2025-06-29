package com.utec.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TelefonoDTO {
    private Integer idTelefono;
    //@NotNull(message = "El numero de telefono no puede ser nulo.")
    //@NotBlank(message = "El numero de telefono no puede estar vacio.")
    private Integer numTelefono;
}
