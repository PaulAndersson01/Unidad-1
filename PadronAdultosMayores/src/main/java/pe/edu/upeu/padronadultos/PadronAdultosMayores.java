package pe.edu.upeu.padronadultos;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import pe.edu.upeu.padronadultos.config.AppContext;

import java.io.IOException;

/**
 * Ciclo de vida de la aplicación JavaFX.
 * init(): arranca el contenedor de DI. start(): carga la vista principal y muestra la ventana.
 */
public class PadronAdultosMayores extends Application {

    private AppContext context;

    @Override
    public void init() {
        System.out.println("Iniciando AppContext...");
        context = AppContext.getInstance(); // dispara el registro de repos/servicios/controladores
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/maingui.fxml"));
        loader.setControllerFactory(context::getBean);
        Parent parent = loader.load();

        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        Scene scene = new Scene(parent, bounds.getWidth(), bounds.getHeight() - 100);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

        stage.setScene(scene);
        stage.setTitle("Padrón de Adultos Mayores");
        stage.setResizable(true);
        stage.show();
    }
}
