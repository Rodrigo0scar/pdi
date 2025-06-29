package com.utec.controller;

import com.utec.dto.SocioPagaCuotaDTO;
import com.utec.service.SocioPagaCuotaService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pagos")
public class SocioPagaCuotaController {

    @Autowired
    private SocioPagaCuotaService socioPagaCuotaService;

    // post/pagos
    @PostMapping
    @Operation(summary = "Pagar la cuota de un socio", description = "Metodo que paga la cuota de un socio, lo que hace es realizar un POST a la tabla socio_paga_cuota, en esta tabla se guardan todos los pagos relacionados al pago de las cuotas del socio")
    public ResponseEntity<String> registrarPago(@RequestBody SocioPagaCuotaDTO dto) {
        socioPagaCuotaService.registrarPago(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Pago registrado correctamente.");
    }

    //atualizar el pago
    @PutMapping
    @Operation(summary = "Modificar el pago de una cuota", description = "Permite modificar la fecha y el modo de pago de una cuota ya pagada por el socio.")
    public ResponseEntity<String> modificarPago(@RequestBody SocioPagaCuotaDTO dto) {
        socioPagaCuotaService.modificarPago(dto);
        return ResponseEntity.ok("Pago actualizado correctamente.");
    }

}
