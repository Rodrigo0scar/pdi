package com.utec.service;

import com.utec.dto.TelefonoDTO;
import com.utec.mapper.TelefonoMapper;
import com.utec.model.Telefono;
import com.utec.model.Usuario;
import com.utec.repository.TelefonoRepository;
import com.utec.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class TelefonoService {

    private final TelefonoRepository telefonoRepository;
    private final TelefonoMapper telefonoMapper;
    private final UsuarioRepository usuarioRepository;

    private static final Logger logger = LoggerFactory.getLogger(TelefonoService.class);

    @Autowired
    public TelefonoService(TelefonoRepository telefonoRepository, UsuarioRepository usuarioRepository,TelefonoMapper telefonoMapper){
        this.telefonoRepository = telefonoRepository;
        this.telefonoMapper = telefonoMapper;
        this.usuarioRepository = usuarioRepository;
    }

    public TelefonoDTO agregarTelefono(int idUsuario, TelefonoDTO dto){
        Telefono tel = telefonoMapper.toEntity(dto);
        Usuario usuario = (usuarioRepository.findById(idUsuario))
                .orElseThrow(() -> new EntityNotFoundException ("No se encuentra el usuario con id "+idUsuario));

        tel.setUsuario(usuario);

        telefonoRepository.save(tel);

        TelefonoDTO dtoResponse = telefonoMapper.toDto(tel);

        //logger.info("Agregando nuevo numero de telefono: {}",dtoResponse.getNumTelefono());
        //logger.debug("Detalles: {}",dtoResponse);

        return dtoResponse;
    }
}
