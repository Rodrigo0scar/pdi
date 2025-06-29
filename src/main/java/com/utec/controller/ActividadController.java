package com.utec.controller;

import com.utec.dto.ActividadDTO;
import com.utec.dto.GetActividadDTO;
import com.utec.service.ActividadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Controlador de Actividades", description = "Gestión de actividades institucionales. Requiere autenticación JWT.")
@SecurityRequirement(name = "bearerAuth")
public class ActividadController {

    @Autowired
    ActividadService actividadService;

    @GetMapping("/actividad")
    @Operation(
            summary = "Obtener lista de actividades",
            description = "Retorna una lista de todas las actividades registradas en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de actividades obtenida exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ActividadDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "No hay actividades para mostrar"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> obtenerActividades() {
        try {
            List<ActividadDTO> lista = actividadService.ObtenerActividades();
            if (lista.isEmpty()) {
                return ResponseEntity.badRequest().body("No hay actividades para mostrar.");
            }
            return new ResponseEntity<>(lista, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al obtener actividades: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/actividad/{id}")
    @Operation(
            summary = "Obtener actividad por ID",
            description = "Retorna una actividad específica por su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Actividad encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ActividadDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Actividad no encontrada"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> obtenerPorId(
            @Parameter(description = "ID de la actividad a buscar", example = "1")
            @PathVariable int id) {
        try{
            ActividadDTO actividad = actividadService.obtenerPorId(id);
            return new ResponseEntity<>(actividad, HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.badRequest().body("No existe una actividad con id " + id);
        }
    }

    @GetMapping("/actividad/inscripcion")
    @Operation(summary = "Listar actividades disponibles para inscripción (activas)")
    public ResponseEntity<List<GetActividadDTO>> listarActividadesParaInscribirse() {
        List<GetActividadDTO> actividades = actividadService.listarActividadesParaInscribirse();
        return ResponseEntity.ok(actividades);
    }

    @PostMapping("/actividad")
    @Operation(
            summary = "Crear actividad",
            description = "Crea una nueva actividad institucional. Solo administradores pueden crear actividades."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Actividad creada exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ActividadDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado - Se requiere rol ADMIN"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> crearActividad(@RequestBody ActividadDTO dto) {
        try {
            ActividadDTO actividadCreada = actividadService.crearActividad(dto);
            return new ResponseEntity<>(actividadCreada, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear actividad: " + e.getMessage());
        }
    }

   /* @PutMapping("/actividad/{id}")
    @Operation(
            summary = "Actualizar actividad",
            description = "Actualiza una actividad existente por su ID. Solo administradores pueden actualizar actividades."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Actividad actualizada exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ActividadDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o actividad no encontrada"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado - Se requiere rol ADMIN"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> actualizarActividad(
            @Parameter(description = "ID de la actividad a actualizar", example = "1")
            @PathVariable int id,
            @RequestBody ActividadDTO actividadDTO) {
        try {
            // Asignar el id del path al DTO para que el service lo use
            actividadDTO.setId(id);
            System.out.println("ID en Controller: " + actividadDTO.getId());
            ActividadDTO actualizar = actividadService.actualizarActividad(actividadDTO);
            return new ResponseEntity<>(actualizar, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al actualizar actividad: " + e.getMessage());
        }
    }
*/
    @PatchMapping("/actividad/{id}")
    @Operation(summary = "Baja de espacio: Activar o inactivar.", description = "Permite dar de baja lógica o reactivar una actividad por su ID.")
    public ResponseEntity<?> eliminarActividad(@PathVariable int id, @RequestParam boolean activar) {
        try {
            actividadService.eliminarActividad(id, activar);
            return  ResponseEntity.ok("Actividad " + (activar ? "activada" : "inactivada") + " con éxito");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar actividad: " + e.getMessage());
        }
    }



}
