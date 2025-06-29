package com.utec.mapper;

import com.utec.dto.PerfilDTO;
import com.utec.model.Perfil;
import com.utec.repository.EstadoRepository;
import org.springframework.stereotype.Component;

@Component
public class PerfilMapper {

    public Perfil toEntity (PerfilDTO dto){
        return Perfil.builder()
                .nombre(dto.getNombre())
                .description(dto.getDescripcion())
                .estado(null).build();
    }

    public PerfilDTO toDto (Perfil entity){
        return PerfilDTO.builder()
                .nombre(entity.getNombre())
                .descripcion(entity.getDescription())
                .idEstado(entity.getEstado().getIdestado())
                .build();
    }
}
