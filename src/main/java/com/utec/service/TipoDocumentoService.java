package com.utec.service;

import com.utec.dto.TipoDocumentoDTO;
import com.utec.mapper.TipoDocumentoMapper;
import com.utec.model.TipoDocumento;
import com.utec.repository.TipoDocumentoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TipoDocumentoService {

    @Autowired
    TipoDocumentoRepository tipoDocumentoRepository;

    @Autowired
    TipoDocumentoMapper tipoDocumentoMapper;

    private static final Logger logger = LoggerFactory.getLogger(TipoDocumentoService.class);

    // Crear un nuevo tipo de documento
    public TipoDocumentoDTO crearTipoDocumento(TipoDocumentoDTO dto) {
        TipoDocumento tipoDocumento = tipoDocumentoMapper.toEntity(dto);
        TipoDocumento guardado = tipoDocumentoRepository.save(tipoDocumento);

        logger.info("Creando nuevo tipo de documento: {}",dto.getNombre());
        logger.debug("Detalles: {}",dto);

        return tipoDocumentoMapper.toDTO(guardado);
    }

    // Obtener todos los tipos de documento
    public List<TipoDocumentoDTO> obtenerTipoDocumentos() {
        List<TipoDocumento> documentos = tipoDocumentoRepository.findAll();
        return documentos.stream()
                .map(tipoDocumentoMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Obtener un tipo de documento por ID
    public TipoDocumentoDTO obtenerPorId(Integer id) {
        TipoDocumento tipoDocumento = tipoDocumentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("TipoDocumento no encontrado con ID: " + id));
        return tipoDocumentoMapper.toDTO(tipoDocumento);
    }

    // Actualizar un tipo de documento existente
    public TipoDocumentoDTO actualizarTipoDocumento(TipoDocumentoDTO dto) {
        TipoDocumento tipoDocumento = tipoDocumentoMapper.toEntity(dto);
        TipoDocumento actualizado = tipoDocumentoRepository.save(tipoDocumento);

        TipoDocumentoDTO dtoActualizado = tipoDocumentoMapper.toDTO(actualizado);

        logger.info("Actualizando tipo de documento: {}",dto.getNombre());
        logger.debug("Detalles: {}",dtoActualizado);

        return dtoActualizado;
    }

    // Eliminar un tipo de documento
    public void eliminarTipoDocumento(Integer id) {
        logger.info("Eliminando tipo de documento: {}",tipoDocumentoRepository.getById(id).getNombre());
        tipoDocumentoRepository.deleteById(id);
    }
}
