package pe.edu.upeu.padronadultos.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pe.edu.upeu.padronadultos.enums.EstadoSalud;
import pe.edu.upeu.padronadultos.enums.TipoPension;


@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class AdultoMayor extends Persona {

    @NotNull(message = "El tipo de pensión es obligatorio")
    private TipoPension tipoPension;

    @NotNull(message = "El estado de salud es obligatorio")
    private EstadoSalud estadoSalud;

    public AdultoMayor(Long id, String nombreCompleto, Integer edad, String curp, String domicilio,
                       TipoPension tipoPension, EstadoSalud estadoSalud) {
        super(id, nombreCompleto, edad, curp, domicilio);
        this.tipoPension = tipoPension;
        this.estadoSalud = estadoSalud;
    }

    @Override
    public String obtenerResumen() {
        String pension = tipoPension == null ? "-" : tipoPension.getDescripcion();
        String salud = estadoSalud == null ? "-" : estadoSalud.getDescripcion();
        return getNombreCompleto() + " (" + getEdad() + " años)\nPensión: " + pension + " | Salud: " + salud;
    }
}
