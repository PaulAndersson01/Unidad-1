package pe.edu.upeu.padronadultos.service.impl;

import pe.edu.upeu.padronadultos.dto.ComboBoxOption;
import pe.edu.upeu.padronadultos.enums.EstadoSalud;
import pe.edu.upeu.padronadultos.enums.TipoPension;
import pe.edu.upeu.padronadultos.model.AdultoMayor;
import pe.edu.upeu.padronadultos.repository.AdultoMayorRepository;
import pe.edu.upeu.padronadultos.repository.ICrudGenericoRepository;
import pe.edu.upeu.padronadultos.service.IAdultoMayorService;

import java.util.ArrayList;
import java.util.List;

public class AdultoMayorServiceImp extends CrudGenericoServiceImp<AdultoMayor, Long>
        implements IAdultoMayorService {

    private final AdultoMayorRepository adultoMayorRepository;

    public AdultoMayorServiceImp(AdultoMayorRepository adultoMayorRepository) {
        this.adultoMayorRepository = adultoMayorRepository;
    }

    @Override
    protected ICrudGenericoRepository<AdultoMayor, Long> getRepo() {
        return adultoMayorRepository;
    }

    @Override
    public List<ComboBoxOption> listarTipoPension() {
        List<ComboBoxOption> listar = new ArrayList<>();
        for (TipoPension tp : TipoPension.values()) {
            listar.add(new ComboBoxOption(tp.name(), tp.getDescripcion()));
        }
        return listar;
    }

    @Override
    public List<ComboBoxOption> listarEstadoSalud() {
        List<ComboBoxOption> listar = new ArrayList<>();
        for (EstadoSalud es : EstadoSalud.values()) {
            listar.add(new ComboBoxOption(es.name(), es.getDescripcion()));
        }
        return listar;
    }

    @Override
    public List<AdultoMayor> filtrar(Integer edadMin, Integer edadMax, TipoPension tipoPension) {
        return adultoMayorRepository.findAll().stream()
                .filter(a -> edadMin == null || a.getEdad() >= edadMin)
                .filter(a -> edadMax == null || a.getEdad() <= edadMax)
                .filter(a -> tipoPension == null || a.getTipoPension() == tipoPension)
                .toList();
    }

    @Override
    public boolean existeCurp(String curp, Long idExcluido) {
        return adultoMayorRepository.findAll().stream()
                .anyMatch(a -> a.getCurp().equalsIgnoreCase(curp)
                        && (idExcluido == null || !a.getId().equals(idExcluido)));
    }
}
