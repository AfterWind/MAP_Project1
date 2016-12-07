package afterwind.lab1.controller;

import afterwind.lab1.Utils;
import afterwind.lab1.entity.Section;
import afterwind.lab1.exception.ValidationException;
import afterwind.lab1.service.SectionService;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.io.File;
import java.net.MalformedURLException;


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

    /**
     * Constructor pentru controllerul de sectiuni
     */
    public SectionController() { }

    /**
     * Afiseaza toate sectiunile
     */
    public void showAll() {
        tableView.setItems(model);
    }

    /**
     * Seteaza service-ul
     * @param service service-ul
     */
    public void setService(SectionService service) {
        this.service = service;
        this.model = service.getRepo().getData();
        showAll();
    }

    /**
     * Sterge textul din fiecare TextField
     */
    public void clearTextFields() {
        nameTextField.setText("");        
        nrLocTextField.setText("");        
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

    /**
     * Apelat daca selectia se modifica
     * @param oldValue valoarea veche
     * @param newValue valoarea noua
     */
    public void handleSelectionChanged(ObservableValue<? extends Section> o, Section oldValue, Section newValue) {
        if (newValue != null) {
            showDetails(newValue);
        }
    }

    /**
     * Apelat cand se apasa pe butonul Add
     * @param ev evenimentul
     */
    public void handleAdd(ActionEvent ev) {
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

    /**
     * Apelat cand se apasa pe butonul Delete
     * @param ev evenimentul
     */
    public void handleDelete(ActionEvent ev) {
        Section s = tableView.getSelectionModel().getSelectedItem();
        if (s == null) {
            Utils.showErrorMessage("Nu a fost selectata nicio sectie!");
            return;
        }
        service.remove(s);
        clearTextFields();
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
        for (int i = 0; i < 3; i++) {
            tableView.getColumns().get(i).setVisible(false);
            tableView.getColumns().get(i).setVisible(true);
        }
    }

    /**
     * Apelat cand se apasa pe butonul Clear
     * @param ev evenimentul
     */
    public void handleClear(ActionEvent ev) {
        clearTextFields();
    }

    /**
     * Apelat cand se apasa pe butonul Refresh
     * @param ev evenimentul
     */
    public void handleRefresh(ActionEvent ev) {
        showAll();
    }

    /**
     * Apelat cand se apasa pe butonul Save
     * @param ev evenimentul
     */
    public void handleSave(ActionEvent ev) {
        service.getRepo().updateLinks();
        Utils.showInfoMessage("Totul a fost salvat in fisier!");
    }
}
