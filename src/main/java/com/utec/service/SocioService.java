package com.utec.service;

import com.fabdelgado.ciuy.Validator;
import com.utec.dto.SocioRequestDTO;
import com.utec.mail.MensajesEmail;
import com.utec.dto.SocioDTO;
import com.utec.mapper.SocioMapper;
import com.utec.mapper.TelefonoMapper;
import com.utec.model.*;
import com.utec.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.apache.logging.log4j.util.InternalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SocioService {
    private final SocioRepository socioRepository;
    private final SocioMapper socioMapper;
    private final EstadoRepository estadoRepository;
    private final TipoDocumentoRepository tipoDocumentoRepository;
    private final PerfilRepository perfilRepository;
    private final PasswordEncoder passwordEncoder;
    private final SubcomisionRepository subcomisionRepository;
    private final CategoriaRepository categoriaRepository;
    private final TelefonoMapper telefonoMapper;
    private final UsuarioRepository usuarioRepository;
    private static final Logger logger = LoggerFactory.getLogger(SocioService.class);
    private final MensajesEmail  mensajesEmail;

    @Autowired
    public SocioService(SocioRepository socioRepository,
            SocioMapper socioMapper,
            EstadoRepository estadoRepository,
            TipoDocumentoRepository tipoDocumentoRepository,
            PerfilRepository perfilRepository,
            PasswordEncoder passwordEncoder,
            SubcomisionRepository subcomisionRepository,
            CategoriaRepository categoriaRepository,
                        TelefonoMapper telefonoMapper,
                        UsuarioRepository usuarioRepository,
                        MensajesEmail mensajesEmail) {
        this.socioRepository = socioRepository;
        this.socioMapper = socioMapper;
        this.estadoRepository = estadoRepository;
        this.tipoDocumentoRepository = tipoDocumentoRepository;
        this.perfilRepository = perfilRepository;
        this.passwordEncoder = passwordEncoder;
        this.subcomisionRepository = subcomisionRepository;
        this.categoriaRepository = categoriaRepository;
        this.telefonoMapper = telefonoMapper;
        this.usuarioRepository = usuarioRepository;
        this.mensajesEmail = mensajesEmail;
    }

    public SocioDTO crearSocio(SocioDTO socioDTO) throws NotFoundException, BadRequestException {
        // Validar que el perfil sea de socio
        TipoDocumento tipoDocumento = tipoDocumentoRepository.findById(socioDTO.getIdTipoDocumento())
                .orElseThrow(() -> new NotFoundException());

        Optional<Socio> verificarNumeroDocumento = socioRepository.findByTipoDocumentoAndNumeDocumento(tipoDocumento,
                socioDTO.getNumeDocumento());
        if (verificarNumeroDocumento.isPresent()) {
            throw new BadRequestException();
        }
        Optional<Socio> verificarCorreo = socioRepository.findByCorreo(socioDTO.getCorreo());
        if (verificarCorreo.isPresent()) {
            throw new BadRequestException();
        }
        Socio socio = socioMapper.toEntity(socioDTO);
        socio = this.setDependencies(socioDTO, socio, tipoDocumento);
        socio.setContrasenia(passwordEncoder.encode(socioDTO.getContrasenia()));

        if (socioDTO.getTelefonos() != null && !socioDTO.getTelefonos().isEmpty()) {
            List<Telefono> telefonos = socioDTO.getTelefonos().stream()
                    .map(telefonoMapper::toEntity)
                    .collect(Collectors.toList());

            for (Telefono telefono : telefonos) {
                telefono.setUsuario(socio);
            }
            socio.setTelefonos(telefonos);
        }

        Validator ciValidator = new Validator();

        if(!ciValidator.validateCi(socio.getNumeDocumento())){
            throw new BadRequestException("La cedula no es valida");
        }

        socioRepository.save(socio);
        //envio de mail
        mensajesEmail.mensajeRegistroUsuario(socio.getCorreo());

        logger.info("Creando nuevo socio: {}",socio.getCorreo());
        logger.debug("Detalles: {}",socioDTO);

        mensajesEmail.mensajeRegistroUsuario(socio.getCorreo());

        return socioMapper.toDto(socio);
    }

    // Obtener todos los socios
    public List<SocioDTO> obtenerSocios() {
        List<Socio> socios = socioRepository.findAll();
        return socios.stream()
                .map(socioMapper::toDto)
                .collect(Collectors.toList());
    }

    //Modificar perfil:
    public SocioDTO modificarPerfil(int id){
        Socio socio = socioRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No se encuentra al socio con id "+ id));
        Perfil perfilUsuario = perfilRepository.findById(2).orElseThrow(() -> new EntityNotFoundException("No se encuentra el perfil Usuario"));
        Estado estadoInactivo = estadoRepository.findById(2).orElseThrow(() -> new EntityNotFoundException("No se encuentra el estado inactivo"));

        socio.setPerfil(perfilUsuario);
        socio.setEstadoSocio(estadoInactivo);

        socioRepository.save(socio);

        mensajesEmail.mensajeModificacionAUsuario(socio.getCorreo());
        return socioMapper.toDto(socio);
    }
    //CrearSocioDesdeUsuario:
    public void crearSocioDesdeUsuario(SocioRequestDTO dto, int idUsuario){

        Subcomision subcomision = subcomisionRepository.findById(dto.getIdSubcomision()).orElseThrow(()-> new EntityNotFoundException("No se encuentra la subcomision"));
        Categoria categoria = categoriaRepository.findById(dto.getIdCategoria()).orElseThrow(() -> new EntityNotFoundException("No se encuentra la categoria"));

        Usuario usuario = usuarioRepository.findById(idUsuario).orElseThrow(() -> new EntityNotFoundException("No se encuentra el usuario con id "+idUsuario));

        socioRepository.crearSocioDesdeUsuario(idUsuario,
                categoria.getIdCategoria(),
                dto.getDifAuditiva(),
                dto.getUsoLengSenias(),
                usuario.getEstado().getIdestado(),
                LocalDate.now().getMonthValue(),
                dto.getPagaHasta(),
                subcomision.getIdSubcomision());

        mensajesEmail.mensajeRegistroUsuario(usuario.getCorreo());
    }

    // Actualizar un socio
    public SocioDTO actualizarSocio(SocioDTO socioDTO, int id) throws BadRequestException, NotFoundException {
        try {
        TipoDocumento tipoDocumento = tipoDocumentoRepository.findById(socioDTO.getIdTipoDocumento())
            .orElseThrow(() -> new NotFoundException());
        Socio socio = socioMapper.toEntity(socioDTO);
        socio = this.setDependencies(socioDTO, socio, tipoDocumento);
        socio.setId(id);
            socio = socioRepository.save(socio);

            logger.info("Modificando socio: {}",socio.getCorreo());
            logger.debug("Detalles: {}",socioDTO);

            return socioMapper.toDto(socio);
        } catch (Exception e) {
            System.out.println(e);
            throw new InternalException("Error al actualizar el socio" + e.getMessage());
        }

    }

    // Obtener un socio por su ID compuesto
    public SocioDTO obtenerPorId(SocioId socioId) throws NotFoundException {
        Socio socio = socioRepository.findById(socioId.getIdUsuario())
                .orElseThrow(() -> new NotFoundException());
        return socioMapper.toDto(socio);
    }

    // Eliminar un socio por su ID compuesto
    @Transactional
    public void eliminarSocio(
            //SocioId socioId, boolean activar
    Integer idUsuario, boolean activar) throws NotFoundException {
        Socio socio = socioRepository.findById(idUsuario)  //socioId.getIdUsuario())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Socio no encontrado"));

        Integer idEstado = activar ? 1 : 2;

        logger.info("Cambiando estado del socio con correo: {}", socio.getCorreo());
        System.out.println(idEstado);
        System.out.println(socio.toString());
        if (!activar && socio.getEstadoSocio().getIdestado() == 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El socio ya está inactivo.");
        }

        Estado nuevoEstado = estadoRepository.findById(idEstado)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Estado no encontrado"));

        socio.setEstadoSocio(nuevoEstado);
        socioRepository.save(socio);
        socioRepository.flush();

        if(nuevoEstado.getIdestado()==2) {
            mensajesEmail.mensajeBajaUsuario(socio.getCorreo());
        } else if (nuevoEstado.getIdestado()==1){
            mensajesEmail.mensajeActivacionUsuario(socio.getCorreo());
        }
    }

    private Socio setDependencies(SocioDTO socioDTO, Socio socio, TipoDocumento tipoDocumento) throws BadRequestException, NotFoundException {
        try {
        Perfil perfil = perfilRepository.findById(socioDTO.getIdPerfil())
                .orElseThrow(() -> new NotFoundException());
        if (!perfil.getNombre().equalsIgnoreCase("SOCIO")) {
            throw new BadRequestException();
        }
        Estado estado = estadoRepository.findById(socioDTO.getIdEstado())
                .orElseThrow(() -> new NotFoundException());
        Subcomision subcomision = subcomisionRepository.findById(socioDTO.getIdSubcomision())
                .orElseThrow(() -> new NotFoundException());

        Categoria categoria = categoriaRepository.findById(socioDTO.getIdCategoria())
                .orElseThrow(() -> new NotFoundException());

        socio.setEstado(estado);
        socio.setEstadoSocio(estado);
        socio.setTipoDocumento(tipoDocumento);
        socio.setPerfil(perfil);
        socio.setSubcomision(subcomision);
        socio.setCategoria(categoria);
        return socio;
        } catch (Exception e) {
            System.out.println(e);
            throw new InternalException("Error al eliminar el socio" + e.getMessage());
        }
    }
}