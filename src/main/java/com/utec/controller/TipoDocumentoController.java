package com.utec.controller;

import com.utec.dto.TipoDocumentoDTO;
import com.utec.model.TipoDocumento;
import com.utec.service.TipoDocumentoService;
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
import java.util.Optional;

@RestController
@RequestMapping("/app/v")
@Tag(name = "Controlador de Tipos de Documento", description = "Gestión de tipos de documento (DNI, pasaporte, etc.).")
public class TipoDocumentoController {

   @Autowired
   TipoDocumentoService tipoDocumentoService;

   @GetMapping("/tipos-documento")
   @Operation(
           summary = "Obtener todos los tipos de documento",
           description = "Retorna una lista de todos los tipos de documento registrados en el sistema."
   )
   @ApiResponses(value = {
           @ApiResponse(
                   responseCode = "200",
                   description = "Lista de tipos de documento obtenida exitosamente",
                   content = @Content(
                           mediaType = "application/json",
                           schema = @Schema(implementation = TipoDocumentoDTO.class)
                   )
           ),
           @ApiResponse(responseCode = "400", description = "No hay tipos de documento para mostrar"),
           @ApiResponse(responseCode = "500", description = "Error interno del servidor")
   })
   public ResponseEntity<?> obtenerTipoDocumentos() {
       try {
           List<TipoDocumentoDTO> lista = tipoDocumentoService.obtenerTipoDocumentos();
           if (lista.isEmpty()) {
               return ResponseEntity.badRequest().body("No hay tipos de documento para mostrar.");
           }
           return new ResponseEntity<>(lista, HttpStatus.OK);
       } catch (Exception e) {
           return new ResponseEntity<>("Error al obtener tipos de documento: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
       }
   }

   @GetMapping("/tipos-documento/{id}")
   @Operation(
           summary = "Obtener tipo de documento por ID",
           description = "Retorna un tipo de documento específico según su ID."
   )
   @ApiResponses(value = {
           @ApiResponse(
                   responseCode = "200",
                   description = "Tipo de documento encontrado",
                   content = @Content(
                           mediaType = "application/json",
                           schema = @Schema(implementation = TipoDocumentoDTO.class)
                   )
           ),
           @ApiResponse(responseCode = "400", description = "Tipo de documento no encontrado"),
           @ApiResponse(responseCode = "500", description = "Error interno del servidor")
   })
   public ResponseEntity<?> obtenerPorId(
           @Parameter(description = "ID del tipo de documento a obtener", example = "1")
           @PathVariable Integer id) {
       try {
           TipoDocumentoDTO dto = tipoDocumentoService.obtenerPorId(id);
           return new ResponseEntity<>(dto, HttpStatus.OK);
       } catch (Exception e) {
           return new ResponseEntity<>("Error al obtener tipo de documento: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
       }
   }

   @PostMapping("/tipos-documento")
   @Operation(
           summary = "Crear nuevo tipo de documento",
           description = "Crea y registra un nuevo tipo de documento en el sistema."
   )
   @ApiResponses(value = {
           @ApiResponse(
                   responseCode = "201",
                   description = "Tipo de documento creado exitosamente",
                   content = @Content(
                           mediaType = "application/json",
                           schema = @Schema(implementation = TipoDocumentoDTO.class)
                   )
           ),
           @ApiResponse(responseCode = "400", description = "Datos inválidos"),
           @ApiResponse(responseCode = "500", description = "Error interno del servidor")
   })
   public ResponseEntity<?> crearTipoDocumento(@RequestBody TipoDocumentoDTO tipoDocumentoDTO) {
       try {
           TipoDocumentoDTO creado = tipoDocumentoService.crearTipoDocumento(tipoDocumentoDTO);
           return new ResponseEntity<>(creado, HttpStatus.CREATED);
       } catch (Exception e) {
           return new ResponseEntity<>("Error al crear tipo de documento: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
       }
   }

   @PutMapping("/tipos-documento")
   @Operation(
           summary = "Actualizar tipo de documento",
           description = "Modifica un tipo de documento existente en el sistema."
   )
   @ApiResponses(value = {
           @ApiResponse(
                   responseCode = "200",
                   description = "Tipo de documento actualizado exitosamente",
                   content = @Content(
                           mediaType = "application/json",
                           schema = @Schema(implementation = TipoDocumentoDTO.class)
                   )
           ),
           @ApiResponse(responseCode = "400", description = "Datos inválidos o tipo de documento no encontrado"),
           @ApiResponse(responseCode = "500", description = "Error interno del servidor")
   })
   public ResponseEntity<?> actualizarTipoDocumento(@RequestBody TipoDocumentoDTO tipoDocumentoDTO) {
       try {
           TipoDocumentoDTO actualizado = tipoDocumentoService.actualizarTipoDocumento(tipoDocumentoDTO);
           return new ResponseEntity<>(actualizado, HttpStatus.OK);
       } catch (Exception e) {
           return new ResponseEntity<>("Error al actualizar tipo de documento: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
       }
   }

    @DeleteMapping("/tipos-documento/{id}")
    @Operation(
            summary = "Eliminar tipo de documento",
            description = "Elimina un tipo de documento existente por su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipo de documento eliminado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Tipo de documento no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> eliminarTipoDocumento(
            @Parameter(description = "ID del tipo de documento a eliminar", example = "1")
            @PathVariable Integer id) {
        try {
            tipoDocumentoService.eliminarTipoDocumento(id);
            return new ResponseEntity<>("Tipo de documento eliminado correctamente", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al eliminar tipo de documento: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
