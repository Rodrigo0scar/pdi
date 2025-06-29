package com.utec.PDI;

import com.utec.dto.FuncionalidadDTO;
import com.utec.mapper.FuncionalidadMapper;
import com.utec.model.Estado;
import com.utec.model.Funcionalidad;
import com.utec.repository.EstadoRepository;
import com.utec.repository.FuncionalidadRepository;
import com.utec.repository.PerfilAccedeFuncionalidadRepository;
import com.utec.service.FuncionalidadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class FuncionalidadServiceTest {
    @InjectMocks
    private FuncionalidadService service;

    @Mock
    private FuncionalidadRepository funcionalidadRepository;

    @Mock
    private PerfilAccedeFuncionalidadRepository perfilAccedeFuncionalidadRepository;

    @Mock
    private EstadoRepository estadoRepository;

    @Mock
    private FuncionalidadMapper funcionalidadMapper;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void crearFuncionalidad() {
        FuncionalidadDTO dto = new FuncionalidadDTO();
        dto.setNombre("Nueva");
        dto.setDescripcion("Descripción");

        Estado estado = new Estado();
        estado.setIdestado(1);

        when(funcionalidadRepository.existsByNombreIgnoreCase("Nueva")).thenReturn(false);
        when(estadoRepository.findById(1)).thenReturn(Optional.of(estado));

        Funcionalidad funcionalidadGuardada = new Funcionalidad();
        funcionalidadGuardada.setId(1);
        funcionalidadGuardada.setNombre("Nueva");
        funcionalidadGuardada.setDescripcion("Descripción");
        funcionalidadGuardada.setEstado(estado);

        when(funcionalidadRepository.save(any())).thenReturn(funcionalidadGuardada);

        FuncionalidadDTO resultado = service.crearFuncionalidad(dto);

        assertEquals("Nueva", resultado.getNombre());
        assertEquals("Descripción", resultado.getDescripcion());
        assertEquals(1, resultado.getEstado().getIdestado());
    }

    @Test
    void crearFuncionalidad_YaExiste() {
        FuncionalidadDTO dto = new FuncionalidadDTO();
        dto.setNombre("Duplicada");

        when(funcionalidadRepository.existsByNombreIgnoreCase("Duplicada")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> service.crearFuncionalidad(dto));
    }


    @Test
    void modificarFuncionalidad() {
        Funcionalidad funcionalidad = new Funcionalidad();
        funcionalidad.setId(1);
        funcionalidad.setNombre("Original");
        funcionalidad.setDescripcion("Vieja");

        FuncionalidadDTO dto = new FuncionalidadDTO();
        dto.setNombre("Original");
        dto.setDescripcion("Nueva desc");

        when(funcionalidadRepository.findById(1)).thenReturn(Optional.of(funcionalidad));
        when(funcionalidadRepository.save(any())).thenReturn(funcionalidad);
        when(funcionalidadMapper.toDto(any())).thenReturn(dto);

        FuncionalidadDTO resultado = service.modificarFuncionalidad(1, dto);

        assertEquals("Nueva desc", resultado.getDescripcion());
    }

    @Test
    void modificarFuncionalidad_CambiarNombre() {
        Funcionalidad funcionalidad = new Funcionalidad();
        funcionalidad.setId(1);
        funcionalidad.setNombre("NombreOriginal");

        FuncionalidadDTO dto = new FuncionalidadDTO();
        dto.setNombre("OtroNombre");
        dto.setDescripcion("desc");

        when(funcionalidadRepository.findById(1)).thenReturn(Optional.of(funcionalidad));

        assertThrows(IllegalArgumentException.class, () -> service.modificarFuncionalidad(1, dto));
    }


    @Test
    void cambiarEstadoFuncionalidad_DeActivoAInactivo() {
        Funcionalidad funcionalidad = new Funcionalidad();
        funcionalidad.setId(1);
        Estado estadoActual = new Estado();
        estadoActual.setIdestado(1); // activo
        funcionalidad.setEstado(estadoActual);

        Estado estadoInactivo = new Estado();
        estadoInactivo.setIdestado(2);

        when(funcionalidadRepository.findById(1)).thenReturn(Optional.of(funcionalidad));
        when(perfilAccedeFuncionalidadRepository.existsByFuncionalidad_Id(1)).thenReturn(false);
        when(estadoRepository.findById(2)).thenReturn(Optional.of(estadoInactivo));

        service.cambiarEstadoFuncionalidad(1);

        assertEquals(2, funcionalidad.getEstado().getIdestado());
    }

    @Test
    void cambiarEstadoFuncionalidad_AsignadaARol() {
        Funcionalidad funcionalidad = new Funcionalidad();
        funcionalidad.setId(1);
        Estado estado = new Estado();
        estado.setIdestado(1); // activo
        funcionalidad.setEstado(estado);

        when(funcionalidadRepository.findById(1)).thenReturn(Optional.of(funcionalidad));
        when(perfilAccedeFuncionalidadRepository.existsByFuncionalidad_Id(1)).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> service.cambiarEstadoFuncionalidad(1));
    }

    @Test
    void cambiarEstadoFuncionalidad_DeInactivoAActivo() {
        Funcionalidad funcionalidad = new Funcionalidad();
        funcionalidad.setId(1);
        Estado estado = new Estado();
        estado.setIdestado(2); // inactivo
        funcionalidad.setEstado(estado);

        Estado nuevoEstado = new Estado();
        nuevoEstado.setIdestado(1);

        when(funcionalidadRepository.findById(1)).thenReturn(Optional.of(funcionalidad));
        when(estadoRepository.findById(1)).thenReturn(Optional.of(nuevoEstado));

        service.cambiarEstadoFuncionalidad(1);

        assertEquals(1, funcionalidad.getEstado().getIdestado());
    }

}
