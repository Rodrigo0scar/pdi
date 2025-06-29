package com.utec.PDI;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.utec.dto.EspacioDTO;
import com.utec.mapper.EspacioMapper;
import com.utec.model.Espacio;
import com.utec.model.Estado;
import com.utec.repository.*;
import com.utec.service.EspacioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;



public class EspacioServiceTest {
    @Mock
    EspacioRepository espacioRepository;
    @InjectMocks
    EspacioService espacioService;

    @Mock
    private EstadoRepository estadoRepository;

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private ActividadRepository actividadRepository;

    @Mock
    private EspacioMapper espacioMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

        @Test
        void testCrearEspacio() {
            EspacioDTO dto = new EspacioDTO();
            Espacio espacio = new Espacio();
            Estado estado = new Estado();
            estado.setIdestado(2);

            when(espacioMapper.toEntity(dto)).thenReturn(espacio);
            when(estadoRepository.findById(2)).thenReturn(Optional.of(estado));
            when(espacioMapper.toDto(any(Espacio.class))).thenReturn(dto);

            EspacioDTO result = espacioService.crearEspacio(dto);

            verify(espacioRepository).save(espacio);
            assertEquals(dto, result);
        }

        @Test
        void testListarEspaciosporNombre() {
            String nombre = "Gimnasio";
            Espacio espacio = new Espacio();
            List<Espacio> lista = List.of(espacio);
            EspacioDTO dto = new EspacioDTO();

            when(espacioRepository.findByNombreContainingIgnoreCase(nombre)).thenReturn(lista);
            when(espacioMapper.toDto(any())).thenReturn(dto);

            List<EspacioDTO> result = espacioService.listarEspacios(nombre, null);

            assertEquals(1, result.size());
        }

        @Test
        void testListarEspaciosporEstado() {
            Integer estadoId = 1;
            Espacio espacio = new Espacio();
            List<Espacio> lista = List.of(espacio);
            EspacioDTO dto = new EspacioDTO();

            when(espacioRepository.findByEstado_Idestado(estadoId)).thenReturn(lista);
            when(espacioMapper.toDto(any())).thenReturn(dto);

            List<EspacioDTO> result = espacioService.listarEspacios(null, estadoId);

            assertEquals(1, result.size());
        }

        @Test
        void testActualizarEspacio() {
            Integer id = 1;
            EspacioDTO dto = EspacioDTO.builder()
                    .nombre("Cancha")
                    .capacidadMaxima(10)
                    .precioReservaSocio(BigDecimal.TEN)
                    .precioReservaNoSocio(BigDecimal.TEN)
                    .fechaVigenciaPrecio(LocalDate.now())
                    .observaciones("obs")
                    .idEstado(1)
                    .build();

            Espacio espacio = new Espacio();
            Estado estado = new Estado();

            when(espacioRepository.findById(id)).thenReturn(Optional.of(espacio));
            when(estadoRepository.findById(dto.getIdEstado())).thenReturn(Optional.of(estado));
            when(espacioMapper.toDto(any())).thenReturn(dto);

            EspacioDTO result = espacioService.actualizarEspacio(id, dto);

            assertEquals(dto, result);
            verify(espacioRepository).save(espacio);
        }

        @Test
        void testActivarEspacio() {
            Espacio espacio = new Espacio();
            espacio.setEstado(new Estado(2, "INACTIVO"));

            when(espacioRepository.findById(1)).thenReturn(Optional.of(espacio));
            when(estadoRepository.findById(1)).thenReturn(Optional.of(new Estado(1, "ACTIVO")));

            espacioService.cambiarEstadoEspacio(1, true);

            verify(espacioRepository).save(espacio);
            assertEquals(1, espacio.getEstado().getIdestado());
        }

        @Test
        void testCambiarEstadoEspacio_bajaConReservasActivas() {
            Espacio espacio = new Espacio();
            espacio.setEstado(new Estado(1, "ACTIVO"));

            when(espacioRepository.findById(1)).thenReturn(Optional.of(espacio));
            when(estadoRepository.findById(2)).thenReturn(Optional.of(new Estado(2, "INACTIVO")));
            when(reservaRepository.existsByEspacio_IdEspacioAndEstado_Idestado(1, 1)).thenReturn(true);

            Exception ex = assertThrows(IllegalStateException.class, () ->
                    espacioService.cambiarEstadoEspacio(1, false));

            assertTrue(ex.getMessage().contains("tiene reservas activas"));
        }

        @Test
        void testObtenerEspaciosDisponibles() {
            LocalDateTime inicio = LocalDateTime.now();
            int duracionHoras = 2;
            int duracionMin = duracionHoras * 60;
            LocalDateTime finEsperado = inicio.plusMinutes(duracionMin);
            int cantPersonas=40;

            Espacio espacio = new Espacio();
            List<Espacio> espacios = List.of(espacio);
            EspacioDTO dto = new EspacioDTO();

            when(espacioRepository.buscarEspaciosDisponibles(inicio, finEsperado,cantPersonas)).thenReturn(espacios);
            when(espacioMapper.toDto(any())).thenReturn(dto);

            //VER
            List<EspacioDTO> result = espacioService.obtenerEspaciosDisponibles(inicio, duracionHoras,cantPersonas);

            assertEquals(1, result.size());
        }
    }

