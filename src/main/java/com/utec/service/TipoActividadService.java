package com.utec.service;

import com.utec.dto.TipoActividadDTO;
import com.utec.mapper.TipoActividadMapper;
import com.utec.model.Estado;
import com.utec.model.TipoActividad;
import com.utec.model.Usuario;
import com.utec.repository.EstadoRepository;
import com.utec.repository.TipoActividadRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TipoActividadService {

    private final TipoActividadMapper tipoActividadMapper;
    private final TipoActividadRepository tipoActividadRepository;
    private final EstadoRepository estadoRepository;

    private static final Logger logger = LoggerFactory.getLogger(TipoActividadService.class);

    @Autowired
    public TipoActividadService(TipoActividadMapper tipoActividadMapper, TipoActividadRepository tipoActividadRepository, EstadoRepository estadoRepository){
        this.tipoActividadMapper = tipoActividadMapper;
        this.tipoActividadRepository = tipoActividadRepository;
        this.estadoRepository = estadoRepository;
    }

    //Alta
    public TipoActividadDTO agregarTipoActividad (TipoActividadDTO dto ){
        dto.setIdEstado(2);
        TipoActividad nuevoTipoActividad = tipoActividadMapper.toEntity(dto);
        nuevoTipoActividad.setEstado(estadoRepository.findById(2).orElseThrow(() -> new EntityNotFoundException("No se encontro el Estado correspondiente")));
        tipoActividadRepository.save(nuevoTipoActividad);

        logger.info("Creando nuevo tipo de actividad: {}",dto.getTipo());
        logger.debug("Detalles: {}",dto);

        return dto;
    }
    //Activar
    public TipoActividadDTO activarTipoActividad(int id){
        TipoActividad activar = tipoActividadRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No se encuentra el tipo de actividad con id "+id));
        Estado activo = estadoRepository.findById(1).orElseThrow(() -> new EntityNotFoundException("No se encontro el Estado correspondiente"));

        activar.setEstado(activo);

        System.out.println(activar);
        tipoActividadRepository.save(activar);

        TipoActividadDTO dto = tipoActividadMapper.toDTO(activar);

        logger.info("Activando tipo de actividad>: {}",dto.getTipo());
        logger.debug("Detalles: {}",dto);

        return dto;
    }
    //Listar
    public List<TipoActividadDTO> obtenerTodos(){
        return tipoActividadRepository.findAll().stream().map(tipoActividadMapper::toDTO).collect(Collectors.toList());
    }

    //Obtener por Id
    public TipoActividadDTO obtenerPorId(int id){
        TipoActividad obtenida = tipoActividadRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No se puede encontrar el tipo de actividad con id "+id));
        return tipoActividadMapper.toDTO(obtenida);
    }
    //Modificar
    public TipoActividadDTO modificarTipoActividad (TipoActividadDTO dto, int id){
        TipoActividad tipoActividadModificado = tipoActividadRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encuentra el tipo actividad con el id "+id));

        tipoActividadModificado.setTipo(dto.getTipo());
        tipoActividadModificado.setDescripcion(dto.getDescripcion());

        tipoActividadRepository.save(tipoActividadModificado);

        logger.info("Modificando tipo de actividad: {}",dto.getTipo());
        logger.info("Detalles: {}",dto);

        return dto;
    }

    //Baja Logica
    public void eliminarTipoActividad(TipoActividadDTO dto,int id){
        TipoActividad tipoActividadBaja = tipoActividadRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No se encuentra el tipo de actividad con id "+id));
        tipoActividadBaja.setEstado(estadoRepository.findById(3).orElseThrow(() -> new EntityNotFoundException("No se puede encontrar el estado correspondiente")));

        tipoActividadBaja.setComentariosBaja(dto.getComentariosBaja());

        tipoActividadRepository.save(tipoActividadBaja);

        logger.info("Dando de baja tipo de actividad: {}",dto.getTipo());
    }


}
