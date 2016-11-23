package afterwind.lab1.controller;

import afterwind.lab1.Utils;
import afterwind.lab1.entity.Candidate;
import afterwind.lab1.entity.Option;
import afterwind.lab1.entity.Section;
import afterwind.lab1.exception.ValidationException;
import afterwind.lab1.service.SectionService;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;


public class SectionController {

    @FXML
    public Button buttonAdd, buttonUpdate, buttonClear, buttonDelete, buttonRefresh, buttonSave;
    @FXML
    private TableView<Section> tableView;
    @FXML
    private TableColumn<Section, String> idColumn, nameColumn, nrLocColumn;
    @FXML
    private TextField nameTextField, nrLocTextField;
    
    private SectionService service;
    private ObservableList<Section> model;

    public SectionController() { }

    public void showAll() {
        tableView.setItems(model);
    }
    
    public void setService(SectionService service) {
        this.service = service;
        this.model = service.getRepo().getData();
        showAll();
    }
    
    public void clearTextFields() {
        nameTextField.setText("");        
        nrLocTextField.setText("");        
        tableView.getSelectionModel().clearSelection();
    }
    
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

    public void showDetails(Section s) {
        nameTextField.setText(s.getName());
        nrLocTextField.setText(s.getNrLoc() + "");
    }
    
    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nrLocColumn.setCellValueFactory(new PropertyValueFactory<>("nrLoc"));
        tableView.getSelectionModel().selectedItemProperty().addListener(this::handleSelectionChanged);
    }

    public void handleSelectionChanged(ObservableValue<? extends Section> o, Section oldValue, Section newValue) {
        if (newValue != null) {
            showDetails(newValue);
        }
    }

    public void handleDelete(ActionEvent actionEvent) {
        Section s = tableView.getSelectionModel().getSelectedItem();
        if (s == null) {
            Utils.showErrorMessage("Nu a fost selectata nicio sectie!");
            return;
        }
        service.remove(s);
        clearTextFields();
    }

    public void handleRefresh(ActionEvent actionEvent) {
        showAll();
    }

    public void handleSave(ActionEvent actionEvent) {
        service.getRepo().updateLinks();
        Utils.showInfoMessage("Totul a fost salvat in fisier!");
    }

    public void handleAdd(ActionEvent actionEvent) {
        String name = nameTextField.getText();
        String nrLocString = nrLocTextField.getText();
        if (checkFields(name, nrLocString)) {
            return;
        }
        int nrLoc = Integer.parseInt(nrLocString);
        int id = service.getNextId();
        try {
            service.add(new Section(id, name, nrLoc));
        } catch (ValidationException e) {
            Utils.showErrorMessage(e.getMessage());
        }
    }

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
        service.updateSection(s, name, nrLoc);
    }

    public void handleClear(ActionEvent actionEvent) {
        clearTextFields();
    }
}
