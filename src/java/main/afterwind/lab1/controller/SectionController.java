package afterwind.lab1.controller;

import afterwind.lab1.Utils;
import afterwind.lab1.entity.Section;
import afterwind.lab1.exception.ValidationException;
import afterwind.lab1.permission.Permission;
import afterwind.lab1.repository.PaginatedRepository;
import afterwind.lab1.service.SectionService;
import afterwind.lab1.ui.control.StateButton;
import afterwind.lab1.ui.control.StatusBar;
import afterwind.lab1.validator.SectionValidator;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.function.Predicate;


public class SectionController extends EntityController<Section> {

    @FXML
    public TextField fieldFilterName, fieldFilterSeats;
    @FXML
    public StateButton<String> stateFilterSeats;
    @FXML
    private TableColumn<Section, String> columnID, columnName, columnSeats;
    @FXML
    private TextField fieldName, fieldSeats;

    private Predicate<Integer> compTester = (diff) -> diff < 0;

    /**
     * Seteaza service-ul
     * @param service service-ul
     */
    public void setService(SectionService service) {
        this.service = service;
        this.filteredEntities = new PaginatedRepository<>(new SectionValidator(), FancyController.sectionsPerPage);
        this.filteredEntities.sortBy((t1, t2) -> t1.getId() - t2.getId());
        pagination.init(this::handlePageChange, 0);
        updateNumberOfPages();
        if (Permission.QUERY.check()) {
            showAll();
        }
    }

    /**
     * Sterge textul din fiecare TextField
     */
    @Override
    public void clearModificationTextFields() {
        fieldName.setText("");
        fieldSeats.setText("");
        tableView.getSelectionModel().clearSelection();
    }

    @Override
    public void clearFilterTextFields() {
        fieldFilterName.setText("");
        fieldFilterSeats.setText("");
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
            Utils.setErrorBorder(fieldName);
            errored = true;
        }
        if (!Utils.tryParseInt(nrLoc)) {
            Utils.setErrorBorder(fieldSeats);
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
        fieldName.setText(s.getName());
        fieldSeats.setText(s.getNrLoc() + "");
    }

    @FXML
    @Override
    public void initialize() {
        super.initialize();
        columnID.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnSeats.setCellValueFactory(new PropertyValueFactory<>("nrLoc"));

        stateFilterSeats.addState("<");
        stateFilterSeats.addState("≤");
        stateFilterSeats.addState("=");
        stateFilterSeats.addState("≥");
        stateFilterSeats.addState(">");

        fieldFilterName.textProperty().addListener(this::updateFilter);
        fieldFilterSeats.textProperty().addListener(this::updateFilter);

        disableBasedOnPermissions();
    }

    @Override
    void generateStatusBarMessages(StatusBar statusBar) {
        super.generateStatusBarMessages(statusBar);
        statusBar.addMessage(fieldName, "The [new ]name of the [new ]section");
        statusBar.addMessage(fieldSeats, "The [new ]number of seats of the [new ]section");
        statusBar.addMessage(fieldFilterName, "Filters the Section by their name");
        statusBar.addMessage(fieldFilterSeats, "Filters the Section by their number of seats");
        statusBar.addMessage(stateFilterSeats, "Provides the condition on the number of seats for filtering");
    }

    @Override
    protected void disableBasedOnPermissions() {
        super.disableBasedOnPermissions();
        fieldName.setDisable(!Permission.MODIFY.check());
        fieldSeats.setDisable(!Permission.MODIFY.check());

        fieldFilterName.setDisable(!Permission.QUERY.check());
        fieldFilterSeats.setDisable(!Permission.MODIFY.check());
    }

    /**
     * Apelat cand se apasa pe butonul Add
     * @param ev evenimentul
     */
    @Override
    public void handleAdd(ActionEvent ev) {
        String name = fieldName.getText();
        String nrLocString = fieldSeats.getText();
        if (checkFields(name, nrLocString)) {
            return;
        }
        int nrLoc = Integer.parseInt(nrLocString);
        int id = service.getNextId();
        try {
            Section s = new Section(id, name, nrLoc);
            service.add(s);
            if (isFiltered && filter.test(s)) {
                filteredEntities.add(s);
            }
            updateNumberOfPages();
            clearModificationTextFields();
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
//        tableView.getItems().remove(s);
        clearModificationTextFields();
        updateNumberOfPages();
    }

    @Override
    public void handleUpdate(ActionEvent actionEvent) {
        Section s = tableView.getSelectionModel().getSelectedItem();
        if (s == null) {
            Utils.showErrorMessage("Nu a fost selectata nicio sectie!");
            return;
        }
        String name = fieldName.getText();
        String nrLocString = fieldSeats.getText();
        if (checkFields(name, nrLocString)) {
            return;
        }
        int nrLoc = Integer.parseInt(nrLocString);
        service.update(s.getId(), new Section(-1, name, nrLoc));
        for (int i = 0; i < 3; i++) {
            tableView.getColumns().get(i).setVisible(false);
            tableView.getColumns().get(i).setVisible(true);
        }
        if (isFiltered && !filter.test(s)) {
            filteredEntities.remove(s);
        }
    }

    @Override
    public void updateFilter(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        filter = (c) -> true;
        if (!fieldFilterName.getText().equals("")) {
            filter = filter.and((s) -> s.getName().toLowerCase().startsWith(fieldFilterName.getText().toLowerCase()));
            isFiltered = true;
        }
        if (!fieldFilterSeats.getText().equals("") && Utils.tryParseInt(fieldFilterSeats.getText())) {
            int actualNrLoc = Integer.parseInt(fieldFilterSeats.getText());
            filter = filter.and((s) -> compTester.test(s.getNrLoc() - actualNrLoc));
            isFiltered = true;
        }
        buttonClearFilter.setDisable(!isFiltered);
        if (!isFiltered) {
            handleClearFilter(null);
            return;
        }

        List<Section> filteredList = service.filter(filter);
        filteredEntities.clear();
        try {
            filteredEntities.addAll(filteredList);
        } catch (ValidationException e) {
            throw new RuntimeException(e);
        }
        handlePageChange(0);
        updateNumberOfPages();
    }

    public void handleStateChange(ActionEvent ev) {
        stateFilterSeats.changeState();
        switch (stateFilterSeats.getState()) {
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
