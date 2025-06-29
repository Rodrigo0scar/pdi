package com.utec.service;


import com.utec.dto.FuncionalidadDTO;
import com.utec.mapper.FuncionalidadMapper;
import com.utec.model.Estado;
import com.utec.model.Funcionalidad;
import com.utec.model.Reserva;
import com.utec.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class FuncionalidadService {
    @Autowired
    private FuncionalidadRepository funcionalidadRepository;
@Autowired
private PerfilAccedeFuncionalidadRepository perfilAccedeFuncionalidadRepository;
    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private FuncionalidadMapper funcionalidadMapper;

    private static final Logger logger = LoggerFactory.getLogger(FuncionalidadService.class);


    public FuncionalidadDTO crearFuncionalidad(FuncionalidadDTO dto) {
        if (funcionalidadRepository.existsByNombreIgnoreCase(dto.getNombre())) {
            throw new IllegalArgumentException("Ya existe una funcionalidad con ese nombre.");
        }

        Estado estado = estadoRepository.findById(1)
                .orElseThrow(() -> new IllegalStateException("Estado no encontrado"));

        Funcionalidad funcionalidad = new Funcionalidad();
        funcionalidad.setNombre(dto.getNombre().trim());
        funcionalidad.setDescripcion(dto.getDescripcion().trim());
        funcionalidad.setEstado(estado);

        Funcionalidad funcionalidadGuardada = funcionalidadRepository.save(funcionalidad);

        FuncionalidadDTO resultado = new FuncionalidadDTO();
        resultado.setIdFuncionalidad(funcionalidadGuardada.getId());
        resultado.setNombre(funcionalidadGuardada.getNombre());
        resultado.setDescripcion(funcionalidadGuardada.getDescripcion());
        resultado.setEstado(funcionalidadGuardada.getEstado());

        logger.info("Creando funcionalidad: {}",resultado.getNombre());
        logger.info("Detalles: {}",resultado);

        return resultado;

    }

    public List<FuncionalidadDTO> listarFuncionalidades(String nombre, Integer idEstado) {

        boolean tieneNombre = nombre != null && !nombre.trim().isEmpty();
        boolean tieneEstado = idEstado != null;

        if (tieneNombre) {
            return funcionalidadRepository.findByNombreContainingIgnoreCase(nombre.trim())
                    .stream()
                    .map(funcionalidadMapper::toDto)
                    .collect(Collectors.toList());

        } else if (tieneEstado) {
            return funcionalidadRepository.findByEstado_Idestado(idEstado)
                    .stream()
                    .map(funcionalidadMapper::toDto)
                    .collect(Collectors.toList());

        } else {
            // se devuelve lista vacía
            return Collections.emptyList();
        }
    }

    public List<FuncionalidadDTO> listarTodasFuncionalidades() {
        List<Funcionalidad> funcionalidades = funcionalidadRepository.findAllActivas();

        return funcionalidades.stream().map(func -> {
            FuncionalidadDTO dto = new FuncionalidadDTO();
            dto.setIdFuncionalidad(func.getId());
            dto.setNombre(func.getNombre());
            dto.setDescripcion(func.getDescripcion());
            dto.setEstado(func.getEstado());
            return dto;
        }).collect(Collectors.toList());
    }


    public FuncionalidadDTO modificarFuncionalidad(Integer id, FuncionalidadDTO dto) {

        Funcionalidad funcionalidad = funcionalidadRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Funcionalidad no encontrada: " + id));

        if (dto.getNombre() != null && !funcionalidad.getNombre().equalsIgnoreCase(dto.getNombre().trim())) {
            throw new IllegalArgumentException("No se puede modificar el nombre de la funcionalidad.");
        }

        if (dto.getDescripcion() == null || dto.getDescripcion().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción no puede estar vacía.");
        }

        funcionalidad.setDescripcion(dto.getDescripcion().trim());

        Funcionalidad funcionalidadActualizada = funcionalidadRepository.save(funcionalidad);

        logger.info("Modificando funcionalidad: {}",dto.getNombre());
        logger.debug("Detalles: {}",dto);

        return funcionalidadMapper.toDto(funcionalidadActualizada);
    }


    public void cambiarEstadoFuncionalidad(Integer id) {
        Funcionalidad funcionalidad = funcionalidadRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Funcionalidad no encontrada con ID: " + id));

        int estadoActual = funcionalidad.getEstado().getIdestado();
        int nuevoIdEstado;

        if (estadoActual == 1) {
        boolean estaAsignadaARol = perfilAccedeFuncionalidadRepository.existsByFuncionalidad_IdAndEstado_Idestado(id,1);

        if (estaAsignadaARol) {
                throw new IllegalStateException("No se puede dar de baja la funcionalidad porque está asignada a un rol.");
            }
        Estado inactivo = estadoRepository.findById(2)
                .orElseThrow(() -> new EntityNotFoundException("Estado no encontrado."));
        funcionalidad.setEstado(inactivo);
        } else if (estadoActual == 2) {
        Estado activo = estadoRepository.findById(1)
                .orElseThrow(() -> new EntityNotFoundException("Estado no encontrado."));
        funcionalidad.setEstado(activo);
        } else {
            throw new IllegalStateException("No se puede cambiar. Estado: " + estadoActual);
        }

        funcionalidadRepository.save(funcionalidad);

        logger.info("Cambiando estado a funcionalidad: {}",funcionalidad.getNombre());
        logger.debug("Se cambia el estado a: {}",funcionalidad.getEstado().getDescripcion());
    }
}
