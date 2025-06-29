
CREATE TABLE estado (
	Id_Estado SERIAL PRIMARY KEY, 
	Descripcion VARCHAR(100) NOT NULL CHECK (Descripcion ~ '^[A-Za-zÁÉÍÓÚáéíóúÑñü,.\- ]+$' AND LENGTH(TRIM(Descripcion)) >= 1 AND LENGTH(Descripcion) <= 100)
);

CREATE TABLE cuota (
	Id_Cuota SERIAL PRIMARY KEY, 
	Valo_Cuota INTEGER NOT NULL CHECK (Valo_Cuota >= 1 AND Valo_Cuota <= 1000),
	Mes_Cuota INTEGER NOT NULL CHECK (Mes_Cuota >= 1 AND Mes_Cuota <= 12)
 );

CREATE TABLE modo_pago (
	Id_Modo_Pago SERIAL PRIMARY KEY, 
	Modo VARCHAR(50) NOT NULL CHECK (Modo ~ '^[A-Za-zÁÉÍÓÚáéíóúÑñü ]+$' AND LENGTH(TRIM(Modo)) >= 1 AND LENGTH(Modo) <= 50)
);

CREATE TABLE categoria (
	Id_Categoria SERIAL PRIMARY KEY, 
	Nombre VARCHAR(50) NOT NULL CHECK (Nombre ~ '^[A-Za-zÁÉÍÓÚáéíóúÑñü ]+$' AND LENGTH(TRIM(Nombre)) >= 1 AND LENGTH(Nombre) <= 50),
	Descripcion VARCHAR(250) NOT NULL CHECK (Descripcion ~ '^[A-Za-z0-9ÁÉÍÓÚáéíóúÑñüü,.\- ]+$' AND LENGTH(TRIM(Descripcion)) >= 1 AND LENGTH(Descripcion) <= 250)
);

CREATE TABLE subcomision (
	Id_Subcomision SERIAL PRIMARY KEY,
	Nombre VARCHAR(50) NOT NULL CHECK (Nombre ~ '^[A-Za-zÁÉÍÓÚáéíóúÑñü ]+$' AND LENGTH(TRIM(Nombre)) >= 1 AND LENGTH(Nombre) <= 50),
	Descripcion VARCHAR(250) NOT NULL CHECK (Descripcion ~ '^[A-Za-z0-9ÁÉÍÓÚáéíóúÑñüü,.\- ]+$' AND LENGTH(TRIM(Descripcion)) >= 1 AND LENGTH(Descripcion) <= 250)
);

CREATE TABLE auditoria (
	Id_Auditoria SERIAL PRIMARY KEY,
	Usuario VARCHAR(50) NOT NULL CHECK (Usuario ~ '^[A-Za-z0-9ÁÉÍÓÚáéíóúÑñü ]+$' AND LENGTH(TRIM(Usuario)) >= 1 AND LENGTH(Usuario) <= 50),
	Fecha_Hora TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP CHECK (Fecha_Hora <= CURRENT_TIMESTAMP),
	Terminal VARCHAR(50) NOT NULL CHECK (Terminal ~ '^[A-Za-z0-9ÁÉÍÓÚáéíóúÑñü_\- ]+$' AND LENGTH(TRIM(Terminal)) >= 1 AND LENGTH(Terminal) <= 50)
);

CREATE TABLE tipo_documento (
	Id_Tipo_Documento SERIAL PRIMARY KEY, 
	Nomb_Documento VARCHAR(50) UNIQUE NOT NULL CHECK (Nomb_Documento ~ '^[A-Za-z0-9ÁÉÍÓÚáéíóúÑñü,.\- ]+$' AND LENGTH(Nomb_Documento) >= 1 AND LENGTH(Nomb_Documento) <= 50),
	Descripcion VARCHAR(250) NOT NULL CHECK (Descripcion ~ '^[A-Za-z0-9ÁÉÍÓÚáéíóúÑñü,.\- ]+$' AND LENGTH(TRIM(Descripcion)) >= 1 AND LENGTH(Descripcion) <= 50)
);

