package com.utec.service;


import com.utec.auditoria.Auditable;
import com.utec.dto.EspacioDTO;
import com.utec.mapper.EspacioMapper;
import com.utec.model.Espacio;
import com.utec.model.Estado;
import com.utec.repository.EspacioRepository;
import com.utec.repository.EstadoRepository;
import com.utec.repository.ReservaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EspacioService {

    @Autowired
    private EspacioRepository espacioRepository;

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private EspacioMapper espacioMapper;

    private static final Logger logger = LoggerFactory.getLogger(EspacioService.class);

    @Auditable(operacion = "Crear espacio")
    public EspacioDTO crearEspacio(EspacioDTO dto) {

        Espacio espacio = espacioMapper.toEntity(dto);

        Estado estado = estadoRepository.findById(2) //por defecto se crea inactivo
                .orElseThrow(() -> new IllegalArgumentException("Estado no encontrado: " + dto.getIdEstado()));
        espacio.setEstado(estado);

        espacioRepository.save(espacio);

        logger.info("Creando espacio: {}",dto.getNombre());
        logger.debug("Detalles: {}",dto);

        return espacioMapper.toDto(espacio);
    }

    public List<EspacioDTO> listarEspacios(String nombre, Integer idEstado) {

            boolean tieneNombre = nombre != null && !nombre.trim().isEmpty();
            boolean tieneEstado = idEstado != null;

            if (tieneNombre) {
                return espacioRepository.findByNombreContainingIgnoreCase(nombre.trim())
                        .stream()
                        .map(espacioMapper::toDto)
                        .collect(Collectors.toList());

            } else if (tieneEstado) {
                return espacioRepository.findByEstado_Idestado(idEstado)
                        .stream()
                        .map(espacioMapper::toDto)
                        .collect(Collectors.toList());

            } else {

                return espacioRepository.findByEstado_Idestado(1)
                        .stream()
                        .map(espacioMapper::toDto)
                        .collect(Collectors.toList());
            }
        }

    @Auditable(operacion = "Actualizar espacio")
    public EspacioDTO actualizarEspacio(Integer id, EspacioDTO dto) {
        Espacio espacio = espacioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Id de espacio no existe: " + id));

        espacio.setNombre(dto.getNombre());
        espacio.setCapacidadMaxima(dto.getCapacidadMaxima());
        espacio.setPrecioReservaSocio(dto.getPrecioReservaSocio());
        espacio.setPrecioReservaNoSocio(dto.getPrecioReservaNoSocio());
        espacio.setFechaVigenciaPrecio(dto.getFechaVigenciaPrecio());
        espacio.setObservaciones(dto.getObservaciones());


        Estado nuevoEstado = estadoRepository.findById(dto.getIdEstado())
                .orElseThrow(() -> new IllegalArgumentException("Estado no encontrado con id: " + dto.getIdEstado()));
        espacio.setEstado(nuevoEstado);

        espacioRepository.save(espacio);

        logger.info("Modificando espacio: {}",dto.getNombre());
        logger.debug("Detalles de modificacion: {}",dto);

        return espacioMapper.toDto(espacio);
    }

    @Auditable(operacion = "Actualizar estado espacio")
    public void cambiarEstadoEspacio(Integer idEspacio, boolean activar) {
        Espacio espacio = espacioRepository.findById(idEspacio)
                .orElseThrow(() -> new EntityNotFoundException("Espacio no encontrado: " + idEspacio));

        Estado estadoActual = espacio.getEstado();

        Estado estadoObjetivo = estadoRepository.findById(activar ? 1 : 2) // 1 = ACTIVO, 2 = INACTIVO
                .orElseThrow(() -> new IllegalArgumentException("Estado no encontrado."));

        // Si ya tiene ese estado doy error
        if (estadoActual.getIdestado().equals(estadoObjetivo.getIdestado())) {
            throw new IllegalStateException("El espacio ya se encuentra " + (activar ? "activo" : "inactivo") + ".");
        }

        if (!activar) {
            // Baja lógica: validaciones
            boolean tieneReservas = reservaRepository.existsByEspacio_IdEspacioAndEstado_Idestado(idEspacio, 1); // 1 = ACTIVO
            if (tieneReservas) {
                throw new IllegalStateException("El espacio tiene reservas activas asociadas.");
            }

        }


        espacio.setEstado(estadoObjetivo);
        espacioRepository.save(espacio);

        logger.info("Cambiando estado de espacio: {}",espacio.getNombre());

        logger.debug("Estado actualizado a: {}",estadoObjetivo.getDescripcion());
    }

    public List<EspacioDTO> obtenerEspaciosDisponibles(LocalDateTime fechaInicioReserva, int duracionHoras, int cantPersonas) {
//solo se reserva por horas
        int duracionMinutos = duracionHoras * 60;
        LocalDateTime fechaFinReserva = fechaInicioReserva.plusMinutes(duracionMinutos);


        List<Espacio> espacios = espacioRepository.buscarEspaciosDisponibles(
                fechaInicioReserva,fechaFinReserva, cantPersonas);

        if (espacios.isEmpty()) {
            throw new IllegalStateException("No hay espacios disponibles para los criterios solicitados.");
        }

        return espacios.stream()
                .map(espacioMapper::toDto)
                .collect(Collectors.toList());
    }

}

