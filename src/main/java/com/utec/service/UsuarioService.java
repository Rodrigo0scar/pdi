package com.utec.service;

import com.fabdelgado.ciuy.Validator;
import com.utec.dto.*;
import com.utec.mail.EmailFacade;
import com.utec.mail.MensajesEmail;
import com.utec.mapper.TelefonoMapper;
import com.utec.mapper.UsuarioMapper;
import com.utec.mapper.UsuarioResponseMapper;
import com.utec.model.*;
import com.utec.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final EstadoRepository estadoRepository;
    private final TipoDocumentoRepository tipoDocumentoRepository;
    private final PerfilRepository perfilRepository;
    private final UsuarioResponseMapper usuarioResponseMapper;
    private final TelefonoMapper telefonoMapper;
    private final PasswordEncoder passwordEncoder;
    private final SocioService socioService;
    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    @Autowired
    private EmailFacade emailFacade;


    @Autowired
    private MensajesEmail mensajesEmail;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository,
                          UsuarioMapper usuarioMapper,
                          EstadoRepository estadoRepository,
                          TipoDocumentoRepository tipoDocumentoRepository,
                          PerfilRepository perfilRepository,
                          TelefonoRepository telefonoRepository,
                          TelefonoMapper telefonoMapper,
                          UsuarioResponseMapper usuarioResponseMapper,
                          PasswordEncoder passwordEncoder,
                          SocioService socioService) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        this.estadoRepository = estadoRepository;
        this.tipoDocumentoRepository = tipoDocumentoRepository;
        this.perfilRepository = perfilRepository;
        this.telefonoMapper = telefonoMapper;
        this.usuarioResponseMapper = usuarioResponseMapper;
        this.passwordEncoder = passwordEncoder;
        this.socioService = socioService;
    }

    //Crear usuario
    public UsuarioDTO crearUsuario (UsuarioDTO dto) throws BadRequestException {

        //Validamos que el perfil sea de usuario
        Perfil perfil = perfilRepository.findById(dto.getIdPerfil())
                .orElseThrow(() -> new EntityNotFoundException("No se encuentra el perfil"));
        if(!perfil.getNombre().equalsIgnoreCase("Usuario")){
            throw new BadRequestException("El perfil no es correcto.");
        }

        //Validamos que el estado sea pendiente (3)
        Estado estado = estadoRepository.findById(dto.getIdEstado())
                .orElseThrow(() -> new EntityNotFoundException("No se encuentra el estado"));
        if(!estado.getDescripcion().equalsIgnoreCase("Pendiente")){
            throw new BadRequestException("El estado no es correcto.");
        }

        //Validamos que exista el tipo documento
        TipoDocumento tipoDocumento = tipoDocumentoRepository.findById(dto.getIdTipoDocumento())
                .orElseThrow(() -> new EntityNotFoundException("No se encuentra el tipo de documento"));

        //Verificar duplicados:

        Optional<Usuario> verificarCI = usuarioRepository.findByTipoDocumentoAndNumeDocumento(tipoDocumento,dto.getNumeDocumento());
        if(verificarCI.isPresent()){
            throw new BadRequestException("Ya existe un usuario con ese documento.");
        }
        Optional<Usuario> verificarCorreo = usuarioRepository.findByCorreo(dto.getCorreo());
        if(verificarCorreo.isPresent()){
            throw new BadRequestException("Ya existe un usuario con ese  correo");
        }

        //Creamos el usuario:
        Usuario usuario = usuarioMapper.toEntity(dto);
        usuario.setContrasenia(passwordEncoder.encode(dto.getContrasenia()));
        usuario.setEstado(estado);
        usuario.setPerfil(perfil);
        usuario.setTipoDocumento(tipoDocumento);

        if (dto.getTelefonos() != null && !dto.getTelefonos().isEmpty()) {
            List<Telefono> telefonos = dto.getTelefonos().stream()
                    .map(telefonoMapper::toEntity)
                    .collect(Collectors.toList());

            for (Telefono telefono : telefonos) {
                telefono.setUsuario(usuario);
            }

            usuario.setTelefonos(telefonos);
        }

        Validator ciValidator = new Validator();

        if(!ciValidator.validateCi(usuario.getNumeDocumento())){
            throw new BadRequestException("La cedula no es valida");
        }

        usuarioRepository.save(usuario);

        logger.info("Creando usuario: {}",dto.getCorreo());
        logger.debug("Detalles: {}",dto);

        //envio correo
        mensajesEmail.mensajeRegistroUsuario(usuario.getCorreo());
        return dto;
    }

    //Modificar perfil de usuario
    public void modificarPerfil(int id,SocioRequestDTO socioDto) throws BadRequestException, NotFoundException {

        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No se encuentra el usuario con id "+ id));

        Perfil perfilSocio = perfilRepository.findById(3).orElseThrow(() -> new EntityNotFoundException("Perfil Socio no encontrado"));

        usuario.setPerfil(perfilSocio);

        usuarioRepository.save(usuario); //Modificamos el registro del usuario con perfil: socio (3)

        socioService.crearSocioDesdeUsuario(socioDto,id);

        mensajesEmail.mensajeModificacionASocio(usuario.getCorreo());
    }
    // Obtener todos los usuarios
    public List<UsuarioResponseDTO> obtenerUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarios.stream()
                .map(usuarioResponseMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Obtener un usuario por ID
    public UsuarioResponseDTO obtenerPorId(Integer id) throws NotFoundException {
        Usuario user = usuarioRepository.findById(id)
        .orElseThrow(() -> new NotFoundException());
        return this.usuarioResponseMapper.toDTO(user);
    }

    // Actualizar un usuario
    public UsuarioDTO actualizarUsuario(UsuarioDTO usuarioDTO) throws NotFoundException {
        Usuario usuarioExistente = usuarioRepository.findById(usuarioDTO.getIdEstado())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        Usuario usuarioActualizado = usuarioMapper.toEntity(usuarioDTO);
        usuarioActualizado.setId(usuarioExistente.getId()); // aseguramos la ID

        Usuario guardado = usuarioRepository.save(usuarioActualizado);

        UsuarioDTO dtoActualizado = usuarioMapper.toDTO(guardado);

        logger.info("Actualizando informacion del usuario: {}",usuarioDTO.getCorreo());
        logger.debug("Detalles: {}",dtoActualizado);

        return dtoActualizado;
    }

    // Eliminar un usuario
    public void eliminarUsuario(Integer id, boolean activar) throws NotFoundException {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        Integer idEstado = activar ? 1 : 2;
        logger.debug("Cambiando estado del usuario con correo: {}",usuario.getCorreo());

        if (!activar && usuario.getEstado().getIdestado() == 2){
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario ya está inactivo.");
        }

        Estado estado = estadoRepository.findById(idEstado)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Estado no encontrado"));

        usuario.setEstado(estado);
        usuarioRepository.save(usuario);

        if(estado.getIdestado()==2) {
            mensajesEmail.mensajeBajaUsuario(usuario.getCorreo());
        } else if (estado.getIdestado()==1){
            mensajesEmail.mensajeActivacionUsuario(usuario.getCorreo());
        }
    }

    //LISTADOS

    //nombre
    public List<UsuarioDTO> obtenerUsuNombre(String primNombre) {
        List<Usuario> usuarios = usuarioRepository.findByPrimNombre(primNombre);
        List<UsuarioDTO> usuarioDTOs = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            UsuarioDTO dto = usuarioMapper.toDTO(usuario);
            usuarioDTOs.add(dto);
        }
        return usuarioDTOs;
    }

    //por apellido 
    public List<UsuarioDTO> obtenerUsuApellido(String primApellido) {
        List<Usuario> usuarios = usuarioRepository.findByPrimApellido(primApellido);
        List<UsuarioDTO> usuarioDTOs = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            UsuarioDTO dto = usuarioMapper.toDTO(usuario);
            usuarioDTOs.add(dto);
        }
        return usuarioDTOs;
    }

    //por estado
    public List<UsuarioDTO> obtenerUsuEstado(EstadoDTO estadoDto) {
        Optional<Estado> estado= estadoRepository.findById(estadoDto.getIdestado());
        List<Usuario> usuarios = usuarioRepository.findByEstado(estado.get());
        List<UsuarioDTO> usuarioDTOs = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            UsuarioDTO dto = usuarioMapper.toDTO(usuario);
            usuarioDTOs.add(dto);
        }
        return usuarioDTOs;
    }

    //documento
    public UsuarioDTO obtenerUsuDocumento(String numeDocumento) {
        Usuario usuario = usuarioRepository.findByNumeDocumento(numeDocumento);
        UsuarioDTO usuarioDTO = usuarioMapper.toDTO(usuario);
       return usuarioDTO;
    }

    //por perfil
    public List<UsuarioDTO> obtenerUsuPorPerfil(Integer idPerfil) throws NotFoundException {
        Perfil perfil = perfilRepository.findById(idPerfil)
                .orElseThrow(() -> new NotFoundException());
        List<Usuario> usuarios = usuarioRepository.findByPerfil(perfil);

        return usuarios.stream()
                .map(usuarioMapper::toDTO)
                .collect(Collectors.toList());
    }

    public UsuarioDTO actualizarDatosPropios(String email, UsuarioDatosPropiosDTO dto) {
        Usuario usuario = usuarioRepository.findByCorreo(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        if (dto.getPrimNombre() != null) usuario.setPrimNombre(dto.getPrimNombre());
        if (dto.getSeguNombre() != null) usuario.setSeguNombre(dto.getSeguNombre());
        if (dto.getPrimApellido() != null) usuario.setPrimApellido(dto.getPrimApellido());
        if (dto.getSeguApellido() != null) usuario.setSeguApellido(dto.getSeguApellido());
        if (dto.getFechNacimiento() != null) usuario.setFechNacimiento(dto.getFechNacimiento());
        if (dto.getCalle() != null) usuario.setCalle(dto.getCalle());
        if (dto.getApartamento() != null) usuario.setApartamento(dto.getApartamento());
        if (dto.getNumePuerta() != null) usuario.setNumePuerta(dto.getNumePuerta());

        if (dto.getNuevaContrasenia() != null && !dto.getNuevaContrasenia().isBlank()) {
            usuario.setContrasenia(passwordEncoder.encode(dto.getNuevaContrasenia()));
        }

        if (dto.getTelefonos() != null) {
            List<Telefono> telefonos = dto.getTelefonos().stream()
                    .map(telefonoMapper::toEntity)
                    .peek(t -> t.setUsuario(usuario))
                    .collect(Collectors.toList());
            usuario.setTelefonos(telefonos);
        }

        Usuario guardado = usuarioRepository.save(usuario);
        return usuarioMapper.toDTO(guardado);
    }

}

