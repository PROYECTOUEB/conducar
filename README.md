# ConduCar – Sistema de información para un Centro de Enseñanza Automovilística

Proyecto final de Bases de Datos 1 – Universidad El Bosque, Bogotá.
Integrantes: Nicolás Rincón, Laura Morales, Juan Diego Contreras.
Docente: Ing., Esp., M.Ed. Christian Felipe Duarte.

## Arquitectura (MVC sin frameworks en el backend)

```
Angular (Vista)  --HTTP/JSON-->  Backend Java  --JDBC-->  MySQL 8 (base conducar)
localhost:4200     /api/...      localhost:8080            usuario consultor (solo SELECT)
```

| Capa | Qué hace | Dónde está |
|---|---|---|
| **Modelo** | Records con los datos de cada tabla o consulta | `backend/.../model/` |
| **Vista** | Pantallas en Angular | `frontend/src/app/features/` |
| **Controlador** | Atiende cada ruta `/api/...` y devuelve JSON | `backend/.../controller/` |
| Servicio | Reglas de negocio y validaciones | `backend/.../service/` |
| Repositorio | SQL con JDBC: `DriverManager`, `PreparedStatement`, `ResultSet` | `backend/.../repository/` |
| HTTP | Servidor HTTP del JDK, rutas y conversión a JSON hechas a mano | `backend/.../http/` |

El backend **no usa Spring Boot ni otro framework**. La única dependencia es el controlador JDBC de MySQL
(`mysql-connector-j`). La conexión se abre en `ConexionBD.java` con `DriverManager.getConnection(...)` y cada
consulta usa un `PreparedStatement` con parámetros `?`.

## Requisitos

MySQL Server 8.0 y Workbench, JDK 17 o superior (recomendado 21), Node.js 22 LTS y Visual Studio Code con
*Extension Pack for Java* y *Angular Language Service*.

## Cómo ejecutar

1. **Base de datos.** En Workbench, como root, ejecute en orden los scripts de `Base de Datos/`:
   `01_crear_tablas.sql`, `02_insertar_datos.sql` y `03_crear_usuarios.sql`.
2. **Backend.** Abra `backend/src/main/java/co/edu/unbosque/conducar/ConducarApplication.java` y haga clic en
   **Run** sobre `main`. Cuando la terminal diga `Backend ConduCar iniciado`, abra <http://localhost:8080/api/estado>.
3. **Frontend.** En otra terminal: `cd frontend`, `npm install` (solo la primera vez) y `npm start`.
   Abra <http://localhost:4200>.

## Conexión a MySQL

Los datos de conexión están en `backend/src/main/resources/config.properties`
(por defecto `localhost:3306`, base `conducar`, usuario `consultor`, contraseña `Consultor_2026`).
Para usar la máquina virtual, cambie `DB_PORT` (por ejemplo, a 3307) o defina la variable de entorno del mismo nombre.

## Endpoints

`/api/estado`, `/api/panel`, `/api/alumnos?q=`, `/api/alumnos/{id}`, `/api/matriculas?estado=`, `/api/instructores`,
`/api/vehiculos`, `/api/vehiculos/{placa}`, `/api/categorias`, `/api/catalogos`,
`/api/clases?estado=&tipo=&instructor=&placa=&desde=&hasta=`, `/api/consultas` y `/api/consultas/{n}`.
