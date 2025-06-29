package com.utec.service;

import com.utec.auditoria.AuditoriaRepository;
import com.utec.dto.AuditoriaDTO;
import com.utec.dto.FiltroReporteAuditoriaDTO;
import com.utec.model.Auditoria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuditoriaService {
    @Autowired
    private AuditoriaRepository auditoriaRepository;

    public List<AuditoriaDTO> obtenerReporteAuditoria(FiltroReporteAuditoriaDTO filtro) {
        if (filtro.getFechaDesde() == null || filtro.getFechaHasta() == null) {
            throw new IllegalArgumentException("Debe especificar un rango de fechas");
        }

        String usuario = filtro.getUsuario();
        String operacion = filtro.getOperacion();

        // Convertir "" a null para no filtrar con strings vacíos
        if (usuario != null && usuario.isBlank()) {
            usuario = null;
        }
        if (operacion != null && operacion.isBlank()) {
            operacion = null;
        }

        List<Auditoria> lista = auditoriaRepository.buscarPorFiltros(
                usuario,
                filtro.getFechaDesde(),
                filtro.getFechaHasta(),
                operacion
        );

        // Convertir cada Auditoria a AuditoriaDTO y devolver la lista
        return lista.stream()
                .map(auditoria -> {
                    AuditoriaDTO dto = new AuditoriaDTO();
                    dto.setUsuario(auditoria.getUsuario());
                    dto.setFechaHora(auditoria.getFechaHora());
                    dto.setOperacion(auditoria.getOperacion());
                    dto.setTerminal(auditoria.getTerminal());
                    return dto;
                })
                .collect(Collectors.toList());
    }


}