CREATE TABLE funcionalidad (
	Id_Funcionalidad SERIAL PRIMARY KEY,
	Nombre VARCHAR(50) UNIQUE NOT NULL CHECK (Nombre ~ '^[A-Za-zÁÉÍÓÚáéíóúÑñü ]+$' AND LENGTH(TRIM(Nombre)) >= 1 AND LENGTH(Nombre) <= 50),
	Descripcion VARCHAR(250) NOT NULL CHECK (Descripcion ~ '^[A-Za-z0-9ÁÉÍÓÚáéíóúÑñü,.\- ]+$' AND LENGTH(TRIM(Descripcion)) >= 1 AND LENGTH(Descripcion) <= 50),
	Id_Estado INTEGER NOT NULL,
	CONSTRAINT FK_Funcionalidad_Estado FOREIGN KEY (Id_estado) REFERENCES estado(Id_estado)
	);

CREATE TABLE perfil (
	Id_Perfil SERIAL PRIMARY KEY, 
	Nombre VARCHAR(50) UNIQUE NOT NULL CHECK (Nombre ~ '^[A-Za-zÁÉÍÓÚáéíóúÑñü ]+$' AND LENGTH(TRIM(Nombre)) >= 1 AND LENGTH(Nombre) <= 50),
	Descripcion VARCHAR(250) NOT NULL CHECK (Descripcion ~ '^[A-Za-z0-9ÁÉÍÓÚáéíóúÑñü,.\- ]+$' AND LENGTH(TRIM(Descripcion)) >= 1 AND LENGTH(Descripcion) <= 250),
	Id_Estado INTEGER NOT NULL,
	CONSTRAINT FK_Perfil_Estado FOREIGN KEY (Id_Estado) REFERENCES estado(Id_Estado)
	);

CREATE TABLE perfil_accede_funcionalidad (
	Id_Perfil INTEGER NOT NULL,
	Id_Funcionalidad INTEGER NOT NULL,
	CONSTRAINT PK_Perfil_Accede_Funcionalidad PRIMARY KEY (Id_Perfil, Id_Funcionalidad),
	--AÑADIDO FOREIGN KEY
	CONSTRAINT FK_Perfil FOREIGN KEY (Id_Perfil) REFERENCES perfil(Id_Perfil),
	CONSTRAINT FK_Funcionalidad FOREIGN KEY (Id_Funcionalidad) REFERENCES funcionalidad(Id_Funcionalidad)
	);

