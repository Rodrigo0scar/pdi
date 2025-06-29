package com.utec.PDI;

import com.utec.dto.FiltroReportePorTipoActividadDTO;
import com.utec.dto.ReportePorTipoActividadDTO;
import com.utec.dto.ReporteTipoActDTO;
import com.utec.dto.UsuarioConcurreActividadDTO;
import com.utec.mapper.UsuarioConcurreActividadMapper;
import com.utec.model.Actividad;
import com.utec.model.Usuario;
import com.utec.model.UsuarioConcurreActividad;
import com.utec.repository.ActividadRepository;
import com.utec.repository.UsuarioConcurreActividadRepository;
import com.utec.repository.UsuarioRepository;
import com.utec.service.UsuarioConcurreActividadService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UsuarioConcurreActividadServiceTest {

    @Mock
    private UsuarioConcurreActividadRepository usuarioConcurreActividadRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ActividadRepository actividadRepository;

    @Mock
    private UsuarioConcurreActividadMapper usuarioConcurreActividadMapper;

    @InjectMocks
    private UsuarioConcurreActividadService usuarioConcurreActividadService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testInscribirUsuario_ok() {
        UsuarioConcurreActividadDTO dto = new UsuarioConcurreActividadDTO();
        dto.setIdUsuario(1);
        dto.setIdActividad(2);
        dto.setAsistencia(false);

        Usuario usuario = new Usuario();
        usuario.setId(1);
        Actividad actividad = new Actividad();
        actividad.setId(2);

        when(usuarioConcurreActividadRepository.existsByUsuarioIdAndActividadId(1, 2)).thenReturn(false);
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(actividadRepository.findById(2)).thenReturn(Optional.of(actividad));
        when(usuarioConcurreActividadMapper.toEntity(dto, usuario, actividad))
                .thenAnswer(invocation -> {
                    UsuarioConcurreActividadDTO d = invocation.getArgument(0);
                    Usuario u = invocation.getArgument(1);
                    Actividad a = invocation.getArgument(2);
                    UsuarioConcurreActividad entidad = new UsuarioConcurreActividad();
                    entidad.setUsuario(u);
                    entidad.setActividad(a);
                    entidad.setAsistencia(d.getAsistencia());//cambio pago
                    return entidad;
                });

        usuarioConcurreActividadService.inscribirUsuario(1, 2, dto);

        verify(usuarioConcurreActividadRepository).save(any(UsuarioConcurreActividad.class));
    }

    @Test
    void testInscribirUsuario_yaInscripto_lanzaExcepcion() {
        when(usuarioConcurreActividadRepository.existsByUsuarioIdAndActividadId(1, 2)).thenReturn(true);

        UsuarioConcurreActividadDTO dto = new UsuarioConcurreActividadDTO();
        dto.setIdUsuario(1);
        dto.setIdActividad(2);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            usuarioConcurreActividadService.inscribirUsuario(1, 2, dto);
        });

        assertEquals("El usuario ya está inscrito en esta actividad.", ex.getMessage());
        verify(usuarioConcurreActividadRepository, never()).save(any());
    }

//    @Test
//    void testCancelarInscripcion_ok() {
//        UsuarioConcurreActividad inscripcion = new UsuarioConcurreActividad();
//        when(usuarioConcurreActividadRepository.findByUsuarioIdAndActividadId(1, 2))
//                .thenReturn(Optional.of(inscripcion));
//
//        usuarioConcurreActividadService.cancelarInscripcion(1, 2);
//
//        verify(usuarioConcurreActividadRepository).delete(inscripcion);
//    }

//    @Test
//    void testCancelarInscripcion_noExiste_lanzaExcepcion() {
//        when(usuarioConcurreActividadRepository.findByUsuarioIdAndActividadId(1, 2))
//                .thenReturn(Optional.empty());
//
//        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
//            usuarioConcurreActividadService.cancelarInscripcion(1, 2);
//        });
//
//        assertEquals("Inscripción no encontrado", ex.getMessage());
//        verify(usuarioConcurreActividadRepository, never()).delete(any());
//    }

    @Test
    void testObtenerReportePorTipoActividad_ok() {

        LocalDateTime desde = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime hasta = LocalDateTime.of(2024, 12, 31, 23, 59);
        List<String> tipos = List.of("Deporte");

        FiltroReportePorTipoActividadDTO filtro = new FiltroReportePorTipoActividadDTO();
        filtro.setFechaDesde(desde);
        filtro.setFechaHasta(hasta);
        filtro.setTiposActividad(tipos);

        ReportePorTipoActividadDTO dato1 = new ReportePorTipoActividadDTO();
        dato1.setCantidadInscriptos(5);
        dato1.setCantidadCancelados(2);

        List<ReportePorTipoActividadDTO> mockResultado = List.of(dato1);

        when(usuarioConcurreActividadRepository.obtenerReportePorTipoActividad(
                desde.toLocalDate(), hasta.toLocalDate(), tipos))
                .thenReturn(mockResultado);

        ReporteTipoActDTO resultado = usuarioConcurreActividadService.obtenerReportePorTipoActividad(filtro);

        assertNotNull(resultado);
        assertEquals(5, resultado.getTotalInscriptos());
        assertEquals(2, resultado.getTotalCancelados());

        List<ReportePorTipoActividadDTO> reporteDatos = resultado.getReportePorTipoActividad();
        assertEquals(1, reporteDatos.size());
        assertEquals(5, reporteDatos.get(0).getCantidadInscriptos());
        assertEquals(2, reporteDatos.get(0).getCantidadCancelados());

        verify(usuarioConcurreActividadRepository).obtenerReportePorTipoActividad(
                desde.toLocalDate(), hasta.toLocalDate(), tipos);
    }


}
