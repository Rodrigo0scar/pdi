package com.utec.PDI;

import com.utec.dto.ActividadDTO;
import com.utec.dto.GetActividadDTO;
import com.utec.mapper.ActividadMapper;
import com.utec.model.*;
import com.utec.repository.*;
import com.utec.service.ActividadService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ActividadServiceTest {

    @InjectMocks
    private ActividadService actividadService;

    @Mock
    private ActividadRepository actividadRepository;

    @Mock
    private ActividadMapper actividadMapper;

    @Mock
    private TipoActividadRepository tipoActividadRepository;

    @Mock
    private EspacioRepository espacioRepository;

    @Mock
    private ModoPagoRepository modoPagoRepository;

    @Mock
    private EstadoRepository estadoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //CREAR ACTIVIDADES

    @Test
    void testCrearActividad() throws Exception {

        // Arrange
        ActividadDTO dto = new ActividadDTO();
        dto.setIdTipoActividad(1);
        dto.setIdEspacio(2);
        dto.setIdModoPago(3);
        dto.setIdEstado(4);
        dto.setIdUsuario(5);

        TipoActividad tipo = new TipoActividad();
        Espacio espacio = new Espacio();
        ModoPago modoPago = new ModoPago();
        Estado estado = new Estado();
        Usuario usuario = new Usuario();

        Actividad actividad = new Actividad();
        Actividad actividadGuardada = new Actividad();
        ActividadDTO dtoResultado = new ActividadDTO();
        dtoResultado.setId(10);

        when(tipoActividadRepository.findById(1)).thenReturn(Optional.of(tipo));
        when(espacioRepository.findById(2)).thenReturn(Optional.of(espacio));
        when(modoPagoRepository.findById(3)).thenReturn(Optional.of(modoPago));
        when(estadoRepository.findById(4)).thenReturn(Optional.of(estado));
        when(usuarioRepository.findById(5)).thenReturn(Optional.of(usuario));
        when(actividadMapper.toEntity(dto)).thenReturn(actividad);
        when(actividadRepository.save(actividad)).thenReturn(actividadGuardada);
        when(actividadMapper.toDto(actividadGuardada)).thenReturn(dtoResultado);

        // Act
        ActividadDTO resultado = actividadService.crearActividad(dto);

        // Assert
        assertNotNull(resultado);
        assertEquals(10, resultado.getId());

        verify(actividadRepository).save(actividad);
        verify(tipoActividadRepository).findById(1);
        verify(espacioRepository).findById(2);
        verify(modoPagoRepository).findById(3);
        verify(estadoRepository).findById(4);
        verify(usuarioRepository).findById(5);
    }

    @Test
    void testCrearActividadTipoNoExiste() {
        ActividadDTO dto = new ActividadDTO();
        dto.setIdTipoActividad(1);

        when(tipoActividadRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> actividadService.crearActividad(dto));
    }

    @Test
    void testCrearActividadEspacioNoExiste() {
        ActividadDTO dto = new ActividadDTO();
        dto.setIdTipoActividad(1);
        dto.setIdEspacio(2);

        when(tipoActividadRepository.findById(1)).thenReturn(Optional.of(new TipoActividad()));
        when(espacioRepository.findById(2)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> actividadService.crearActividad(dto));
    }

    @Test
    void testCrearActividadModoPagoNoExiste() {
        ActividadDTO dto = new ActividadDTO();
        dto.setIdTipoActividad(1);
        dto.setIdEspacio(2);
        dto.setIdModoPago(3);

        when(tipoActividadRepository.findById(1)).thenReturn(Optional.of(new TipoActividad()));
        when(espacioRepository.findById(2)).thenReturn(Optional.of(new Espacio()));
        when(modoPagoRepository.findById(3)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> actividadService.crearActividad(dto));
    }

    @Test
    void testCrearActividadEstadoNoExiste() {
        ActividadDTO dto = new ActividadDTO();
        dto.setIdTipoActividad(1);
        dto.setIdEspacio(2);
        dto.setIdModoPago(3);
        dto.setIdEstado(4);

        when(tipoActividadRepository.findById(1)).thenReturn(Optional.of(new TipoActividad()));
        when(espacioRepository.findById(2)).thenReturn(Optional.of(new Espacio()));
        when(modoPagoRepository.findById(3)).thenReturn(Optional.of(new ModoPago()));
        when(estadoRepository.findById(4)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> actividadService.crearActividad(dto));
    }

    //OBTENER ACTIVIDADES
    @Test
    void testObtenerActividades() {
        // Arrange
        Actividad actividad1 = new Actividad();
        actividad1.setId(1);
        Actividad actividad2 = new Actividad();
        actividad2.setId(2);

        List<Actividad> lista = List.of(actividad1, actividad2);

        ActividadDTO dto1 = new ActividadDTO();
        dto1.setId(1);
        ActividadDTO dto2 = new ActividadDTO();
        dto2.setId(2);

        when(actividadRepository.findAll()).thenReturn(lista);
        when(actividadMapper.toDto(actividad1)).thenReturn(dto1);
        when(actividadMapper.toDto(actividad2)).thenReturn(dto2);

        // Act
        List<ActividadDTO> resultado = actividadService.ObtenerActividades();

        // Assert
        assertEquals(2, resultado.size());
        assertEquals(1, resultado.get(0).getId());
        assertEquals(2, resultado.get(1).getId());

        verify(actividadRepository).findAll();
        verify(actividadMapper, times(1)).toDto(actividad1);
        verify(actividadMapper, times(1)).toDto(actividad2);
    }

    //obtener por id
    @Test
    void testObtenerPorId() {
        Actividad actividad = new Actividad();
        ActividadDTO dto = new ActividadDTO();
        dto.setId(1);

        when(actividadRepository.findById(1)).thenReturn(Optional.of(actividad));
        when(actividadMapper.toDto(actividad)).thenReturn(dto);

        ActividadDTO resultado = actividadService.obtenerPorId(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        verify(actividadRepository).findById(1);
    }

    // con un id que no exista
    @Test
    void testObtenerPorIdNoExiste() {
        when(actividadRepository.findById(99)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> actividadService.obtenerPorId(99));

        assertTrue(ex.getMessage().contains("Actividad no encontrado con ID"));
    }

    //listar actividades
    @Test
    void testListarActividadesParaInscribirse() {
        Actividad actividad = new Actividad();
        List<Actividad> lista = List.of(actividad, actividad);

        GetActividadDTO dto = new GetActividadDTO();

        when(actividadRepository.findActividadesActivasParaInscripcionWithRelations()).thenReturn(lista);
        when(actividadMapper.toGetActividadDTO(any(Actividad.class))).thenReturn(dto);

        List<GetActividadDTO> resultado = actividadService.listarActividadesParaInscribirse();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(actividadMapper, times(2)).toGetActividadDTO(any(Actividad.class));
    }


    //actualizar actividad
    @Test
    void testActualizarActividad() {
        ActividadDTO dto = new ActividadDTO();
        dto.setId(1);
        dto.setNombre("Nueva actividad");
        dto.setIdTipoActividad(1);
        dto.setIdEstado(2);
        dto.setIdEspacio(3);
        dto.setIdModoPago(4);
        dto.setIdUsuario(5);

        Actividad actividadExistente = new Actividad();
        Actividad actualizado = new Actividad();
        ActividadDTO dtoResultado = new ActividadDTO();

        when(actividadRepository.findById(1)).thenReturn(Optional.of(actividadExistente));
        when(tipoActividadRepository.findById(1)).thenReturn(Optional.of(new TipoActividad()));
        when(estadoRepository.findById(2)).thenReturn(Optional.of(new Estado()));
        when(espacioRepository.findById(3)).thenReturn(Optional.of(new Espacio()));
        when(modoPagoRepository.findById(4)).thenReturn(Optional.of(new ModoPago()));
        when(usuarioRepository.findById(5)).thenReturn(Optional.of(new Usuario()));
        when(actividadRepository.save(actividadExistente)).thenReturn(actualizado);
        when(actividadMapper.toDto(actualizado)).thenReturn(dtoResultado);

        ActividadDTO resultado = actividadService.actualizarActividad(dto);

        assertNotNull(resultado);
        verify(actividadRepository).save(actividadExistente);
    }

    //actualizar actividad que no exista
    @Test
    void testActualizarActividadNoExiste() {
        ActividadDTO dto = new ActividadDTO();
        dto.setId(99);

        when(actividadRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> actividadService.actualizarActividad(dto));
    }

    //eliminar actividad
//    @Test
//    void testEliminarActividad() {
//        Actividad actividad = new Actividad();
//
//        when(actividadRepository.findById(1)).thenReturn(Optional.of(actividad));
//
//        actividadService.eliminarActividad(1);
//
//        verify(actividadRepository).delete(actividad);
//    }

//    //eliminar actividad que no existe
//    @Test
//    void testEliminarActividadNoExiste() {
//        when(actividadRepository.findById(99)).thenReturn(Optional.empty());
//
//        assertThrows(ResponseStatusException.class, () -> actividadService.eliminarActividad(99));
//    }


}
