package com.utec.service;

import com.utec.dto.CuotaDTO;
import com.utec.mapper.CuotaMapper;
import com.utec.repository.CuotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CuotaService {
    @Autowired
    private CuotaRepository cuotaRepository;

    @Autowired
    private CuotaMapper cuotaMapper;

    // listar todas las cuotas
    public List<CuotaDTO> obtenerTodasLasCuotas() {
        return cuotaRepository.findAll()
                .stream()
                .map(cuotaMapper::toDTO)
                .toList();
    }

    // buscar cuotas por mes
    public List<CuotaDTO> buscarCuotasPorMes(Integer mes) {
        return cuotaRepository.findByMes(mes)
                .stream()
                .map(cuotaMapper::toDTO)
                .toList();
    }
}
