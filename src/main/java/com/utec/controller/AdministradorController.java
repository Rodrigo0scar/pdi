package com.utec.controller;

import com.utec.dto.AdministradorDTO;
import com.utec.service.AdministradorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

@RestController
@RequestMapping("/api/v1")
public class AdministradorController {

    private final AdministradorService administradorService;

    @Autowired
    public AdministradorController(AdministradorService administradorService) {
        this.administradorService = administradorService;
    }
    //@SecurityRequirements({})
    @PostMapping("/administradores")
    @Operation(summary = "Crear un nuevo admin", description = "Crea y registra un nuevo admin.")
    public ResponseEntity<?> crearAdministrador(@RequestBody AdministradorDTO administradorDTO) {
        try {
            AdministradorDTO administradorCreado = administradorService.crearAdministrador(administradorDTO);
            return new ResponseEntity<>(administradorCreado, HttpStatus.CREATED);
        } catch (BadRequestException e) {
            return new ResponseEntity<>("Datos inválidos o duplicados", HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e) {
            return new ResponseEntity<>("Recurso no encontrado", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno del servidor", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
} 