CREATE TABLE espacio (
	Id_Espacio SERIAL PRIMARY KEY,
	Nombre VARCHAR(50) UNIQUE NOT NULL CHECK (Nombre ~ '^[A-Za-zÁÉÍÓÚáéíóúÑñü ]+$' AND LENGTH(TRIM(Nombre)) >= 1 AND LENGTH(Nombre) <= 50),
	Id_Estado INTEGER NOT NULL, 
	Capa_Maxima INTEGER NOT NULL CHECK (Capa_Maxima >= 1 AND Capa_Maxima <= 999),
	Prec_Rese_Socio DECIMAL NOT NULL CHECK (Prec_Rese_Socio >= 1 AND Prec_Rese_Socio <= 99999), 
	Prec_Rese_No_Socio DECIMAL NOT NULL CHECK (Prec_Rese_No_Socio >= 1 AND Prec_Rese_No_Socio <= 99999), 
	Observaciones VARCHAR(250) CHECK (Observaciones IS NULL OR (Observaciones ~ '^[A-Za-z0-9ÁÉÍÓÚáéíóúÑñü,.\- ]+$' AND LENGTH(TRIM(Observaciones)) >= 1 AND LENGTH(Observaciones) <= 250)),
	Fech_Vige_Precio DATE NOT NULL,
	CONSTRAINT FK_Espacio_Estado FOREIGN KEY (Id_Estado) REFERENCES estado(Id_Estado)
	);

	CREATE TABLE usuario (
    Id_Usuario SERIAL PRIMARY KEY,
    Nume_Documento VARCHAR(15) UNIQUE NOT NULL CHECK (Nume_Documento ~ '^[A-Za-z0-9]{1,15}$'),
    Id_Perfil INTEGER NOT NULL,
    Id_Tipo_Documento INTEGER NOT NULL,
    Prim_Nombre VARCHAR(20) NOT NULL CHECK (Prim_Nombre ~ '^[A-Za-zÁÉÍÓÚáéíóúÑñü ]+$' AND LENGTH(TRIM(Prim_Nombre)) >= 1 AND LENGTH(Prim_Nombre) <= 20),
    Segu_Nombre VARCHAR(20) CHECK (Segu_Nombre IS NULL OR Segu_Nombre ~ '^[A-Za-zÁÉÍÓÚáéíóúÑñü ]+$' AND LENGTH(Segu_Nombre) <= 20),
    Prim_Apellido VARCHAR(20) NOT NULL CHECK (Prim_Apellido ~ '^[A-Za-zÁÉÍÓÚáéíóúÑñü ]+$' AND LENGTH(TRIM(Prim_Apellido)) >= 1 AND LENGTH(Prim_Apellido) <= 20),
    Segu_Apellido VARCHAR(20) CHECK (Segu_Apellido ~ '^[A-Za-zÁÉÍÓÚáéíóúÑñü ]+$' AND LENGTH(TRIM(Segu_Apellido)) >= 1 AND LENGTH(Segu_Apellido) <= 20),
    Contrasenia VARCHAR(60) NOT NULL CHECK (LENGTH(TRIM(Contrasenia)) >= 8 AND LENGTH(Contrasenia) <= 60),
    Correo VARCHAR(100) UNIQUE NOT NULL CHECK (
        correo ~ E'^[a-zA-Z0-9._%+-]+@(gmail\\.com|hotmail\\.com|asur\\.com|([a-zA-Z0-9-]+\\.)*utec\\.edu\\.uy)$'),
    Fech_Nacimiento DATE NOT NULL CHECK (Fech_Nacimiento <= CURRENT_DATE),
    Apartamento VARCHAR(10) CHECK (Apartamento ~ '^[A-Za-z0-9 ]*$' AND LENGTH(Apartamento) <= 10),
    Calle VARCHAR(50) NOT NULL CHECK (Calle ~ '^[A-Za-z0-9ÁÉÍÓÚáéíóúÑñü,.\- ]+$' AND LENGTH(TRIM(Calle)) >= 1 AND LENGTH(Calle) <= 50),
    Nume_Puerta INTEGER NOT NULL CHECK (Nume_Puerta >= 1 AND Nume_Puerta <= 99999),
    Id_Estado INTEGER NOT NULL,
    CONSTRAINT FK_Usuario_Perfil FOREIGN KEY (Id_Perfil) REFERENCES perfil(Id_Perfil),
    CONSTRAINT FK_Usuario_Tipo_Documento FOREIGN KEY (Id_Tipo_Documento) REFERENCES tipo_documento (Id_Tipo_Documento),
    CONSTRAINT FK_Usuario_Estado FOREIGN KEY (Id_Estado) REFERENCES estado (Id_Estado));

CREATE TABLE tipo_actividad (
	Id_Tipo_Actividad SERIAL PRIMARY KEY,
	Tipo VARCHAR(30) NOT NULL CHECK (Tipo ~ '^[A-Za-z0-9ÁÉÍÓÚáéíóúÑñü,.\- ]+$' AND LENGTH(TRIM(Tipo)) >= 1 AND LENGTH(Tipo) <= 30),
	Descripcion VARCHAR(250) CHECK (Descripcion ~ '^[A-Za-z0-9ÁÉÍÓÚáéíóúÑñü,.\- ]+$' AND LENGTH(TRIM(Descripcion)) >= 1 AND LENGTH(Descripcion) <= 250),
	comentarios_baja VARCHAR(200)
);

