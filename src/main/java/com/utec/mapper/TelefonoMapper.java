package com.utec.mapper;
import com.utec.dto.TelefonoDTO;
import com.utec.model.Telefono;
import org.springframework.stereotype.Component;

@Component
public class TelefonoMapper {
    public Telefono toEntity(TelefonoDTO dto){

        if(dto == null){
            return null;
        }

        return Telefono.builder()
                .numTelefono(dto.getNumTelefono())
                .build();
    }

    public TelefonoDTO toDto(Telefono tel){

        if(tel == null){
            return null;
        }

        return TelefonoDTO.builder()
                .numTelefono(tel.getNumTelefono()).build();
    }
}
