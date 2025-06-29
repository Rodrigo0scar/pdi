package com.utec.mapper;

import com.utec.dto.UsuarioConcurreActividadDTO;
import com.utec.model.Actividad;
import com.utec.model.Usuario;
import com.utec.model.UsuarioConcurreActividad;
import org.springframework.stereotype.Component;

@Component
public class UsuarioConcurreActividadMapper {

    public UsuarioConcurreActividadDTO toDto(UsuarioConcurreActividad entity){
        return UsuarioConcurreActividadDTO.builder()
                .idUsuario(entity.getUsuario().getId())
                .idActividad(entity.getActividad().getId())
                .fechCobro(entity.getFechCobro())
                .montCobrado(entity.getMontCobrado())
                .asistencia(entity.getAsistencia()) //cambio para pagos
                .pagoTicket(entity.getPagoTicket())//cambio para pagos
                .build();
    }

    public UsuarioConcurreActividad toEntity(UsuarioConcurreActividadDTO dto, Usuario usuario, Actividad actividad){
        return UsuarioConcurreActividad.builder()
                .usuario(usuario)
                .actividad(actividad)
                .fechCobro(dto.getFechCobro())
                .montCobrado(dto.getMontCobrado())
                .asistencia(dto.getAsistencia())//cambio para pagos
                .pagoTicket(dto.getPagoTicket())//cambio para pagos
                .build();
    }
}
