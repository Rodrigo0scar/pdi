package com.utec.dto;

import com.utec.model.Cuota;
import com.utec.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class SocioPagaCuotaDTO {

    private Integer idUsuario;
    private Integer idCuota;
    private LocalDate fechaCobro;
    private Integer modoPago;

}
