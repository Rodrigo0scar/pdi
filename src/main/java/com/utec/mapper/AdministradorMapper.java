package com.utec.mapper;

import com.utec.dto.AdministradorDTO;
import com.utec.model.Administrador;
import com.utec.model.Telefono;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class AdministradorMapper {

    @Autowired
    TelefonoMapper telefonoMapper;

    public AdministradorDTO toDto(Administrador administrador) {
        if (administrador == null) {
            return null;
        }

        return AdministradorDTO.builder()
                //.idUsuario(administrador.getId())
                .numeDocumento(administrador.getNumeDocumento())
                .primerNombre(administrador.getPrimNombre())
                .segundoNombre(administrador.getSeguNombre())
                .primerApellido(administrador.getPrimApellido())
                .seguApellido(administrador.getSeguApellido())
                .correo(administrador.getCorreo())
                .fechaNacimiento(administrador.getFechNacimiento())
                .apartamento(administrador.getApartamento())
                .calle(administrador.getCalle())
                .numePuerta(administrador.getNumePuerta())
                .idEstado(administrador.getEstado() != null ? administrador.getEstado().getIdestado() : null)
                .idTipoDocumento(administrador.getTipoDocumento() != null ? administrador.getTipoDocumento().getId() : null)
                .idPerfil(administrador.getPerfil() != null ? administrador.getPerfil().getIdPerfil() : null)
                .telefonos(administrador.getTelefonos() != null
                        ? administrador.getTelefonos().stream()
                        .map(telefonoMapper :: toDto)
                        .collect(Collectors.toList())
                        :null)
                .build();
    }

    public Administrador toEntity(AdministradorDTO dto) {
        if (dto == null) {
            return null;
        }

        Administrador administrador = new Administrador();
       // administrador.setId(dto.getIdUsuario());
        administrador.setNumeDocumento(dto.getNumeDocumento());
        administrador.setPrimNombre(dto.getPrimerNombre());
        administrador.setSeguNombre(dto.getSegundoNombre());
        administrador.setPrimApellido(dto.getPrimerApellido());
        administrador.setSeguApellido(dto.getSeguApellido());
        administrador.setContrasenia(dto.getContrasenia());
        administrador.setCorreo(dto.getCorreo());
        administrador.setFechNacimiento(dto.getFechaNacimiento());
        administrador.setApartamento(dto.getApartamento());
        administrador.setCalle(dto.getCalle());
        administrador.setNumePuerta(dto.getNumePuerta());
        administrador.setTelefonos(dto.getTelefonos() != null
                ? dto.getTelefonos()
                .stream()
                .map(telefonoMapper :: toEntity)
                .collect(Collectors.toList())
                :null);

        // Los IDs de las entidades relacionadas se manejan en el servicio
        return administrador;
    }
} 