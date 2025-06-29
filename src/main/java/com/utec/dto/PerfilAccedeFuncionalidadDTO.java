package com.utec.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerfilAccedeFuncionalidadDTO {
    private Integer idPerfil;
    private List<Integer> funcionalidades;
}
