package com.utec.service;

import com.utec.dto.PerfilDTO;
import com.utec.mapper.PerfilMapper;
import com.utec.model.Estado;
import com.utec.model.Perfil;
import com.utec.repository.EstadoRepository;
import com.utec.repository.PerfilRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PerfilService {

    @Autowired
    PerfilRepository perfilRepository;

    @Autowired
    EstadoRepository estadoRepository;

    @Autowired
    PerfilMapper perfilMapper;

    private static final Logger logger = LoggerFactory.getLogger(PerfilService.class);

    //Crear nuevo Perfil
    public PerfilDTO crearPerfil (PerfilDTO dto){
        Perfil perfil = perfilMapper.toEntity(dto); //Convertimos el DTO a entidad para guardar
        perfil.setEstado(estadoRepository.getById(dto.getIdEstado())); //Obtenemos el estado y lo asignamos al eprfil
        perfilRepository.save(perfil); //Guardamos el perfil.

        logger.info("Creando nuevo perfil: {}",dto.getNombre());
        logger.debug("Detalles: {}",dto);

        return perfilMapper.toDto(perfil); //Retornamos el perfil como DTO.
    }

    //Obtener todos los perfiles
    public List<PerfilDTO> obtenerPerfiles (){
    List<Perfil> perfiles = perfilRepository.findAll(); //Obtenemos la lista de perfiles
    return perfiles.stream().map(perfilMapper::toDto) //Convertimos todos a DTO, los metemos en una List y retornamos.
            .collect(Collectors.toList());
    }

    //Obtener perfil por ID
    public PerfilDTO obtenerPorId(int id){
        return perfilMapper.toDto(perfilRepository.getById(id));
    }

    //Actualizar perfil
    public PerfilDTO actualizarPerfil (PerfilDTO dto, int id){
        Perfil perfil = perfilRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Perfil no encontrado con el id "+id));

        Estado estado = estadoRepository.findById(dto.getIdEstado()).orElseThrow(() -> new EntityNotFoundException("No se encuentra el estado con id "+dto.getIdEstado()));
        perfil.setNombre(dto.getNombre());
        perfil.setDescription(dto.getDescripcion());
        perfil.setEstado(estado);
        perfilRepository.save(perfil);

        logger.info("Actualizando perfil: {}",dto.getNombre());
        logger.info("Detalles: {}",dto);

        return perfilMapper.toDto(perfil);
    }

    //Eliminar perfil por ID
    public void eliminarPerfil (int id){
       /* logger.info("Eliminando perfil (NO BAJA LOGICA): {}",perfilRepository.getById(id).getNombre());

        perfilRepository.deleteById(id);*/

            Perfil perfil = perfilRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Perfil no encontrado con ID: " + id));

            int estadoActual = perfil.getEstado().getIdestado();

            if (estadoActual == 1) {
                Estado inactivo = estadoRepository.findById(2)
                        .orElseThrow(() -> new EntityNotFoundException("Estado no encontrado."));
                perfil.setEstado(inactivo);
                logger.info("Inactivando perfil: ", perfil.getNombre());

            } else if (estadoActual == 2) {  // Si ya está inactivo, quizá querés reactivarlo
                Estado activo = estadoRepository.findById(1)
                        .orElseThrow(() -> new EntityNotFoundException("Estado no encontrado."));
                perfil.setEstado(activo);
                logger.info("Reactivando perfil: ", perfil.getNombre());

            } else {
                throw new IllegalStateException("Estado no válido para cambiar: " + estadoActual);
            }

            perfilRepository.save(perfil);
        }


    }

