# 🏢 PDI - Sistema de Gestión Institucional

## 📋 Descripción del Proyecto

**PDI (Proyecto de Desarrollo Institucional)** es una aplicación web desarrollada en Spring Boot que gestiona las actividades, reservas de espacios, usuarios y pagos de una institución educativa. El sistema permite administrar actividades institucionales, reservar espacios, gestionar usuarios con diferentes perfiles y manejar el sistema de pagos y cuotas.

### 🎯 Funcionalidades Principales

- **Gestión de Usuarios**: Registro, autenticación y gestión de perfiles (Administrador, Usuario, Socio)
- **Gestión de Actividades**: Creación, modificación y eliminación de actividades institucionales
- **Reservas de Espacios**: Sistema de reservas con validación de disponibilidad
- **Gestión de Pagos**: Control de pagos de actividades y cuotas mensuales
- **Reportes y Auditoría**: Seguimiento de cambios y generación de reportes
- **Seguridad**: Autenticación JWT y control de acceso basado en roles

## 🛠️ Tecnologías Utilizadas

### Backend
- **Java 21** - Lenguaje de programación
- **Spring Boot 3.5.0** - Framework principal
- **Spring Security** - Autenticación y autorización
- **Spring Data JPA** - Persistencia de datos
- **PostgreSQL 16** - Base de datos principal
- **H2 Database** - Base de datos para pruebas
- **JWT** - Tokens de autenticación
- **Maven** - Gestión de dependencias

### Herramientas de Desarrollo
- **Docker & Docker Compose** - Contenedorización
- **Swagger/OpenAPI** - Documentación de API
- **Lombok** - Reducción de código boilerplate
- **Spring Actuator** - Monitoreo y métricas
- **Spring Mail** - Envío de correos electrónicos

## 🏗️ Arquitectura del Proyecto

```
src/main/java/com/utec/
├── config/           # Configuraciones de Spring
├── controller/       # Controladores REST
├── dto/             # Objetos de transferencia de datos
├── enums/           # Enumeraciones
├── jwt/             # Configuración JWT
├── mail/            # Servicios de correo
├── mapper/          # Mappers entre entidades y DTOs
├── model/           # Entidades JPA
├── repository/      # Repositorios de datos
├── service/         # Lógica de negocio
├── util/            # Utilidades
└── auditoria/       # Sistema de auditoría
```

## 🚀 Instalación y Configuración

### Prerrequisitos

- Java 21 o superior
- Maven 3.8+
- Docker y Docker Compose
- Git

### Pasos de Instalación

1. **Clonar el Repositorio**
   ```bash
   git clone https://git.utec.edu.uy/4group/pdi.git
   cd pdi
   ```

2. **Levantar Base de Datos con Docker**
   ```bash
   docker-compose up -d
   ```

