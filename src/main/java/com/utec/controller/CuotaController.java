package com.utec.controller;

import com.utec.dto.CuotaDTO;
import com.utec.service.CuotaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cuotas")
@Tag(name = "Controlador de Cuotas", description = "Gestión de cuotas mensuales de los socios.")
public class CuotaController {

    @Autowired
    private CuotaService cuotaService;

    @GetMapping
    @Operation(
            summary = "Obtener todas las cuotas",
            description = "Retorna una lista de todas las cuotas registradas en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de cuotas obtenida exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CuotaDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "204", description = "No hay cuotas registradas"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<CuotaDTO>> obtenerTodasLasCuotas() {
        List<CuotaDTO> cuotas = cuotaService.obtenerTodasLasCuotas();
        if (cuotas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(cuotas);
    }

    @GetMapping("/mes/{mes}")
    @Operation(
            summary = "Buscar cuotas por mes",
            description = "Retorna una lista de cuotas filtradas por el mes especificado."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de cuotas por mes obtenida exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CuotaDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "204", description = "No hay cuotas para el mes especificado"),
            @ApiResponse(responseCode = "400", description = "Mes inválido"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<CuotaDTO>> buscarCuotasPorMes(
            @Parameter(description = "Mes a filtrar (1-12)", example = "6")
            @PathVariable Integer mes) {
        List<CuotaDTO> cuotas = cuotaService.buscarCuotasPorMes(mes);
        if (cuotas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(cuotas);
    }
}
