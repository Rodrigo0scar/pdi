package com.utec.PDI;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.utec.dto.ReservaDTO;
import com.utec.model.*;
import com.utec.repository.*;
import com.utec.service.ReservaService;
import com.utec.mapper.ReservaMapper;
import jakarta.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

import com.utec.service.ReservaService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class ReservaServiceTest {
    @InjectMocks
    private ReservaService reservaService;

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private EspacioRepository espacioRepository;

    @Mock
    private EstadoRepository estadoRepository;

    @Mock
    private ReservaMapper reservaMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }



        @Test
        void crearReserva() {

            ReservaDTO reservaDTO = new ReservaDTO();
            reservaDTO.setIdEspacio(1);
            reservaDTO.setFechHoraReserva(LocalDateTime.of(2025,7,2,14,0));
            reservaDTO.setDuracion(2);
            reservaDTO.setCantPersonas(4);
            reservaDTO.setIdModoPago(1);

            Usuario usuario = new Usuario();
            Perfil perfil = new Perfil();
            perfil.setIdPerfil(2); // socio
            usuario.setPerfil(perfil);
            usuario.setCorreo("user@correo.com");

            Espacio espacio = new Espacio();
            espacio.setIdEspacio(1);
            espacio.setPrecioReservaSocio(BigDecimal.valueOf(100));
            espacio.setPrecioReservaNoSocio(BigDecimal.valueOf(150));

            Estado estadoActivo = new Estado();
            estadoActivo.setIdestado(1);

            ModoPago modoPago = new ModoPago();
            modoPago.setId(reservaDTO.getIdModoPago());
            modoPago.setModo("Efectivo");

            Reserva reservaEntity = Reserva.builder()
                    .usuario(usuario)
                    .espacio(espacio)
                    .fechHoraReserva(reservaDTO.getFechHoraReserva())
                    .duracion(reservaDTO.getDuracion())
                    .cantPersonas(reservaDTO.getCantPersonas())
                    .impoTotal(espacio.getPrecioReservaSocio())
                    .fechPagoSenia(null)
                    .impoSeniPagado(BigDecimal.ZERO)
                    .fechCobro(null)
                    .estado(estadoActivo)
                    .modoPago(modoPago)
                    //.idModoPago(reservaDTO.getIdModoPago())
                    .fechVtoSenia(reservaDTO.getFechHoraReserva().toLocalDate().minusDays(5))
                    .build();

            ReservaDTO reservaDTOResult = new ReservaDTO();
            reservaDTOResult.setIdReserva(123);


            when(usuarioRepository.findById(anyInt())).thenReturn(Optional.of(usuario));
            when(espacioRepository.findById(reservaDTO.getIdEspacio())).thenReturn(Optional.of(espacio));
            when(estadoRepository.findById(1)).thenReturn(Optional.of(estadoActivo));
            when(espacioRepository.buscarEspaciosDisponibles(any(), any(), any())).thenReturn(List.of(espacio));
            when(reservaRepository.save(any())).thenReturn(reservaEntity);
            when(reservaMapper.toDto(any())).thenReturn(reservaDTOResult);

            // cuando no se pasa usuario
            Authentication auth = mock(Authentication.class);
            SecurityContext securityContext = mock(SecurityContext.class);
            when(securityContext.getAuthentication()).thenReturn(auth);
            when(auth.getName()).thenReturn("user@correo.com");
            SecurityContextHolder.setContext(securityContext);


            ReservaDTO resultado = reservaService.crearReserva(reservaDTO, Optional.of(1));

            assertNotNull(resultado);
            assertEquals(123, resultado.getIdReserva());

            verify(usuarioRepository).findById(1);
            verify(espacioRepository).findById(1);
            verify(estadoRepository).findById(1);
            verify(reservaRepository).save(any(Reserva.class));
        }


        @Test
        void crearReservaEspacioNoDisponible() {
            ReservaDTO dto = new ReservaDTO();
            dto.setIdEspacio(1);
            dto.setFechHoraReserva(LocalDateTime.now());
            dto.setDuracion(1);
            dto.setIdModoPago(1);

            Usuario usuario = new Usuario();
            Perfil perfil = new Perfil();
            perfil.setIdPerfil(1);
            usuario.setPerfil(perfil);

            Espacio espacio = new Espacio();
            espacio.setIdEspacio(1);

            Estado estadoActivo = new Estado();
            estadoActivo.setIdestado(1);

            when(usuarioRepository.findById(anyInt())).thenReturn(Optional.of(usuario));
            when(espacioRepository.findById(dto.getIdEspacio())).thenReturn(Optional.of(espacio));
            when(estadoRepository.findById(1)).thenReturn(Optional.of(estadoActivo));
            when(espacioRepository.buscarEspaciosDisponibles(any(), any(), any())).thenReturn(Collections.emptyList());

            Exception ex = assertThrows(IllegalStateException.class, () -> {
                reservaService.crearReserva(dto, Optional.of(1));
            });

            assertEquals("El espacio no está disponible para la fecha y hora indicadas", ex.getMessage());
        }

        @Test
        void crearReservaUsuarioNoEncontrado() {
            ReservaDTO dto = new ReservaDTO();
            dto.setIdEspacio(1);
            dto.setFechHoraReserva(LocalDateTime.now());
            dto.setDuracion(1);
            dto.setIdModoPago(1);

            Authentication auth = mock(Authentication.class);
            SecurityContext securityContext = mock(SecurityContext.class);
            when(securityContext.getAuthentication()).thenReturn(auth);
            when(auth.getName()).thenReturn("noexiste@correo.com");
            SecurityContextHolder.setContext(securityContext);

            when(usuarioRepository.findByCorreo("noexiste@correo.com")).thenReturn(Optional.empty());

            Exception ex = assertThrows(org.springframework.security.core.userdetails.UsernameNotFoundException.class, () -> {
                reservaService.crearReserva(dto, Optional.empty());
            });

            assertEquals("Usuario no encontrado en token", ex.getMessage());
        }

        @Test
        void crearReservaEspacioNoEncontrado() {
            ReservaDTO dto = new ReservaDTO();
            dto.setIdEspacio(1);
            dto.setFechHoraReserva(LocalDateTime.now());
            dto.setDuracion(1);
            dto.setIdModoPago(1);

            when(usuarioRepository.findById(anyInt())).thenReturn(Optional.of(new Usuario()));

            when(espacioRepository.findById(dto.getIdEspacio())).thenReturn(Optional.empty());

            Exception ex = assertThrows(EntityNotFoundException.class, () -> {
                reservaService.crearReserva(dto, Optional.of(1));
            });

            assertEquals("Espacio no encontrado", ex.getMessage());
        }



    @Test
    void testListarReservas() {
        Reserva reserva = new Reserva();
        ReservaDTO reservaDTO = new ReservaDTO();
        when(reservaRepository.findAll()).thenReturn(List.of(reserva));
        when(reservaMapper.toDto(reserva)).thenReturn(reservaDTO);

        List<ReservaDTO> result = reservaService.listarReservas();

        assertEquals(1, result.size());
    }

    @Test
    void testObtenerReservaPorId() {
        Reserva reserva = new Reserva();
        ReservaDTO dto = new ReservaDTO();
        when(reservaRepository.findById(1)).thenReturn(Optional.of(reserva));
        when(reservaMapper.toDto(reserva)).thenReturn(dto);

        ReservaDTO result = reservaService.obtenerReservaPorId(1);

        assertEquals(dto, result);
    }

    @Test
    void testObtenerReservaPorIdNoExiste() {
        when(reservaRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> reservaService.obtenerReservaPorId(1));
    }

    @Test
    void testListarReservasPorEspacio() {
        Reserva reserva = new Reserva();
        ReservaDTO dto = new ReservaDTO();
        when(reservaRepository.findByEspacio_IdEspacio(1)).thenReturn(List.of(reserva));
        when(reservaMapper.toDto(reserva)).thenReturn(dto);

        List<ReservaDTO> result = reservaService.listarReservasPorEspacio(1);

        assertEquals(1, result.size());
    }

    @Test
    void testCancelarReserva() {
        Reserva reserva = new Reserva();
        Estado estadoActivo = new Estado(1, "Activo");
        Estado estadoCancelado = new Estado(2, "Inactivo");

        reserva.setEstado(estadoActivo);
        reserva.setFechHoraReserva(LocalDateTime.now().plusHours(1));
        reserva.setDuracion(60); // minutos

        when(reservaRepository.findById(1)).thenReturn(Optional.of(reserva));
        when(estadoRepository.findById(2)).thenReturn(Optional.of(estadoCancelado));

        assertDoesNotThrow(() -> reservaService.cancelarReserva(1));
        assertEquals(estadoCancelado, reserva.getEstado());
    }

    @Test
    void testCancelarReservaInactiva() {
        Estado estadoCancelado = new Estado(2, "Inactivo");
        Reserva reserva = new Reserva();
        reserva.setEstado(estadoCancelado);

        when(reservaRepository.findById(1)).thenReturn(Optional.of(reserva));

        assertThrows(IllegalStateException.class, () -> reservaService.cancelarReserva(1));
    }

    @Test
    void testCancelarReservaFinalizada() {
        Estado estadoActivo = new Estado(1, "Activo");
        Reserva reserva = new Reserva();
        reserva.setEstado(estadoActivo);
        reserva.setFechHoraReserva(LocalDateTime.now().minusHours(2));
        reserva.setDuracion(60);

        when(reservaRepository.findById(1)).thenReturn(Optional.of(reserva));

        assertThrows(IllegalStateException.class, () -> reservaService.cancelarReserva(1));
    }
}


