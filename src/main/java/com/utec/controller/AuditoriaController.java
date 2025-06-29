package com.utec.controller;

import com.utec.dto.AuditoriaDTO;
import com.utec.dto.FiltroReporteAuditoriaDTO;
import com.utec.model.Auditoria;
import com.utec.service.AuditoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/auditoria")
public class AuditoriaController {
    @Autowired
    private AuditoriaService auditoriaService;


    @GetMapping("/reporte")
    @Operation(
            summary = "Genera reporte de auditoría de usuarios",
            description = "Filtra y muestra las acciones realizadas por usuario dentro de un rango de fechas, " +
                    "y por operación (nombre del método o acción).",
            tags = {"Auditoría"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Reporte generado con éxito",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AuditoriaDTO.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Parámetros inválidos"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    public ResponseEntity<List<AuditoriaDTO>> obtenerReporteAuditoria(
            @Parameter(
                    description = "Correo del usuario (si no se ingresa trae todos)",
                    example = "juanperez@asur.com"
            )
            @RequestParam(value = "usuario", required = false) String usuario,

            @Parameter(
                    description = "Fecha desde",
                    example = "2025-06-01T00:00:00"
            )
            @RequestParam(value = "fechaDesde")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaDesde,

            @Parameter(
                    description = "Fecha hasta",
                    example = "2025-06-26T23:59:59"
            )
            @RequestParam(value = "fechaHasta")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaHasta,

            @Parameter(
                    description = "Nombre de la operación realizada (opcional)",
                    example = "login"
            )
            @RequestParam(value = "operacion", required = false) String operacion
    )

   {

        FiltroReporteAuditoriaDTO filtro = new FiltroReporteAuditoriaDTO();
        filtro.setUsuario(usuario);
        filtro.setFechaDesde(fechaDesde);
        filtro.setFechaHasta(fechaHasta);
        filtro.setOperacion(operacion);

        List<AuditoriaDTO> resultado = auditoriaService.obtenerReporteAuditoria(filtro);


        return ResponseEntity.ok(resultado);
    }

}
