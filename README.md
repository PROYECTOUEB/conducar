# ConduCar – Sistema de información para un Centro de Enseñanza Automovilística

Proyecto final de **Bases de Datos 1** – Universidad El Bosque, Bogotá.
Integrantes: Nicolás Rincón, Laura Morales, Juan Diego Contreras.
Docente: Ing., Esp., M.Ed. Christian Felipe Duarte.

ConduCar administra una escuela de conducción (CEA) simulada en Bogotá que ofrece los cursos para las licencias
**A2** (motocicletas), **B1** (carros particulares) y **C1** (carros de servicio público): alumnos, matrículas, pagos,
clases de teoría, taller y práctica, instructores, vehículos, exámenes y certificados.

## Arquitectura

```
┌──────────────────────────┐   HTTP + JSON   ┌───────────────────────────────────────────┐   JDBC   ┌──────────────┐
│ Frontend: Angular 20     │ ──────────────▶ │ Backend: Spring Boot 3 (Java 17+)         │ ───────▶ │ MySQL 8      │
│ (Vista)                  │   /api/...      │ Controller → Service → Repository → Model │          │ base conducar│
│ http://localhost:4200    │                 │ http://localhost:8080                     │          │ consultor    │
└──────────────────────────┘                 └───────────────────────────────────────────┘          └──────────────┘
```

| Capa | Qué hace | Dónde está |
|---|---|---|
| **Modelo** | Clases (records) con los datos de cada consulta | `backend/.../model/` |
| **Vista** | Pantallas de la aplicación | `frontend/src/app/features/` |
| **Controlador** | Recibe las peticiones `/api/...` y responde JSON | `backend/.../controller/` |
| Servicio | Reglas de negocio (validaciones, cálculos, unir resultados) | `backend/.../service/` |
| Repositorio | Consultas SQL a MySQL con `JdbcTemplate` | `backend/.../repository/` |

La aplicación solo **consulta** datos: se conecta con el usuario `consultor`, que solo tiene permiso `SELECT`.

## Base de datos (13 tablas)

| Tabla | Contenido |
|---|---|
| `categoria_licencia` | Categorías A1 a C3 (Res. 1500 de 2005), edad mínima y valor del curso |
| `modulo` | Materias del curso: teoría, taller y práctica (Res. 20223040009425 de 2022) |
| `plan_curso` | Horas de cada módulo en cada categoría (N:M) |
| `alumno` | Datos del alumno |
| `certificado_crc` | Examen médico del Centro de Reconocimiento de Conductores |
| `instructor` | Instructores y su licencia |
| `instructor_categoria` | Categorías que puede enseñar cada instructor (N:M) |
| `vehiculo` | Vehículos de enseñanza, SOAT y revisión técnico-mecánica |
| `matricula` | Inscripción de un alumno en un curso |
| `pago` | Pagos de cada matrícula |
| `clase` | Clases dictadas, programadas o canceladas |
| `examen` | Exámenes teórico y práctico (se aprueba con 80 puntos) |
| `certificado_aptitud` | Certificado que se entrega al terminar el curso (1:1 con matrícula) |

Scripts en `database/` (ejecutar en orden): `01_crear_tablas.sql`, `02_insertar_datos.sql` (2 325 registros simulados),
`03_crear_usuarios.sql` (usuarios `admin`, `gestionador` y `consultor`) y `04_consultas.sql` (42 consultas de ejemplo).

## Requisitos

| Programa | Versión |
|---|---|
| MySQL Server (y Workbench, opcional) | 8.0.31 o superior |
| JDK (Java) | 17 o superior (recomendado 21) |
| Node.js | 22 LTS |
| Visual Studio Code | con *Extension Pack for Java*, *Spring Boot Extension Pack* y *Angular Language Service* |

## Cómo ejecutar

### 1. Base de datos

En MySQL Workbench (como `root`) abra y ejecute en orden los scripts 01, 02 y 03.
Sin Workbench, desde una terminal en la carpeta del proyecto:

```powershell
$mysql = "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe"
& $mysql -u root -p --default-character-set=utf8mb4 -e "source database/01_crear_tablas.sql"
& $mysql -u root -p --default-character-set=utf8mb4 -e "source database/02_insertar_datos.sql"
& $mysql -u root -p --default-character-set=utf8mb4 -e "source database/03_crear_usuarios.sql"
```

### 2. Backend

Abra la carpeta en VS Code, espere a que cargue el proyecto de Java y haga clic en **Run** sobre `main` en
`backend/src/main/java/co/edu/unbosque/conducar/ConducarApplication.java`.
Prueba: <http://localhost:8080/api/estado>

### 3. Frontend

En otra terminal:

```powershell
cd frontend
npm install      # solo la primera vez
npm start
```

Abra <http://localhost:4200>.

### Conexión a MySQL

Los datos están en `backend/src/main/resources/application.properties` (por defecto `localhost:3306`, base `conducar`,
usuario `consultor`, contraseña `Consultor_2026`). También se pueden cambiar con las variables de entorno
`DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER` y `DB_PASSWORD`.

## Endpoints

| Ruta | Descripción |
|---|---|
| `GET /api/panel` | Indicadores, alertas y estadísticas |
| `GET /api/clases?estado=&tipo=&instructor=&placa=&desde=&hasta=` | Agenda de clases |
| `GET /api/alumnos?q=` · `GET /api/alumnos/{id}` | Alumnos y detalle |
| `GET /api/matriculas?estado=` | Matrículas con pagos y saldo |
| `GET /api/instructores` | Instructores |
| `GET /api/vehiculos` · `GET /api/vehiculos/{placa}` | Vehículos y detalle |
| `GET /api/categorias` | Categorías de licencia y plan de estudios |
| `GET /api/catalogos` | Valores para los filtros |
| `GET /api/consultas` · `GET /api/consultas/{n}` | Consultas SQL de ejemplo ejecutadas en vivo |
| `GET /api/estado` | Estado de la conexión |

## Problemas frecuentes

| Mensaje | Solución |
|---|---|
| `Access denied for user 'consultor'` | Ejecute `03_crear_usuarios.sql` o revise la contraseña en `application.properties`. |
| `Communications link failure` | Inicie el servicio MySQL80 en *Servicios* de Windows. |
| `Port 8080 was already in use` | Ya hay un backend abierto; ciérrelo antes de volver a ejecutar. |
| La página dice *No hay conexión con el backend* | Inicie el backend (paso 2). |
