package com.utec.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import com.utec.model.Estado;
import com.utec.model.Perfil;
import com.utec.model.TipoDocumento;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioResponseDTO {
    private Integer id;
    private String numeDocumento;
    private String primNombre;
    private String seguNombre;
    private String primApellido;
    private String seguApellido;
    private String correo;
    private LocalDate fechNacimiento;
    private String apartamento;
    private String calle;
    private Integer numePuerta;
    private Estado estado;
    private TipoDocumento tipoDocumento;
    private Perfil perfil;
}
