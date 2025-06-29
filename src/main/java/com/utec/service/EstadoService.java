package com.utec.service;

import com.utec.dto.EstadoDTO;
import com.utec.mapper.EstadoMapper;
import com.utec.model.Estado;
import com.utec.repository.EstadoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EstadoService {

    @Autowired
    EstadoRepository estadoRepository;

    @Autowired
    EstadoMapper estadoMapper;

    private static final Logger logger = LoggerFactory.getLogger(EstadoService.class);

    // Crear un nuevo estado
    public EstadoDTO crearEstado(EstadoDTO dto) {
        if (dto == null || dto.getDescripcion() == null || dto.getDescripcion().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La descripción no puede estar vacía");
        }

        Estado nuevoEstado = estadoMapper.toEntity(dto);
        Estado guardado = estadoRepository.save(nuevoEstado);

        logger.info("Creando estado: {}",dto.getDescripcion());
        logger.debug("Detalles: {}",dto);

        return estadoMapper.toDto(guardado);
    }

    // Obtener todos los estados
    public List<EstadoDTO> obtenerEstados() {
        List<Estado> estados = estadoRepository.findAll();
        return estados.stream()
                .map(estadoMapper::toDto)
                .collect(Collectors.toList());
    }

    // Obtener estado por ID
    public EstadoDTO obtenerPorId(Integer id) {
        Estado estado = estadoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Estado no encontrado con ID: " + id));
        return estadoMapper.toDto(estado);
    }

    // Actualizar estado existente
    public EstadoDTO actualizarEstado(EstadoDTO dto) {
        Estado existente = estadoRepository.findById(dto.getIdestado())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Estado no encontrado con ID: " + dto.getIdestado()));

        existente.setDescripcion(dto.getDescripcion());
        Estado actualizado = estadoRepository.save(existente);

        EstadoDTO dtoActualizado = estadoMapper.toDto(actualizado);

        logger.info("Actualizando estado: {}",dto.getDescripcion());
        logger.debug("Detalles: {}",dtoActualizado);

        return dtoActualizado;
    }

    // Eliminar estado
    public void eliminarEstado(Integer id) {
        Estado estado = estadoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Estado no encontrado con ID: " + id));

        logger.info("Eliminando estado (NO BAJA LOGICA): {}",estado.getDescripcion());

        estadoRepository.delete(estado);
    }
}
