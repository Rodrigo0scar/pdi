package com.utec.controller;

import com.utec.dto.EspacioDTO;
import com.utec.dto.SocioDTO;
import com.utec.model.Espacio;
import com.utec.service.EspacioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Controlador de Espacios", description = "Gestión de espacios para reservas. Requiere autenticación JWT.")
@SecurityRequirement(name = "bearerAuth")
public class EspacioController {

    @Autowired
    private EspacioService espacioService;

    @GetMapping("/espacios")
    @Operation(
            summary = "Obtener lista de espacios",
            description = "Retorna una lista de todos los espacios disponibles para reservas que se pueden filtrar por nombre o estado. Por defecto retorna los activos"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de espacios obtenida exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EspacioDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "No hay espacios para mostrar"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> obtenerEspacios(
            @Parameter(description = "Nombre del espacio para filtrar (opcional)", required = false)
            @RequestParam(required = false) String nombre,
            @Parameter(description = "ID del estado para filtrar (opcional)", required = false)
            @RequestParam(required = false) Integer idEstado) {
        try {
            List<EspacioDTO> espacios = espacioService.listarEspacios(nombre, idEstado);
            if (espacios.isEmpty()) {
                return ResponseEntity.badRequest().body("No hay espacios para mostrar");
            }
            return new ResponseEntity<>(espacios, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al obtener espacios: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/espacios")
    @Operation(
            summary = "Crear espacio",
            description = "Crea un nuevo espacio para reservas. Solo administradores pueden crear espacios."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Espacio creado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EspacioDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o espacio ya existe"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado - Se requiere rol ADMIN"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> crearEspacio(@Valid @RequestBody EspacioDTO dto) {
        try {
            EspacioDTO espacioCreado = espacioService.crearEspacio(dto);
            return new ResponseEntity<>(espacioCreado, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear espacio: " + e.getMessage());
        }
    }

    @PutMapping("/espacios/{id}")
    @Operation(
            summary = "Actualizar espacio",
            description = "Actualiza un espacio existente por su ID. Solo administradores pueden actualizar espacios."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Espacio actualizado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EspacioDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o espacio no encontrado"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado - Se requiere rol ADMIN"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> actualizarEspacio(
            @Parameter(description = "ID del espacio a actualizar", example = "1")
            @PathVariable Integer id,
            @Valid @RequestBody EspacioDTO dto) {
        try {
            EspacioDTO espacioActualizado = espacioService.actualizarEspacio(id, dto);
            return new ResponseEntity<>(espacioActualizado, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al actualizar espacio: " + e.getMessage());
        }
    }

    @PatchMapping("/espacios/{id}/estado")
    @Operation(
            summary = "Cambiar estado de espacio",
            description = "Activa o inactiva un espacio. Solo administradores pueden cambiar el estado."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado del espacio cambiado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Espacio no encontrado o no se puede cambiar estado"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado - Se requiere rol ADMIN"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> cambiarEstadoEspacio(
            @Parameter(description = "ID del espacio", example = "1")
            @PathVariable Integer id,
            @Parameter(description = "true para activar, false para inactivar", example = "true")
            @RequestParam boolean activar) {
        try {
            espacioService.cambiarEstadoEspacio(id, activar);
            return new ResponseEntity<>("El espacio fue " + (activar ? "activado" : "inactivado") + " correctamente.", HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al cambiar estado: " + e.getMessage());
        }
    }

    @GetMapping("/espacios/disponibles")
    @Operation(
            summary = "Verificar disponibilidad de espacios",
            description = "Verifica la disponibilidad de espacios en una fecha y hora específica."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Disponibilidad verificada exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Map.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos o no hay espacios disponibles"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Map<String, Object>> verificarDisponibilidad(
            @Parameter(description = "Fecha y hora de inicio", example = "2025-07-01T14:00:00")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @Parameter(description = "Duración en horas", example = "2")
            @RequestParam int duracionHoras,
            @Parameter(description = "Cantidad de personas", example = "5")
            @RequestParam int cantidadPersonas) {
        try {
            List<EspacioDTO> espaciosDisponibles = espacioService.obtenerEspaciosDisponibles(fechaInicio, duracionHoras, cantidadPersonas);
            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("fechaInicio", fechaInicio);
            respuesta.put("duracionHoras", duracionHoras);
            respuesta.put("cantidadPersonas", cantidadPersonas);
            respuesta.put("espaciosDisponibles", espaciosDisponibles);
            respuesta.put("totalDisponibles", espaciosDisponibles.size());
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Error al verificar disponibilidad: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
