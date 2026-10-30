-- ConduCar: creación de la base de datos y las tablas

DROP DATABASE IF EXISTS conducar;
CREATE DATABASE conducar;
USE conducar;

CREATE TABLE categoria_licencia (
  codigo_categoria VARCHAR(2) PRIMARY KEY,
  descripcion VARCHAR(150) NOT NULL,
  tipo_servicio VARCHAR(10) NOT NULL,
  edad_minima SMALLINT NOT NULL,
  valor_curso DECIMAL(10,2),
  CHECK (tipo_servicio IN ('PARTICULAR','PUBLICO')),
  CHECK (edad_minima >= 16)
);

CREATE TABLE alumno (
  id_alumno INT PRIMARY KEY,
  tipo_documento VARCHAR(2) NOT NULL,
  numero_documento VARCHAR(15) NOT NULL,
  nombres VARCHAR(60) NOT NULL,
  apellidos VARCHAR(60) NOT NULL,
  fecha_nacimiento DATE NOT NULL,
  telefono VARCHAR(15),
  correo VARCHAR(100) NOT NULL UNIQUE,
  fecha_registro DATE NOT NULL,
  UNIQUE (tipo_documento, numero_documento),
  CHECK (tipo_documento IN ('CC','TI','CE','PA'))
);

CREATE TABLE certificado_crc (
  id_certificado INT PRIMARY KEY,
  id_alumno INT NOT NULL,
  nombre_crc VARCHAR(80) NOT NULL,
  fecha_examen DATE NOT NULL,
  resultado VARCHAR(8) NOT NULL,
  CHECK (resultado IN ('APTO','NO APTO')),
  FOREIGN KEY (id_alumno) REFERENCES alumno(id_alumno)
);

CREATE TABLE instructor (
  id_instructor INT PRIMARY KEY,
  numero_documento VARCHAR(15) NOT NULL UNIQUE,
  nombres VARCHAR(60) NOT NULL,
  apellidos VARCHAR(60) NOT NULL,
  telefono VARCHAR(15),
  correo VARCHAR(100) NOT NULL UNIQUE,
  numero_licencia_instructor VARCHAR(20) NOT NULL UNIQUE,
  fecha_venc_licencia DATE NOT NULL,
  estado VARCHAR(8) NOT NULL,
  CHECK (estado IN ('ACTIVO','INACTIVO'))
);

CREATE TABLE instructor_categoria (
  id_instructor INT NOT NULL,
  codigo_categoria VARCHAR(2) NOT NULL,
  fecha_habilitacion DATE NOT NULL,
  PRIMARY KEY (id_instructor, codigo_categoria),
  FOREIGN KEY (id_instructor) REFERENCES instructor(id_instructor),
  FOREIGN KEY (codigo_categoria) REFERENCES categoria_licencia(codigo_categoria)
);

CREATE TABLE vehiculo (
  placa VARCHAR(7) PRIMARY KEY,
  codigo_categoria VARCHAR(2) NOT NULL,
  tipo_vehiculo VARCHAR(12) NOT NULL,
  marca VARCHAR(30) NOT NULL,
  linea VARCHAR(30) NOT NULL,
  modelo_anio SMALLINT NOT NULL,
  transmision VARCHAR(10) NOT NULL,
  estado VARCHAR(17) NOT NULL,
  fecha_venc_soat DATE NOT NULL,
  fecha_venc_rtm DATE NOT NULL,
  CHECK (tipo_vehiculo IN ('MOTOCICLETA','AUTOMOVIL','CAMIONETA')),
  CHECK (transmision IN ('MECANICA','AUTOMATICA')),
  CHECK (estado IN ('DISPONIBLE','EN_MANTENIMIENTO','FUERA_DE_SERVICIO')),
  FOREIGN KEY (codigo_categoria) REFERENCES categoria_licencia(codigo_categoria)
);

CREATE TABLE modulo (
  id_modulo INT PRIMARY KEY,
  nombre VARCHAR(80) NOT NULL UNIQUE,
  tipo VARCHAR(8) NOT NULL,
  descripcion VARCHAR(200) NOT NULL,
  CHECK (tipo IN ('TEORIA','TALLER','PRACTICA'))
);

CREATE TABLE plan_curso (
  codigo_categoria VARCHAR(2) NOT NULL,
  id_modulo INT NOT NULL,
  horas SMALLINT NOT NULL,
  PRIMARY KEY (codigo_categoria, id_modulo),
  CHECK (horas > 0),
  FOREIGN KEY (codigo_categoria) REFERENCES categoria_licencia(codigo_categoria),
  FOREIGN KEY (id_modulo) REFERENCES modulo(id_modulo)
);

CREATE TABLE matricula (
  id_matricula INT PRIMARY KEY,
  id_alumno INT NOT NULL,
  codigo_categoria VARCHAR(2) NOT NULL,
  fecha_matricula DATE NOT NULL,
  valor_pactado DECIMAL(10,2) NOT NULL,
  estado VARCHAR(10) NOT NULL,
  CHECK (valor_pactado > 0),
  CHECK (estado IN ('ACTIVA','FINALIZADA','CANCELADA')),
  FOREIGN KEY (id_alumno) REFERENCES alumno(id_alumno),
  FOREIGN KEY (codigo_categoria) REFERENCES categoria_licencia(codigo_categoria)
);

CREATE TABLE pago (
  id_pago INT PRIMARY KEY,
  id_matricula INT NOT NULL,
  fecha_pago DATE NOT NULL,
  valor DECIMAL(10,2) NOT NULL,
  medio_pago VARCHAR(13) NOT NULL,
  CHECK (valor > 0),
  CHECK (medio_pago IN ('EFECTIVO','TARJETA','TRANSFERENCIA')),
  FOREIGN KEY (id_matricula) REFERENCES matricula(id_matricula)
);

CREATE TABLE clase (
  id_clase INT PRIMARY KEY,
  id_matricula INT NOT NULL,
  id_modulo INT NOT NULL,
  id_instructor INT NOT NULL,
  placa VARCHAR(7),
  fecha DATE NOT NULL,
  hora_inicio TIME NOT NULL,
  horas SMALLINT NOT NULL,
  estado VARCHAR(10) NOT NULL,
  CHECK (horas BETWEEN 1 AND 4),
  CHECK (estado IN ('PROGRAMADA','DICTADA','CANCELADA')),
  FOREIGN KEY (id_matricula) REFERENCES matricula(id_matricula),
  FOREIGN KEY (id_modulo) REFERENCES modulo(id_modulo),
  FOREIGN KEY (id_instructor) REFERENCES instructor(id_instructor),
  FOREIGN KEY (placa) REFERENCES vehiculo(placa)
);

CREATE TABLE examen (
  id_examen INT PRIMARY KEY,
  id_matricula INT NOT NULL,
  tipo VARCHAR(8) NOT NULL,
  fecha DATE NOT NULL,
  puntaje DECIMAL(5,2) NOT NULL,
  id_instructor INT NOT NULL,
  CHECK (tipo IN ('TEORICO','PRACTICO')),
  CHECK (puntaje BETWEEN 0 AND 100),
  FOREIGN KEY (id_matricula) REFERENCES matricula(id_matricula),
  FOREIGN KEY (id_instructor) REFERENCES instructor(id_instructor)
);

CREATE TABLE certificado_aptitud (
  id_certificado INT PRIMARY KEY,
  id_matricula INT NOT NULL UNIQUE,
  numero_certificado VARCHAR(20) NOT NULL UNIQUE,
  fecha_expedicion DATE NOT NULL,
  FOREIGN KEY (id_matricula) REFERENCES matricula(id_matricula)
);

