package com.utec.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioDTO {
    @Size(min = 1, max = 15, message = "No puede ingresar mas de 15 digitos")
    @Pattern(regexp = "^[A-Za-z0-9]{1,15}$")
    @NotNull(message = "Este campo es obligatorio")
    private String numeDocumento;

    @NotNull(message = "Este campo es obligatorio")
    @Size(min = 1, max = 20, message = "No puede ingresar mas de 20 digitos")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñü ]+$")
    private String primNombre;

    @Size(min = 1, max = 20, message = "No puede ingresar mas de 20 digitos")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñü ]+$")
    private String seguNombre;

    @NotNull(message = "Este campo es obligatorio")
    @Size(max = 20, message = "No puede ingresar mas de 20 digitos")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñü ]+$")
    private String primApellido;

    @Size(max = 20, message = "No puede ingresar mas de 20 digitos")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñü ]+$")
    private String seguApellido;

    @NotNull(message = "Este campo es obligatorio")
    @NotBlank
    @Size(min = 8, max = 20, message = "No puede ingresar mas de 20 digitos")
    private String contrasenia;

    @NotBlank
    @Email
    @Size(max = 100)
    @Pattern(regexp ="^[a-zA-Z0-9._%+-]+@(gmail\\.com|hotmail\\.com|asur\\.com|([a-zA-Z0-9-]+\\.)*utec\\.edu\\.uy)$")
    @NotNull(message = "Este campo es obligatorio")
    private String correo;

    @PastOrPresent
    @NotNull(message = "Este campo es obligatorio")
    private LocalDate fechNacimiento;

    @Pattern(regexp = "^[A-Za-z0-9 ]*$")
    @Size(max = 10, message = "No puede ingresar mas de 10 digitos")
    @NotNull(message = "Este campo es obligatorio")
    private String apartamento;

    @Pattern(regexp = "^[A-Za-z0-9ÁÉÍÓÚáéíóúÑñü,\\.\\- ]+$")
    @Size(min = 1, max = 50, message = "No puede ingresar mas de 50 digitos")
    @NotNull(message = "Este campo es obligatorio")
    private String calle;

    @NotNull(message = "Este campo es obligatorio")
    private Integer numePuerta;

    @NotNull(message = "Este campo es obligatorio")
    private Integer idEstado;

    @NotNull(message = "Este campo es obligatorio")
    private Integer idTipoDocumento;

    @NotNull(message = "Este campo es obligatorio")
    private Integer idPerfil;

    private List<TelefonoDTO> telefonos;

}