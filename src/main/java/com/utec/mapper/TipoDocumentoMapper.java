package com.utec.mapper;

import com.utec.dto.TipoDocumentoDTO;
import com.utec.model.TipoDocumento;
import org.springframework.stereotype.Component;

@Component
public class TipoDocumentoMapper {

    public TipoDocumentoDTO toDTO (TipoDocumento entity){
        return TipoDocumentoDTO.builder().id(entity.getId())
                .nombre(entity.getNombre())
                .descripcion(entity.getDescripcion())
                .build();
    }

    public TipoDocumento toEntity (TipoDocumentoDTO dto){
        return TipoDocumento.builder().id(dto.getId())
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .build();
    }
}
