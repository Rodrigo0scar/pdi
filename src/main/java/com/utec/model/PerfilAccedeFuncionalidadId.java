package com.utec.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class PerfilAccedeFuncionalidadId implements Serializable {
    @Column(name = "Id_Perfil")
    private Integer idPerfil;

    @Column(name = "Id_Funcionalidad")
    private Integer idFuncionalidad;
}
