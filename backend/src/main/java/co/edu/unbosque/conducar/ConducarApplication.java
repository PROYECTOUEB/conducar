package co.edu.unbosque.conducar;

import co.edu.unbosque.conducar.config.Configuracion;
import co.edu.unbosque.conducar.controller.AlumnoController;
import co.edu.unbosque.conducar.controller.CatalogoController;
import co.edu.unbosque.conducar.controller.CategoriaController;
import co.edu.unbosque.conducar.controller.ClaseController;
import co.edu.unbosque.conducar.controller.ConsultaController;
import co.edu.unbosque.conducar.controller.EstadoController;
import co.edu.unbosque.conducar.controller.InstructorController;
import co.edu.unbosque.conducar.controller.MatriculaController;
import co.edu.unbosque.conducar.controller.PanelController;
import co.edu.unbosque.conducar.controller.VehiculoController;
import co.edu.unbosque.conducar.http.Router;
import co.edu.unbosque.conducar.http.Servidor;
import co.edu.unbosque.conducar.repository.AlumnoRepository;
import co.edu.unbosque.conducar.repository.CategoriaRepository;
import co.edu.unbosque.conducar.repository.ClaseRepository;
import co.edu.unbosque.conducar.repository.ConexionBD;
import co.edu.unbosque.conducar.repository.ConsultaRepository;
import co.edu.unbosque.conducar.repository.EstadoRepository;
import co.edu.unbosque.conducar.repository.InstructorRepository;
import co.edu.unbosque.conducar.repository.MatriculaRepository;
import co.edu.unbosque.conducar.repository.PanelRepository;
import co.edu.unbosque.conducar.repository.VehiculoRepository;
import co.edu.unbosque.conducar.service.AlumnoService;
import co.edu.unbosque.conducar.service.CatalogoService;
import co.edu.unbosque.conducar.service.CategoriaService;
import co.edu.unbosque.conducar.service.ClaseService;
import co.edu.unbosque.conducar.service.ConsultaService;
import co.edu.unbosque.conducar.service.EstadoService;
import co.edu.unbosque.conducar.service.InstructorService;
import co.edu.unbosque.conducar.service.MatriculaService;
import co.edu.unbosque.conducar.service.PanelService;
import co.edu.unbosque.conducar.service.VehiculoService;

/**
 * Punto de entrada del backend de ConduCar.
 * Crea a mano todas las capas del patrón MVC (sin frameworks):
 * Repository (SQL con JDBC) -> Service (reglas) -> Controller (rutas /api) -> Servidor HTTP.
 */
public class ConducarApplication {

    public static void main(String[] args) throws Exception {
        Configuracion config = new Configuracion();
        ConexionBD conexionBD = new ConexionBD(config);

        // Capa de acceso a datos
        AlumnoRepository alumnoRepo = new AlumnoRepository(conexionBD);
        CategoriaRepository categoriaRepo = new CategoriaRepository(conexionBD);
        ClaseRepository claseRepo = new ClaseRepository(conexionBD);
        ConsultaRepository consultaRepo = new ConsultaRepository(conexionBD);
        EstadoRepository estadoRepo = new EstadoRepository(conexionBD);
        InstructorRepository instructorRepo = new InstructorRepository(conexionBD);
        MatriculaRepository matriculaRepo = new MatriculaRepository(conexionBD);
        PanelRepository panelRepo = new PanelRepository(conexionBD);
        VehiculoRepository vehiculoRepo = new VehiculoRepository(conexionBD);

        // Capa de servicios y controladores; cada controlador registra sus rutas
        Router router = new Router();
        new AlumnoController(new AlumnoService(alumnoRepo, claseRepo)).registrar(router);
        new CatalogoController(new CatalogoService(instructorRepo, vehiculoRepo)).registrar(router);
        new CategoriaController(new CategoriaService(categoriaRepo)).registrar(router);
        new ClaseController(new ClaseService(claseRepo)).registrar(router);
        new ConsultaController(new ConsultaService(consultaRepo)).registrar(router);
        new EstadoController(new EstadoService(estadoRepo)).registrar(router);
        new InstructorController(new InstructorService(instructorRepo)).registrar(router);
        new MatriculaController(new MatriculaService(matriculaRepo)).registrar(router);
        new PanelController(new PanelService(panelRepo, claseRepo)).registrar(router);
        new VehiculoController(new VehiculoService(vehiculoRepo, claseRepo)).registrar(router);

        int puerto = config.entero("SERVER_PORT");
        Servidor servidor = new Servidor(puerto, router);
        servidor.iniciar();
        System.out.println("Backend ConduCar iniciado en http://localhost:" + puerto + "/api/estado");
        System.out.println("Base de datos: " + conexionBD.getUrl() + " (usuario " + conexionBD.getUsuario() + ")");
    }
}
