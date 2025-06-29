package com.utec.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.utec.service.CustomUsuarioDetailsService;
import com.utec.jwt.JwtTokenUtil;
import com.utec.dto.AuthRequest;
import com.utec.dto.AuthResponse;


@RestController
@Tag(name = "Autenticación", description = "Endpoints para autenticación y generación de tokens JWT")
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final CustomUsuarioDetailsService customUsuarioDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    public AuthController(AuthenticationManager authenticationManager, CustomUsuarioDetailsService customUsuarioDetailsService, JwtTokenUtil jwtTokenUtil){
        this.authenticationManager = authenticationManager;
        this.customUsuarioDetailsService = customUsuarioDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @SecurityRequirements({})
    @Operation(
            summary = "Iniciar sesión",
            description = "Autentica al usuario con email y contraseña, devolviendo un token JWT válido para acceder a los demás endpoints protegidos."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Autenticación exitosa",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class),
                            examples = @ExampleObject(
                                    name = "Respuesta exitosa",
                                    value = """
                                            {
                                                "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Credenciales inválidas",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Error de autenticación",
                                    value = """
                                            {
                                                "error": "Nombre de usuario o password ingresada es incorrecto el dato!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Credenciales de autenticación",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthRequest.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Admin",
                                            summary = "Credenciales de administrador",
                                            value = """
                                                    {
                                                        "correo": "admin@asur.com",
                                                        "contrasenia": "admin1234"
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Usuario",
                                            summary = "Credenciales de usuario regular",
                                            value = """
                                                    {
                                                        "correo": "valeria@asur.com",
                                                        "contrasenia": "user1234"
                                                    }
                                                    """
                                    )
                            }
                    )
            )
            @RequestBody AuthRequest authRequest) throws Exception{

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getCorreo(), authRequest.getContrasenia()));
        } catch (Exception e) {
            throw new Exception("Nombre de usuario o password ingresada es incorrecto el dato!", e);
        }

        final UserDetails userDetails = customUsuarioDetailsService.loadUserByUsername(authRequest.getCorreo());
        final String jwt = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new AuthResponse(jwt));
    }
}

