package com.utec.controller;

import com.utec.dto.*;
import com.utec.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Controlador de Usuarios", description = "Gestión de usuarios del sistema. Requiere autenticación JWT.")
@SecurityRequirement(name = "bearerAuth")
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    @PostMapping("/usuarios")
    @Operation(
            summary = "Crear usuario",
            description = "Crea un nuevo usuario de tipo no socio. Solo administradores pueden crear usuarios."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuario creado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o usuario ya existe"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado - Se requiere rol ADMIN"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public UsuarioDTO crearUsuario(@RequestBody UsuarioDTO dto) throws BadRequestException {
        return usuarioService.crearUsuario(dto);

    }
    @PutMapping("/usuarios/modificar_perfil/{id}")
    @Operation(summary = "Modificar perfil de no socio a socio", description = "Cambia el tipo de perfil de usuario a socio.")
    public ResponseEntity<?> modificarPerfil (@PathVariable int id,@RequestBody SocioRequestDTO socioDTO) throws BadRequestException, NotFoundException {
        usuarioService.modificarPerfil(id,socioDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/usuarios")
    @Operation(
            summary = "Obtener lista de usuarios",
            description = "Retorna una lista de todos los usuarios registrados en el sistema. Solo administradores pueden ver todos los usuarios."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de usuarios obtenida exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioResponseDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "No hay usuarios para mostrar"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado - Se requiere rol ADMIN"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> obtenerUsuarios(){
        List<UsuarioResponseDTO> usuarios = usuarioService.obtenerUsuarios();
        if(usuarios.isEmpty()){
            return ResponseEntity.badRequest().body("No hay usuarios para mostrar");
        }
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }

    @GetMapping("/usuarios/{id}")
    @Operation(
            summary = "Obtener usuario por ID",
            description = "Retorna un usuario específico por su ID. Solo administradores pueden ver usuarios específicos."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuario encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioResponseDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado - Se requiere rol ADMIN"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> obtenerPorId(
            @Parameter(description = "ID del usuario a buscar", example = "1")
            @PathVariable int id){
        try {
            UsuarioResponseDTO usuario = usuarioService.obtenerPorId(id);
            return new ResponseEntity<>(usuario, HttpStatus.OK);
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().body("No existe el usuario con id " + id);
        }
    }

    @PatchMapping("/usuario/{id}")
    @Operation(summary = "Cambiar de estado a un usuario especifico.", description = "Metodo para activar/desactivar un usuario existente en el sistema por su ID.")
    public ResponseEntity<?> eliminarUsuario(@PathVariable int id, boolean activar) {
        try {
            usuarioService.eliminarUsuario(id, activar);
            String mensaje = activar ? "Usuario activado correctamente" : "Usuario dado de baja correctamente";
            return new ResponseEntity<>(mensaje,HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getReason(),HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error inesperado: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/usuario/nombre-filtro/{primNombre}")
    @Operation(
            summary = "Filtrar usuarios por nombre",
            description = "Filtra usuarios por el primer nombre. Solo administradores pueden usar este filtro."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuarios encontrados",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "No hay usuarios con ese nombre"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado - Se requiere rol ADMIN"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> filtrarPorNombre(
            @Parameter(description = "Primer nombre para filtrar", example = "Juan")
            @PathVariable String primNombre) {
        List<UsuarioDTO> usuario = usuarioService.obtenerUsuNombre(primNombre);
        if (usuario.isEmpty()) {
            return ResponseEntity.badRequest().body("No hay usuarios para mostrar " + primNombre);
        }
        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }

    @GetMapping("/usuario/apellido-filtro/{primApellido}")
    @Operation(
            summary = "Filtrar usuarios por apellido",
            description = "Filtra usuarios por el primer apellido. Solo administradores pueden usar este filtro."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuarios encontrados",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "No hay usuarios con ese apellido"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado - Se requiere rol ADMIN"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> filtrarPorApellido(
            @Parameter(description = "Primer apellido para filtrar", example = "Pérez")
            @PathVariable String primApellido) {
        List<UsuarioDTO> usuario = usuarioService.obtenerUsuApellido(primApellido);
        if (usuario.isEmpty()) {
            return ResponseEntity.badRequest().body("No hay usuarios para mostrar " + primApellido);
        }
        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }

    @GetMapping("/usuario/documento-filtro/{numeDocumento}")
    @Operation(
            summary = "Filtrar usuario por número de documento",
            description = "Filtra usuarios por el número de documento. Solo administradores pueden usar este filtro."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuario encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "No hay usuarios con ese documento"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado - Se requiere rol ADMIN"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> filtrarPorNumeroDocumento(
            @Parameter(description = "Número de documento para filtrar", example = "12345678")
            @PathVariable String numeDocumento) {
        UsuarioDTO usuario = usuarioService.obtenerUsuDocumento(numeDocumento);
        if (usuario == null) {
            return ResponseEntity.badRequest().body("No hay usuarios para mostrar " + numeDocumento);
        }
        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }

    @GetMapping("/usuario/estado-filtro/{estado}")
    @Operation(
            summary = "Filtrar usuarios por estado",
            description = "Filtra usuarios por estado (activo, inactivo, pendiente). Solo administradores pueden usar este filtro."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuarios encontrados",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "No hay usuarios con ese estado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado - Se requiere rol ADMIN"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> filtrarPorEstado(
            @Parameter(description = "Estado para filtrar", example = "Activo")
            @PathVariable EstadoDTO estado) {
        List<UsuarioDTO> usuario = usuarioService.obtenerUsuEstado(estado);
        if (usuario.isEmpty()) {
            return ResponseEntity.badRequest().body("No hay usuarios para mostrar " + estado);
        }
        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }

    @PutMapping("/usuarios/modificar-datos-propios")
    @Operation(
            summary = "Modificar datos propios",
            description = "Permite a un usuario modificar sus datos personales. No requiere rol de administrador."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Datos actualizados exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<UsuarioDTO> actualizarMisDatos(@RequestBody @Valid UsuarioDatosPropiosDTO dto, Authentication auth) {
        String email = auth.getName();
        UsuarioDTO actualizado = usuarioService.actualizarDatosPropios(email, dto);
        return ResponseEntity.ok(actualizado);
    }
}
