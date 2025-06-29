package com.utec.service;

import com.utec.dto.ModoPagoDTO;
import com.utec.mapper.ModoPagoMapper;
import com.utec.repository.ModoPagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModoPagoService {

    @Autowired
    private ModoPagoRepository modoPagoRepository;

    @Autowired
    private ModoPagoMapper modoPagoMapper;

    // obtener todos los modos de pago como DTO
    public List<ModoPagoDTO> obtenerTodosLosModosDePago() {
        return modoPagoRepository.findAll()
                .stream()
                .map(modoPagoMapper::toDTO)
                .toList();
    }
}