CREATE TABLE reserva (
	Id_Usuario INTEGER NOT NULL,
	Id_Espacio INTEGER NOT NULL,
	Fech_Hora_Reserva TIMESTAMP NOT NULL,
	Duracion INTEGER NOT NULL CHECK (Duracion >= 1 AND Duracion <= 5),
	Cant_Personas INTEGER NOT NULL CHECK (Cant_Personas >= 1 AND Cant_Personas <= 99),
	Id_Estado INTEGER NOT NULL,
	Impo_Total DECIMAL NOT NULL CHECK (Impo_Total >= 0),
	Fech_Pago_Senia DATE,
	Impo_Seni_Pagado DECIMAL CHECK (Impo_Seni_Pagado >= 0),
	Fech_Cobro TIMESTAMP NOT NULL,
	Id_Modo_Pago INTEGER NOT NULL CHECK (Id_Modo_Pago >= 0),

	CONSTRAINT FK_Reserva_Usuario FOREIGN KEY(Id_Usuario) REFERENCES usuario (Id_Usuario),
	CONSTRAINT FK_Reserva_Espacio FOREIGN KEY(Id_Espacio) REFERENCES espacio(Id_Espacio),
	CONSTRAINT FK_Reserva_Estado FOREIGN KEY(Id_Estado) REFERENCES estado (Id_Estado),
	CONSTRAINT FK_Reserva_Modo_Pago FOREIGN KEY (Id_Modo_Pago) REFERENCES modo_pago (Id_Modo_Pago)
	);

CREATE TABLE telefono (
	Id_Telefono SERIAL NOT NULL,
	Id_Usuario INTEGER NOT NULL,
	Nume_Telefono VARCHAR(20) NOT NULL CHECK (Nume_Telefono ~ '^[0-9]+$' AND LENGTH(Nume_Telefono) >= 8 AND LENGTH(Nume_Telefono) <= 9),
	Tipo_Telefono VARCHAR(50) NOT NULL CHECK (Tipo_Telefono IN ('Celular', 'Fijo')),
	CONSTRAINT PK_Telefono PRIMARY KEY(Id_Telefono, Id_Usuario),
	CONSTRAINT FK_Telefono_Usuario FOREIGN KEY (Id_Usuario) REFERENCES usuario (Id_Usuario),
CONSTRAINT unico_usuario_telefono UNIQUE (id_usuario, nume_telefono)
	);

CREATE TABLE socio (

	Id_Usuario INTEGER NOT NULL,
	Id_Categoria INTEGER NOT NULL,
	Dif_Auditiva BOOLEAN NOT NULL DEFAULT TRUE,
	Uso_Leng_Senia BOOLEAN NOT NULL DEFAULT FALSE,
	Id_Estado INTEGER NOT NULL,
	Paga_Desde INTEGER NOT NULL,
	Paga_Hasta INTEGER NOT NULL,
	id_subcomision INTEGER,
	CONSTRAINT PK_Socio PRIMARY KEY (Id_Usuario),
	CONSTRAINT FK_Socio_Usuario FOREIGN KEY (Id_Usuario) REFERENCES usuario (Id_Usuario),
	CONSTRAINT FK_Socio_Categoria FOREIGN KEY (Id_Categoria) REFERENCES categoria (Id_Categoria),
	CONSTRAINT FK_Socio_Estado FOREIGN KEY (Id_Estado) REFERENCES estado (Id_Estado),
	CONSTRAINT fk_subcomision FOREIGN KEY (id_subcomision) REFERENCES
subcomision (id_subcomision)
	);

CREATE TABLE socio_paga_cuota (
	Id_Usuario INTEGER NOT NULL,
	Id_Cuota INTEGER NOT NULL,
	Fech_Cobro TIMESTAMP NOT NULL,
	Mont_Cobrado INTEGER NOT NULL CHECK (Mont_Cobrado >= 0),
	Id_Modo_Pago INTEGER NOT NULL,
	CONSTRAINT PK_Socio_Paga_Cuota PRIMARY KEY (Id_Usuario,Id_Cuota),
	CONSTRAINT FK_Socio_Paga FOREIGN KEY(Id_Usuario) REFERENCES socio (Id_Usuario),
	CONSTRAINT FK_Cuota_Paga FOREIGN KEY(Id_Cuota) REFERENCES cuota (Id_Cuota),
	CONSTRAINT FK_Modo_Pago_Cuota FOREIGN KEY (Id_Modo_Pago) REFERENCES modo_pago (Id_Modo_Pago)
	);

CREATE TABLE administrador (

	Id_Usuario INTEGER PRIMARY KEY ,
	CONSTRAINT FK_Administrador_Usuario FOREIGN KEY (Id_Usuario) REFERENCES usuario (Id_Usuario)
	);

