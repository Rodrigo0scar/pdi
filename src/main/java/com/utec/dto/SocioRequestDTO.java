package com.utec.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SocioRequestDTO {
    private Boolean difAuditiva;
    private Boolean usoLengSenias;
    private Integer pagaDesde;
    private Integer pagaHasta;
    private Integer IdSubcomision;
    private Integer IdCategoria;
}
