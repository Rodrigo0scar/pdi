package com.utec.mapper;

import com.utec.dto.ModoPagoDTO;
import com.utec.model.ModoPago;
import org.springframework.stereotype.Component;

@Component
public class ModoPagoMapper {

    public ModoPagoDTO toDTO(ModoPago entity) {
        return ModoPagoDTO.builder()
                .id(entity.getId())
                .modo(entity.getModo())
                .build();
    }

    public ModoPago toEntity(ModoPagoDTO dto) {
        return ModoPago.builder()
                .id(dto.getId())
                .modo(dto.getModo())
                .build();
    }}
