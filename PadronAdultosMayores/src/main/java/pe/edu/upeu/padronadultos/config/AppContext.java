package pe.edu.upeu.padronadultos.config;

import pe.edu.upeu.padronadultos.controller.AdultoMayorController;
import pe.edu.upeu.padronadultos.controller.MainguiController;
import pe.edu.upeu.padronadultos.repository.AdultoMayorRepository;
import pe.edu.upeu.padronadultos.service.IAdultoMayorService;
import pe.edu.upeu.padronadultos.service.impl.AdultoMayorServiceImp;

import java.util.HashMap;
import java.util.Map;


public class AppContext {

    // Singleton: una sola instancia en toda la app
    private static AppContext instance;

    public static synchronized AppContext getInstance() {
        if (instance == null) instance = new AppContext();
        return instance;
    }

    // El "directorio": Clase -> Objeto
    private final Map<Class<?>, Object> contenedor = new HashMap<>();

    // Constructor privado: aquí se arma toda la aplicación
    private AppContext() {
        registrarRepositorios();
        registrarServicios();
        registrarControladores();
    }

    // CAPA 1 — REPOSITORIOS (base de datos en memoria: ArrayList)
    private void registrarRepositorios() {
        AdultoMayorRepository adultoMayorRepository = new AdultoMayorRepository();
        adultoMayorRepository.seedData(); // datos de ejemplo
        registrar(AdultoMayorRepository.class, adultoMayorRepository);
    }

    // CAPA 2 — SERVICIOS: cada uno recibe su repositorio por constructor
    private void registrarServicios() {
        registrar(IAdultoMayorService.class,
                new AdultoMayorServiceImp(getBean(AdultoMayorRepository.class)));
    }

    // CAPA 3 — CONTROLADORES JavaFX: el FXMLLoader los busca aquí (setControllerFactory)
    private void registrarControladores() {
        registrar(AdultoMayorController.class,
                new AdultoMayorController(getBean(IAdultoMayorService.class)));
        registrar(MainguiController.class, new MainguiController());
    }

    /** Guarda un objeto en el directorio, indexado por su tipo o interfaz. */
    private void registrar(Class<?> tipo, Object bean) {
        contenedor.put(tipo, bean);
    }

    /** Busca y devuelve un objeto por su tipo o interfaz. */
    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> tipo) {
        Object bean = contenedor.get(tipo);
        if (bean == null) {
            bean = contenedor.values().stream()
                    .filter(b -> tipo.isAssignableFrom(b.getClass()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException(
                            "Bean no encontrado: " + tipo.getName() +
                                    "\n→ ¿Lo registraste en AppContext?"));
        }
        return (T) bean;
    }
}
