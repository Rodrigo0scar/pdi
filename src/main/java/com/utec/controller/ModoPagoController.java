package com.utec.controller;

import com.utec.dto.ModoPagoDTO;
import com.utec.service.ModoPagoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/modos-pago")
@Tag(name = "Controlador de Modos de Pago", description = "Gestión de modos de pago disponibles en el sistema.")
public class ModoPagoController {

    @Autowired
    private ModoPagoService modoPagoService;

    @GetMapping
    @Operation(
            summary = "Obtener todos los modos de pago",
            description = "Retorna una lista de todos los modos de pago disponibles en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de modos de pago obtenida exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ModoPagoDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "204", description = "No hay modos de pago registrados"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<ModoPagoDTO>> findAll() {
        List<ModoPagoDTO> modosPago = modoPagoService.obtenerTodosLosModosDePago();
        if (modosPago.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(modosPago);
    }
}
