package com.utec.controller;


import com.utec.dto.FiltroReporteReservaDTO;
import com.utec.dto.ReporteReservaDTO;
import com.utec.dto.ReservaDTO;
import com.utec.dto.ReservaPagoSeniaDTO;
import com.utec.service.ReservaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/reservas")
@Tag(name = "Controlador de Reservas", description = "Gestión de reservas de espacios. Requiere autenticación JWT.")
@SecurityRequirement(name = "bearerAuth")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @PostMapping
    @Operation(
            summary = "Crear reserva",
            description = "Crea y registra una nueva reserva de espacio. Los usuarios pueden reservar para sí mismos o para otros usuarios (si tienen permisos)."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Reserva creada exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReservaDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o espacio no disponible"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            summary = "Ejemplo de reserva válida",
                            value = """
                {
                  "idEspacio": 1,
                  "fechHoraReserva": "2025-07-01T14:00:00",
                  "duracion": 2,
                  "cantPersonas": 5,
                  "idModoPago": 1
                }
                """
                    )
            )
    )
    public ResponseEntity<?> crearReserva(
            @Valid @RequestBody ReservaDTO reservaDto,
            @Parameter(description = "Campo OPCIONAL: completar con ID si el usuario que reserva no es el logueado", required = false)
            @RequestParam(required = false) Integer idUsuarioReserva) {
        try {
            ReservaDTO reservaCreada = reservaService.crearReserva(reservaDto, Optional.ofNullable(idUsuarioReserva));
            return ResponseEntity.status(HttpStatus.CREATED).body(reservaCreada);
        } catch (EntityNotFoundException | IllegalStateException | IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @GetMapping
    @Operation(
            summary = "Listar reservas",
            description = "Retorna una lista de todas las reservas. Solo administradores pueden ver todas las reservas."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de reservas obtenida exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReservaDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "204", description = "No hay reservas para mostrar"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado - Se requiere rol ADMIN"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<ReservaDTO>> listarReservas() {
        List<ReservaDTO> lista = reservaService.listarReservas();
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/espacio/{idEspacio}")
    @Operation(
            summary = "Listar reservas por espacio",
            description = "Retorna una lista de reservas filtradas por espacio específico."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de reservas por espacio obtenida exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReservaDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Espacio no encontrado"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<ReservaDTO>> listarPorEspacio(
            @Parameter(description = "ID del espacio para filtrar", example = "1")
            @PathVariable Integer idEspacio) {
        List<ReservaDTO> lista = reservaService.listarReservasPorEspacio(idEspacio);
        return ResponseEntity.ok(lista);
    }

    @PatchMapping("/cancelar/{id}")
    @Operation(
            summary = "Cancelar reserva",
            description = "Cancela una reserva existente. Solo el propietario de la reserva o administradores pueden cancelarla."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva cancelada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Reserva no encontrada o no se puede cancelar"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado - No es propietario de la reserva"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<String> cancelarReserva(
            @Parameter(description = "ID de la reserva a cancelar", example = "1")
            @PathVariable Integer id) {
        reservaService.cancelarReserva(id);
        return ResponseEntity.ok("Reserva cancelada correctamente.");
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtener reserva por ID",
            description = "Retorna los datos de una reserva específica por su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Reserva encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReservaDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Reserva no encontrada"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ReservaDTO> obtenerReserva(
            @Parameter(description = "ID de la reserva a obtener", example = "1")
            @PathVariable Integer id) {
        ReservaDTO dto = reservaService.obtenerReservaPorId(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/reporte-reservas-por-fecha")
    @Operation(
            summary = "Reporte de reservas por fecha",
            description = "Genera un reporte de reservas agrupadas por fecha. Filtra por rango de fechas, espacios y tipo de operación. Solo administradores pueden generar reportes."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Reporte generado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReporteReservaDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado - Se requiere rol ADMIN"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<ReporteReservaDTO>> obtenerReportePorFecha(
            @Parameter(
                    description = "Fecha desde en formato dd/MM/yyyy",
                    example = "01/06/2025"
            )
            @RequestParam("fechaDesde") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate fechaDesde,

            @Parameter(
                    description = "Fecha hasta en formato dd/MM/yyyy",
                    example = "30/06/2025"
            )
            @RequestParam("fechaHasta") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate fechaHasta,

            @Parameter(
                    description = "Lista de ID de espacios a incluir",
                    example = "[1, 2, 3]"
            )
            @RequestParam("idEspacios") LinkedList<Integer> idEspacios,

            @Parameter(
                    description = "Tipo de operación: INSCRIPCION, CANCELACION o AMBAS",
                    example = "AMBAS"
            )
            @RequestParam("tipoOperacion") String tipoOperacion
    ) {
        FiltroReporteReservaDTO filtro = new FiltroReporteReservaDTO();
        filtro.setFechaDesde(fechaDesde);
        filtro.setFechaHasta(fechaHasta);
        filtro.setIdEspacios(idEspacios);
        filtro.setTipoOperacion(tipoOperacion);

        return ResponseEntity.ok(reservaService.obtenerReportePorFechas(filtro));
    }

    @GetMapping("/reporte-reservas-por-espacio")
    @Operation(
            summary = "Reporte de reservas por espacio",
            description = "Genera un reporte de reservas agrupadas por espacio. Filtra por rango de fechas, espacios y tipo de operación. Solo administradores pueden generar reportes."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Reporte generado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReporteReservaDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado - Se requiere rol ADMIN"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<ReporteReservaDTO>> obtenerReportePorEspacio(
            @Parameter(
                    description = "Fecha desde en formato dd/MM/yyyy",
                    example = "01/06/2025"
            )
            @RequestParam("fechaDesde") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate fechaDesde,

            @Parameter(
                    description = "Fecha hasta en formato dd/MM/yyyy",
                    example = "30/06/2025"
            )
            @RequestParam("fechaHasta") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate fechaHasta,

            @Parameter(
                    description = "Lista de ID de espacios a incluir",
                    example = "[1, 2, 3]"
            )
            @RequestParam("idEspacios") LinkedList<Integer> idEspacios,

            @Parameter(
                    description = "Tipo de operación: INSCRIPCION, CANCELACION o AMBAS",
                    example = "AMBAS"
            )
            @RequestParam("tipoOperacion") String tipoOperacion
    ) {
        FiltroReporteReservaDTO filtro = new FiltroReporteReservaDTO();
        filtro.setFechaDesde(fechaDesde);
        filtro.setFechaHasta(fechaHasta);
        filtro.setIdEspacios(idEspacios);
        filtro.setTipoOperacion(tipoOperacion);

        return ResponseEntity.ok(reservaService.obtenerReportePorEspacios(filtro));
    }

    @PatchMapping("/reserva/pago-senia")
    @Operation(
            summary = "Registrar pago de seña",
            description = "Actualiza la seña pagada y la fecha de cobro en una reserva existente. Solo administradores pueden registrar pagos."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Pago de seña registrado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReservaPagoSeniaDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o reserva no encontrada"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado - Se requiere rol ADMIN"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ReservaPagoSeniaDTO> registrarPagoSenia(@RequestBody ReservaPagoSeniaDTO dto) {
        ReservaPagoSeniaDTO resultado = reservaService.registrarPagoSenia(dto);
        return ResponseEntity.ok(resultado);
    }
}