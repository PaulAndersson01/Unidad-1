package pe.edu.upeu.padronadultos;

import javafx.application.Application;

/**
 * Punto de entrada plano (main) que delega en Application.launch(...),
 * evitando problemas de classloading con el sistema de módulos.
 */
public class App {
    public static void main(String[] args) {
        Application.launch(PadronAdultosMayores.class, args);
    }
}
