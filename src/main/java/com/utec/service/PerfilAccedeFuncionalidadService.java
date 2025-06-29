package com.utec.service;

import com.utec.dto.PerfilAccedeFuncionalidadDTO;
import com.utec.model.*;
import com.utec.repository.EstadoRepository;
import com.utec.repository.FuncionalidadRepository;
import com.utec.repository.PerfilAccedeFuncionalidadRepository;
import com.utec.repository.PerfilRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PerfilAccedeFuncionalidadService {

@Autowired
private PerfilAccedeFuncionalidadRepository perfilAccedeFuncionalidadRepository;

@Autowired
private PerfilRepository perfilRepository;

@Autowired
private FuncionalidadRepository funcionalidadRepository;

@Autowired
private EstadoRepository estadoRepository;

private static final Logger logger = LoggerFactory.getLogger(PerfilAccedeFuncionalidadService.class);


    public void asignarFuncionalidadesAPerfil(PerfilAccedeFuncionalidadDTO dto) {
        if (dto.getIdPerfil() == null || dto.getFuncionalidades() == null || dto.getFuncionalidades().isEmpty()) {
            throw new IllegalArgumentException("Perfil y funcionalidades son obligatorios.");
        }

        Perfil perfil = perfilRepository.findById(dto.getIdPerfil())
                .orElseThrow(() -> new EntityNotFoundException("Perfil no encontrado."));

        Estado estadoActivo = estadoRepository.findById(1)
                .orElseThrow(() -> new EntityNotFoundException("Estado activo no encontrado."));

        for (Integer idFuncionalidad : dto.getFuncionalidades()) {
            Funcionalidad funcionalidad = funcionalidadRepository.findById(idFuncionalidad)
                    .orElseThrow(() -> new EntityNotFoundException("Funcionalidad no encontrada: " + idFuncionalidad));

            PerfilAccedeFuncionalidadId id = new PerfilAccedeFuncionalidadId(perfil.getIdPerfil(), funcionalidad.getId());

            Optional<PerfilAccedeFuncionalidad> existente = perfilAccedeFuncionalidadRepository.findById(id);

            if (existente.isPresent()) {
                PerfilAccedeFuncionalidad relacion = existente.get();
                if (relacion.getEstado().getIdestado() == 1) {
                    throw new IllegalStateException("La funcionalidad con ID " + idFuncionalidad + " ya está asignada al perfil.");
                }

                relacion.setEstado(estadoActivo);
                perfilAccedeFuncionalidadRepository.save(relacion);

                logger.info("Activando acceso a funcionalidades al perfil: {}",perfil.getNombre());
                logger.debug("Detalle de funcionalidades: {}",funcionalidad.getNombre());

            } else {
                PerfilAccedeFuncionalidad nueva = new PerfilAccedeFuncionalidad(id);
                nueva.setEstado(estadoActivo);
                perfilAccedeFuncionalidadRepository.save(nueva);

                logger.info("Asignando nuevas funcionalidades al perfil> {}",perfil.getNombre());
                logger.debug("Detalle de nuevas funcionalidades: {}",funcionalidad.getNombre());
            }
        }
    }


    public void desvincularFuncionalidadesDePerfil(PerfilAccedeFuncionalidadDTO dto) {
        if (dto.getIdPerfil() == null || dto.getFuncionalidades() == null || dto.getFuncionalidades().isEmpty()) {
            throw new IllegalArgumentException("Perfil y funcionalidades son obligatorios.");
        }

        Perfil perfil = perfilRepository.findById(dto.getIdPerfil())
                .orElseThrow(() -> new EntityNotFoundException("Perfil no encontrado."));

        Estado estadoInactivo = estadoRepository.findById(2)
                .orElseThrow(() -> new EntityNotFoundException("Estado inactivo no encontrado."));

        for (Integer idFuncionalidad : dto.getFuncionalidades()) {
            Funcionalidad funcionalidad = funcionalidadRepository.findById(idFuncionalidad)
                    .orElseThrow(() -> new EntityNotFoundException("Funcionalidad no encontrada: " + idFuncionalidad));

            PerfilAccedeFuncionalidadId id = new PerfilAccedeFuncionalidadId(perfil.getIdPerfil(), funcionalidad.getId());
            PerfilAccedeFuncionalidad relacion = perfilAccedeFuncionalidadRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("No existe relación entre perfil y funcionalidad."));

            if (relacion.getEstado().getIdestado() == 2) {
                throw new IllegalStateException("La funcionalidad con ID " + idFuncionalidad + " ya está desvinculada del perfil.");
            }

            relacion.setEstado(estadoInactivo);
            perfilAccedeFuncionalidadRepository.save(relacion);

            logger.info("Desvinculando funcionalidades al perfil: {}",perfil.getNombre());
            logger.debug("Detalle de funcionalidades desvinculadas: {}",funcionalidad.getNombre());
        }
    }

    public PerfilAccedeFuncionalidadDTO obtenerIdFuncionalidadesPorPerfil(Integer idPerfil) {
        // Traer solo id de funcionalidades relacionadas
        List<Integer> idFuncionalidades = perfilAccedeFuncionalidadRepository.findIdsFuncionalidadesByPerfil(idPerfil);

        PerfilAccedeFuncionalidadDTO dto = new PerfilAccedeFuncionalidadDTO();
        dto.setIdPerfil(idPerfil);
        dto.setFuncionalidades(idFuncionalidades);

        return dto;
    }


}
