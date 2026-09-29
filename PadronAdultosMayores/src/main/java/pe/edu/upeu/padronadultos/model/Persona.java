package pe.edu.upeu.padronadultos.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Clase abstracta base del dominio: datos comunes de cualquier persona registrada en un padrón.
 * Las subclases deben implementar {@link #obtenerResumen()} (polimorfismo).
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public abstract class Persona {

    private Long id;

    @NotBlank(message = "El nombre completo es obligatorio")
    @Size(max = 100, message = "El nombre completo no debe superar los 100 caracteres")
    private String nombreCompleto;

    @NotNull(message = "La edad es obligatoria")
    @Min(value = 60, message = "Un adulto mayor debe tener 60 años o más")
    @Max(value = 120, message = "La edad no puede ser mayor a 120 años")
    private Integer edad;

    // El patrón acepta vacío para que, en ese caso, solo se muestre el mensaje de "obligatorio".
    @NotBlank(message = "El CURP es obligatorio")
    @Pattern(regexp = "^$|^[A-Z]{4}\\d{6}[HM][A-Z]{5}[A-Z0-9]\\d$",
            message = "CURP inválido: debe tener 18 caracteres (ej. GOLJ480720HDFMPN05)")
    private String curp;

    @NotBlank(message = "El domicilio es obligatorio")
    @Size(max = 150, message = "El domicilio no debe superar los 150 caracteres")
    private String domicilio;

    /** Descripción corta del registro; cada subclase la implementa a su manera. */
    public abstract String obtenerResumen();
}
