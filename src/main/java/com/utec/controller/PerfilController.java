package com.utec.controller;

import com.utec.dto.PerfilDTO;
import com.utec.service.PerfilService;
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
@RequestMapping("api/v1")
@Tag(name = "Controlador de Perfiles", description = "Gestión de perfiles de usuario y sus permisos.")
public class PerfilController {
    private final PerfilService perfilService;

    @Autowired
    public PerfilController(PerfilService perfilService) {
        this.perfilService = perfilService;
    }

    @PostMapping("/perfiles")
    @Operation(
            summary = "Crear nuevo perfil",
            description = "Crea y registra un nuevo perfil de usuario en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Perfil creado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PerfilDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> addPerfil(@RequestBody PerfilDTO perfilDTO) {
        try {
            PerfilDTO perfilDTOResponse = perfilService.crearPerfil(perfilDTO);
            return new ResponseEntity<>(perfilDTOResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al crear perfil: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/perfiles")
    @Operation(
            summary = "Obtener todos los perfiles",
            description = "Retorna una lista de todos los perfiles registrados en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de perfiles obtenida exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PerfilDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "No hay perfiles para mostrar"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> obtenerPerfiles(){
        List<PerfilDTO> perfiles = perfilService.obtenerPerfiles();
        if (perfiles.isEmpty()){
        return ResponseEntity.badRequest().body("No hay perfiles para mostrar");
        }
        return new ResponseEntity<>(perfiles,HttpStatus.OK);
    }

    @PutMapping("/perfiles/{id}")
    @Operation(
            summary = "Modificar perfil",
            description = "Actualiza un perfil existente por su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Perfil modificado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PerfilDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o perfil no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> modificarPerfil(
            @Parameter(description = "ID del perfil a modificar", example = "1")
            @PathVariable int id,
            @RequestBody PerfilDTO dto){
        try{
            perfilService.actualizarPerfil(dto,id);
            return new ResponseEntity<>(dto,HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Se produjo un error al modificar el perfil " + e.getMessage());
        }
    }

    @DeleteMapping("/perfiles/{id}")
    @Operation(
            summary = "Activar/inactivar perfil",
            description = "Activa o inactiva un perfil existente por su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil eliminado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Perfil no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<String> eliminarPerfil(
            @Parameter(description = "ID del perfil a eliminar", example = "1")
            @PathVariable int id){
        try {
            perfilService.eliminarPerfil(id);
            return ResponseEntity.ok("Perfil modificado correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al eliminar perfil: " + e.getMessage());
        }
    }
}
