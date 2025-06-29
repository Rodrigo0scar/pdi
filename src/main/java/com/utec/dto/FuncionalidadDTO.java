package com.utec.dto;


import com.utec.model.Estado;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
public class FuncionalidadDTO {

    private Integer idFuncionalidad;
@NotNull
@Size(min = 1, max = 50)
private String nombre;

@NotNull
@Size(min = 1, max = 250)
private String descripcion;

//private Integer idEstado;

private Estado estado;
}
