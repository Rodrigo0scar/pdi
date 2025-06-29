package com.utec.mapper;

import com.utec.dto.UsuarioDTO;
import com.utec.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UsuarioMapper {

    @Autowired
    TelefonoMapper telefonoMapper;

    public UsuarioDTO toDTO (Usuario entity){
        return UsuarioDTO.builder()
                .numeDocumento(entity.getNumeDocumento())
                .primNombre(entity.getPrimNombre())
                .seguNombre(entity.getSeguNombre())
                .primApellido(entity.getPrimApellido())
                .seguApellido(entity.getSeguApellido())
                .contrasenia(entity.getContrasenia())
                .correo(entity.getCorreo())
                .fechNacimiento(entity.getFechNacimiento())
                .apartamento(entity.getApartamento())
                .calle(entity.getCalle())
                .numePuerta(entity.getNumePuerta())
                .idEstado(entity.getEstado().getIdestado())
                .idTipoDocumento(entity.getTipoDocumento().getId())
                .idPerfil(entity.getPerfil().getIdPerfil())
                .telefonos(
                        entity.getTelefonos() != null
                                ? entity.getTelefonos().stream()
                                .map(telefonoMapper::toDto)
                                .collect(Collectors.toList())
                                : null)
                .build();
    }

    public Usuario toEntity (UsuarioDTO dto){
        return Usuario.builder()
                .numeDocumento(dto.getNumeDocumento())
                .primNombre(dto.getPrimNombre())
                .seguNombre(dto.getSeguNombre())
                .primApellido(dto.getPrimApellido())
                .seguApellido(dto.getSeguApellido())
                .contrasenia(dto.getContrasenia())
                .correo(dto.getCorreo())
                .fechNacimiento(dto.getFechNacimiento())
                .apartamento(dto.getApartamento())
                .calle(dto.getCalle())
                .numePuerta(dto.getNumePuerta())
                .estado(null)
                .tipoDocumento(null)
                .perfil(null)
                .telefonos(
                        dto.getTelefonos() != null
                                ? dto.getTelefonos().stream()
                                .map(telefonoMapper::toEntity)
                                .collect(Collectors.toList())
                                : null)
                .build();
    }
}
