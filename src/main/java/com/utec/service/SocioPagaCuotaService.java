package com.utec.service;

import com.utec.dto.SocioPagaCuotaDTO;
import com.utec.mapper.SocioPagaCuotaMapper;
import com.utec.model.*;
import com.utec.repository.CuotaRepository;
import com.utec.repository.ModoPagoRepository;
import com.utec.repository.SocioPagaCuotaRepository;
import com.utec.repository.SocioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SocioPagaCuotaService {

    @Autowired
    private SocioRepository socioRepository;

    @Autowired
    private CuotaRepository cuotaRepository;

    @Autowired
    private ModoPagoRepository modoPagoRepository;

    @Autowired
    private SocioPagaCuotaRepository socioPagaCuotaRepository;

    @Autowired
    private SocioPagaCuotaMapper socioPagaCuotaMapper;

    public void registrarPago(SocioPagaCuotaDTO dto) {
        Socio socio = socioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Socio no encontrado"));

        Cuota cuota = cuotaRepository.findById(dto.getIdCuota())
                .orElseThrow(() -> new RuntimeException("Cuota no encontrada"));

        ModoPago modoPago = modoPagoRepository.findById(dto.getModoPago())
                .orElseThrow(() -> new RuntimeException("Modo de pago no encontrado"));

        SocioPagaCuota pago = socioPagaCuotaMapper.toEntity(dto, socio, cuota, modoPago);
        socioPagaCuotaRepository.save(pago);
    }

    public void modificarPago(SocioPagaCuotaDTO dto) {
        SocioPagaCuotaId id = new SocioPagaCuotaId(dto.getIdUsuario(), dto.getIdCuota());

        SocioPagaCuota pagoExistente = socioPagaCuotaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se puede modificar: el socio no ha pagado esa cuota."));

        // actualizar datos
        ModoPago nuevoModoPago = modoPagoRepository.findById(dto.getModoPago())
                .orElseThrow(() -> new RuntimeException("Modo de pago no encontrado"));

        pagoExistente.setFechCobro(dto.getFechaCobro());
        pagoExistente.setModoPago(nuevoModoPago);

        socioPagaCuotaRepository.save(pagoExistente);
    }

}