CREATE TABLE actividad (
	Id_Actividad SERIAL PRIMARY KEY,
	Nombre VARCHAR(50) NOT NULL CHECK (Nombre ~ '^[A-Za-zÁÉÍÓÚáéíóúÑñü ]+$' AND LENGTH(TRIM(Nombre)) >= 1 AND LENGTH(Nombre) <= 50),
	Descripcion VARCHAR(250) NOT NULL CHECK (Descripcion ~ '^[A-Za-z0-9ÁÉÍÓÚáéíóúÑñü,.\- ]+$' AND LENGTH(TRIM(Descripcion)) >= 1 AND LENGTH(Descripcion) <= 250),
	Objetivo VARCHAR(100) NOT NULL CHECK (LENGTH(Objetivo) >= 1 AND LENGTH(Objetivo) <= 100),
	Id_Tipo_Actividad INTEGER NOT NULL,
	Fech_Hora_Actividad TIMESTAMP NOT NULL CHECK (Fech_Hora_Actividad >= CURRENT_TIMESTAMP),
	Id_Espacio INTEGER NOT NULL,
	Duracion INTEGER NOT NULL CHECK (Duracion >= 1),
	Requ_Inscripcion BOOLEAN NOT NULL DEFAULT TRUE,
	Fech_Apertura_Inscripcion DATE CHECK (Fech_Hora_Actividad >= CURRENT_DATE),
	Id_Modo_Pago INTEGER NOT NULL,
	Costo DECIMAL NOT NULL CHECK (Costo >= 0),
	Observaciones VARCHAR(250) CHECK (Observaciones IS NULL OR (
        Observaciones ~ '^[A-Za-z0-9ÁÉÍÓÚáéíóúÑñü,.\- ]+$'
        AND LENGTH(TRIM(Observaciones)) >= 1 AND LENGTH(Observaciones) <= 250)),
  Id_Estado INTEGER NOT NULL,
	Id_Usuario INTEGER NOT NULL,
	CONSTRAINT FK_Tipo_Actividad FOREIGN KEY (Id_Tipo_Actividad) REFERENCES tipo_actividad (Id_Tipo_Actividad),
	CONSTRAINT FK_Actividad_Espacio FOREIGN KEY (Id_Espacio) REFERENCES espacio (Id_Espacio),
	CONSTRAINT FK_Actividad_Modo_Pago FOREIGN KEY (Id_Modo_Pago) REFERENCES modo_pago (Id_Modo_Pago),
	CONSTRAINT FK_Actividad_Estado FOREIGN KEY (Id_Estado) REFERENCES estado (Id_Estado),
	CONSTRAINT FK_Actividad_Administrador FOREIGN KEY (Id_Usuario) REFERENCES usuario (Id_Usuario)
	);

CREATE TABLE usuario_concurre_actividad (
    id_usuario INTEGER NOT NULL,
    id_actividad INTEGER NOT NULL,
    fech_cobro DATE,
    mont_cobrado NUMERIC,
    id_modo_pago INTEGER,
    asistencia boolean NOT NULL,
    pago_ticket boolean,
    CONSTRAINT pk_usuario_concurre_actividad PRIMARY KEY (id_usuario, id_actividad),
    CONSTRAINT fk_usuario_concurre FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario),
	CONSTRAINT fk_actividad_concurre FOREIGN KEY (id_actividad) REFERENCES actividad (id_actividad),
	CONSTRAINT fk_modo_pago_usuario_actividad FOREIGN KEY (id_modo_pago) REFERENCES modo_pago (id_modo_pago),
	CONSTRAINT usuario_concurre_actividad_mont_cobrado_check CHECK (mont_cobrado >= 0::numeric OR mont_cobrado IS NULL));

INSERT INTO estado (Descripcion) VALUES 
('Activo'),
('Inactivo'),
('Pendiente');

INSERT INTO tipo_documento (Nomb_Documento, Descripcion) VALUES 
('Cedula de Identidad', 'Documento de identidad'),
('Pasaporte', 'Documento de viaje');

INSERT INTO perfil (Nombre, Descripcion, Id_Estado) VALUES
('Administrador', 'Acceso completo', 1),
('Usuario', 'Acceso limitado', 1),
('Socio', 'Acceso parcial', 1);

