package com.utec.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioDatosPropiosDTO {

    @Size(min = 1, max = 20)
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñü ]+$")
    private String primNombre;

    @Size(max = 20)
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñü ]+$")
    private String seguNombre;

    @Size(max = 20)
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñü ]+$")
    private String primApellido;

    @Size(max = 20)
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñü ]+$")
    private String seguApellido;

    @PastOrPresent
    private LocalDate fechNacimiento;

    @Pattern(regexp = "^[A-Za-z0-9ÁÉÍÓÚáéíóúÑñü,\\.\\- ]+$")
    @Size(max = 50)
    private String calle;

    @Size(max = 10)
    private String apartamento;

    private Integer numePuerta;

    private List<TelefonoDTO> telefonos;

    // Campo opcional para cambiar contraseña
    @Size(min = 8, max = 20)
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]+$", message = "Debe contener letras y números")
    private String nuevaContrasenia;
}
