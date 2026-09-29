# Padrón de Adultos Mayores (Caso 4) — JavaFX + Maven

CRUD de escritorio para registrar personas de la tercera edad.
Datos por registro: nombre completo, edad, CURP, domicilio, tipo de pensión y estado de salud general.
La tabla se puede filtrar por **rango de edad** y por **tipo de pensión**.

- Java 21, JavaFX 21, Maven, Lombok, Jakarta Validation (Hibernate Validator)
- Base de datos: `ArrayList` en memoria (los datos se pierden al cerrar la aplicación)
- Arquitectura en capas: `Model → Repository → Service → Controller → View (FXML)`
- Inyección de dependencias manual con `AppContext` (sin Spring)

## Ejecutar

```bash
mvn clean javafx:run
```

En IntelliJ IDEA: abrir la carpeta del proyecto (pom.xml) y ejecutar `javafx:run` desde la pestaña Maven
(Plugins → javafx → javafx:run).

## Estructura

```
pe.edu.upeu.padronadultos
 ├── App / PadronAdultosMayores   arranque de la aplicación JavaFX
 ├── config/AppContext            contenedor de DI (Singleton)
 ├── model/                       Persona (abstracta) → AdultoMayor
 ├── enums/                       TipoPension, EstadoSalud
 ├── repository/                  ICrudGenericoRepository, AbstractJpaRepository, AdultoMayorRepository
 ├── service/ (+ impl/)           ICrudGenericoService, IAdultoMayorService, servicios genéricos y concretos
 ├── controller/                  MainguiController, AdultoMayorController
 ├── components/                  TableViewHelper, ComboBoxAutoComplete, Toast, ToltipCustom, ColumnInfo
 ├── dto/                         ComboBoxOption
 └── exception/                   ModelNotFoundException
```
