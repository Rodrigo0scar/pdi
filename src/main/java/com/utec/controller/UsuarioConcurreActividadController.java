package com.utec.controller;

import com.utec.dto.*;
import com.utec.service.UsuarioConcurreActividadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Controlador de Inscripciones", description = "Gestión de inscripciones, cancelaciones y reportes de actividades.")
public class UsuarioConcurreActividadController {

    @Autowired
    private UsuarioConcurreActividadService usuarioConcurreActividadService;

    @PostMapping("/inscripciones")
    @Operation(
            summary = "Inscribir usuario a actividad",
            description = "Permite inscribir a un usuario a una actividad específica."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inscripción realizada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o usuario ya inscrito"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<String> inscribirUsuario(@RequestBody @Valid UsuarioConcurreActividadDTO dto) {
        System.out.println("Entrando a inscribirUsuario: " + dto);

        usuarioConcurreActividadService.inscribirUsuario(dto.getIdUsuario(), dto.getIdActividad(), dto);
        return new ResponseEntity<>("Inscripción realizada con éxito.", HttpStatus.OK);
    }

    @PatchMapping("/inscripciones")
    @Operation(summary = "Cancelar inscripción de un usuario.", description = "Permite cancelar la inscripción de un usuario a una actividad específica.")
    public ResponseEntity<String> cancelarInscripcion(@RequestParam Integer id_usuario, @RequestParam Integer id_actividad, @RequestParam boolean activar) {
        try {
            usuarioConcurreActividadService.cancelarInscripcion(id_usuario, id_actividad, activar);
            String mensaje = activar ? "Inscripción activada correctamente" : "Inscripción cancelada correctamente";
            return new ResponseEntity<>(mensaje, HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getReason(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error inesperado: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/inscripciones/reporte")
    @Operation(
            summary = "Generar reporte detallado de inscripciones (RF 005-08)",
            description = "Genera un reporte detallado de inscripciones y cancelaciones a actividades por rango de fechas con filtros adicionales."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Reporte generado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReporteInscripcionDetalladoDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Filtros inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ReporteInscripcionDetalladoDTO> generarReporteDetallado(
            @RequestBody @Valid FiltroReporteInscripcionDTO filtro) {
        
        ReporteInscripcionDetalladoDTO reporte = usuarioConcurreActividadService.generarReporteDetallado(filtro);
        return new ResponseEntity<>(reporte, HttpStatus.OK);
    }

    @PostMapping("/inscripciones/reporte-tipo-actividad")
    @Operation(
            summary = "Generar reporte por tipo de actividad (RF 005-09)",
            description = "Genera un reporte detallado de inscripciones y cancelaciones por tipo de actividad con filtros adicionales."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Reporte generado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReporteTipoActDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Filtros inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ReporteTipoActDTO> obtenerReportePorTipoActividad(
            @RequestBody @Valid FiltroReportePorTipoActividadDTO filtroDTO) {
        ReporteTipoActDTO reporte = usuarioConcurreActividadService.obtenerReportePorTipoActividad(filtroDTO);
        return ResponseEntity.ok(reporte);
    }

    @GetMapping("/inscripciones/actividades/disponibles")
    @Operation(
            summary = "Obtener actividades disponibles para filtros",
            description = "Devuelve la lista de actividades que tienen inscripciones registradas para usar en filtros de reporte."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de actividades obtenida exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Integer.class)
                    )
            ),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<Integer>> obtenerActividadesDisponibles() {
        List<Integer> actividades = usuarioConcurreActividadService.obtenerActividadesDisponibles();
        return new ResponseEntity<>(actividades, HttpStatus.OK);
    }

    @GetMapping("/todos-registros")
    @Operation(
            summary = "Obtener todos los registros de inscripciones",
            description = "Retorna todos los registros de la tabla usuario_concurre_actividad."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de registros obtenida exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioConcurreActividadDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<UsuarioConcurreActividadDTO>> listarTodos() {
        List<UsuarioConcurreActividadDTO> lista = usuarioConcurreActividadService.obtenerTodos();
        return ResponseEntity.ok(lista);
    }

    @PatchMapping("/pago")
    @Operation(
            summary = "Registrar pago de actividad",
            description = "Actualiza el estado de pago de una inscripción a actividad."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Pago registrado correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioConcurreActividadDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "No se encontró la relación usuario-actividad"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<UsuarioConcurreActividadDTO> registrarPago(@RequestBody UsuarioConcurreActividadDTO dto) {
        UsuarioConcurreActividadDTO actualizado = usuarioConcurreActividadService.actualizarPagoActividad(dto);
        return ResponseEntity.ok(actualizado);
    }
}
