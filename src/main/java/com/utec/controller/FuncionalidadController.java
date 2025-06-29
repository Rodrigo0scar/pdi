package com.utec.controller;

import com.utec.dto.EspacioDTO;
import com.utec.dto.FuncionalidadDTO;
import com.utec.dto.PerfilAccedeFuncionalidadDTO;
import com.utec.jwt.JwtTokenUtil;
import com.utec.model.Funcionalidad;
import com.utec.service.FuncionalidadService;
import com.utec.service.PerfilAccedeFuncionalidadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.NoSuchElementException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/funcionalidades")
@Tag(name = "Controlador de Funcionalidades", description = "Gestión de funcionalidades del sistema y sus permisos.")
public class FuncionalidadController {
@Autowired
private FuncionalidadService funcionalidadService;

@Autowired
private PerfilAccedeFuncionalidadService perfilAccedeFuncionalidadService;

@Autowired
private JwtTokenUtil jwtUtil;

@PostMapping
@Operation(
        summary = "Crear funcionalidad",
        description = "Crea y registra una nueva funcionalidad en el sistema."
)
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Funcionalidad registrada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
})
public ResponseEntity<?> crearFuncionalidad(@Valid @RequestBody FuncionalidadDTO dto) {
funcionalidadService.crearFuncionalidad(dto);

return ResponseEntity.ok("Funcionalidad registrada correctamente.");
}

@GetMapping("funcionalidades")
@Operation(
        summary = "Listar funcionalidades",
        description = "Retorna una lista de funcionalidades filtradas por nombre o estado."
)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Lista de funcionalidades obtenida exitosamente",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = FuncionalidadDTO.class)
                )
        ),
        @ApiResponse(responseCode = "204", description = "No hay funcionalidades para mostrar"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
})
public ResponseEntity<List<FuncionalidadDTO>> listarFuncionalidades(
            @Parameter(description = "Nombre de la funcionalidad para filtrar (opcional)", required = false)
            @RequestParam(required = false) String nombre,
            @Parameter(description = "ID del estado para filtrar (opcional)", required = false)
            @RequestParam(required = false) Integer idEstado) {

    List<FuncionalidadDTO> lista = funcionalidadService.listarFuncionalidades(nombre, idEstado);

    if (lista.isEmpty()) {
        return ResponseEntity.noContent().build();
    }

    return ResponseEntity.ok(lista);
    }

@PutMapping("/funcionalidades/{id}")
@Operation(
        summary = "Modificar funcionalidad",
        description = "Modifica la descripción de una funcionalidad existente."
)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Funcionalidad modificada exitosamente",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = Map.class)
                )
        ),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o funcionalidad no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
})
public ResponseEntity<?> modificarFuncionalidad(
        @Parameter(description = "ID de la funcionalidad a modificar", example = "1")
        @PathVariable Integer id,
        @RequestBody FuncionalidadDTO dto) {

    try {
        FuncionalidadDTO actualizada = funcionalidadService.modificarFuncionalidad(id, dto);
        return ResponseEntity.ok(Map.of(
                "message", "Funcionalidad modificada correctamente.",
                "data", actualizada
            ));
    } catch (IllegalArgumentException | NoSuchElementException | IllegalStateException e) {
        return ResponseEntity.badRequest().body(Map.of(
                "error", e.getMessage()
        ));
    }
    }

@PatchMapping("/funcionalidades/estado/{id}")
@Operation(
        summary = "Cambiar estado de funcionalidad",
        description = "Activa o inactiva una funcionalidad (baja lógica)."
)
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estado de funcionalidad cambiado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Funcionalidad no encontrada o no se puede cambiar estado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
})
public ResponseEntity<?> cambiarEstadoFuncionalidad(
        @Parameter(description = "ID de la funcionalidad", example = "1")
        @PathVariable Integer id) {
    try {
        funcionalidadService.cambiarEstadoFuncionalidad(id);
        return ResponseEntity.ok("La funcionalidad fue modificada correctamente.");
    } catch (IllegalStateException | EntityNotFoundException | IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
        return ResponseEntity.internalServerError().body("Error inesperado: " + e.getMessage());
    }
    }

