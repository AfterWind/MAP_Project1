package afterwind.lab1.controller;

import afterwind.lab1.Utils;
import afterwind.lab1.entity.Candidate;
import afterwind.lab1.entity.Option;
import afterwind.lab1.entity.Section;
import afterwind.lab1.exception.ValidationException;
import afterwind.lab1.permission.Permission;
import afterwind.lab1.repository.PaginatedRepository;
import afterwind.lab1.service.CandidateService;
import afterwind.lab1.service.OptionService;
import afterwind.lab1.service.SectionService;
import afterwind.lab1.ui.control.StatusBar;
import afterwind.lab1.validator.OptionValidator;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * MVC Controller pentru Options
 */
public class OptionController extends EntityController<Option> {

    @FXML
    public TableColumn<Option, String> columnID;
    @FXML
    public TableColumn<Option, Section> columnSection;
    @FXML
    public TableColumn<Option, Candidate> columnCandidate;
    @FXML
    public ListView<Candidate> listCandidates;
    @FXML
    public ListView<Section> listSections;
    @FXML
    public TextField fieldFilterCandidate, fieldFilterSection;

    private CandidateService candidateService;
    private SectionService sectionService;

    @Override
    public void showAll() {
        List<Option> toRemove = new ArrayList<>();
        for (Iterator<Option> it = service.getRepo().getData().iterator(); it.hasNext();) {
            Option o = it.next();
            if (o.getCandidate() == null || o.getSection() == null) {
                toRemove.add(o);
            }
        }
        for (Option o : toRemove) {
            service.remove(o);
        }

        super.showAll();
        listCandidates.setItems(candidateService.getRepo().getData());
        listSections.setItems(sectionService.getRepo().getData());
    }

    public void setServices(OptionService service, CandidateService candidateService, SectionService sectionService) {
        this.service = service;
        this.candidateService = candidateService;
        this.sectionService = sectionService;
        this.filteredEntities = new PaginatedRepository<>(new OptionValidator(), FancyController.optionsPerPage);
        this.filteredEntities.sortBy((t1, t2) -> t1.getId() - t2.getId());
        pagination.init(this::handlePageChange, 0);
        updateNumberOfPages();
        if (Permission.QUERY.check()) {
            showAll();
        }
    }

    public void clearFilterTextFields() {
        fieldFilterCandidate.setText("");
        fieldFilterSection.setText("");
    }

    @Override
    public void clearModificationTextFields() {
        listCandidates.getSelectionModel().clearSelection();
        listSections.getSelectionModel().clearSelection();
    }

