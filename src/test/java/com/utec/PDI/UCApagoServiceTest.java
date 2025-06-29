package com.utec.PDI;

import com.utec.dto.UsuarioConcurreActividadDTO;
import com.utec.mapper.UsuarioConcurreActividadMapper;
import com.utec.model.UsuarioConcurreActividadId;
import com.utec.repository.UsuarioConcurreActividadRepository;
import com.utec.service.UsuarioConcurreActividadService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class UCApagoServiceTest {


    @Mock
    private UsuarioConcurreActividadRepository repository;

    @Mock
    private UsuarioConcurreActividadMapper mapper;

    @InjectMocks
    private UsuarioConcurreActividadService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void actualizarPagoActividad_relacionNoExiste_lanzaExcepcion() {
        // Arrange
        Integer idUsuario = 1;
        Integer idActividad = 99;
        UsuarioConcurreActividadId id = new UsuarioConcurreActividadId(idUsuario, idActividad);

        UsuarioConcurreActividadDTO dto = UsuarioConcurreActividadDTO.builder()
                .idUsuario(idUsuario)
                .idActividad(idActividad)
                .build();

        when(repository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            service.actualizarPagoActividad(dto);
        });

        assertEquals("No se encontro la relacion usuario-actividad", ex.getMessage());
    }



}
