package com.utec.PDI;

import com.utec.dto.TipoActividadDTO;
import com.utec.mapper.TipoActividadMapper;
import com.utec.model.Estado;
import com.utec.model.TipoActividad;
import com.utec.repository.EstadoRepository;
import com.utec.repository.TipoActividadRepository;
import com.utec.service.TipoActividadService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TipoActividadServiceTest {
    @Mock
    private TipoActividadRepository tipoActividadRepository;

    @Mock
    private EstadoRepository estadoRepository;

    @Mock
    private TipoActividadMapper tipoActividadMapper;

    @Mock
    private TipoActividad tipoActividad;

    @Mock
    private TipoActividadDTO tipoActividadDTO;

    @InjectMocks
    private TipoActividadService tipoActividadService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tipoActividad = new TipoActividad();
        tipoActividad.setTipo("Deporte");
        tipoActividad.setDescripcion("Futbol");

        tipoActividadDTO = new TipoActividadDTO();
        tipoActividadDTO.setTipo("Deporte");
        tipoActividadDTO.setDescripcion("Futbol");
        tipoActividadDTO.setIdEstado(2);
    }

    @Test
    void testAgregarTipoActividad() {
        Estado estado = new Estado();
        estado.setIdestado(2);

        when(tipoActividadMapper.toEntity(tipoActividadDTO)).thenReturn(tipoActividad);
        when(estadoRepository.findById(2)).thenReturn(Optional.of(estado));

        TipoActividadDTO resultado = tipoActividadService.agregarTipoActividad(tipoActividadDTO);

        assertEquals("Deporte", resultado.getTipo());
        assertEquals("Futbol", resultado.getDescripcion());
        verify(tipoActividadRepository).save(tipoActividad);
    }

    //activar actividad
    @Test
    void testActivarTipoActividad() {
        TipoActividad tipo = new TipoActividad();
        Estado activo = new Estado();
        TipoActividadDTO dtoEsperado = new TipoActividadDTO();

        when(tipoActividadRepository.findById(1)).thenReturn(Optional.of(tipo));
        when(estadoRepository.findById(1)).thenReturn(Optional.of(activo));
        when(tipoActividadMapper.toDTO(tipo)).thenReturn(dtoEsperado);

        TipoActividadDTO resultado = tipoActividadService.activarTipoActividad(1);

        assertNotNull(resultado);
        verify(tipoActividadRepository).save(tipo);
    }

    @Test
    void testActivarTipoActividadNoExiste() {
        when(tipoActividadRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> tipoActividadService.activarTipoActividad(1));
    }

    //obtener todos
    @Test
    void testObtenerTodos() {
        TipoActividad tipo = new TipoActividad();
        TipoActividadDTO dto = new TipoActividadDTO();

        when(tipoActividadRepository.findAll()).thenReturn(List.of(tipo));
        when(tipoActividadMapper.toDTO(tipo)).thenReturn(dto);

        List<TipoActividadDTO> resultado = tipoActividadService.obtenerTodos();

        assertEquals(1, resultado.size());
        verify(tipoActividadRepository).findAll();
    }

    //obtener por id
    @Test
    void testObtenerPorId() {
        TipoActividad tipo = new TipoActividad();
        TipoActividadDTO dto = new TipoActividadDTO();

        when(tipoActividadRepository.findById(1)).thenReturn(Optional.of(tipo));
        when(tipoActividadMapper.toDTO(tipo)).thenReturn(dto);

        TipoActividadDTO resultado = tipoActividadService.obtenerPorId(1);

        assertNotNull(resultado);
        verify(tipoActividadRepository).findById(1);
    }

    @Test
    void testObtenerPorIdNoExiste() {
        when(tipoActividadRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> tipoActividadService.obtenerPorId(1));
    }

    //modificar tipo
    @Test
    void testModificarTipoActividad() {
        TipoActividad tipo = new TipoActividad();
        TipoActividadDTO dto = new TipoActividadDTO();
        dto.setTipo("Cultural");
        dto.setDescripcion("Actividades artísticas");

        when(tipoActividadRepository.findById(1)).thenReturn(Optional.of(tipo));

        TipoActividadDTO resultado = tipoActividadService.modificarTipoActividad(dto, 1);

        assertEquals("Cultural", resultado.getTipo());
        assertEquals("Actividades artísticas", resultado.getDescripcion());
        verify(tipoActividadRepository).save(tipo);
    }

    @Test
    void testModificarTipoActividadNoExiste() {
        TipoActividadDTO dto = new TipoActividadDTO();
        when(tipoActividadRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> tipoActividadService.modificarTipoActividad(dto, 99));
    }

    //baja logica
    @Test
    void testEliminarTipoActividad() {
        TipoActividad tipo = new TipoActividad();
        Estado estadoBaja = new Estado();
        estadoBaja.setIdestado(3);
        TipoActividadDTO dto = new TipoActividadDTO();
        dto.setComentariosBaja("No se dicta más");

        when(tipoActividadRepository.findById(1)).thenReturn(Optional.of(tipo));
        when(estadoRepository.findById(3)).thenReturn(Optional.of(estadoBaja));

        tipoActividadService.eliminarTipoActividad(dto, 1);

        verify(tipoActividadRepository).save(tipo);
    }

    @Test
    void testEliminarTipoActividadNoExiste() {
        TipoActividadDTO dto = new TipoActividadDTO();

        when(tipoActividadRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> tipoActividadService.eliminarTipoActividad(dto, 1));
    }

}