--para pruebas de seguridad:
INSERT INTO usuario (Nume_Documento, Id_Perfil, Id_Tipo_Documento, Prim_Nombre, Segu_Nombre, Prim_Apellido, Segu_Apellido, Contrasenia, Correo, Fech_Nacimiento, Apartamento, Calle, Nume_Puerta, Id_Estado) VALUES
    ('53427641', 1, 1, 'Admin', 'Admin', 'Admin', 'Admin', '$2a$12$RqIQRq7flnR6IPYOrG2GmOj7D1sFIQJAPuAdJHxH4krrF6f942sUG', 'admin@asur.com', '1990-01-01', '111', 'ASUR', '1234', 1);
INSERT INTO administrador VALUES
    (1);
--password admin1234

INSERT INTO usuario (
    Nume_Documento, Id_Perfil, Id_Tipo_Documento, Prim_Nombre, Segu_Nombre,
    Prim_Apellido, Segu_Apellido, Contrasenia, Correo, Fech_Nacimiento,
    Apartamento, Calle, Nume_Puerta, Id_Estado
) VALUES
    ('31245603', 2, 1, 'Valeria', 'Soledad', 'Ortiz', 'Muñoz', '$2a$12$0rLOFTuR57TNx2br0rdMxOGj3l8xj3tcYIsaJO7DLo4MiM6VceKm2',
     'valeria@asur.com', '1999-04-20', 'C1', 'Bulevar Artigas', 321, 1);
-- password user1234

--Atributos de auditoria PERFIL_ACCEDE_FUNCIONALIDAD
ALTER TABLE perfil_accede_funcionalidad ADD COLUMN Crea_Por varchar (20);
ALTER TABLE perfil_accede_funcionalidad ADD COLUMN Fech_Creacion DATE;
ALTER TABLE perfil_accede_funcionalidad ADD COLUMN Modi_Por varchar (20);
ALTER TABLE perfil_accede_funcionalidad ADD COLUMN Fech_Modificacion DATE;

INSERT INTO subcomision (nombre, descripcion)
VALUES ('Deportes', 'Subcomisión encargada de organizar actividades deportivas.');

INSERT INTO categoria (nombre, descripcion)
VALUES ('Activo', 'Categoría de socios con participación activa en las actividades.');

--Atributos de auditoria PERFIL:
ALTER TABLE perfil ADD COLUMN Crea_Por varchar (20);
ALTER TABLE perfil ADD COLUMN Fech_Creacion DATE;
ALTER TABLE perfil ADD COLUMN Modi_Por varchar (20);
ALTER TABLE perfil ADD COLUMN Fech_Modificacion DATE;

--Atributos de auditoria USUARIO
ALTER TABLE usuario ADD COLUMN Crea_Por varchar (20);
ALTER TABLE usuario ADD COLUMN Fech_Creacion DATE;
ALTER TABLE usuario ADD COLUMN Modi_Por varchar (20);
ALTER TABLE usuario ADD COLUMN Fech_Modificacion DATE;

--Atributos de auditoria CATEGORIA:
ALTER TABLE categoria ADD COLUMN Crea_Por varchar (20);
ALTER TABLE categoria ADD COLUMN Fech_Creacion DATE;
ALTER TABLE categoria ADD COLUMN Modi_Por varchar (20);
ALTER TABLE categoria ADD COLUMN Fech_Modificacion DATE;

--Atributos de auditoria ESPACIO:
ALTER TABLE espacio ADD COLUMN Crea_Por varchar (20);
ALTER TABLE espacio ADD COLUMN Fech_Creacion DATE;
ALTER TABLE espacio ADD COLUMN Modi_Por varchar (20);
ALTER TABLE espacio ADD COLUMN Fech_Modificacion DATE;

--Atributos de auditoria ESTADO:
ALTER TABLE estado ADD COLUMN Crea_Por varchar (20);
ALTER TABLE estado ADD COLUMN Fech_Creacion DATE;
ALTER TABLE estado ADD COLUMN Modi_Por varchar (20);
ALTER TABLE estado ADD COLUMN Fech_Modificacion DATE;

