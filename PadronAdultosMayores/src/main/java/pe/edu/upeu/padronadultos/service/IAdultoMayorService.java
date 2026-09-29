package pe.edu.upeu.padronadultos.service;

import pe.edu.upeu.padronadultos.dto.ComboBoxOption;
import pe.edu.upeu.padronadultos.enums.TipoPension;
import pe.edu.upeu.padronadultos.model.AdultoMayor;

import java.util.List;

public interface IAdultoMayorService extends ICrudGenericoService<AdultoMayor, Long> {

    List<ComboBoxOption> listarTipoPension();

    List<ComboBoxOption> listarEstadoSalud();


    List<AdultoMayor> filtrar(Integer edadMin, Integer edadMax, TipoPension tipoPension);

    /** Indica si ya existe un registro con ese CURP (excluyendo el id indicado, útil al editar). */
    boolean existeCurp(String curp, Long idExcluido);
}
