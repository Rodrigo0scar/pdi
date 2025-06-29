package com.utec.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable

public class SocioPagaCuotaId implements Serializable {
    private Integer idUsuario;
    private Integer idCuota;

}