3. **Configurar Base de Datos**
   
   El archivo `application.properties` debe contener:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/utec
   spring.datasource.username=utecsuruser
   spring.datasource.password=utecsurpass
   spring.datasource.driver-class-name=org.postgresql.Driver
   
   spring.jpa.hibernate.ddl-auto=validate
   spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
   ```

4. **Ejecutar la Aplicación**
   
   **Opción A - IntelliJ IDEA:**
   - Abrir el proyecto en IntelliJ
   - Ejecutar `PdiApplication.java`
   
   **Opción B - Línea de Comandos:**
   ```bash
   ./mvnw spring-boot:run
   ```

5. **Acceder a la Aplicación**
   - **API REST**: http://localhost:8080
   - **Swagger UI**: http://localhost:8080/swagger-ui.html
   - **Actuator**: http://localhost:8080/actuator

## 🔐 Credenciales de Prueba

### Usuario Administrador
- **Email**: admin@asur.com
- **Contraseña**: admin1234
- **Perfil**: Administrador

### Usuario Regular
- **Email**: valeria@asur.com
- **Contraseña**: user1234
- **Perfil**: Usuario

## 📚 Documentación de API

La documentación completa de la API está disponible a través de Swagger UI en:
- **URL**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

## 🧪 Pruebas

Para ejecutar las pruebas unitarias:
```bash
./mvnw test
```

## 📦 Entregas del Proyecto

### ✅ Entrega 1 – Setup del Proyecto y Primeros Endpoints
**📅 Fecha**: 7 de Junio

#### Checklist de Objetivos:

**Configuración Inicial:**
- [x] Crear repositorio en GitLab con estructura del proyecto
- [x] Inicializar Spring Boot
- [x] Configurar conexión con PostgreSQL

**Primeros Endpoints:**
- [x] RF001-01: Registro de usuarios
- [x] RF001-02: Listado de usuarios
- [x] Validaciones básicas (ej. nombre único, email válido)

**Arquitectura:**
- [x] Crear entidades base (Usuario, Perfil)
- [x] Implementar DTOs

**Documentación:**
- [x] Configurar Swagger o ReDoc

**Pruebas:**
- [x] Unitarias para creación y listado de usuarios (JUnit, Mockito)

**README.md:**
- [x] Descripción del proyecto, tecnologías y estructura
- [x] Incluir en la entrega:
  - [x] Enlace al repositorio GitLab
  - [x] Swagger/ReDoc
  - [x] Video demostrativo con endpoints funcionando
  - [x] README.md actualizado

### ✅ Entrega 2 – Seguridad, Perfiles y CRUDs Iniciales
**📅 Fecha**: 14 de Junio

#### Checklist de Objetivos:

**Seguridad:**
- [x] Spring Security + JWT
- [x] Endpoint de login
- [x] Restricción de acceso a endpoints por autenticación

**Roles y permisos:**
- [x] Definir roles (admin/user)
- [x] Control de acceso según rol

**CRUDs:**
- [x] Usuarios: crear, leer, actualizar, eliminar
- [x] Perfiles: idem
- [x] Validaciones (email único, etc.)

**Auditoría básica:**
- [x] Registro de cambios con JPA Auditing

**Pruebas:**
- [x] Login y JWT
- [x] Acceso según roles

**Documentación:**
- [x] Swagger/ReDoc actualizado

**Incluir en la entrega:**
- [x] Repositorio actualizado
- [x] Swagger/ReDoc con autenticación
- [x] Video demostrativo
- [x] README.md actualizado

**Comentarios sobre la entrega:**
- [x] El acceso según roles se controla en los endpoints desde Security Config con el role definido en el payload del token JWT.
- [x] El método de modificar Perfiles no tiene restricciones, por lo que el control de los filtros se traslada al frontend.

### ✅ Entrega 3 – Módulos Institucionales, Reportes y Pagos
**📅 Fecha**: 21 de Junio

#### Checklist de Objetivos:

**Módulos:**
- [x] Actividades (RF005)
- [x] Espacios (RF006)
- [x] Tipos de Actividad (RF007)
- [x] CRUD completo
- [x] Validar solapamientos de actividades

**Pagos:**
- [x] Registro y CRUD de pagos
- [x] Validaciones (ej. monto no negativo)

**Reportes:**
- [x] Consultas por fecha, usuario, actividad, espacio

**Auditoría avanzada:**
- [x] Ampliar auditoría a pagos y actividades

**Pruebas:**
- [x] Unitarias de nuevas funcionalidades

**Entregables:**
- [x] Repositorio actualizado
- [x] Swagger/ReDoc con nuevos endpoints
- [x] Video mostrando actividades, pagos y reportes
- [x] README.md actualizado

**Comentarios sobre la entrega:**
- [x] Para el módulo Espacios quedan disponibles listados para que el frontend pueda filtrar.
- [x] El ingreso de reservas contempla el modo de pago y la fecha de vencimiento de la seña (es calculada), pero los otros campos relacionados al pago se derivan al módulo de Pagos y al frontend.
- [x] Los métodos de modificar Tipo Actividad y Actividad no tienen restricción en todos los campos. Por lo que el filtro se traslada al frontend.

### ✅ Entrega Final – Últimos Detalles y Ajustes Finales
**📅 Fecha**: 28 de Junio

#### Checklist de Objetivos:

**Finalización del sistema:**
- [x] Todos los endpoints implementados
- [x] Validación de seguridad

**Documentación completa:**
- [x] README.md con instalación, arquitectura y detalles técnicos

**Despliegue:**
- [x] En servidor local o nube (opcional)
- [x] Instrucciones en el repositorio

**Video final:**
- [x] Demostración completa del sistema (máx. 30 min)

**Entregables:**
- [x] Repositorio final
- [x] Video con todo el sistema funcionando
- [x] README.md final
- [x] Despliegue mostrado y documentado

**Comentarios sobre la entrega:**
- [x] Para el módulo Funcionalidades y su relación con el acceso de Perfiles, quedan disponibles listados para que el frontend pueda filtrar y determinar el acceso.
- [x] La validación de la cédula Uruguaya utiliza la libreria de Fabian Delgado y se puede encontrar en el siguient enlacel: https://github.com/fabdelgado/ciuy

## 🔧 Endpoints Principales

### Autenticación
- `POST /api/auth/login` - Iniciar sesión
- `POST /api/auth/register` - Registro de usuarios

### Usuarios
- `GET /api/usuarios` - Listar usuarios
- `POST /api/usuarios` - Crear usuario
- `PUT /api/usuarios/{id}` - Actualizar usuario
- `DELETE /api/usuarios/{id}` - Eliminar usuario

### Actividades
- `GET /api/actividades` - Listar actividades
- `POST /api/actividades` - Crear actividad
- `PUT /api/actividades/{id}` - Actualizar actividad
- `DELETE /api/actividades/{id}` - Eliminar actividad

### Espacios
- `GET /api/espacios` - Listar espacios
- `POST /api/espacios` - Crear espacio
- `PUT /api/espacios/{id}` - Actualizar espacio
- `DELETE /api/espacios/{id}` - Eliminar espacio

### Reservas
- `GET /api/reservas` - Listar reservas
- `POST /api/reservas` - Crear reserva
- `PUT /api/reservas/{id}` - Actualizar reserva
- `DELETE /api/reservas/{id}` - Eliminar reserva

## 📊 Estado del Proyecto

- **Estado**: ✅ Completado
- **Última actualización**: Junio 2024
- **Versión**: 1.0.0
- **Equipo**: Grupo 4 - UTEC

## 🤝 Contribución

Para contribuir al proyecto:

1. Fork el repositorio
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## 📄 Licencia

Este proyecto está bajo la licencia MIT. Ver el archivo `LICENSE` para más detalles.

## 📞 Contacto

- **Equipo**: Grupo 4 - UTEC
- **Email**: [email del equipo]
- **Repositorio**: https://git.utec.edu.uy/4group/pdi

---

**Desarrollado con ❤️ por el Grupo 4 de UTEC**