--Atributos de auditoria RESERVA:
ALTER TABLE reserva ADD COLUMN Crea_Por varchar (20);
ALTER TABLE reserva ADD COLUMN Fech_Creacion DATE;
ALTER TABLE reserva ADD COLUMN Modi_Por varchar (20);
ALTER TABLE reserva ADD COLUMN Fech_Modificacion DATE;

--Atributos de auditoria SUBCOMISION:
ALTER TABLE subcomision ADD COLUMN Crea_Por varchar (20);
ALTER TABLE subcomision ADD COLUMN Fech_Creacion DATE;
ALTER TABLE subcomision ADD COLUMN Modi_Por varchar (20);
ALTER TABLE subcomision ADD COLUMN Fech_Modificacion DATE;

--Atributos de auditoria TIPO_ACTIVIDAD:
ALTER TABLE tipo_actividad ADD COLUMN Crea_Por varchar (20);
ALTER TABLE tipo_actividad ADD COLUMN Fech_Creacion DATE;
ALTER TABLE tipo_actividad ADD COLUMN Modi_Por varchar (20);
ALTER TABLE tipo_actividad ADD COLUMN Fech_Modificacion DATE;

--Atributos de auditoria TIPO_DOCUMENTO
ALTER TABLE tipo_documento ADD COLUMN Crea_Por varchar (20);
ALTER TABLE tipo_documento ADD COLUMN Fech_Creacion DATE;
ALTER TABLE tipo_documento ADD COLUMN Modi_Por varchar (20);
ALTER TABLE tipo_documento ADD COLUMN Fech_Modificacion DATE;

--Atributo faltante en tabla Tipo_Actividad:
ALTER TABLE tipo_actividad ADD COLUMN Id_Estado INTEGER NOT NULL;
ALTER TABLE tipo_actividad ADD CONSTRAINT "Fk_TipoActividad_Estado" FOREIGN KEY (Id_Estado) REFERENCES estado(Id_Estado);

--Atributo faltante en tabla Reserva:
ALTER TABLE reserva ADD COLUMN Id_Reserva SERIAL PRIMARY KEY;
ALTER TABLE reserva ADD COLUMN Fech_Vto_Senia DATE;
ALTER TABLE reserva ALTER COLUMN Fech_Cobro DROP NOT NULL;

-- Agregar columna 'id_estado' a usuario_concurre_actividad
ALTER TABLE usuario_concurre_actividad ADD COLUMN id_estado INT;

-- Establecer clave foránea a tabla estado
ALTER TABLE usuario_concurre_actividad
ADD CONSTRAINT fk_estado
FOREIGN KEY (id_estado)
REFERENCES estado(id_estado);

--Elimina atributo id_usuario de la tabla actividad
ALTER TABLE actividad ALTER COLUMN id_usuario DROP NOT NULL;

INSERT INTO tipo_actividad (
  tipo, descripcion, comentarios_baja, crea_por, fech_creacion, modi_por, fech_modificacion, id_estado) 
  VALUES ('Taller Educativo','Actividad formativa orientada a adquirir nuevos conocimientos.', NULL, 'admin', '2025-06-19', NULL, NULL, 1);

INSERT INTO espacio (
  nombre,
  id_estado,
  capa_maxima,
  prec_rese_socio,
  prec_rese_no_socio,
  observaciones,
  fech_vige_precio,
  crea_por,
  fech_creacion
) VALUES (
  'Sala de Conferencias A',
  1,
  100,
  500.00,800.00,'Espacio climatizado con proyector y sonido.','2025-07-01',
  'admin','2025-06-19');

--inserts para modo_pago
INSERT INTO modo_pago (modo) VALUES ('Efectivo');
INSERT INTO modo_pago (modo) VALUES ('Transferencia');
INSERT INTO modo_pago (modo) VALUES ('Debito');
INSERT INTO modo_pago (modo) VALUES ('Tarjeta credito');

