package com.utec.mapper;

import com.utec.dto.FuncionalidadDTO;
import com.utec.model.Funcionalidad;
import com.utec.repository.EstadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FuncionalidadMapper {

private EstadoRepository estadoRepository;

public Funcionalidad toEntity(FuncionalidadDTO dto) {
        return Funcionalidad.builder()
                .nombre(dto.getNombre().trim())
                .descripcion(dto.getDescripcion().trim())
                .estado(dto.getEstado())
                .build();
    }


public FuncionalidadDTO toDto(Funcionalidad entity) {
        FuncionalidadDTO dto = new FuncionalidadDTO();
        dto.setNombre(entity.getNombre());
        dto.setDescripcion(entity.getDescripcion());
        dto.setEstado(entity.getEstado());
        return dto;
    }
}
