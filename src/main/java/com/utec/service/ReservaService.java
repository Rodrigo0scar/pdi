package com.utec.service;

import com.utec.dto.FiltroReporteReservaDTO;
import com.utec.dto.ReporteReservaDTO;
import com.utec.dto.ReservaDTO;
import com.utec.dto.ReservaPagoSeniaDTO;
import com.utec.mapper.ReservaMapper;
import com.utec.model.*;
import com.utec.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReservaService {
    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EspacioRepository espacioRepository;

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private ReservaMapper reservaMapper;
@Autowired
private ModoPagoRepository modoPagoRepository;

    private static final Logger logger = LoggerFactory.getLogger(ReservaService.class);

    public ReservaDTO crearReserva(ReservaDTO reservaDto, Optional<Integer> idUsuarioReservaOpt) {
        // Obtener usuario para quien se hace la reserva
        Usuario usuarioReserva = idUsuarioReservaOpt
                .map(id -> usuarioRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id " + id)))
                .orElseGet(() -> {
                    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                    String username = auth.getName();
                    return usuarioRepository.findByCorreo(username)//el logueado filtrar rol!
                            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado en token"));
                });
        // Validar rol del usuario logueado que no sea administrador
        if (usuarioReserva.getPerfil() != null && usuarioReserva.getPerfil().getIdPerfil() == 1) {
            throw new AccessDeniedException("No se permite crear reservas con perfil administrador.");
        }

        // obtener espacio
        Espacio espacio = espacioRepository.findById(reservaDto.getIdEspacio())
                .orElseThrow(() -> new EntityNotFoundException("Espacio no encontrado"));

        //las reservas son solo por horas
        // validar que espacio esté disponible para fecha y duración
        LocalDateTime fechaInicio = reservaDto.getFechHoraReserva();
        LocalDateTime fechaFin = fechaInicio.plusHours(reservaDto.getDuracion());


        List<Espacio> disponibles = espacioRepository.buscarEspaciosDisponibles(fechaInicio, fechaFin, reservaDto.getCantPersonas());


        boolean espacioDisponible = disponibles.stream()
                .anyMatch(e -> e.getIdEspacio().equals(espacio.getIdEspacio()));

        if (!espacioDisponible) {
            throw new IllegalStateException("El espacio no está disponible para la fecha y hora indicadas");
        }

        // Calcular importe si es socio o no
        BigDecimal importe;
        if (usuarioReserva.getPerfil().getIdPerfil() == 2) { // 2 = socio
            importe = espacio.getPrecioReservaSocio();
        } else {
            importe = espacio.getPrecioReservaNoSocio();
        }

        // Calcular fecha de vencimiento de la seña
        //
        LocalDate fechaVtoSenia = reservaDto.getFechHoraReserva().toLocalDate().minusDays(5);

        //busco estado para setear
        Estado estado = estadoRepository.findById(1)
                .orElseThrow(() -> new EntityNotFoundException("Estado no encontrado"));
// Obtener modo de pago
        ModoPago modoPago = modoPagoRepository.findById(reservaDto.getIdModoPago())
                .orElseThrow(() -> new EntityNotFoundException("Modo de pago no encontrado con id " + reservaDto.getIdModoPago()));


        // Crear entidad
        Reserva reserva = Reserva.builder()
                .usuario(usuarioReserva)
                .espacio(espacio)
                .fechHoraReserva(reservaDto.getFechHoraReserva())
                .duracion(reservaDto.getDuracion())
                .cantPersonas(reservaDto.getCantPersonas())
                .impoTotal(importe)
                .fechPagoSenia(null)
                .impoSeniPagado(BigDecimal.ZERO)
                .fechCobro(null)//LocalDateTime.now()) PORQUE ESTA NOT NULL
                .estado(estado) // por ejemplo: 1 = RESERVA ACTIVA
                .modoPago(modoPago)
             //   .idModoPago(reservaDto.getIdModoPago()) //mapear con entidad despues
                .fechVtoSenia(fechaVtoSenia)
                .build();

        // Guardar
        Reserva reservaGuardada = reservaRepository.save(reserva);

        ReservaDTO dto = reservaMapper.toDto(reservaGuardada);

        logger.info("Creando reserva: {}",reservaGuardada.getEspacio().getNombre() + "en fecha " + reservaGuardada.getFechHoraReserva());
        logger.debug("Detalles: {}",dto);

        return dto;
    }

    public List<ReservaDTO> listarReservas() {
        return reservaRepository.findAll()
                .stream()
                .map(reservaMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<ReservaDTO> listarReservasPorEspacio(Integer idEspacio) {
        return reservaRepository.findByEspacio_IdEspacio(idEspacio)
                .stream()
                .map(reservaMapper::toDto)
                .collect(Collectors.toList());
    }

    public void cancelarReserva(Integer idReserva) {
        Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new EntityNotFoundException("Reserva no encontrada"));

        if (reserva.getEstado().getIdestado() == 2) {
            throw new IllegalStateException("La reserva ya está cancelada.");
        }

        // Validar fecha
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime inicioReserva = reserva.getFechHoraReserva();
        LocalDateTime finReserva = inicioReserva.plusMinutes(reserva.getDuracion());

        if (ahora.isAfter(finReserva)) {
            throw new IllegalStateException("No se puede cancelar una reserva que ya finalizó.");
        }


        Estado estadoCancelada = estadoRepository.findById(2)
                .orElseThrow(() -> new EntityNotFoundException("Estado no encontrado"));

        reserva.setEstado(estadoCancelada);
        reservaRepository.save(reserva);

        ReservaDTO dto = reservaMapper.toDto(reserva);

        //logger.info("Cancelando reserva en: {}",reserva.getEspacio().getNombre() +" con fecha: {} " + reserva.getFechHoraReserva());
        logger.debug("Cancelando reserva: {}", dto);
    }

    public ReservaDTO obtenerReservaPorId(Integer idReserva) {
        Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new EntityNotFoundException("Reserva no encontrada: " + idReserva));
        ReservaDTO dto = reservaMapper.toDto(reserva);
        return dto;
    }

    //reporte agrupado por fechas
public List<ReporteReservaDTO> obtenerReportePorFechas(FiltroReporteReservaDTO filtro) {
    List<Object[]> resultados = reservaRepository.obtenerReporteAgrupadoPorFecha(
            filtro.getFechaDesde(),
            filtro.getFechaHasta(),
            filtro.getIdEspacios()
    );

    String tipo = filtro.getTipoOperacion().toUpperCase();

    List<ReporteReservaDTO> reportes = new ArrayList<>();

    for (Object[] fila : resultados) {
        LocalDate fecha = ((java.sql.Date) fila[0]).toLocalDate();
        Long reservas = ((Number) fila[1]).longValue();
        Long cancelaciones = ((Number) fila[2]).longValue();
        Integer idEspacio = ((Number) fila[3]).intValue();

        // Si el tipo es INSCRIPCION y no hay reservas, ignoramos esta fila
        if (tipo.equals("INSCRIPCION") && reservas == 0) {
            continue;
        }
        // Si el tipo es CANCELACION y no hay cancelaciones, ignoramos esta fila
        if (tipo.equals("CANCELACION") && cancelaciones == 0) {
            continue;
        }

        switch (tipo) {
            case "INSCRIPCION" -> cancelaciones = 0L;
            case "CANCELACION" -> reservas = 0L;
            case "AMBAS" -> {}
            default -> throw new IllegalArgumentException("Tipo de operación inválido: " + tipo);
        }

        reportes.add(new ReporteReservaDTO(fecha, reservas, cancelaciones,idEspacio));
    }

    return reportes;
}

    //reporte agrupado por espacios
    public List<ReporteReservaDTO> obtenerReportePorEspacios(FiltroReporteReservaDTO filtro) {
        List<Object[]> resultados = reservaRepository.obtenerReporteAgrupadoPorEspacio(
                filtro.getFechaDesde(),
                filtro.getFechaHasta(),
                filtro.getIdEspacios()
        );

        String tipo = filtro.getTipoOperacion().toUpperCase();

        List<ReporteReservaDTO> reportes = new ArrayList<>();

        for (Object[] fila : resultados) {
            LocalDate fecha = ((java.sql.Date) fila[0]).toLocalDate();
            Long reservas = ((Number) fila[1]).longValue();
            Long cancelaciones = ((Number) fila[2]).longValue();
            Integer idEspacio = ((Number) fila[3]).intValue();

            // Si el tipo es INSCRIPCION y no hay reservas, ignoramos esta fila
            if (tipo.equals("INSCRIPCION") && reservas == 0) {
                continue;
            }
            // Si el tipo es CANCELACION y no hay cancelaciones, ignoramos esta fila
            if (tipo.equals("CANCELACION") && cancelaciones == 0) {
                continue;
            }

            switch (tipo) {
                case "INSCRIPCION" -> cancelaciones = 0L;
                case "CANCELACION" -> reservas = 0L;
                case "AMBAS" -> {}
                default -> throw new IllegalArgumentException("Tipo de operación inválido: " + tipo);
            }

            reportes.add(new ReporteReservaDTO(fecha, reservas, cancelaciones,idEspacio));
        }

        return reportes;
    }

    //para pagos
    @Transactional
    public ReservaPagoSeniaDTO registrarPagoSenia(ReservaPagoSeniaDTO dto) {
        Reserva reserva = reservaRepository.findById(dto.getIdReserva())
                .orElseThrow(() -> new EntityNotFoundException("No se encontro la reserva con id " + dto.getIdReserva()));

        reserva.setFechPagoSenia(dto.getFechPagoSenia());
        reserva.setImpoSeniPagado(dto.getImpoSeniPagado());
        reserva.setFechCobro(dto.getFechCobro());

        reservaRepository.save(reserva);

        return ReservaPagoSeniaDTO.builder()
                .idReserva(reserva.getIdReserva())
                .fechPagoSenia(reserva.getFechPagoSenia())
                .impoSeniPagado(reserva.getImpoSeniPagado())
                .fechCobro(reserva.getFechCobro())
                .build();
    }


}
