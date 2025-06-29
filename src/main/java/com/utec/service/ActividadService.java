package com.utec.service;

import com.utec.dto.ActividadDTO;
import com.utec.dto.GetActividadDTO;
import com.utec.mapper.ActividadMapper;
import com.utec.model.Actividad;
import com.utec.model.Espacio;
import com.utec.model.Estado;
import com.utec.model.TipoActividad;
import com.utec.model.Usuario;
import com.utec.model.ModoPago;
import com.utec.repository.ActividadRepository;
import com.utec.repository.EspacioRepository;
import com.utec.repository.EstadoRepository;
import com.utec.repository.ModoPagoRepository;
import com.utec.repository.TipoActividadRepository;
import com.utec.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActividadService {

    @Autowired
    private ActividadMapper actividadMapper;
    @Autowired
    private ActividadRepository actividadRepository;
    @Autowired
    private TipoActividadRepository tipoActividadRepository;
    @Autowired
    private EspacioRepository espacioRepository;
    @Autowired
    private ModoPagoRepository modoPagoRepository;
    @Autowired
    private EstadoRepository estadoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    private static final Logger logger = LoggerFactory.getLogger(ActividadService.class);

    public ActividadService(ActividadRepository actividadRepository, ActividadMapper actividadMapper, TipoActividadRepository tipoActividadRepository, EspacioRepository espacioRepository, ModoPagoRepository modoPagoRepository, EstadoRepository estadoRepository, UsuarioRepository usuarioRepository) {
        this.actividadRepository = actividadRepository;
        this.actividadMapper = actividadMapper;
        this.tipoActividadRepository = tipoActividadRepository;
        this.espacioRepository = espacioRepository;
        this.modoPagoRepository = modoPagoRepository;
        this.estadoRepository = estadoRepository;
    }

    public ActividadDTO crearActividad(ActividadDTO dto) throws NotFoundException {
        try {
        TipoActividad tipoActividad = this.tipoActividadRepository.findById(dto.getIdTipoActividad())
        .orElseThrow(() -> new NotFoundException());
        Espacio espacio = this.espacioRepository.findById(dto.getIdEspacio())
        .orElseThrow(() -> new NotFoundException());
        ModoPago modoPago = this.modoPagoRepository.findById(dto.getIdModoPago())
        .orElseThrow(() -> new NotFoundException());
        Estado estado = this.estadoRepository.findById(dto.getIdEstado())
        .orElseThrow(() -> new NotFoundException());


        Actividad actividad = actividadMapper.toEntity(dto);
        actividad.setTipoActividad(tipoActividad);
        actividad.setEspacio(espacio);
        actividad.setModoPago(modoPago);
        actividad.setEstado(estado);

        
        Actividad guardar = actividadRepository.save(actividad);
        logger.info("Creando actividad: {}",dto.getNombre());
        logger.debug("Detalles: {}",dto);

        return actividadMapper.toDto(guardar);
    } catch (Exception e) {
        System.out.println(e);
        throw e;
    }
    }

    public List<ActividadDTO> ObtenerActividades(){
        List<Actividad> actividades = actividadRepository.findAll();
        return actividades.stream()
                .map(actividadMapper::toDto)
                .collect(Collectors.toList());
    }

    public ActividadDTO obtenerPorId(Integer id)  {
        Actividad actividad = actividadRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Actividad no encontrado con ID: " + id));
        return actividadMapper.toDto(actividad);
    }

    public List<GetActividadDTO> listarActividadesParaInscribirse() {
        List<Actividad> actividades = actividadRepository.findActividadesActivasParaInscripcionWithRelations();
        System.out.println(actividades);
        return actividades.stream()
                .map(actividadMapper::toGetActividadDTO)
                .collect(Collectors.toList());
    }

    public ActividadDTO actualizarActividad(ActividadDTO dto) {
        logger.debug("ID recibido en DTO: {}", dto.getId());
        Actividad actividad = actividadRepository.findById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Actividad no encontrada"));

        actividad.setNombre(dto.getNombre());
        actividad.setDescripcion(dto.getDescripcion());
        actividad.setObjetivo(dto.getObjetivo());
        actividad.setFech_hora_actividad(dto.getFech_hora_actividad());
        actividad.setDuracion(dto.getDuracion());
        actividad.setRequ_inscripcion(dto.isRequ_inscripcion());
        actividad.setFech_apertura_inscripcion(dto.getFech_apertura_inscripcion());
        actividad.setCosto(dto.getCosto());
        actividad.setObservaciones(dto.getObservaciones());

        TipoActividad tipoActividad = tipoActividadRepository.findById(dto.getIdTipoActividad())
                .orElseThrow(() -> new EntityNotFoundException("TipoActividad no encontrado"));
        actividad.setTipoActividad(tipoActividad);
        Estado estado = estadoRepository.findById(dto.getIdEstado())
                .orElseThrow(() -> new EntityNotFoundException("Estado no encontrado"));
        actividad.setEstado(estado);
        Espacio espacio = espacioRepository.findById(dto.getIdEspacio())
                .orElseThrow(() -> new EntityNotFoundException("Espacio no encontrado"));
        actividad.setEspacio(espacio);
        ModoPago modoPago = modoPagoRepository.findById(dto.getIdModoPago())
                .orElseThrow(() -> new EntityNotFoundException("ModoPago no encontrado"));
        actividad.setModoPago(modoPago);

        Actividad actualizado = actividadRepository.save(actividad);
        ActividadDTO dtoActualizado = actividadMapper.toDto(actualizado);

        logger.info("Actualizando actividad:{}",dto.getNombre());
        logger.debug("Detalles>{}",dtoActualizado);

        return dtoActualizado;
    }

    public void eliminarActividad(Integer id, boolean activar) {
        Actividad actividad = actividadRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Actividad no encontrada"));

        Integer idEstadoObjetivo  = activar ? 1 : 2;
        logger.info("Dando baja lógica a la actividad: {}",actividad.getNombre());

        if (!activar && actividad.getEstado().getIdestado() == 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,  "La actividad ya está " + (activar ? "activa" : "inactiva") + ".");
        }
        Estado estadoInactivo = estadoRepository.findById(idEstadoObjetivo)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Estado " + (activar ? "activo" : "inactivo") + " no encontrado"));
        actividad.setEstado(estadoInactivo);
        actividadRepository.save(actividad);

    }
}