package pe.edu.upeu.padronadultos.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import pe.edu.upeu.padronadultos.components.ColumnInfo;
import pe.edu.upeu.padronadultos.components.ComboBoxAutoComplete;
import pe.edu.upeu.padronadultos.components.TableViewHelper;
import pe.edu.upeu.padronadultos.components.Toast;
import pe.edu.upeu.padronadultos.components.ToltipCustom;
import pe.edu.upeu.padronadultos.dto.ComboBoxOption;
import pe.edu.upeu.padronadultos.enums.EstadoSalud;
import pe.edu.upeu.padronadultos.enums.TipoPension;
import pe.edu.upeu.padronadultos.model.AdultoMayor;
import pe.edu.upeu.padronadultos.model.Persona;
import pe.edu.upeu.padronadultos.service.IAdultoMayorService;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;


public class AdultoMayorController {

    // Formulario xd
    @FXML private TextField txtNombreCompleto, txtEdad, txtCurp, txtDomicilio;
    @FXML private ComboBox<ComboBoxOption> cbxTipoPension, cbxEstadoSalud;

    // Filtros
    @FXML private TextField txtEdadMin, txtEdadMax;
    @FXML private ComboBox<ComboBoxOption> cbxFiltroPension;

    // Tabla y mensajes
    @FXML private TableView<AdultoMayor> tableView;
    @FXML private Label lbnMsg, lblFiltroMsg, lblTotal;
    @FXML private AnchorPane miContenedor;

    private final IAdultoMayorService service;

    public AdultoMayorController(IAdultoMayorService service) {
        this.service = service;
    }

    private Validator validator;
    private AdultoMayor formulario;
    private Long idAdultoCE = 0L; // 0 = modo creación; > 0 = id del registro en edición

    private final ToltipCustom ttc = new ToltipCustom();


    //  INICIALIZACIÓN

    @FXML
    public void initialize() {
        cbxTipoPension.getItems().addAll(service.listarTipoPension());
        new ComboBoxAutoComplete<>(cbxTipoPension);

        cbxEstadoSalud.getItems().addAll(service.listarEstadoSalud());
        new ComboBoxAutoComplete<>(cbxEstadoSalud);

        // primera opción
        cbxFiltroPension.getItems().add(new ComboBoxOption("", "Todos los tipos"));
        cbxFiltroPension.getItems().addAll(service.listarTipoPension());
        cbxFiltroPension.getSelectionModel().selectFirst();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        TableViewHelper<AdultoMayor> tableViewHelper = new TableViewHelper<>();
        LinkedHashMap<String, ColumnInfo> columns = new LinkedHashMap<>();
        columns.put("ID", new ColumnInfo("id", 50.0));
        columns.put("Nombre completo", new ColumnInfo("nombreCompleto", 220.0));
        columns.put("Edad", new ColumnInfo("edad", 60.0));
        columns.put("CURP", new ColumnInfo("curp", 170.0));
        columns.put("Domicilio", new ColumnInfo("domicilio", 190.0));
        columns.put("Tipo de pensión", new ColumnInfo("tipoPension.descripcion", 150.0));
        columns.put("Estado de salud", new ColumnInfo("estadoSalud.descripcion", 120.0));

        Consumer<AdultoMayor> updateAction = this::editForm;
        Consumer<AdultoMayor> deleteAction = this::confirmarEliminar;

        tableViewHelper.addColumnsInOrderWithSize(tableView, columns, updateAction, deleteAction);
        tableView.setTableMenuButtonVisible(true);

        lbnMsg.setText("");
        lblFiltroMsg.setText("");
        filtrarTabla();
    }


    //  FILTROS

    @FXML
    public void aplicarFiltro() {
        filtrarTabla();
    }

    @FXML
    public void limpiarFiltro() {
        txtEdadMin.clear();
        txtEdadMax.clear();
        cbxFiltroPension.getSelectionModel().selectFirst();
        filtrarTabla();
    }


