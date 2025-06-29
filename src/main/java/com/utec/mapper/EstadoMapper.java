package com.utec.mapper;

import com.utec.dto.EstadoDTO;
import com.utec.model.Estado;
import com.utec.repository.EstadoRepository;
import org.springframework.stereotype.Component;

@Component
public class EstadoMapper {

    public Estado toEntity (EstadoDTO dto){
        return Estado.builder().idestado(dto.getIdestado())
                .descripcion(dto.getDescripcion())
                .build();
    }

    public EstadoDTO toDto (Estado entity){
        return EstadoDTO.builder().idestado(entity.getIdestado())
                .descripcion(entity.getDescripcion())
                .build();
    }
}
