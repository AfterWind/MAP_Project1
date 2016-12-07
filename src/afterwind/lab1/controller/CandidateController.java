package afterwind.lab1.controller;

import afterwind.lab1.Utils;
import afterwind.lab1.entity.Candidate;
import afterwind.lab1.exception.ValidationException;
import afterwind.lab1.repository.IRepository;
import afterwind.lab1.repository.Repository;
import afterwind.lab1.service.CandidateService;
import afterwind.lab1.ui.CandidateView;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.function.Predicate;

/**
 * MVC controller perntru Candidate
 */
public class CandidateController implements Observer {

    @FXML
    public TableView<Candidate> tableView;
    @FXML
    public TableColumn columnID, columnName, columnTel, columnAddress;
    @FXML
    public TextField nameTextField, addressTextField, telTextField;
    @FXML
    public TextField nameFilterTextField, addressFilterTextField, telFilterTextField;
    @FXML
    public Button clearFilterButton;
    private CandidateService service;
    private ObservableList<Candidate> model;

    /**
     * Constructor pentru CandidateController
     */
    public CandidateController() { }

    public void setService(CandidateService service) {
        this.service = service;
        this.model = service.getRepo().getData();

        columnID.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnTel.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        columnAddress.setCellValueFactory(new PropertyValueFactory<>("address"));

        tableView.getSelectionModel().selectedItemProperty().addListener(this::handleSelectionChanged);

        showAll();
    }

    /**
     * Afiseaza toti candidatii
     */
    public void showAll() {
        tableView.setItems(model);
    }

    /**
     * Afiseaza detaliile despre un candidat in TextField-uri
     * @param c
     */
    public void showDetails(Candidate c) {
        nameTextField.setText(c.getName());
        addressTextField.setText(c.getAddress());
        telTextField.setText(c.getTelephone());
    }

    /**
     * Goleste textul din toate TextField-uri
     */
    public void clearTextFields() {
        nameTextField.setText("");
        addressTextField.setText("");
        telTextField.setText("");
        tableView.getSelectionModel().clearSelection();
    }

    public void clearFilterTextFields() {
        nameFilterTextField.setText("");
        addressFilterTextField.setText("");
        telFilterTextField.setText("");
        tableView.getSelectionModel().clearSelection();
    }

    /**
     * Verifica daca textul din TextField-uri este valid
     * @param name Numele candidatului
     * @param address Adresa candidatului
     * @param tel Numarul de telefon al candidatului
     * @return daca textul e valid
     */
    public boolean checkFields(String name, String address, String tel) {
        boolean errored = false;
        if (name.equals("")) {
            Utils.setErrorBorder(nameTextField);
            errored = true;
        }
        if (address.equals("")) {
            Utils.setErrorBorder(addressTextField);
            errored = true;
        }
        if (tel.equals("")) {
            Utils.setErrorBorder(telTextField);
            errored = true;
        } else {
            for (char ch : tel.toCharArray()) {
                if (ch < '0' || ch > '9') {
                    Utils.setErrorBorder(telTextField);
                    errored = true;
                }
            }
        }
        return errored;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof CandidateService) {
            model.setAll(((CandidateService) o).getRepo().getData());
        }
    }

    /**
     * Apelat daca selectia se modifica
     * @param oldValue valoarea veche
     * @param newValue valoarea noua
     */
    public void handleSelectionChanged(ObservableValue<? extends Candidate> o, Candidate oldValue, Candidate newValue) {
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
        String address = addressTextField.getText();
        String tel = telTextField.getText();

        if (checkFields(name, address, tel)) {
            return;
        }

        Integer id = service.getNextId();
        try {
            service.add(new Candidate(id, name, tel, address));
        } catch (ValidationException ex) {
            Utils.showErrorMessage(ex.getMessage());
        }
    }

    /**
     * Apelat cand se apasa pe butonul Delete
     * @param ev evenimentul
     */
    public void handleDelete(ActionEvent ev) {
        Candidate c = tableView.getSelectionModel().getSelectedItem();
        if (c == null) {
            Utils.showErrorMessage("Nu a fost selectat nici un candidat!");
            return;
        }
        service.remove(c);
        clearTextFields();
    }

    /**
     * Apelat cand se apasa pe butonul Update
     * @param ev evenimentul
     */
    public void handleUpdate(ActionEvent ev) {
        Candidate c = tableView.getSelectionModel().getSelectedItem();
        if (c == null) {
            Utils.showErrorMessage("Nu a fost selectat nici un candidat!");
            return;
        }

        String name = nameTextField.getText();
        String address = addressTextField.getText();
        String tel = telTextField.getText();

        if (checkFields(name, address, tel)) {
            return;
        }

        service.updateCandidate(c, name, tel, address);
        for (int i = 0; i < 4; i++) {
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
        Utils.showInfoMessage("Totul s-a salvat in fisier!");
        service.getRepo().updateLinks();
    }

    /**
     * Apelat cand se modifica textul dintr-un TextField
     * @param oldValue vechea valoare
     * @param newValue noua valoare
     */
    public void handleTextChanged(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (observable instanceof StringProperty && ((StringProperty) observable).getBean() instanceof TextField) {
            ((TextField) ((StringProperty) observable).getBean()).borderProperty().set(new Border(new BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        }
    }

    public void handleNameFilter(ActionEvent ev) {
        if(nameFilterTextField.getText().equals("")) {
            Utils.setErrorBorder(nameFilterTextField);
            return;
        }
        IRepository<Candidate, Integer> filtered = service.filterByName(nameFilterTextField.getText());
        tableView.setItems(filtered.getData());
        clearFilterButton.setDisable(false);
    }

    public void handleAddressFilter(ActionEvent ev) {
        if(addressFilterTextField.getText().equals("")) {
            Utils.setErrorBorder(addressFilterTextField);
            return;
        }
        IRepository<Candidate, Integer> filtered = service.filterByAddress(addressFilterTextField.getText());
        tableView.setItems(filtered.getData());
        clearFilterButton.setDisable(false);
    }

    public void handleTelFilter(ActionEvent ev) {
        if(telFilterTextField.getText().equals("")) {
            Utils.setErrorBorder(telFilterTextField);
            return;
        }
        IRepository<Candidate, Integer> filtered = service.filterByTelephone(telFilterTextField.getText());
        tableView.setItems(filtered.getData());
        clearFilterButton.setDisable(false);
    }

    public void handleFilter(ActionEvent ev) {
        if (nameFilterTextField.getText().equals("") && addressFilterTextField.getText().equals("") && telFilterTextField.getText().equals("")) {
            Utils.setErrorBorder(nameFilterTextField);
            Utils.setErrorBorder(addressFilterTextField);
            Utils.setErrorBorder(telFilterTextField);
            return;
        }
        Predicate<Candidate> pred = (c) -> true;
        if (!nameFilterTextField.getText().equals("")) {
            pred = pred.and((c) -> c.getName().startsWith(nameFilterTextField.getText()));
        }
        if (!addressFilterTextField.getText().equals("")) {
            pred = pred.and((c) -> c.getAddress().startsWith(addressFilterTextField.getText()));
        }
        if (!telFilterTextField.getText().equals("")) {
            pred = pred.and((c) -> c.getTelephone().startsWith(telFilterTextField.getText()));
        }
        List<Candidate> filtered = service.filter(pred);
        tableView.setItems(FXCollections.observableArrayList(filtered));
        clearFilterButton.setDisable(false);
    }

    public void handleClearFilter(ActionEvent ev) {
        tableView.setItems(model);
        clearFilterTextFields();
        clearFilterButton.setDisable(true);
    }
}