    private boolean filtrarTabla() {
        Integer min;
        Integer max;
        try {
            min = parseEntero(txtEdadMin.getText());
            max = parseEntero(txtEdadMax.getText());
        } catch (NumberFormatException e) {
            mostrarErrorFiltro("La edad debe ser un número entero");
            return false;
        }
        if (min != null && max != null && min > max) {
            mostrarErrorFiltro("La edad mínima no puede ser mayor que la máxima");
            return false;
        }

        ComboBoxOption opcion = cbxFiltroPension.getSelectionModel().getSelectedItem();
        TipoPension tipo = (opcion == null || opcion.getKey().isEmpty())
                ? null : TipoPension.valueOf(opcion.getKey());

        lblFiltroMsg.setText("");
        List<AdultoMayor> resultado = service.filtrar(min, max, tipo);
        tableView.setItems(FXCollections.observableArrayList(resultado));
        lblTotal.setText("Mostrando " + resultado.size() + " de " + service.findAll().size() + " registros");
        return true;
    }

    private void mostrarErrorFiltro(String mensaje) {
        lblFiltroMsg.setText(mensaje);
        lblFiltroMsg.setStyle("-fx-text-fill: red;");
    }

    private Integer parseEntero(String texto) {
        if (texto == null || texto.trim().isEmpty()) return null;
        return Integer.parseInt(texto.trim());
    }


    //  CREAR Y ACTUALIZAR

    @FXML
    public void validarFormulario() {
        limpiarError();

        Integer edad;
        try {
            edad = parseEntero(txtEdad.getText());
        } catch (NumberFormatException e) {
            ttc.marcarError(txtEdad, "La edad debe ser un número entero");
            mostrarMensaje("La edad debe ser un número entero", true);
            Platform.runLater(txtEdad::requestFocus);
            return;
        }

        formulario = new AdultoMayor();
        formulario.setNombreCompleto(txtNombreCompleto.getText().trim());
        formulario.setEdad(edad);
        formulario.setCurp(txtCurp.getText().trim().toUpperCase());
        formulario.setDomicilio(txtDomicilio.getText().trim());

        ComboBoxOption tp = cbxTipoPension.getSelectionModel().getSelectedItem();
        formulario.setTipoPension(tp == null ? null : TipoPension.valueOf(tp.getKey()));

        ComboBoxOption es = cbxEstadoSalud.getSelectionModel().getSelectedItem();
        formulario.setEstadoSalud(es == null ? null : EstadoSalud.valueOf(es.getKey()));

        Set<ConstraintViolation<AdultoMayor>> violaciones = validator.validate(formulario);
        List<ConstraintViolation<AdultoMayor>> violacionesOrdenadas = violaciones.stream()
                .sorted(Comparator.comparing(v -> v.getPropertyPath().toString()))
                .toList();

        if (!violacionesOrdenadas.isEmpty()) {
            mostrarErroresValidacion(violacionesOrdenadas);
            return;
        }

        // Regla de negocio: el CURP no puede repetirse
        Long idExcluido = idAdultoCE > 0L ? idAdultoCE : null;
        if (service.existeCurp(formulario.getCurp(), idExcluido)) {
            ttc.marcarError(txtCurp, "Ya existe un registro con este CURP");
            mostrarMensaje("Ya existe un registro con este CURP", true);
            Platform.runLater(txtCurp::requestFocus);
            return;
        }

        procesarFormulario();
    }

    private void mostrarErroresValidacion(List<ConstraintViolation<AdultoMayor>> violaciones) {
        Map<String, Control> campos = new LinkedHashMap<>();
        campos.put("nombreCompleto", txtNombreCompleto);
        campos.put("edad", txtEdad);
        campos.put("curp", txtCurp);
        campos.put("domicilio", txtDomicilio);
        campos.put("tipoPension", cbxTipoPension);
        campos.put("estadoSalud", cbxEstadoSalud);

        String primerMensaje = null;
        Control primerControl = null;

        for (Map.Entry<String, Control> campo : campos.entrySet()) {
            Optional<ConstraintViolation<AdultoMayor>> violacion = violaciones.stream()
                    .filter(v -> v.getPropertyPath().toString().equals(campo.getKey()))
                    .findFirst();
            if (violacion.isPresent()) {
                String mensaje = violacion.get().getMessage().trim();
                ttc.marcarError(campo.getValue(), mensaje);
                if (primerControl == null) {
                    primerControl = campo.getValue();
                    primerMensaje = mensaje;
                }
            }
        }

        if (primerControl != null) {
            mostrarMensaje(primerMensaje, true);
            final Control foco = primerControl;
            Platform.runLater(foco::requestFocus);
        }
    }

