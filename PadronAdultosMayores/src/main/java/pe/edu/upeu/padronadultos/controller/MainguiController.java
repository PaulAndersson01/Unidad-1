package pe.edu.upeu.padronadultos.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import pe.edu.upeu.padronadultos.config.AppContext;

import java.io.IOException;
import java.util.Map;


public class MainguiController {

    @FXML
    BorderPane bp;
    @FXML
    MenuBar menuBar;
    @FXML
    MenuItem menuItem1, menuItem2;
    @FXML
    TabPane tabPane;

    @FXML
    public void initialize() {
        MenuItemListener miL = new MenuItemListener();
        menuItem1.setOnAction(miL::handle);
        menuItem2.setOnAction(miL::handle);

        // Abre el módulo de Adultos Mayores apenas inicia la aplicación
        menuItem1.fire();
    }

    class MenuItemListener {

        Map<String, String[]> menuConfig = Map.of(
                "menuItem1", new String[]{"/view/main_adultomayor.fxml", "Adultos Mayores", "T"},
                "menuItem2", new String[]{"", "Salir", "C"}
        );

        public void handle(ActionEvent e) {
            String id = ((MenuItem) e.getSource()).getId();
            if (menuConfig.containsKey(id)) {
                String[] items = menuConfig.get(id);
                if (items[2].equals("C")) {
                    Platform.exit();
                    System.exit(0);
                } else {
                    abrirTabPaneFXML(items[0], items[1]);
                }
            }
        }

        private void abrirTabPaneFXML(String fxmlPath, String title) {
            try {
                AppContext context = AppContext.getInstance();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
                fxmlLoader.setControllerFactory(context::getBean);
                Parent root = fxmlLoader.load();

                ScrollPane scrollPane = new ScrollPane(root);
                scrollPane.setFitToWidth(true);
                scrollPane.setFitToHeight(true);
                Tab newTab = new Tab(title, scrollPane);
                tabPane.getTabs().clear();
                tabPane.getTabs().add(newTab);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
