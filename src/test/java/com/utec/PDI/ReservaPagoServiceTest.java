package com.utec.PDI;

import com.utec.dto.ReservaPagoSeniaDTO;
import com.utec.model.Reserva;
import com.utec.repository.ReservaRepository;
import com.utec.service.ReservaService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReservaPagoServiceTest {
    @Mock
    private ReservaRepository reservaRepository;

    @InjectMocks
    private ReservaService reservaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registrarPagoSenia_reservaExistente_actualizaCampos() {
        // Arrange
        int idReserva = 5;
        Reserva reserva = Reserva.builder()
                .idReserva(idReserva)
                .fechCobro(LocalDateTime.now().minusDays(1))
                .build();

        ReservaPagoSeniaDTO dto = ReservaPagoSeniaDTO.builder()
                .idReserva(idReserva)
                .fechCobro(LocalDateTime.now())
                .fechPagoSenia(LocalDate.now())
                .impoSeniPagado(new BigDecimal("750.00"))
                .build();

        when(reservaRepository.findById(idReserva)).thenReturn(Optional.of(reserva));

        // Act
        ReservaPagoSeniaDTO resultado = reservaService.registrarPagoSenia(dto);

        // Assert
        assertEquals(dto.getFechCobro(), resultado.getFechCobro());
        assertEquals(dto.getFechPagoSenia(), resultado.getFechPagoSenia());
        assertEquals(dto.getImpoSeniPagado(), resultado.getImpoSeniPagado());

        verify(reservaRepository).save(reserva);
    }

    @Test
    void registrarPagoSenia_reservaNoExiste_lanzaExcepcion() {
        // Arrange
        int idInvalido = 99;
        ReservaPagoSeniaDTO dto = ReservaPagoSeniaDTO.builder()
                .idReserva(idInvalido)
                .fechCobro(LocalDateTime.now())
                .fechPagoSenia(LocalDate.now())
                .impoSeniPagado(new BigDecimal("500.00"))
                .build();

        when(reservaRepository.findById(idInvalido)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            reservaService.registrarPagoSenia(dto);
        });

        assertEquals("No se encontro la reserva con id " + idInvalido, ex.getMessage());
    }
}
