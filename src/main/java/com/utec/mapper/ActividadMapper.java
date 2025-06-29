package com.utec.mapper;

import com.utec.dto.ActividadDTO;
import com.utec.dto.GetActividadDTO;
import com.utec.model.Actividad;
import org.springframework.stereotype.Component;

@Component
public class ActividadMapper {

    public ActividadDTO toDto(Actividad entity) {
        return ActividadDTO.builder()
                .nombre(entity.getNombre())
                .descripcion(entity.getDescripcion())
                .objetivo(entity.getObjetivo())
                .fech_hora_actividad(entity.getFech_hora_actividad())
                .duracion(entity.getDuracion())
                .requ_inscripcion(entity.isRequ_inscripcion())
                .fech_apertura_inscripcion(entity.getFech_apertura_inscripcion())
                .costo(entity.getCosto())
                .observaciones(entity.getObservaciones())
                .build();

    }

    public Actividad toEntity(ActividadDTO dto) {
        return Actividad.builder()
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .objetivo(dto.getObjetivo())
                .fech_hora_actividad(dto.getFech_hora_actividad())
                .duracion(dto.getDuracion())
                .requ_inscripcion(dto.isRequ_inscripcion())
                .fech_apertura_inscripcion(dto.getFech_apertura_inscripcion())
                .costo(dto.getCosto())
                .observaciones(dto.getObservaciones())
                .build();
    }

    public GetActividadDTO toGetActividadDTO(Actividad actividad) {
        return GetActividadDTO.builder()
            .id(actividad.getId())
            .nombre(actividad.getNombre())
            .estado(actividad.getEstado().getDescripcion())
            .costo(actividad.getCosto())
            .fecha(actividad.getFech_hora_actividad())
            .tipoDeActividad(actividad.getTipoActividad().getTipo())
            .build();
            
    }
}
