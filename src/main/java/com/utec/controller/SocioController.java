package com.utec.controller;

import com.utec.dto.SocioDTO;
import com.utec.model.SocioId;
import com.utec.service.SocioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.logging.log4j.util.InternalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1") // Ruta base
@Tag(name = "Controlador de Socios", description = "Gestión de socios de la institución.")
public class SocioController {

    @Autowired
    SocioService socioService;

//    @SecurityRequirements({})
    @PostMapping("/socios")
    @Operation(
            summary = "Crear nuevo socio",
            description = "Crea y registra un nuevo socio en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Socio creado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SocioDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> crearSocio(@RequestBody SocioDTO socioDTO) {
        try {
            SocioDTO creado = socioService.crearSocio(socioDTO);
            return new ResponseEntity<>(creado, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al crear socio: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    //Modificar perfil de socio a usuario
    @PutMapping("/socios/modificar_perfil/{id}")
    @Operation(summary="Modificar perfil de socio a no socio", description = "Cambia el perfil de un socio a usuario.")
    public ResponseEntity<?> modificarPerfil(@PathVariable int id){
    SocioDTO socio = socioService.modificarPerfil(id);

    return new ResponseEntity<>(socio,HttpStatus.OK);
    }

    @GetMapping("/socios/listar-todos")
    @Operation(
            summary = "Obtener todos los socios",
            description = "Retorna una lista de todos los socios registrados en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de socios obtenida exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SocioDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "No hay socios para mostrar"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
   // @GetMapping("/socios")
    //@Operation(summary = "Obtener todos los socios", description = "Devuelve todos los socios registrados.")
    public ResponseEntity<?> obtenerSocios() {
        try {
            List<SocioDTO> socios = socioService.obtenerSocios();
            if (socios.isEmpty()) {
                return ResponseEntity.badRequest().body("No hay socios para mostrar.");
            }
            return new ResponseEntity<>(socios, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al obtener socios: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/socios/{idUsuario}/{idTipoDocumento}")
    @Operation(
            summary = "Obtener socio por ID compuesto",
            description = "Retorna un socio específico por su ID de usuario y tipo de documento."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Socio encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SocioDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Socio no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> obtenerPorId(
            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable int idUsuario,
            @Parameter(description = "ID del tipo de documento", example = "1")
            @PathVariable int idTipoDocumento) {
        try {
            SocioId socioId = new SocioId(idUsuario, idTipoDocumento);
            SocioDTO socio = socioService.obtenerPorId(socioId);
            return new ResponseEntity<>(socio, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al obtener socio: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PatchMapping("/socios/{idUsuario}/{idTipoDocumento}")
    @Operation(summary = "Activar o dar de baja lógicamente a un socio", description = "Permite cambiar el estado de un socio (activo/inactivo) por su ID compuesto.")
    public ResponseEntity<?> eliminarSocio(@PathVariable int idUsuario,
                                         //  @PathVariable int idTipoDocumento,
                                           @RequestParam boolean activar) {
        try {
            //SocioId socioId = new SocioId(idUsuario, idTipoDocumento);
            socioService.eliminarSocio(idUsuario,activar); //socioId, activar);
            String mensaje = activar ? "Socio activado correctamente" : "Socio dado de baja correctamente";
            return new ResponseEntity<>(mensaje, HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getReason(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al cambiar estado del socio: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

