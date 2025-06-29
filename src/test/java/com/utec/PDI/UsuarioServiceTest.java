package com.utec.PDI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.utec.dto.EstadoDTO;
import com.utec.dto.UsuarioDTO;
import com.utec.dto.UsuarioDatosPropiosDTO;
import com.utec.dto.UsuarioResponseDTO;
import com.utec.mapper.EstadoMapper;
import com.utec.mapper.UsuarioMapper;
import com.utec.mapper.UsuarioResponseMapper;
import com.utec.model.Estado;
import com.utec.model.Perfil;
import com.utec.model.TipoDocumento;
import com.utec.repository.EstadoRepository;
import com.utec.repository.PerfilRepository;
import com.utec.repository.TipoDocumentoRepository;
import com.utec.service.UsuarioService;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.utec.model.Usuario;
import com.utec.repository.UsuarioRepository;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.crypto.password.PasswordEncoder;

//@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private UsuarioMapper usuarioMapper;

    @Mock
    private UsuarioResponseMapper usuarioResponseMapper;

    @Mock
    private EstadoMapper estadoMapper;
    @Mock
    private EstadoRepository estadoRepository;

    @Mock
    private TipoDocumentoRepository tipoDocumentoRepository;

    @Mock
    private PerfilRepository perfilRepository;

    @Mock
    private PasswordEncoder passwordEncoder;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // TODO: BORRRAR ESTE TEST LUEGO DE REARMAR LOS DE SOCIO Y ADMINISTRADOR
    // @Test
    // void testCrearUsuario() throws ChangeSetPersister.NotFoundException, BadRequestException {
    //     UsuarioDTO usuarioDto = new UsuarioDTO();
    //     usuarioDto.setIdTipoDocumento(1);
    //     usuarioDto.setNumeDocumento("123456789");
    //     usuarioDto.setPrimNombre("Juan");
    //     usuarioDto.setSeguNombre("Pedro");
    //     usuarioDto.setPrimApellido("Perez");
    //     usuarioDto.setSeguApellido("Perez");
    //     usuarioDto.setContrasenia("123456");
    //     usuarioDto.setCorreo("admin@asur.com");
    //     LocalDate fecha = LocalDate.of(1995, 5, 10);
    //     usuarioDto.setFechNacimiento(fecha);
    //     usuarioDto.setCalle("Mercedes");
    //     usuarioDto.setNumePuerta(1080);
    //     usuarioDto.setApartamento("PB");
    //     usuarioDto.setIdEstado(1);
    //     usuarioDto.setIdPerfil(2);

    //     Estado estado = new Estado();
    //     TipoDocumento tipoDocumento = new TipoDocumento();
    //     Perfil perfil = new Perfil();

    //     Usuario usuario = new Usuario();
    //     usuario.setContrasenia("123456");
    //     usuario.setFechNacimiento(usuarioDto.getFechNacimiento());
    //     usuario.setPrimNombre("Juan");
    //     usuario.setPrimApellido("Perez");

    //     when(usuarioMapper.toEntity(usuarioDto)).thenReturn(usuario);
    //     when(estadoRepository.findById(1)).thenReturn(Optional.of(estado));
    //     when(tipoDocumentoRepository.findById(1)).thenReturn(Optional.of(tipoDocumento));
    //     when(perfilRepository.findById(2)).thenReturn(Optional.of(perfil));
    //     when(usuarioRepository.findByTipoDocumentoAndNumeDocumento(tipoDocumento, "123456789")).thenReturn(Optional.empty());
    //     when(usuarioRepository.findByCorreo("admin@asur.com")).thenReturn(Optional.empty());
    //     when(passwordEncoder.encode("123456")).thenReturn("encodedPassword");
    //     when(usuarioRepository.save(usuario)).thenReturn(usuario);
    //     when(usuarioMapper.toDTO(usuario)).thenReturn(usuarioDto);

    //     UsuarioDTO creado = usuarioService.crearUsuario(usuarioDto);

    //     assertThat(creado).isNotNull();
    //     assertThat(creado.getFechNacimiento()).isEqualTo(LocalDate.of(1995, 5, 10));
    //     assertThat(creado.getContrasenia()).isEqualTo("encodedPassword");
    //     assertThat(usuario.getEstado()).isEqualTo(estado);
    //     assertThat(usuario.getPerfil()).isEqualTo(perfil);
    //     assertThat(usuario.getTipoDocumento()).isEqualTo(tipoDocumento);
    //     assertThat(usuario.getPrimNombre()).isEqualTo(usuarioDto.getPrimNombre());
    //     assertThat(usuario.getPrimApellido()).isEqualTo(usuarioDto.getPrimApellido());
    // }


    @Test
    void testObtenerUsuNombre() {
    //para buscar
        String primerNombre = "Juan";

        Usuario usuario1 = new Usuario();
        usuario1.setId(1);
        usuario1.setPrimNombre("Juan");
        usuario1.setPrimApellido("Perez");
        usuario1.setCorreo("juan1@email.com");

        Usuario usuario2 = new Usuario();
        usuario2.setId(2);
        usuario2.setPrimNombre("Juan");
        usuario2.setPrimApellido("Rodriguez");
        usuario2.setCorreo("juan2@email.com");

        List<Usuario> usuariosEncontrados = Arrays.asList(usuario1, usuario2);

        UsuarioDTO dto1 = new UsuarioDTO();
        //dto1.setId(1);
        dto1.setPrimNombre("Juan");
        dto1.setPrimApellido("Perez");
        dto1.setCorreo("juan1@email.com");

        UsuarioDTO dto2 = new UsuarioDTO();
        //dto2.setId(2);
        dto2.setPrimNombre("Juan");
        dto2.setPrimApellido("Rodriguez");
        dto2.setCorreo("juan2@email.com");


        when(usuarioRepository.findByPrimNombre(primerNombre)).thenReturn(usuariosEncontrados);
        when(usuarioMapper.toDTO(usuario1)).thenReturn(dto1);
        when(usuarioMapper.toDTO(usuario2)).thenReturn(dto2);


        List<UsuarioDTO> resultado = usuarioService.obtenerUsuNombre(primerNombre);


        assertThat(resultado).isNotNull();
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getPrimNombre()).isEqualTo("Juan");
        assertThat(resultado.get(0).getPrimApellido()).isEqualTo("Perez");
        assertThat(resultado.get(1).getPrimNombre()).isEqualTo("Juan");
        assertThat(resultado.get(1).getPrimApellido()).isEqualTo("Rodriguez");


        verify(usuarioRepository).findByPrimNombre(primerNombre);
        verify(usuarioMapper).toDTO(usuario1);
        verify(usuarioMapper).toDTO(usuario2);
    }

    @Test
    void testObtenerUsuApellido() {
        //para buscar
        String primerApellido = "Martinez";

        Usuario usuario1 = new Usuario();
        usuario1.setId(1);
        usuario1.setPrimNombre("Juan");
        usuario1.setPrimApellido("Perez");
        usuario1.setCorreo("juan@asur.com");

        Usuario usuario2 = new Usuario();
        usuario2.setId(2);
        usuario2.setPrimNombre("Pedro");
        usuario2.setPrimApellido("Martinez");
        usuario2.setCorreo("pedro@gmail.com");
//hago lista como que los hubiera traido de la base
        List<Usuario> usuariosEncontrados = Arrays.asList(usuario1, usuario2);

        UsuarioDTO dto1 = new UsuarioDTO();
        //dto1.setId(1);
        dto1.setPrimNombre("Juan");
        dto1.setPrimApellido("Perez");
        dto1.setCorreo("juan@asur.com");

        UsuarioDTO dto2 = new UsuarioDTO();
        //dto2.setId(2);
        dto2.setPrimNombre("Pedro");
        dto2.setPrimApellido("Martinez");
        dto2.setCorreo("pedro@gmail.com");


        when(usuarioRepository.findByPrimApellido(primerApellido)).thenReturn(usuariosEncontrados);
        when(usuarioMapper.toDTO(usuario1)).thenReturn(dto1);
        when(usuarioMapper.toDTO(usuario2)).thenReturn(dto2);


        List<UsuarioDTO> resultado = usuarioService.obtenerUsuApellido(primerApellido);


        assertThat(resultado).isNotNull();
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getPrimNombre()).isEqualTo("Juan");
        assertThat(resultado.get(0).getPrimApellido()).isEqualTo("Perez");
        assertThat(resultado.get(1).getPrimNombre()).isEqualTo("Pedro");
        assertThat(resultado.get(1).getPrimApellido()).isEqualTo("Martinez");

       // verify(usuarioRepository).findByPrimApellido(primerApellido);
        //verify(usuarioMapper).toDTO(usuario1);
        //verify(usuarioMapper).toDTO(usuario2);

    }

    @Test
    void testObtenerUsuEstado() {
        Estado estado = new Estado();
        estado.setIdestado(1);
        estado.setDescripcion("Activo");

        Usuario usuario1 = new Usuario();
        usuario1.setId(1);
        usuario1.setPrimNombre("Ana");
        usuario1.setPrimApellido("Gomez");
        usuario1.setCorreo("ana@asur.com");
        usuario1.setEstado(estado);

        Usuario usuario2 = new Usuario();
        usuario2.setId(2);
        usuario2.setPrimNombre("Luis");
        usuario2.setPrimApellido("Martinez");
        usuario2.setCorreo("luis@gmail.com");
        usuario2.setEstado(estado);

        UsuarioDTO dto1 = new UsuarioDTO();
        dto1.setPrimNombre("Ana");
        dto1.setPrimApellido("Gomez");
        dto1.setCorreo("ana@asur.com");

        UsuarioDTO dto2 = new UsuarioDTO();
        dto2.setPrimNombre("Luis");
        dto2.setPrimApellido("Martinez");
        dto2.setCorreo("luis@gmail.com");

        List<Usuario> usuarios = List.of(usuario1, usuario2);

        when(usuarioRepository.findByEstado(estado)).thenReturn(usuarios);
        when(usuarioMapper.toDTO(usuario1)).thenReturn(dto1);
        when(usuarioMapper.toDTO(usuario2)).thenReturn(dto2);

        EstadoDTO estadoDTO = estadoMapper.toDto(estado);
        List<UsuarioDTO> resultado = usuarioService.obtenerUsuEstado(estadoDTO);


        assertThat(resultado).isNotNull();
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getPrimNombre()).isEqualTo("Ana");
        assertThat(resultado.get(1).getPrimNombre()).isEqualTo("Luis");

    //    verify(usuarioRepository).findByEstado(estado+);
     //   verify(usuarioMapper).toDTO(usuario1);
     //   verify(usuarioMapper).toDTO(usuario2);
    }

    @Test
    void testObtenerUsuDocumento() {

        String documento = "12345678";

        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setNumeDocumento(documento);
        usuario.setPrimNombre("Carlos");
        usuario.setPrimApellido("Lopez");
        usuario.setCorreo("carlos@asur.com");

        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setNumeDocumento(documento);
        usuarioDTO.setPrimNombre("Carlos");
        usuarioDTO.setPrimApellido("Lopez");
        usuarioDTO.setCorreo("carlos@aur.com");

        when(usuarioRepository.findByNumeDocumento(documento)).thenReturn(usuario);
        when(usuarioMapper.toDTO(usuario)).thenReturn(usuarioDTO);

        UsuarioDTO resultado = usuarioService.obtenerUsuDocumento(documento);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getNumeDocumento()).isEqualTo(documento);
        assertThat(resultado.getPrimNombre()).isEqualTo("Carlos");
        assertThat(resultado.getPrimApellido()).isEqualTo("Lopez");

       // verify(usuarioRepository).findByNumeDocumento(documento);
       // verify(usuarioMapper).toDTO(usuario);
    }

    @Test
    void testObtenerUsuPorTipo() throws ChangeSetPersister.NotFoundException {
        Integer idPerfil = 1;

        Perfil perfil = new Perfil();
        perfil.setIdPerfil(idPerfil);
        perfil.setNombre("Administrador");

        // Usuario simulado que tienen ese perfil
        Usuario usuario1 = new Usuario();
        usuario1.setPrimNombre("Ana");
        usuario1.setPrimApellido("Gomez");
        usuario1.setCorreo("ana@asur.com");
        usuario1.setPerfil(perfil);

        UsuarioDTO dto1 = new UsuarioDTO();
        dto1.setPrimNombre("Ana");
        dto1.setPrimApellido("Gomez");
        dto1.setCorreo("ana@asur.com");


        when(perfilRepository.findById(idPerfil)).thenReturn(Optional.of(perfil));
        when(usuarioRepository.findByPerfil(perfil)).thenReturn(List.of(usuario1));
        when(usuarioMapper.toDTO(usuario1)).thenReturn(dto1);


        List<UsuarioDTO> resultado = usuarioService.obtenerUsuPorPerfil(idPerfil);


        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getPrimNombre()).isEqualTo("Ana");
        assertThat(resultado.get(0).getPrimApellido()).isEqualTo("Gomez");

    }

    @Test
    void testObtenerTodos() {
        // Preparar datos de prueba
        Usuario usuario1 = new Usuario();
        usuario1.setId(1);
        usuario1.setPrimNombre("Juan");
        usuario1.setPrimApellido("Perez");
        usuario1.setCorreo("juan1@email.com");

        Usuario usuario2 = new Usuario();
        usuario2.setId(2);
        usuario2.setPrimNombre("Juan");
        usuario2.setPrimApellido("Rodriguez");
        usuario2.setCorreo("juan2@email.com");

        UsuarioResponseDTO dto1 = new UsuarioResponseDTO();
        dto1.setPrimNombre("Juan");
        dto1.setPrimApellido("Perez");
        dto1.setCorreo("juan1@email.com");

        UsuarioResponseDTO dto2 = new UsuarioResponseDTO();
        dto2.setPrimNombre("Juan");
        dto2.setPrimApellido("Rodriguez");
        dto2.setCorreo("juan2@email.com");

        // Configurar mocks
        when(usuarioRepository.findAll()).thenReturn(Arrays.asList(usuario1, usuario2));
        when(usuarioResponseMapper.toDTO(usuario1)).thenReturn(dto1);
        when(usuarioResponseMapper.toDTO(usuario2)).thenReturn(dto2);

        // Ejecutar
        List<UsuarioResponseDTO> usuarios = usuarioService.obtenerUsuarios();

        // Verificar
        assertThat(usuarios).isNotNull();
        assertThat(usuarios).hasSize(2);
        assertThat(usuarios.get(0).getPrimNombre()).isEqualTo("Juan");
        assertThat(usuarios.get(0).getPrimApellido()).isEqualTo("Perez");
        assertThat(usuarios.get(1).getPrimNombre()).isEqualTo("Juan");
        assertThat(usuarios.get(1).getPrimApellido()).isEqualTo("Rodriguez");

        // Verificar interacciones
        verify(usuarioRepository).findAll();
        verify(usuarioResponseMapper).toDTO(usuario1);
        verify(usuarioResponseMapper).toDTO(usuario2);
    }

    @Test
    void testObtenerDTOporId() throws ChangeSetPersister.NotFoundException {
        // Preparar entidad
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setPrimNombre("Juan");
        usuario.setPrimApellido("Perez");
        usuario.setCorreo("juan1@email.com");

        // Preparar DTO esperado
        UsuarioResponseDTO dtoEsperado = new UsuarioResponseDTO();
        dtoEsperado.setPrimNombre("Juan");
        dtoEsperado.setPrimApellido("Perez");
        dtoEsperado.setCorreo("juan1@email.com");

        // Simular comportamiento
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(usuarioResponseMapper.toDTO(usuario)).thenReturn(dtoEsperado);

        // Ejecutar
        UsuarioResponseDTO resultado = usuarioService.obtenerPorId(1);

        // Verificar solo los campos que existen en el DTO
        assertThat(resultado).isNotNull();
        assertThat(resultado.getPrimNombre()).isEqualTo("Juan");
        assertThat(resultado.getPrimApellido()).isEqualTo("Perez");
        assertThat(resultado.getCorreo()).isEqualTo("juan1@email.com");

        // Verificar interacción
        verify(usuarioRepository).findById(1);
        verify(usuarioResponseMapper).toDTO(usuario);
    }

    @Test
    void testActualizarDatosPropios() {
        String email = "juan@asur.com";

        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(1);
        usuarioExistente.setCorreo(email);
        usuarioExistente.setPrimNombre("Juan");
        usuarioExistente.setPrimApellido("Perez");
        usuarioExistente.setNumeDocumento("12345678");

        UsuarioDatosPropiosDTO dto = new UsuarioDatosPropiosDTO();
        dto.setPrimNombre("Juan Carlos");
        dto.setPrimApellido("Perez Gomez");


        // devolver el usuario existente
        when(usuarioRepository.findByCorreo(email)).thenReturn(Optional.of(usuarioExistente));

        // devolver el usuario actualizado
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // convierte entidad a DTO
        UsuarioDTO usuarioDto = new UsuarioDTO();

        usuarioDto.setPrimNombre("Juan Carlos");
        usuarioDto.setPrimApellido("Perez Gomez");
        usuarioDto.setCorreo(email);
        usuarioDto.setNumeDocumento("12345678");

        when(usuarioMapper.toDTO(any(Usuario.class))).thenReturn(usuarioDto);


        UsuarioDTO resultado = usuarioService.actualizarDatosPropios(email, dto);


        assertThat(resultado).isNotNull();
        assertThat(resultado.getPrimNombre()).isEqualTo("Juan Carlos");
        assertThat(resultado.getPrimApellido()).isEqualTo("Perez Gomez");
        assertThat(resultado.getCorreo()).isEqualTo(email);

      }


}
