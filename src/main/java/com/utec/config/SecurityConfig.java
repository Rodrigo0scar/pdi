package com.utec.config;

import com.utec.jwt.JwtRequestFilter;
import com.utec.service.CustomUsuarioDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) //para test unitarios
public class SecurityConfig {

    //Injectar el servicio para cargar los detalles del usuario.
    private final CustomUsuarioDetailsService customUsuarioDetailsService;
    private final JwtRequestFilter jwtRequestFilter;

    //Constructor (que es el que injecta)
    @Autowired
    public SecurityConfig(CustomUsuarioDetailsService customUsuarioDetailsService, JwtRequestFilter jwtRequestFilter) {
        this.customUsuarioDetailsService = customUsuarioDetailsService;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        // Solo USER
                        .requestMatchers(HttpMethod.PUT, "/api/v1/usuarios/modificar-datos-propios").hasRole("USER")
                        // Solo ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/v1/usuarios").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/usuarios/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/usuarios/modificar_perfil/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/usuario/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/usuario/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/socios").authenticated()
                        .requestMatchers("/api/v1/socios/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/administradores").authenticated()
                        .requestMatchers("/api/v1/perfiles/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/funcionalidades/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/auditoria/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/actividad/inscripcion").authenticated()
                        .requestMatchers("/api/v1/actividad/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/v1/inscripciones").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/inscripciones").authenticated()
                        .requestMatchers("/api/v1/inscripciones/reporte/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/inscripciones/actividades/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/todos-registros").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/pago").hasRole("ADMIN")

                        .requestMatchers("/api/v1/espacios/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/reservas").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/reservas/cancelar/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/v1/reservas/{id}").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/v1/reservas").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/reservas/espacio/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/reservas/reporte-reservas-por-fecha").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/reservas/reporte-reservas-por-espacio").hasRole("ADMIN")

                        .requestMatchers("/api/v1/cuotas/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/modos-pago/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/pagos/**").hasRole("ADMIN")


                        .anyRequest().authenticated()

                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"error\": \"No está autenticado. Iniciar sesión primero.\"}");
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"error\": \"No tiene permisos para acceder a esta funcionalidad.\"}");
                        })
                );
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();

    }

    //Definir un manager par aautenticación de usuarios y contraseñas
    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder
                .userDetailsService(customUsuarioDetailsService)
                .passwordEncoder(passwordEncoder());
        return builder.build();
    }
} 