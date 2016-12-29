package afterwind.lab1.controller;

import afterwind.lab1.Utils;
import afterwind.lab1.entity.Candidate;
import afterwind.lab1.exception.ValidationException;
import afterwind.lab1.service.CandidateService;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

/**
 * MVC controller perntru Candidate
 */
public class NewCandidateController extends AbstractController<Candidate> {

    @FXML
    public TableColumn columnID, columnName, columnTel, columnAddress;
    @FXML
    public TextField nameTextField, addressTextField, telTextField;
    @FXML
    public TextField nameFilterTextField, addressFilterTextField, telFilterTextField;

    @FXML
    @Override
    public void initialize() {
        super.initialize();

        tableView.getSelectionModel().selectedItemProperty().addListener(this::handleSelectionChanged);
        nameFilterTextField.textProperty().addListener(this::updateFilter);
        addressFilterTextField.textProperty().addListener(this::updateFilter);
        telFilterTextField.textProperty().addListener(this::updateFilter);

        columnID.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnTel.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        columnAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
    }

    public void setService(CandidateService service) {
        this.service = service;
        showAll();
    }

    /**
     * Afiseaza detaliile despre un candidat in TextField-uri
     * @param c
     */
    @Override
    public void showDetails(Candidate c) {
        nameTextField.setText(c.getName());
        addressTextField.setText(c.getAddress());
        telTextField.setText(c.getTelephone());
    }

    /**
     * Goleste textul din toate TextField-uri
     */
    @Override
    public void clearModificationTextFields() {
        nameTextField.setText("");
        addressTextField.setText("");
        telTextField.setText("");
        tableView.getSelectionModel().clearSelection();
    }

    @Override
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

    /**
     * Apelat cand se apasa pe butonul Add
     * @param ev evenimentul
     */
    @Override
    public void handleAdd(ActionEvent ev) {
        String name = nameTextField.getText();
        String address = addressTextField.getText();
        String tel = telTextField.getText();

        if (checkFields(name, address, tel)) {
            return;
        }

        Integer id = service.getNextId();
        try {
            Candidate c = new Candidate(id, name, tel, address);
            service.add(c);
            if (filter.test(c)) {
                tableView.getItems().add(c);
            }
        } catch (ValidationException ex) {
            Utils.showErrorMessage(ex.getMessage());
        }
    }

    /**
     * Apelat cand se apasa pe butonul Delete
     * @param ev evenimentul
     */
    @Override
    public void handleDelete(ActionEvent ev) {
        Candidate c = tableView.getSelectionModel().getSelectedItem();
        if (c == null) {
            Utils.showErrorMessage("Nu a fost selectat nici un candidat!");
            return;
        }
        service.remove(c);
        tableView.getItems().remove(c);
        clearModificationTextFields();
    }

    /**
     * Apelat cand se apasa pe butonul Update
     * @param ev evenimentul
     */
    @Override
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

        ((CandidateService)service).updateCandidate(c, name, tel, address);
        for (int i = 0; i < 4; i++) {
            tableView.getColumns().get(i).setVisible(false);
            tableView.getColumns().get(i).setVisible(true);
        }
    }

    @Override
    public void updateFilter(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        boolean filtered = false;
        filter = (c) -> true;
        if (!nameFilterTextField.getText().equals("")) {
            filter = filter.and((c) -> c.getName().toLowerCase().startsWith(nameFilterTextField.getText().toLowerCase()));
            filtered = true;
        }
        if (!addressFilterTextField.getText().equals("")) {
            filter = filter.and((c) -> c.getAddress().toLowerCase().startsWith(addressFilterTextField.getText().toLowerCase()));
            filtered = true;
        }
        if (!telFilterTextField.getText().equals("")) {
            filter = filter.and((c) -> c.getTelephone().toLowerCase().startsWith(telFilterTextField.getText().toLowerCase()));
            filtered = true;
        }

        List<Candidate> filteredList = service.filter(filter);
        tableView.setItems(FXCollections.observableArrayList(filteredList));
        buttonClearFilter.setDisable(!filtered);
    }
}
