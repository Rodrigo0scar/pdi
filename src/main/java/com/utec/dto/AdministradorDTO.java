package com.utec.dto;

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
public class AdministradorDTO {

   // private Integer idUsuario;
    private String numeDocumento;
    private String primerNombre;
    private String segundoNombre;
    private String primerApellido;
    private String seguApellido;
    private String contrasenia;
    private String correo;
    private LocalDate fechaNacimiento;
    private String apartamento;
    private String calle;
    private Integer numePuerta;
    private Integer idEstado;
    private Integer idTipoDocumento;
    private Integer idPerfil;
    private List<TelefonoDTO> telefonos;
}
