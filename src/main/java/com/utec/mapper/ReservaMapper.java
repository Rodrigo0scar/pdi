package com.utec.mapper;

import com.utec.dto.ReservaDTO;
import com.utec.model.Reserva;
import com.utec.repository.EspacioRepository;
import com.utec.repository.EstadoRepository;
import com.utec.repository.ModoPagoRepository;
import com.utec.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReservaMapper {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EspacioRepository espacioRepository;

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private ModoPagoRepository modoPagoRepository;



    public Reserva toEntity(ReservaDTO dto) {
        return Reserva.builder()
                .idReserva(dto.getIdReserva())
                .usuario(usuarioRepository.findById(dto.getIdUsuario()).orElse(null))
                .espacio(espacioRepository.findById(dto.getIdEspacio()).orElse(null))
                .fechHoraReserva(dto.getFechHoraReserva())
                .duracion(dto.getDuracion())
                .cantPersonas(dto.getCantPersonas())
                .estado(estadoRepository.findById(dto.getIdEstado()).orElse(null))
                .impoTotal(dto.getImpoTotal())
                .fechPagoSenia(dto.getFechPagoSenia())
                .impoSeniPagado(dto.getImpoSeniPagado())
                .fechCobro(dto.getFechCobro())
                .fechVtoSenia(dto.getFechVtoSenia())
                //.modoPago(modoPagoRepository.findById(dto.getIdModoPago()))
               //.idModoPago(dto.getIdModoPago())
                .modoPago(modoPagoRepository.findById(dto.getIdModoPago()).orElse(null))
                .build();

    }


    public ReservaDTO toDto(Reserva reserva) {
       return ReservaDTO.builder()
               .idReserva(reserva.getIdReserva())
               .cantPersonas(reserva.getCantPersonas())
               .idUsuario(reserva.getUsuario().getId())
               .idEspacio(reserva.getEspacio().getIdEspacio())
               .fechHoraReserva(reserva.getFechHoraReserva())
               .duracion(reserva.getDuracion())
               .cantPersonas(reserva.getCantPersonas())
               .idEstado(reserva.getEstado() != null ? reserva.getEstado().getIdestado() : null)
               .impoTotal(reserva.getImpoTotal())
               .fechPagoSenia(reserva.getFechPagoSenia())
               .impoSeniPagado(reserva.getImpoSeniPagado())
               .fechCobro(reserva.getFechCobro())
               .fechVtoSenia(reserva.getFechVtoSenia())
              .idModoPago(reserva.getModoPago().getId())
               .build();
    }
}
