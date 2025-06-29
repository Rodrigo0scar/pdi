package com.utec.mapper;


import com.utec.dto.SocioPagaCuotaDTO;
import com.utec.model.*;
import org.springframework.stereotype.Component;

@Component
public class SocioPagaCuotaMapper {

    //de entidad a DTO
    public SocioPagaCuotaDTO toDTO(SocioPagaCuota entity){
        return SocioPagaCuotaDTO.builder()
                .idCuota(entity.getCuota().getId())
                .fechaCobro(entity.getFechCobro())
                .modoPago(entity.getModoPago().getId())
                .build();
    }

    //de dto a entidad
    public SocioPagaCuota toEntity(SocioPagaCuotaDTO dto, Socio socio, Cuota cuota, ModoPago modoPago){
        return SocioPagaCuota.builder()
                .id(new SocioPagaCuotaId(socio.getId(), cuota.getId()))
                .socio(socio)
                .cuota(cuota)
                .fechCobro(dto.getFechaCobro()) //debe coincidir con el campo exacto en el DTO
                .modoPago(modoPago)
                .build();
    }
}
