package pe.edu.upeu.padronadultos.enums;

import lombok.Getter;


@Getter
public enum TipoPension {
    JUBILACION("Jubilación"),
    VIUDEZ("Viudez"),
    INVALIDEZ("Invalidez"),
    NO_CONTRIBUTIVA("Pensión no contributiva"),
    SIN_PENSION("Sin pensión");

    private final String descripcion;

    TipoPension(String descripcion) {
        this.descripcion = descripcion;
    }
}
