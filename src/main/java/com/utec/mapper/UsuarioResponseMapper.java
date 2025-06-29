package com.utec.mapper;

import com.utec.dto.UsuarioResponseDTO;
import com.utec.model.Usuario;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UsuarioResponseMapper {

    public UsuarioResponseDTO toDTO(Usuario entity) {
        return UsuarioResponseDTO.builder()
                .id(entity.getId())
                .numeDocumento(entity.getNumeDocumento())
                .primNombre(entity.getPrimNombre())
                .seguNombre(entity.getSeguNombre())
                .primApellido(entity.getPrimApellido())
                .seguApellido(entity.getSeguApellido())
                .correo(entity.getCorreo())
                .fechNacimiento(entity.getFechNacimiento())
                .apartamento(entity.getApartamento())
                .calle(entity.getCalle())
                .numePuerta(entity.getNumePuerta())
                .estado(entity.getEstado())
                .tipoDocumento(entity.getTipoDocumento())
                .perfil(entity.getPerfil())
                .build();
    }

    public Usuario toEntity(UsuarioResponseDTO dto) {
        return Usuario.builder()
                .id(dto.getId())
                .numeDocumento(dto.getNumeDocumento())
                .primNombre(dto.getPrimNombre())
                .seguNombre(dto.getSeguNombre())
                .primApellido(dto.getPrimApellido())
                .seguApellido(dto.getSeguApellido())
                .correo(dto.getCorreo())
                .fechNacimiento(dto.getFechNacimiento())
                .apartamento(dto.getApartamento())
                .calle(dto.getCalle())
                .numePuerta(dto.getNumePuerta())
                .estado(dto.getEstado())
                .tipoDocumento(dto.getTipoDocumento())
                .perfil(dto.getPerfil())
                .build();
    }

    public List<UsuarioResponseDTO> toDTOList(List<Usuario> entities) {
        return entities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<Usuario> toEntityList(List<UsuarioResponseDTO> dtos) {
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}