    @FXML
    @Override
    public void initialize() {
        super.initialize();
        columnID.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnCandidate.setCellValueFactory(new PropertyValueFactory<>("candidate"));
        columnSection.setCellValueFactory(new PropertyValueFactory<>("section"));
        listCandidates.setCellFactory(param -> new ListCell<Candidate>() {
            @Override
            protected void updateItem(Candidate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });

        listSections.setCellFactory(param -> new ListCell<Section>() {
            @Override
            protected void updateItem(Section item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });

        fieldFilterCandidate.textProperty().addListener(this::updateFilter);
        fieldFilterSection.textProperty().addListener(this::updateFilter);

        disableBasedOnPermissions();
    }

    @Override
    void generateStatusBarMessages(StatusBar statusBar) {
        super.generateStatusBarMessages(statusBar);

        statusBar.addMessage(fieldFilterCandidate, "Filters the options by their candidate's name");
        statusBar.addMessage(fieldFilterSection, "Filters the options by their section's name");
        statusBar.addMessage(listSections, "The [new ]section for the [new ]option");
        statusBar.addMessage(listCandidates, "The [new ]candidate for the [new ]option");
    }

    @Override
    protected void disableBasedOnPermissions() {
        super.disableBasedOnPermissions();
        listCandidates.setDisable(!Permission.MODIFY.check());
        listSections.setDisable(!Permission.MODIFY.check());

        fieldFilterCandidate.setDisable(!Permission.QUERY.check());
        fieldFilterSection.setDisable(!Permission.QUERY.check());
    }

    /**
     * Afiseaza detalii despre o sectiune
     * @param o Optiunea
     */
    @Override
    public void showDetails(Option o) {
//        System.out.println(o.getSection().getName() + ", " + o.getCandidate().getName());
        if (candidateService.get(o.getCandidate().getId()) == null || sectionService.get(o.getSection().getId()) == null) {
            service.remove(o);
            tableView.setItems(service.getRepo().getData());
        }
        listCandidates.getSelectionModel().select(o.getCandidate());
        listSections.getSelectionModel().select(o.getSection());
    }

    @Override
    public void handleDelete(ActionEvent ev) {
        Option o = tableView.getSelectionModel().getSelectedItem();
        if (o == null) {
            Utils.showErrorMessage("Nu ati selectat nicio Optiune!");
            return;
        }
        service.remove(o);
//        tableView.getItems().remove(o);
        clearModificationTextFields();
        updateNumberOfPages();
    }

    @Override
    public void handleAdd(ActionEvent ev) {
        Candidate c = listCandidates.getSelectionModel().getSelectedItem();
        if (c == null) {
            Utils.showErrorMessage("Nu ati selectat niciun candidat!");
            return;
        }
        Section s = listSections.getSelectionModel().getSelectedItem();
        if (s == null) {
            Utils.showErrorMessage("Nu ati selectat nicio sectie!");
            return;
        }
        for (Option option : service.getData()) {
            if (option.getSection() == s && option.getCandidate() == c) {
                Utils.showErrorMessage("Candidatul " + c.getName() + " deja apartine sectiei " + s.getName() + "!");
                return;
            }
        }

        try {
            Option o = new Option(service.getNextId(), s, c);
            service.add(o);
            if (isFiltered && filter.test(o)) {
                filteredEntities.add(o);
            }
            clearModificationTextFields();
            updateNumberOfPages();
        } catch (ValidationException e) {
            Utils.showErrorMessage(e.getMessage());
        }
    }

    public void handleUpdate(ActionEvent ev) {
        Option o = tableView.getSelectionModel().getSelectedItem();
        if (o == null) {
            Utils.showErrorMessage("Nu ati selectat nicio optiune!");
            return;
        }
        Candidate c = listCandidates.getSelectionModel().getSelectedItem();
        if (c == null) {
            Utils.showErrorMessage("Nu ati selectat nici un candidat!");
            return;
        }
        Section s = listSections.getSelectionModel().getSelectedItem();
        if (s == null) {
            Utils.showErrorMessage("Nu ati selectat nicio sectie!");
            return;
        }
        if (service.filter((op) -> op.getSection().equals(s) && op.getCandidate().equals(c)).size() > 0) {
            Utils.showErrorMessage("Candidatul " + c.getName() + " apartine deja sectiei " + s.getName() + "!");
            return;
        }

        service.update(o.getId(), new Option(-1, s, c));
        for (int i = 0; i < 3; i++) {
            tableView.getColumns().get(i).setVisible(false);
            tableView.getColumns().get(i).setVisible(true);
        }
        if (isFiltered && !filter.test(o)) {
            filteredEntities.remove(o);
            updateNumberOfPages();
        }
        buttonUpdate.setDisable(true);
    }

    @Override
    public void updateFilter(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        isFiltered = false;
        filter = (c) -> true;
        if (!fieldFilterCandidate.getText().equals("")) {
            filter = filter.and((o) -> o.getCandidate().getName().toLowerCase().startsWith(fieldFilterCandidate.getText().toLowerCase()));
            isFiltered = true;
        }
        if (!fieldFilterSection.getText().equals("")) {
            filter = filter.and((o) -> o.getSection().getName().toLowerCase().startsWith(fieldFilterSection.getText().toLowerCase()));
            isFiltered = true;
        }
        buttonClearFilter.setDisable(!isFiltered);
        if (!isFiltered) {
            handleClearFilter(null);
            return;
        }

        List<Option> filteredList = service.filter(filter);
        filteredEntities.clear();
        try {
            filteredEntities.addAll(filteredList);
        } catch (ValidationException e) {
            throw new RuntimeException(e);
        }
        pagination.setCurrentPage(0);
        updateNumberOfPages();
    }
}
