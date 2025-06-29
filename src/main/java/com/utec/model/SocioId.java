package com.utec.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SocioId implements Serializable {
    private Integer idUsuario;
    private Integer idTipoDocumento;
} 