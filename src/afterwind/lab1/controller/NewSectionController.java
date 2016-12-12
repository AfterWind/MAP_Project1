package afterwind.lab1.controller;

import afterwind.lab1.Utils;
import afterwind.lab1.entity.Section;
import afterwind.lab1.exception.ValidationException;
import afterwind.lab1.service.SectionService;
import afterwind.lab1.ui.control.StateButton;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.function.Predicate;


public class NewSectionController extends AbstractController<Section> {

    @FXML
    public TextField nameFilterTextField, nrLocFilterTextField;
    @FXML
    public StateButton<String> nrLocState;
    @FXML
    private TableColumn<Section, String> idColumn, nameColumn, nrLocColumn;
    @FXML
    private TextField nameTextField, nrLocTextField;

    private Predicate<Integer> compTester = (diff) -> diff < 0;

    /**
     * Seteaza service-ul
     * @param service service-ul
     */
    public void setService(SectionService service) {
        this.service = service;
        showAll();
    }

    /**
     * Sterge textul din fiecare TextField
     */
    @Override
    public void clearModificationTextFields() {
        nameTextField.setText("");
        nrLocTextField.setText("");
        tableView.getSelectionModel().clearSelection();
    }

    @Override
    public void clearFilterTextFields() {
        nameFilterTextField.setText("");
        nrLocFilterTextField.setText("");
        tableView.getSelectionModel().clearSelection();
    }

    /**
     * Verifica datele din TextField-uri
     * @param name Numele sectiunii
     * @param nrLoc Numarul de locuri in acea sectiune
     * @return daca datele sunt valide
     */
    public boolean checkFields(String name, String nrLoc) {
        boolean errored = false;
        if (name.equals("")) {
            Utils.setErrorBorder(nameTextField);
            errored = true;
        }
        if (!Utils.tryParseInt(nrLoc)) {
            Utils.setErrorBorder(nrLocTextField);
            errored = true;
        }
        return errored;
    }

    /**
     * Afiseaza detalii despre o sectiune
     * @param s Sectiunea
     */
    @Override
    public void showDetails(Section s) {
        nameTextField.setText(s.getName());
        nrLocTextField.setText(s.getNrLoc() + "");
    }

    @FXML
    @Override
    public void initialize() {
        super.initialize();
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nrLocColumn.setCellValueFactory(new PropertyValueFactory<>("nrLoc"));

        nrLocState.addState("<");
        nrLocState.addState("≤");
        nrLocState.addState("=");
        nrLocState.addState("≥");
        nrLocState.addState(">");

        nameFilterTextField.textProperty().addListener(this::updateFilter);
        nrLocFilterTextField.textProperty().addListener(this::updateFilter);
    }

    /**
     * Apelat cand se apasa pe butonul Add
     * @param ev evenimentul
     */
    @Override
    public void handleAdd(ActionEvent ev) {
        String name = nameTextField.getText();
        String nrLocString = nrLocTextField.getText();
        if (checkFields(name, nrLocString)) {
            return;
        }
        int nrLoc = Integer.parseInt(nrLocString);
        int id = service.getNextId();
        try {
            Section s = new Section(id, name, nrLoc);
            service.add(s);
            if (filter.test(s)) {
                tableView.getItems().add(s);
            }
        } catch (ValidationException e) {
            Utils.showErrorMessage(e.getMessage());
        }
    }

    /**
     * Apelat cand se apasa pe butonul Delete
     * @param ev evenimentul
     */
    @Override
    public void handleDelete(ActionEvent ev) {
        Section s = tableView.getSelectionModel().getSelectedItem();
        if (s == null) {
            Utils.showErrorMessage("Nu a fost selectata nicio sectie!");
            return;
        }
        service.remove(s);
        clearModificationTextFields();
    }

    @Override
    public void handleUpdate(ActionEvent actionEvent) {
        Section s = tableView.getSelectionModel().getSelectedItem();
        if (s == null) {
            Utils.showErrorMessage("Nu a fost selectata nicio sectie!");
            return;
        }
        String name = nameTextField.getText();
        String nrLocString = nrLocTextField.getText();
        if (checkFields(name, nrLocString)) {
            return;
        }
        int nrLoc = Integer.parseInt(nrLocString);
        ((SectionService)service).updateSection(s, name, nrLoc);
        for (int i = 0; i < 3; i++) {
            tableView.getColumns().get(i).setVisible(false);
            tableView.getColumns().get(i).setVisible(true);
        }
    }

    @Override
    public void updateFilter(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        boolean filtered = false;
        filter = (c) -> true;
        if (!nameFilterTextField.getText().equals("")) {
            filter = filter.and((s) -> s.getName().toLowerCase().startsWith(nameFilterTextField.getText().toLowerCase()));
            filtered = true;
        }
        if (!nrLocFilterTextField.getText().equals("") && Utils.tryParseInt(nrLocFilterTextField.getText())) {
            int actualNrLoc = Integer.parseInt(nrLocFilterTextField.getText());
            filter = filter.and((s) -> compTester.test(s.getNrLoc() - actualNrLoc));
            filtered = true;
        }

        List<Section> filteredList = service.filter(filter);
        tableView.setItems(FXCollections.observableArrayList(filteredList));
        buttonClearFilter.setDisable(!filtered);
    }

    public void handleStateChange(ActionEvent ev) {
        nrLocState.changeState();
        switch (nrLocState.getState()) {
            case "<":
                compTester = (diff) -> diff < 0;
                break;
            case "≤":
                compTester = (diff) -> diff <= 0;
                break;
            case "=":
                compTester = (diff) -> diff == 0;
                break;
            case "≥":
                compTester = (diff) -> diff >= 0;
                break;
            case ">":
                compTester = (diff) -> diff > 0;
                break;
        }
        updateFilter(null, null, null);
    }
}
