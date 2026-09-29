package pe.edu.upeu.padronadultos.enums;

import lombok.Getter;


@Getter
public enum EstadoSalud {
    BUENO("Bueno"),
    REGULAR("Regular"),
    DELICADO("Delicado"),
    GRAVE("Grave");

    private final String descripcion;

    EstadoSalud(String descripcion) {
        this.descripcion = descripcion;
    }
}
