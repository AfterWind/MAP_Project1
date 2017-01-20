package afterwind.lab1.controller;

import afterwind.lab1.Utils;
import afterwind.lab1.entity.Candidate;
import afterwind.lab1.exception.ValidationException;
import afterwind.lab1.permission.Permission;
import afterwind.lab1.repository.PaginatedRepository;
import afterwind.lab1.service.CandidateService;
import afterwind.lab1.ui.control.StatusBar;
import afterwind.lab1.validator.CandidateValidator;
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
public class CandidateController extends EntityController<Candidate> {

    @FXML
    public TableColumn columnID, columnName, columnTel, columnAddress;
    @FXML
    public TextField fieldName, fieldAddress, fieldTelephone;
    @FXML
    public TextField fieldFilterName, fieldFilterAddress, fieldFilterTelephone;

    @FXML
    @Override
    public void initialize() {
        super.initialize();
        fieldFilterName.textProperty().addListener(this::updateFilter);
        fieldFilterAddress.textProperty().addListener(this::updateFilter);
        fieldFilterTelephone.textProperty().addListener(this::updateFilter);

        columnID.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnTel.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        columnAddress.setCellValueFactory(new PropertyValueFactory<>("address"));

        disableBasedOnPermissions();
    }

    @Override
    void generateStatusBarMessages(StatusBar statusBar) {
        super.generateStatusBarMessages(statusBar);
        statusBar.addMessage(fieldName, "The [new ]name of the [new ]candidate");
        statusBar.addMessage(fieldAddress, "The [new ]address of the [new ]candidate");
        statusBar.addMessage(fieldTelephone, "The [new ]telephone of the [new ]candidate");

        statusBar.addMessage(fieldFilterName, "Filters the candidates by their name");
        statusBar.addMessage(fieldFilterAddress, "Filters the candidates by their address");
        statusBar.addMessage(fieldFilterTelephone, "Filters the candidates by their telephone number");
    }

    @Override
    protected void disableBasedOnPermissions() {
        super.disableBasedOnPermissions();
        fieldName.setDisable(!Permission.MODIFY.check());
        fieldAddress.setDisable(!Permission.MODIFY.check());
        fieldTelephone.setDisable(!Permission.MODIFY.check());

        fieldFilterName.setDisable(!Permission.QUERY.check());
        fieldFilterAddress.setDisable(!Permission.QUERY.check());
        fieldFilterTelephone.setDisable(!Permission.QUERY.check());
    }

    public void setService(CandidateService service) {
        this.service = service;
        this.filteredEntities = new PaginatedRepository<>(new CandidateValidator(), FancyController.candidatesPerPage);
        pagination.init(this::handlePageChange, 0);
        updateNumberOfPages();
        if (Permission.QUERY.check()) {
            showAll();
        }
    }

    /**
     * Afiseaza detaliile despre un candidat in TextField-uri
     * @param c
     */
    @Override
    public void showDetails(Candidate c) {
        fieldName.setText(c.getName());
        fieldAddress.setText(c.getAddress());
        fieldTelephone.setText(c.getTelephone());
    }

    /**
     * Goleste textul din toate TextField-uri
     */
    @Override
    public void clearModificationTextFields() {
        fieldName.setText("");
        fieldAddress.setText("");
        fieldTelephone.setText("");
        tableView.getSelectionModel().clearSelection();
    }

    @Override
    public void clearFilterTextFields() {
        fieldFilterName.setText("");
        fieldFilterAddress.setText("");
        fieldFilterTelephone.setText("");
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
            Utils.setErrorBorder(fieldName);
            errored = true;
        }
        if (address.equals("")) {
            Utils.setErrorBorder(fieldAddress);
            errored = true;
        }
        if (tel.equals("")) {
            Utils.setErrorBorder(fieldTelephone);
            errored = true;
        } else {
            for (char ch : tel.toCharArray()) {
                if (ch < '0' || ch > '9') {
                    Utils.setErrorBorder(fieldTelephone);
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
        String name = fieldName.getText();
        String address = fieldAddress.getText();
        String tel = fieldTelephone.getText();

        if (checkFields(name, address, tel)) {
            return;
        }

        Integer id = service.getNextId();
        try {
            Candidate c = new Candidate(id, name, tel, address);
            service.add(c);
            if (isFiltered && filter.test(c)) {
                filteredEntities.add(c);
            }
            clearModificationTextFields();
            updateNumberOfPages();
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
        updateNumberOfPages();
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

        String name = fieldName.getText();
        String address = fieldAddress.getText();
        String tel = fieldTelephone.getText();

        if (checkFields(name, address, tel)) {
            return;
        }

        service.update(c.getId(), new Candidate(-1, name, tel, address));
        for (int i = 0; i < 4; i++) {
            tableView.getColumns().get(i).setVisible(false);
            tableView.getColumns().get(i).setVisible(true);
        }

        if (isFiltered && !filter.test(c)) {
            filteredEntities.remove(c);
        }
    }

    @Override
    public void updateFilter(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        isFiltered = false;
        filter = (c) -> true;
        if (!fieldFilterName.getText().equals("")) {
            filter = filter.and((c) -> c.getName().toLowerCase().startsWith(fieldFilterName.getText().toLowerCase()));
            isFiltered = true;
        }
        if (!fieldFilterAddress.getText().equals("")) {
            filter = filter.and((c) -> c.getAddress().toLowerCase().startsWith(fieldFilterAddress.getText().toLowerCase()));
            isFiltered = true;
        }
        if (!fieldFilterTelephone.getText().equals("")) {
            filter = filter.and((c) -> c.getTelephone().toLowerCase().startsWith(fieldFilterTelephone.getText().toLowerCase()));
            isFiltered = true;
        }
        buttonClearFilter.setDisable(!isFiltered);
        if (!isFiltered) {
            handleClearFilter(null);
            return;
        }

        List<Candidate> filteredList = service.filter(filter);
        filteredEntities.clear();
        try {
            filteredEntities.addAll(filteredList);
        } catch (ValidationException e) {
            throw new RuntimeException(e);
        }
        handlePageChange(0);
        updateNumberOfPages();
    }
}
