package com.utec.controller;

import com.utec.dto.TipoActividadDTO;
import com.utec.service.TipoActividadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.GeneratedValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Controlador de Tipos de Actividad", description = "Gestión de tipos de actividad (categorías) para las actividades institucionales.")
public class TipoActividadController {

    private final TipoActividadService tipoActividadService;

    @Autowired
    public TipoActividadController(TipoActividadService tipoActividadService){
        this.tipoActividadService = tipoActividadService;
    }

@PostMapping("/tipos_actividad")
@Operation(
        summary = "Crear tipo de actividad",
        description = "Agrega un nuevo tipo de actividad al sistema. El mismo se podrá utilizar para generar nuevas actividades de ese tipo."
)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Tipo de actividad creado correctamente",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = TipoActividadDTO.class)
                )
        ),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
})
@io.swagger.v3.oas.annotations.parameters.RequestBody(
        content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                        summary = "Ejemplo de JSON para crear un tipo de actividad",
                        value = """
                                {
                                    "tipo":"Aire libre",
                                    "descripcion":"Actividades en el patio de ASUR",
                                    "fechaBaja":null,
                                    "razonBaja":null,
                                    "comentariosBaja":null,
                                    "idUsuarioBaja":null,
                                    "idEstado":null
                                }
                """
                )
        )
)
    public ResponseEntity<?> agregarTipoActividad(@RequestBody TipoActividadDTO dto){
        try{
            tipoActividadService.agregarTipoActividad(dto);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Se produjo un error al crear el tipo de actividad");
        }
}

@GetMapping("/tipos_actividad/{id}")
@Operation(
        summary = "Obtener tipo de actividad por ID",
        description = "Obtiene un tipo de actividad específico por su ID desde la base de datos."
)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Tipo de actividad obtenido correctamente",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = TipoActividadDTO.class)
                )
        ),
        @ApiResponse(responseCode = "404", description = "Tipo de actividad no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
})
public ResponseEntity<?> obtenerPorId(
        @Parameter(description = "ID del tipo de actividad que se desea obtener", example = "1")
        @PathVariable int id){
        try {
            TipoActividadDTO dto = tipoActividadService.obtenerPorId(id);
            return new ResponseEntity<>(dto,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
}

@GetMapping("/tipos_actividad")
@Operation(
        summary = "Obtener todos los tipos de actividad",
        description = "Obtiene todos los tipos de actividad desde la base de datos."
)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Tipos de actividad obtenidos correctamente",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = TipoActividadDTO.class)
                )
        ),
        @ApiResponse(responseCode = "404", description = "No hay tipos de actividad registrados"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
})
public ResponseEntity<?> obtenerTodos(){
    List<TipoActividadDTO> tipos = tipoActividadService.obtenerTodos();
    if (tipos.isEmpty()){
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(tipos,HttpStatus.OK);
}

@PutMapping("/tipos_actividad/activar/{id}")
@Operation(
        summary = "Activar tipo de actividad",
        description = "Cambia el estado de inactivo (por defecto) a activo para poder usar los tipos de actividad."
)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Tipo de actividad activado correctamente",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = TipoActividadDTO.class)
                )
        ),
        @ApiResponse(responseCode = "400", description = "No se pudo activar el tipo de actividad"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
})
public ResponseEntity<?> activarTipoActividad(
        @Parameter(description = "ID del tipo de actividad que se desea activar", example = "1")
        @PathVariable int id){
        try {
            TipoActividadDTO dto = tipoActividadService.activarTipoActividad(id);
            return new ResponseEntity<>(dto,HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.badRequest().body("No se pudo activar el tipo de actividad con id "+id);
        }
}

@PutMapping("/tipos_actividad/{id}")
@Operation(
        summary = "Modificar tipo de actividad",
        description = "Modifica un tipo de actividad ya existente en el sistema."
)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Tipo de actividad modificado correctamente",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = TipoActividadDTO.class)
                )
        ),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
})
@io.swagger.v3.oas.annotations.parameters.RequestBody(
        content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                        summary = "Ejemplo de JSON para modificar un tipo de actividad",
                        value = """
                                {
                                    "tipo":"Aire libre",
                                    "descripcion":"Actividades en el patio de ASUR",
                                    "fechaBaja":null,
                                    "razonBaja":null,
                                    "comentariosBaja":null,
                                    "idUsuarioBaja":null,
                                    "idEstado":null
                                }
                """
                )
        )
)
    public ResponseEntity<?> modificarTipoActividad(
            @Parameter(description = "ID del tipo de actividad que se desea modificar", example = "1")
            @PathVariable int id,
            @RequestBody TipoActividadDTO dto){
        try {
            tipoActividadService.modificarTipoActividad(dto,id);
            return new ResponseEntity<>(dto,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
}

@PutMapping("/tipos_actividad/eliminar/{id}")
@Operation(
        summary = "Eliminar tipo de actividad",
        description = "Cambia el estado del tipo de actividad a inactivo. No se elimina el registro de la base de datos."
)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Tipo de actividad eliminado correctamente",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = TipoActividadDTO.class)
                )
        ),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
})
@io.swagger.v3.oas.annotations.parameters.RequestBody(
        content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                        summary = "Ejemplo de JSON para eliminar un tipo de actividad",
                        value = """
{
    "tipo": "Recreativas",
    "descripcion": "De recreacion",
    "fechaBaja": "2025-06-18",
    "razonBaja": "No se haran",
    "comentariosBaja": "Se dejaran de hacer",
    "idUsuarioBaja": null,
    "idEstado": null
}
                """
                )
        )
)
    public ResponseEntity<?> eliminarTipoActividad(
            @Parameter(description = "ID del tipo de actividad que se desea eliminar", example = "1")
            @PathVariable int id,
            @RequestBody TipoActividadDTO dto){
        try {
            tipoActividadService.eliminarTipoActividad(dto,id);
            return new ResponseEntity<>(dto,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