@PostMapping("/funcionalidades/asignar")
@Operation(
        summary = "Asignar funcionalidades a perfil",
        description = "Asigna funcionalidades específicas a un perfil de usuario."
)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Acceso asignado exitosamente",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = Map.class)
                )
        ),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
})
public ResponseEntity<?> asignarAccesoFuncionalidad(@RequestBody PerfilAccedeFuncionalidadDTO dto) {
    try {
        perfilAccedeFuncionalidadService.asignarFuncionalidadesAPerfil(dto);
        return ResponseEntity.ok(Map.of("message", "Acceso asignado correctamente."));
    } catch (Exception e) {
        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    }
    }

@PutMapping("/perfiles/modificar-acceso-funcionalidad")
@Operation(
        summary = "Desvincular funcionalidades de perfil",
        description = "Desvincula funcionalidades específicas de un perfil de usuario."
)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Funcionalidades desvinculadas exitosamente",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = Map.class)
                )
        ),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
})
public ResponseEntity<?> modificarAccesoFuncionalidad(@RequestBody PerfilAccedeFuncionalidadDTO dto) {
    try {
        perfilAccedeFuncionalidadService.desvincularFuncionalidadesDePerfil(dto);
        return ResponseEntity.ok(Map.of("message", "Funcionalidades desvinculadas correctamente."));
    } catch (Exception e) {
        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

@GetMapping("/por-perfil/{idPerfil}")
@Operation(
        summary = "Listar funcionalidades por perfil",
        description = "Retorna las funcionalidades asignadas a un perfil específico."
)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Lista de funcionalidades por perfil obtenida exitosamente",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = FuncionalidadDTO.class)
                )
        ),
        @ApiResponse(responseCode = "400", description = "Perfil no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
})
public ResponseEntity<List<FuncionalidadDTO>> obtenerPorPerfil(
        @Parameter(description = "ID del perfil", example = "1")
        @PathVariable Integer idPerfil) {
        PerfilAccedeFuncionalidadDTO dto = perfilAccedeFuncionalidadService.obtenerIdFuncionalidadesPorPerfil(idPerfil);
    List<FuncionalidadDTO> funcionalidadesTodas = funcionalidadService.listarTodasFuncionalidades();
    List<Integer> idsBuscar = dto.getFuncionalidades();

    List<FuncionalidadDTO> funcionalidadesFiltradas = funcionalidadesTodas.stream()
            .filter(f -> idsBuscar.contains(f.getIdFuncionalidad()))
            .collect(Collectors.toList());

        return ResponseEntity.ok(funcionalidadesFiltradas);
    }

@GetMapping("/activas")
@Operation(
        summary = "Listar funcionalidades activas",
        description = "Retorna todas las funcionalidades activas del sistema."
)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Lista de funcionalidades activas obtenida exitosamente",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = FuncionalidadDTO.class)
                )
        ),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
})
public ResponseEntity<List<FuncionalidadDTO>> listarFuncionalidadesActivas() {
    List<FuncionalidadDTO> funcionalidades = funcionalidadService.listarTodasFuncionalidades();
    return ResponseEntity.ok(funcionalidades);
}

@GetMapping("/func-del-usuario-logueado")
@Operation(
        summary = "Funcionalidades del usuario logueado",
        description = "Retorna las funcionalidades disponibles para el usuario actualmente autenticado."
)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Funcionalidades del usuario obtenidas exitosamente",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = FuncionalidadDTO.class)
                )
        ),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
})
public ResponseEntity<List<FuncionalidadDTO>> funcionalidadesDelUsuario(HttpServletRequest request) {
        Integer idPerfil = jwtUtil.extractIdPerfilDesdeJWT(request); //extraer desde el token

        PerfilAccedeFuncionalidadDTO funcDePerfil = perfilAccedeFuncionalidadService.obtenerIdFuncionalidadesPorPerfil(idPerfil);
        List<FuncionalidadDTO> funcionalidadesTodas = funcionalidadService.listarTodasFuncionalidades();
        List<Integer> idsBuscar = funcDePerfil.getFuncionalidades();

        List<FuncionalidadDTO> funcionalidadesFiltradas = funcionalidadesTodas.stream()
                .filter(f -> idsBuscar.contains(f.getIdFuncionalidad()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(funcionalidadesFiltradas);
    }
}
