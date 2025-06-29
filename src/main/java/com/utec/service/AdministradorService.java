package com.utec.service;

import com.fabdelgado.ciuy.Validator;
import com.utec.mail.MensajesEmail;
import com.utec.dto.AdministradorDTO;
import com.utec.mapper.AdministradorMapper;
import com.utec.mapper.TelefonoMapper;
import com.utec.model.*;
import com.utec.repository.AdministradorRepository;
import com.utec.repository.EstadoRepository;
import com.utec.repository.PerfilRepository;
import com.utec.repository.TipoDocumentoRepository;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdministradorService {
    private final AdministradorRepository administradorRepository;
    private final AdministradorMapper administradorMapper;
    private final EstadoRepository estadoRepository;
    private final TipoDocumentoRepository tipoDocumentoRepository;
    private final PerfilRepository perfilRepository;
    private final PasswordEncoder passwordEncoder;
    private final TelefonoMapper telefonoMapper;

    @Autowired
    private MensajesEmail mensajesEmail;

    private static final Logger logger = LoggerFactory.getLogger(AdministradorService.class);

    @Autowired
    public AdministradorService(AdministradorRepository administradorRepository,
            AdministradorMapper administradorMapper,
            EstadoRepository estadoRepository,
            TipoDocumentoRepository tipoDocumentoRepository,
            PerfilRepository perfilRepository,
            PasswordEncoder passwordEncoder,
                                TelefonoMapper telefonoMapper) {

        this.administradorRepository = administradorRepository;
        this.administradorMapper = administradorMapper;
        this.estadoRepository = estadoRepository;
        this.tipoDocumentoRepository = tipoDocumentoRepository;
        this.perfilRepository = perfilRepository;
        this.passwordEncoder = passwordEncoder;
        this.telefonoMapper = telefonoMapper;
    }

    public AdministradorDTO crearAdministrador(AdministradorDTO administradorDTO)
            throws NotFoundException, BadRequestException {
        // Validar que el perfil sea de administrador
        Perfil perfil = perfilRepository.findById(administradorDTO.getIdPerfil())
                .orElseThrow(() -> new NotFoundException());
        if (!perfil.getNombre().equalsIgnoreCase("Administrador")) {
            throw new BadRequestException();
        }

        // Validar existencia de estado y tipo documento
        Estado estado = estadoRepository.findById(administradorDTO.getIdEstado())
                .orElseThrow(() -> new NotFoundException());
        TipoDocumento tipoDocumento = tipoDocumentoRepository.findById(administradorDTO.getIdTipoDocumento())
                .orElseThrow(() -> new NotFoundException());

        // Validar duplicados
        Optional<Administrador> verificarNumeroDocumento = administradorRepository
                .findByTipoDocumentoAndNumeDocumento(tipoDocumento, administradorDTO.getNumeDocumento());
        if (verificarNumeroDocumento.isPresent()) {
            throw new BadRequestException();
        }
        Optional<Administrador> verificarCorreo = administradorRepository.findByCorreo(administradorDTO.getCorreo());
        if (verificarCorreo.isPresent()) {
            throw new BadRequestException();
        }

        // Crear y configurar el administrador
        Administrador administrador = administradorMapper.toEntity(administradorDTO);
        administrador.setContrasenia(passwordEncoder.encode(administradorDTO.getContrasenia()));
        administrador.setEstado(estado);
        administrador.setTipoDocumento(tipoDocumento);
        administrador.setPerfil(perfil);

        if (administradorDTO.getTelefonos() != null && !administradorDTO.getTelefonos().isEmpty()) {
            List<Telefono> telefonos = administradorDTO.getTelefonos().stream()
                    .map(telefonoMapper::toEntity)
                    .collect(Collectors.toList());

            for (Telefono telefono : telefonos) {
                telefono.setUsuario(administrador);
            }

            administrador.setTelefonos(telefonos);
        }

        Validator ciValidator = new Validator();

        if(!ciValidator.validateCi(administrador.getNumeDocumento())){
            throw new BadRequestException("La cedula no es valida");
        }

        administradorRepository.save(administrador);

        logger.info("Creando administrador: {}",administradorDTO.getCorreo());
        logger.debug("Detalles: {}",administradorDTO);

        //envio correo
        mensajesEmail.mensajeRegistroUsuario(administrador.getCorreo());

        return administradorMapper.toDto(administrador);

    }
}