--inserts para cuota
INSERT INTO cuota (valo_cuota, mes_cuota) VALUES (500, 1);  -- Enero
INSERT INTO cuota (valo_cuota, mes_cuota) VALUES (500, 2);  -- Febrero
INSERT INTO cuota (valo_cuota, mes_cuota) VALUES (500, 3);  -- Marzo
INSERT INTO cuota (valo_cuota, mes_cuota) VALUES (550, 4);  -- Abril
INSERT INTO cuota (valo_cuota, mes_cuota) VALUES (550, 5);  -- Mayo
INSERT INTO cuota (valo_cuota, mes_cuota) VALUES (600, 6);  -- Junio
INSERT INTO cuota (valo_cuota, mes_cuota) VALUES (600, 7);  -- Julio
INSERT INTO cuota (valo_cuota, mes_cuota) VALUES (600, 8);  -- Agosto
INSERT INTO cuota (valo_cuota, mes_cuota) VALUES (650, 9);  -- Septiembre
INSERT INTO cuota (valo_cuota, mes_cuota) VALUES (650, 10); -- Octubre
INSERT INTO cuota (valo_cuota, mes_cuota) VALUES (700, 11); -- Noviembre
INSERT INTO cuota (valo_cuota, mes_cuota) VALUES (700, 12); -- Diciembre

--borrado y actualizacion de columnas en socio_paga_cuota
ALTER TABLE socio_paga_cuota DROP COLUMN Mont_Cobrado;
ALTER TABLE socio_paga_cuota ALTER COLUMN fech_cobro TYPE DATE;

--AGREGAR ESTADO PARA MANEJAR ROL-FUNCIONALIDAD
ALTER TABLE perfil_accede_funcionalidad
ADD COLUMN id_estado INTEGER;
ALTER TABLE perfil_accede_funcionalidad
ADD CONSTRAINT FK_estado_perfil_func
FOREIGN KEY (id_estado) REFERENCES estado(id_estado);

--Creacion de tabla Telefonos:
CREATE TABLE telefonos (id_telefono SERIAL PRIMARY KEY, id_usuario INTEGER NOT NULL, num_telefono INTEGER);
ALTER TABLE telefonos ADD CONSTRAINT FK_TEL_USUARIO FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario);
ALTER TABLE telefonos ADD CONSTRAINT unique_tel UNIQUE (num_telefono);
ALTER TABLE telefonos ALTER COLUMN num_telefono SET NOT NULL;

--Atributos auditoria PERFIL_ACCEDE_FUNCIONALIDAD:
ALTER TABLE perfil_accede_funcionalidad ADD COLUMN Crea_Por varchar (20);
ALTER TABLE perfil_accede_funcionalidad ADD COLUMN Fech_Creacion DATE;
ALTER TABLE perfil_accede_funcionalidad ADD COLUMN Modi_Por varchar (20);
ALTER TABLE perfil_accede_funcionalidad ADD COLUMN Fech_Modificacion DATE;

--Atributos de auditoria FUNCIONALIDAD
ALTER TABLE funcionalidad ADD COLUMN Crea_Por varchar (20);
ALTER TABLE funcionalidad ADD COLUMN Fech_Creacion DATE;
ALTER TABLE funcionalidad ADD COLUMN Modi_Por varchar (20);
ALTER TABLE funcionalidad ADD COLUMN Fech_Modificacion DATE;
--Atributos de auditoria Telefonos
ALTER TABLE telefonos ADD COLUMN Crea_Por varchar (20);
ALTER TABLE telefonos ADD COLUMN Fech_Creacion DATE;
ALTER TABLE telefonos ADD COLUMN Modi_Por varchar (20);
ALTER TABLE telefonos ADD COLUMN Fech_Modificacion DATE;

DROP TABLE telefono; --Tabla que ya existia previamente.

--para Spring AOP
ALTER TABLE auditoria
ADD COLUMN Operacion VARCHAR(100) NOT NULL DEFAULT 'SIN_REGISTRAR';

ALTER TABLE auditoria drop constraint auditoria_terminal_check;

--para que acepte mails
ALTER TABLE auditoria DROP CONSTRAINT auditoria_usuario_check;

ALTER TABLE auditoria ADD CONSTRAINT auditoria_usuario_check
    CHECK (
        Usuario ~ '^[A-Za-z0-9ÁÉÍÓÚáéíóúÑñü._%+-@]+$'
            AND LENGTH(TRIM(Usuario)) >= 1
            AND LENGTH(Usuario) <= 50
        );

