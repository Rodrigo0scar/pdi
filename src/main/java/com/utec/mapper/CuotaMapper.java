package com.utec.mapper;


import com.utec.dto.CuotaDTO;
import com.utec.model.Cuota;
import org.springframework.stereotype.Component;

@Component
public class CuotaMapper {

    public CuotaDTO toDTO(Cuota entity) {
        return CuotaDTO.builder()
                .id(entity.getId())
                .valor(entity.getValor())
                .mes(entity.getMes())
                .build();
    }

    public Cuota toEntity(CuotaDTO dto) {
        return Cuota.builder()
                .id(dto.getId())
                .valor(dto.getValor())
                .mes(dto.getMes())
                .build();
    }
}
