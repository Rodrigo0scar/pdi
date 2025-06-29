package com.utec.mapper;

import com.utec.dto.TipoActividadDTO;
import com.utec.model.TipoActividad;
import org.springframework.stereotype.Component;

@Component
public class TipoActividadMapper {

    public TipoActividad toEntity(TipoActividadDTO dto){
        return TipoActividad.builder()
                .idTipoActividad(null)
                .tipo(dto.getTipo())
                .descripcion(dto.getDescripcion())
                .comentariosBaja(dto.getComentariosBaja())
                .build();
    }
    public TipoActividadDTO toDTO(TipoActividad entity){
        return TipoActividadDTO.builder()
                .tipo(entity.getTipo())
                .descripcion(entity.getDescripcion())
                .comentariosBaja(entity.getComentariosBaja())
                .idEstado(entity.getEstado().getIdestado())//El id del usuario.
                .build();
    }
}
