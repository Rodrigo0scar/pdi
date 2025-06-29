package com.utec.mapper;

import com.utec.dto.EspacioDTO;
import com.utec.model.Espacio;
import org.springframework.stereotype.Component;

@Component
public class EspacioMapper {
    public Espacio toEntity(EspacioDTO dto) {
        return Espacio.builder()
                .idEspacio(dto.getIdEspacio())
                .nombre(dto.getNombre())
                .capacidadMaxima(dto.getCapacidadMaxima())
                .precioReservaSocio(dto.getPrecioReservaSocio())
                .precioReservaNoSocio(dto.getPrecioReservaNoSocio())
                .fechaVigenciaPrecio(dto.getFechaVigenciaPrecio())
                .observaciones(dto.getObservaciones())
                //.estado().estado(e).idEstado(2) // por defecto
                .build();
    }

    public EspacioDTO toDto(Espacio entity) {
        return EspacioDTO.builder()
                .idEspacio(entity.getIdEspacio())
                .nombre(entity.getNombre())
                .capacidadMaxima(entity.getCapacidadMaxima())
                .precioReservaSocio(entity.getPrecioReservaSocio())
                .precioReservaNoSocio(entity.getPrecioReservaNoSocio())
                .observaciones(entity.getObservaciones())
                .fechaVigenciaPrecio(entity.getFechaVigenciaPrecio())
                .idEstado(entity.getEstado().getIdestado())
                .build();
    }
}
