module pe.edu.upeu.padronadultos {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires static lombok;
    requires java.logging;
    requires jakarta.validation;
    requires javafx.graphics;

    opens pe.edu.upeu.padronadultos.controller to javafx.fxml;
    opens pe.edu.upeu.padronadultos.model;
    opens pe.edu.upeu.padronadultos to javafx.fxml;
    exports pe.edu.upeu.padronadultos;
}
