package com.utec.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("PDI - API de Gestión Institucional")
                        .description("API REST para el sistema de gestión de actividades, reservas, usuarios y pagos de la institución. " +
                                "Esta API permite administrar actividades institucionales, reservar espacios, gestionar usuarios " +
                                "con diferentes perfiles y manejar el sistema de pagos y cuotas.\n\n" +
                                "## Autenticación\n" +
                                "La mayoría de endpoints requieren autenticación JWT. Para obtener un token:\n" +
                                "1. Use el endpoint `/api/v1/auth/login` con sus credenciales\n" +
                                "2. Incluya el token en el header: `Authorization: Bearer {token}`\n\n" +
                                "## Roles Disponibles\n" +
                                "- **ADMINISTRADOR**: Acceso completo a todas las funcionalidades\n" +
                                "- **USUARIO**: Acceso limitado a funcionalidades básicas\n" +
                                "- **SOCIO**: Acceso parcial con beneficios especiales\n\n" +
                                "## Credenciales de Prueba\n" +
                                "- **Admin**: admin@asur.com / admin1234\n" +
                                "- **Usuario**: valeria@asur.com / user1234")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Grupo 4 - UTEC")
                                .email("grupo4@utec.edu.uy")
                                .url("https://git.utec.edu.uy/4group/pdi"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Servidor de Desarrollo")
                ))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(
                        new io.swagger.v3.oas.models.Components()
                                .addSecuritySchemes(securitySchemeName,
                                        new SecurityScheme()
                                                .name(securitySchemeName)
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                                .description("Token JWT obtenido del endpoint de login"))
                );
    }
}