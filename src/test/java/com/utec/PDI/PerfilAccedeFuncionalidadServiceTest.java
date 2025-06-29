package com.utec.PDI;


import com.utec.dto.PerfilAccedeFuncionalidadDTO;
import com.utec.model.*;
import com.utec.repository.*;
import com.utec.service.PerfilAccedeFuncionalidadService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PerfilAccedeFuncionalidadServiceTest {

    @InjectMocks
    private PerfilAccedeFuncionalidadService service;

    @Mock
    private PerfilAccedeFuncionalidadRepository perfilAccedeFuncionalidadRepository;

    @Mock
    private PerfilRepository perfilRepository;

    @Mock
    private FuncionalidadRepository funcionalidadRepository;

    @Mock
    private EstadoRepository estadoRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void asignarFuncionalidadesAPerfil_DatosInvalidos() {
        PerfilAccedeFuncionalidadDTO dto = new PerfilAccedeFuncionalidadDTO();
        dto.setIdPerfil(null);
        dto.setFuncionalidades(Collections.emptyList());

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> service.asignarFuncionalidadesAPerfil(dto));
        assertEquals("Perfil y funcionalidades son obligatorios.", thrown.getMessage());
    }

    @Test
    void asignarFuncionalidadesAPerfil_PerfilNoExiste() {
        PerfilAccedeFuncionalidadDTO dto = new PerfilAccedeFuncionalidadDTO();
        dto.setIdPerfil(1);
        dto.setFuncionalidades(List.of(10));

        when(perfilRepository.findById(1)).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class,
                () -> service.asignarFuncionalidadesAPerfil(dto));
        assertEquals("Perfil no encontrado.", thrown.getMessage());
    }

    @Test
    void asignarFuncionalidadesAPerfil() {
        PerfilAccedeFuncionalidadDTO dto = new PerfilAccedeFuncionalidadDTO();
        dto.setIdPerfil(1);
        dto.setFuncionalidades(List.of(10));

        Perfil perfil = new Perfil();
        perfil.setIdPerfil(1);
        when(perfilRepository.findById(1)).thenReturn(Optional.of(perfil));

        Funcionalidad funcionalidad = new Funcionalidad();
        funcionalidad.setId(10);
        when(funcionalidadRepository.findById(10)).thenReturn(Optional.of(funcionalidad));

        Estado estadoActivo = new Estado();
        estadoActivo.setIdestado(1);
        when(estadoRepository.findById(1)).thenReturn(Optional.of(estadoActivo));

        when(perfilAccedeFuncionalidadRepository.findById(any(PerfilAccedeFuncionalidadId.class))).thenReturn(Optional.empty());

        service.asignarFuncionalidadesAPerfil(dto);

        verify(perfilAccedeFuncionalidadRepository).save(argThat(relacion -> {
            System.out.println("relacion en verify: " + relacion.getId());
            if (relacion == null) return false;
            if (relacion.getId() == null) return false;
            if (relacion.getEstado() == null) return false;
            return relacion.getId().getIdPerfil().equals(dto.getIdPerfil()) &&
                    relacion.getId().getIdFuncionalidad().equals(dto.getFuncionalidades().get(0)) &&
                    relacion.getEstado().getIdestado() == 1;
        }));
    }

    @Test
    void asignarFuncionalidadesAPerfil_DebeReactivarRelacionExistente() {
        PerfilAccedeFuncionalidadDTO dto = new PerfilAccedeFuncionalidadDTO();
        dto.setIdPerfil(1);
        dto.setFuncionalidades(List.of(10));

        Perfil perfil = new Perfil();
        perfil.setIdPerfil(1);
        when(perfilRepository.findById(1)).thenReturn(Optional.of(perfil));

        Funcionalidad funcionalidad = new Funcionalidad();
        funcionalidad.setId(10);
        when(funcionalidadRepository.findById(10)).thenReturn(Optional.of(funcionalidad));

        Estado estadoActivo = new Estado();
        estadoActivo.setIdestado(1);
        when(estadoRepository.findById(1)).thenReturn(Optional.of(estadoActivo));

        PerfilAccedeFuncionalidadId id = new PerfilAccedeFuncionalidadId(1, 10);
        PerfilAccedeFuncionalidad relacionExistente = new PerfilAccedeFuncionalidad(id);
        Estado estadoInactivo = new Estado();
        estadoInactivo.setIdestado(2);
        relacionExistente.setEstado(estadoInactivo);

        when(perfilAccedeFuncionalidadRepository.findById(id)).thenReturn(Optional.of(relacionExistente));

        service.asignarFuncionalidadesAPerfil(dto);

        assertEquals(estadoActivo, relacionExistente.getEstado());
        verify(perfilAccedeFuncionalidadRepository, times(1)).save(relacionExistente);
    }

    @Test
    void asignarFuncionalidadesAPerfil_RelacionYaActiva() {
        PerfilAccedeFuncionalidadDTO dto = new PerfilAccedeFuncionalidadDTO();
        dto.setIdPerfil(1);
        dto.setFuncionalidades(List.of(10));

        Perfil perfil = new Perfil();
        perfil.setIdPerfil(1);
        when(perfilRepository.findById(1)).thenReturn(Optional.of(perfil));

        Funcionalidad funcionalidad = new Funcionalidad();
        funcionalidad.setId(10);
        when(funcionalidadRepository.findById(10)).thenReturn(Optional.of(funcionalidad));

        Estado estadoActivo = new Estado();
        estadoActivo.setIdestado(1);
        when(estadoRepository.findById(1)).thenReturn(Optional.of(estadoActivo));

        PerfilAccedeFuncionalidadId id = new PerfilAccedeFuncionalidadId(1, 10);
        PerfilAccedeFuncionalidad relacionExistente = new PerfilAccedeFuncionalidad(id);
        relacionExistente.setEstado(estadoActivo);

        when(perfilAccedeFuncionalidadRepository.findById(id)).thenReturn(Optional.of(relacionExistente));

        IllegalStateException thrown = assertThrows(IllegalStateException.class,
                () -> service.asignarFuncionalidadesAPerfil(dto));
        assertTrue(thrown.getMessage().contains("ya está asignada"));
    }


    // --- Tests para desvincularFuncionalidadesDePerfil ---

    @Test
    void desvincularFuncionalidadesDePerfil_DatosInvalidos() {
        PerfilAccedeFuncionalidadDTO dto = new PerfilAccedeFuncionalidadDTO();
        dto.setIdPerfil(null);
        dto.setFuncionalidades(Collections.emptyList());

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> service.desvincularFuncionalidadesDePerfil(dto));
        assertEquals("Perfil y funcionalidades son obligatorios.", thrown.getMessage());
    }

    @Test
    void desvincularFuncionalidadesDePerfil_RelacionNoExiste() {
        PerfilAccedeFuncionalidadDTO dto = new PerfilAccedeFuncionalidadDTO();
        dto.setIdPerfil(1);
        dto.setFuncionalidades(List.of(10));

        Estado estadoInactivo = new Estado();
        estadoInactivo.setIdestado(2);
        when(estadoRepository.findById(2)).thenReturn(Optional.of(estadoInactivo));

        Perfil perfil = new Perfil();
        perfil.setIdPerfil(1);
        when(perfilRepository.findById(1)).thenReturn(Optional.of(perfil));

        Funcionalidad funcionalidad = new Funcionalidad();
        funcionalidad.setId(10);
        when(funcionalidadRepository.findById(10)).thenReturn(Optional.of(funcionalidad));

        PerfilAccedeFuncionalidadId id = new PerfilAccedeFuncionalidadId(1, 10);
        when(perfilAccedeFuncionalidadRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class,
                () -> service.desvincularFuncionalidadesDePerfil(dto));

        assertTrue(thrown.getMessage().contains("No existe relación entre perfil y funcionalidad."));
    }

    @Test
    void desvincularFuncionalidadesDePerfil() {
        PerfilAccedeFuncionalidadDTO dto = new PerfilAccedeFuncionalidadDTO();
        dto.setIdPerfil(1);
        dto.setFuncionalidades(List.of(10));

        Perfil perfil = new Perfil();
        perfil.setIdPerfil(1);
        when(perfilRepository.findById(1)).thenReturn(Optional.of(perfil));

        Funcionalidad funcionalidad = new Funcionalidad();
        funcionalidad.setId(10);
        when(funcionalidadRepository.findById(10)).thenReturn(Optional.of(funcionalidad));

        Estado estadoInactivo = new Estado();
        estadoInactivo.setIdestado(2);
        when(estadoRepository.findById(2)).thenReturn(Optional.of(estadoInactivo));

        PerfilAccedeFuncionalidadId id = new PerfilAccedeFuncionalidadId(1, 10);
        PerfilAccedeFuncionalidad relacionExistente = new PerfilAccedeFuncionalidad(id);
        Estado estadoActivo = new Estado();
        estadoActivo.setIdestado(1);
        relacionExistente.setEstado(estadoActivo);

        when(perfilAccedeFuncionalidadRepository.findById(id)).thenReturn(Optional.of(relacionExistente));

        service.desvincularFuncionalidadesDePerfil(dto);

        assertEquals(estadoInactivo, relacionExistente.getEstado());
        verify(perfilAccedeFuncionalidadRepository, times(1)).save(relacionExistente);
    }

    @Test
    void desvincularFuncionalidadesDePerfil_RelacionYaInactiva() {
        PerfilAccedeFuncionalidadDTO dto = new PerfilAccedeFuncionalidadDTO();
        dto.setIdPerfil(1);
        dto.setFuncionalidades(List.of(10));

        Perfil perfil = new Perfil();
        perfil.setIdPerfil(1);
        when(perfilRepository.findById(1)).thenReturn(Optional.of(perfil));

        Funcionalidad funcionalidad = new Funcionalidad();
        funcionalidad.setId(10);
        when(funcionalidadRepository.findById(10)).thenReturn(Optional.of(funcionalidad));

        Estado estadoInactivo = new Estado();
        estadoInactivo.setIdestado(2);
        when(estadoRepository.findById(2)).thenReturn(Optional.of(estadoInactivo));

        PerfilAccedeFuncionalidadId id = new PerfilAccedeFuncionalidadId(1, 10);
        PerfilAccedeFuncionalidad relacionExistente = new PerfilAccedeFuncionalidad(id);
        relacionExistente.setEstado(estadoInactivo);

        when(perfilAccedeFuncionalidadRepository.findById(id)).thenReturn(Optional.of(relacionExistente));

        IllegalStateException thrown = assertThrows(IllegalStateException.class,
                () -> service.desvincularFuncionalidadesDePerfil(dto));
        assertTrue(thrown.getMessage().contains("ya está desvinculada"));
    }
}

