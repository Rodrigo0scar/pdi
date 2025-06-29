package com.utec.PDI;
import com.utec.dto.SocioPagaCuotaDTO;
import com.utec.mapper.SocioPagaCuotaMapper;
import com.utec.model.*;
import com.utec.repository.*;
import com.utec.service.SocioPagaCuotaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class SocioPagaCuotaServiceTest {
    @InjectMocks
    private SocioPagaCuotaService service;

    @Mock
    private SocioRepository socioRepository;
    @Mock
    private CuotaRepository cuotaRepository;
    @Mock
    private ModoPagoRepository modoPagoRepository;
    @Mock
    private SocioPagaCuotaRepository socioPagaCuotaRepository;
    @Mock
    private SocioPagaCuotaMapper socioPagaCuotaMapper;

    @InjectMocks
    private SocioPagaCuotaService socioPagaCuotaService;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registrarPago_deberiaGuardarPagoCorrectamente() {
        SocioPagaCuotaDTO dto = SocioPagaCuotaDTO.builder()
                .idUsuario(1)
                .idCuota(2)
                .fechaCobro(LocalDate.now())
                .modoPago(3)
                .build();

        Socio socio = new Socio();
        socio.setId(1);
        Cuota cuota = new Cuota();
        cuota.setId(2);
        ModoPago modoPago = new ModoPago();
        modoPago.setId(3);

        SocioPagaCuota entity = new SocioPagaCuota();

        when(socioRepository.findById(1)).thenReturn(Optional.of(socio));
        when(cuotaRepository.findById(2)).thenReturn(Optional.of(cuota));
        when(modoPagoRepository.findById(3)).thenReturn(Optional.of(modoPago));
        when(socioPagaCuotaMapper.toEntity(dto, socio, cuota, modoPago)).thenReturn(entity);

        service.registrarPago(dto);

        verify(socioPagaCuotaRepository).save(entity);
    }

    @Test
    void registrarPago_deberiaLanzarExcepcionSiNoEncuentraSocio() {
        SocioPagaCuotaDTO dto = SocioPagaCuotaDTO.builder().idUsuario(99).build();
        when(socioRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.registrarPago(dto));
    }

    @Test
    void testModificarPago_Exitoso() {
        // Arrange
        SocioPagaCuotaId id = new SocioPagaCuotaId(1, 10);
        SocioPagaCuotaDTO dto = SocioPagaCuotaDTO.builder()
                .idUsuario(1)
                .idCuota(10)
                .fechaCobro(LocalDate.of(2025, 6, 20))
                .modoPago(2)
                .build();

        SocioPagaCuota pagoExistente = new SocioPagaCuota();
        pagoExistente.setId(id);
        pagoExistente.setFechCobro(LocalDate.of(2025, 6, 10));
        pagoExistente.setModoPago(new ModoPago(1, "Efectivo"));

        ModoPago nuevoModoPago = new ModoPago(2, "Transferencia");

        when(socioPagaCuotaRepository.findById(id)).thenReturn(Optional.of(pagoExistente));
        when(modoPagoRepository.findById(2)).thenReturn(Optional.of(nuevoModoPago));

        // Act
        socioPagaCuotaService.modificarPago(dto);

        // Assert
        verify(socioPagaCuotaRepository).save(pagoExistente);
        assertEquals(dto.getFechaCobro(), pagoExistente.getFechCobro());
        assertEquals(nuevoModoPago, pagoExistente.getModoPago());
    }

    @Test
    void testModificarPago_PagoNoExiste() {
        // Arrange
        SocioPagaCuotaDTO dto = SocioPagaCuotaDTO.builder()
                .idUsuario(1)
                .idCuota(10)
                .fechaCobro(LocalDate.of(2025, 6, 20))
                .modoPago(2)
                .build();

        SocioPagaCuotaId id = new SocioPagaCuotaId(1, 10);

        when(socioPagaCuotaRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            socioPagaCuotaService.modificarPago(dto);
        });

        assertEquals("No se puede modificar: el socio no ha pagado esa cuota.", exception.getMessage());
    }


}