    private void procesarFormulario() {
        boolean esEdicion = idAdultoCE > 0L;
        if (esEdicion) {
            formulario.setId(idAdultoCE);
            service.update(idAdultoCE, formulario);
            mostrarToast("Se actualizó correctamente!!");
        } else {
            service.save(formulario);
            mostrarToast("Se guardó correctamente!!");
        }
        clearForm();
        mostrarMensaje(esEdicion ? "Registro actualizado correctamente" : "Registro guardado correctamente", false);
        filtrarTabla();
    }


    //  EDITAR y ELIMINAR

    public void editForm(AdultoMayor adulto) {
        limpiarError();
        txtNombreCompleto.setText(adulto.getNombreCompleto());
        txtEdad.setText(String.valueOf(adulto.getEdad()));
        txtCurp.setText(adulto.getCurp());
        txtDomicilio.setText(adulto.getDomicilio());
        seleccionar(cbxTipoPension, adulto.getTipoPension() == null ? null : adulto.getTipoPension().name());
        seleccionar(cbxEstadoSalud, adulto.getEstadoSalud() == null ? null : adulto.getEstadoSalud().name());
        idAdultoCE = adulto.getId();
        mostrarMensaje("Editando el registro N° " + adulto.getId(), false);
    }

    private void seleccionar(ComboBox<ComboBoxOption> combo, String key) {
        Optional<ComboBoxOption> opcion = combo.getItems().stream()
                .filter(o -> o.getKey().equals(key))
                .findFirst();
        if (opcion.isPresent()) {
            combo.getSelectionModel().select(opcion.get());
        } else {
            combo.getSelectionModel().clearSelection();
        }
    }

    private void confirmarEliminar(AdultoMayor adulto) {
        Persona persona = adulto;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(miContenedor.getScene().getWindow());
        alert.setTitle("Eliminar registro");
        alert.setHeaderText("¿Desea eliminar este registro?");
        alert.setContentText(persona.obtenerResumen());

        Optional<ButtonType> respuesta = alert.showAndWait();
        if (respuesta.isPresent() && respuesta.get() == ButtonType.OK) {
            service.delete(adulto.getId());
            if (adulto.getId().equals(idAdultoCE)) {
                clearForm();
            }
            mostrarToast("Se eliminó correctamente!!");
            filtrarTabla();
        }
    }


    //  UTILIDADES DE FORMULARIO

    @FXML
    public void cancelar() {
        clearForm();
        lbnMsg.setText("");
    }

    public void clearForm() {
        txtNombreCompleto.clear();
        txtEdad.clear();
        txtCurp.clear();
        txtDomicilio.clear();
        cbxTipoPension.getSelectionModel().clearSelection();
        cbxEstadoSalud.getSelectionModel().clearSelection();
        idAdultoCE = 0L;
        limpiarError();
    }

    public void limpiarError() {
        List.of(txtNombreCompleto, txtEdad, txtCurp, txtDomicilio, cbxTipoPension, cbxEstadoSalud)
                .forEach(ttc::limpiarCampo);
    }

    private void mostrarMensaje(String texto, boolean error) {
        lbnMsg.setText(texto);
        lbnMsg.setStyle(error
                ? "-fx-text-fill: red; -fx-font-size: 14px;"
                : "-fx-text-fill: green; -fx-font-size: 14px;");
    }

    private void mostrarToast(String mensaje) {
        Stage stage = (Stage) miContenedor.getScene().getWindow();
        double x = stage.getX() + stage.getWidth() / 1.5;
        double y = stage.getY() + stage.getHeight() / 2;
        Toast.showToast(stage, mensaje, 2000, x, y);
    }
}
