package pe.edu.upeu.padronadultos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class ComboBoxOption {
    String key;
    String value; // etiqueta = texto visible

    @Override
    public String toString() {
        return value;
    }
}
