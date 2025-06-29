package com.utec.PDI;

import com.utec.config.SecurityConfig;
import com.utec.controller.AdministradorController;
import com.utec.dto.AdministradorDTO;
import com.utec.jwt.JwtRequestFilter;
import com.utec.jwt.JwtTokenUtil;
import com.utec.service.AdministradorService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;


@WebMvcTest(controllers = AdministradorController.class)
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
@SuppressWarnings("removal")
public class AdministradorEndpointSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdministradorService administradorService;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @MockBean
    private JwtRequestFilter jwtRequestFilter;

    @Test
    @WithMockUser(username = "admin@utec.edu.uy", roles = {"ADMIN"})
    void adminCreaAdministrador() throws Exception {
        AdministradorDTO dto = new AdministradorDTO();
        //dto.setIdUsuario(1);

        when(administradorService.crearAdministrador(any())).thenReturn(dto);

        mockMvc.perform(post("/api/v1/administradores")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"idUsuario\": 1}"))
                .andDo(print())
                .andExpect(status().isCreated());
    }


}
