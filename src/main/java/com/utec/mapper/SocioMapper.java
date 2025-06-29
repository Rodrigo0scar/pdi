package com.utec.mapper;

import com.utec.dto.SocioDTO;
import com.utec.model.Socio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class SocioMapper {

    @Autowired
    TelefonoMapper telefonoMapper;

    //modificar este
    public SocioDTO toDto(Socio entity) {
        if (entity == null) return null;
    
        return SocioDTO.builder()
                .numeDocumento(entity.getNumeDocumento())
                .primerNombre(entity.getPrimNombre())
                .segundoNombre(entity.getSeguNombre())
                .primerApellido(entity.getPrimApellido())
                .seguApellido(entity.getSeguApellido())
                .correo(entity.getCorreo())
                .fechaNacimiento(entity.getFechNacimiento())
                .apartamento(entity.getApartamento())
                .calle(entity.getCalle())
                .numePuerta(entity.getNumePuerta())
                .idEstado(entity.getEstado() != null ? entity.getEstado().getIdestado() : null)
                .idTipoDocumento(entity.getTipoDocumento() != null ? entity.getTipoDocumento().getId() : null)
                .idPerfil(entity.getPerfil() != null ? entity.getPerfil().getIdPerfil() : null)
                .telefonos(entity.getTelefonos() != null? entity.getTelefonos().stream().map(telefonoMapper :: toDto).collect(Collectors.toList()):null)
                .IdSubcomision(entity.getSubcomision() != null ? entity.getSubcomision().getIdSubcomision() : null)
                .IdCategoria(entity.getCategoria() != null ? entity.getCategoria().getIdCategoria() : null)
                .difAuditiva(entity.getDifAuditiva())
                .usoLengSenias(entity.getUsoLengSenias())
                .pagaDesde(entity.getPagaDesde())
                .pagaHasta(entity.getPagaHasta())
                .build();
    }
    

    //terminiar este
    public Socio toEntity (SocioDTO dto){
        if(dto == null)   return null;

        Socio socio = new Socio();

        socio.setNumeDocumento(dto.getNumeDocumento());
        socio.setPrimNombre(dto.getPrimerNombre());
        socio.setSeguNombre(dto.getSegundoNombre());
        socio.setPrimApellido(dto.getPrimerApellido());
        socio.setSeguApellido(dto.getSeguApellido());
        socio.setContrasenia(dto.getContrasenia());
        socio.setCorreo(dto.getCorreo());
        socio.setFechNacimiento(dto.getFechaNacimiento());
        socio.setApartamento(dto.getApartamento());
        socio.setCalle(dto.getCalle());
        socio.setNumePuerta(dto.getNumePuerta());
        socio.setTelefonos(dto.getTelefonos() != null? dto.getTelefonos().stream().map(telefonoMapper :: toEntity).collect(Collectors.toList()):null);
        socio.setDifAuditiva(dto.getDifAuditiva());
        socio.setUsoLengSenias(dto.getUsoLengSenias());
        socio.setPagaDesde(dto.getPagaDesde());
        socio.setPagaHasta(dto.getPagaHasta());

        return socio;
    }
}
