package afterwind.lab1.controller;

import afterwind.lab1.Utils;
import afterwind.lab1.entity.Candidate;
import afterwind.lab1.entity.Option;
import afterwind.lab1.entity.Section;
import afterwind.lab1.exception.ValidationException;
import afterwind.lab1.repository.FileRepository;
import afterwind.lab1.service.CandidateService;
import afterwind.lab1.service.OptionService;
import afterwind.lab1.service.SectionService;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

/**
 * MVC Controller pentru Options
 */
public class NewOptionController extends AbstractController<Option> {

    @FXML
    public TableColumn<Option, String> idColumn;
    @FXML
    public TableColumn<Option, Section> sectionColumn;
    @FXML
    public TableColumn<Option, Candidate> candidateColumn;
    @FXML
    public ListView<Candidate> candidateList;
    @FXML
    public ListView<Section> sectionList;
    public TextField candidateFilterTextField, sectionFilterTextField;

    private CandidateService candidateService;
    private SectionService sectionService;

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
        candidateList.setItems(candidateService.getRepo().getData());
        sectionList.setItems(sectionService.getRepo().getData());
    }

    public void setServices(OptionService service, CandidateService candidateService, SectionService sectionService) {
        this.service = service;
        this.candidateService = candidateService;
        this.sectionService = sectionService;
        if (!(service.getRepo() instanceof FileRepository)) {
            buttonSave.setDisable(true);
        }
        showAll();
    }

    public void clearFilterTextFields() {
        candidateFilterTextField.setText("");
        sectionFilterTextField.setText("");
    }

    @Override
    public void clearModificationTextFields() {
        candidateList.getSelectionModel().clearSelection();
        sectionList.getSelectionModel().clearSelection();
    }

    @FXML
    @Override
    public void initialize() {
        super.initialize();
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        candidateColumn.setCellValueFactory(new PropertyValueFactory<>("candidate"));
        sectionColumn.setCellValueFactory(new PropertyValueFactory<>("section"));
        candidateList.setCellFactory(param -> new ListCell<Candidate>() {
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

        sectionList.setCellFactory(param -> new ListCell<Section>() {
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

        candidateFilterTextField.textProperty().addListener(this::updateFilter);
        sectionFilterTextField.textProperty().addListener(this::updateFilter);
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
        candidateList.getSelectionModel().select(o.getCandidate());
        sectionList.getSelectionModel().select(o.getSection());
    }

    @Override
    public void handleDelete(ActionEvent ev) {
        Option o = tableView.getSelectionModel().getSelectedItem();
        if (o == null) {
            Utils.showErrorMessage("Nu ati selectat nicio Optiune!");
            return;
        }
        service.remove(o);
        clearModificationTextFields();
        tableView.getItems().remove(o);
    }

    @Override
    public void handleAdd(ActionEvent ev) {
        Candidate c = candidateList.getSelectionModel().getSelectedItem();
        if (c == null) {
            Utils.showErrorMessage("Nu ati selectat niciun candidat!");
            return;
        }
        Section s = sectionList.getSelectionModel().getSelectedItem();
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
            if (filter.test(o)) {
                tableView.getItems().add(o);
            }
            clearModificationTextFields();
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
        Candidate c = candidateList.getSelectionModel().getSelectedItem();
        if (c == null) {
            Utils.showErrorMessage("Nu ati selectat nici un candidat!");
            return;
        }
        Section s = sectionList.getSelectionModel().getSelectedItem();
        if (s == null) {
            Utils.showErrorMessage("Nu ati selectat nicio sectie!");
            return;
        }
        if (service.filter((op) -> op.getSection().equals(s) && op.getCandidate().equals(c)).size() > 0) {
            Utils.showErrorMessage("Candidatul " + c.getName() + " apartine deja sectiei " + s.getName() + "!");
            return;
        }

        ((OptionService) service).updateOption(o, c, s);
        for (int i = 0; i < 3; i++) {
            tableView.getColumns().get(i).setVisible(false);
            tableView.getColumns().get(i).setVisible(true);
        }
        buttonUpdate.setDisable(true);
    }

    @Override
    public void updateFilter(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        boolean filtered = false;
        filter = (c) -> true;
        if (!candidateFilterTextField.getText().equals("")) {
            filter = filter.and((o) -> o.getCandidate().getName().toLowerCase().startsWith(candidateFilterTextField.getText().toLowerCase()));
            filtered = true;
        }
        if (!sectionFilterTextField.getText().equals("")) {
            filter = filter.and((o) -> o.getSection().getName().toLowerCase().startsWith(sectionFilterTextField.getText().toLowerCase()));
            filtered = true;
        }
        List<Option> filteredList = service.filter(filter);
        tableView.setItems(FXCollections.observableArrayList(filteredList));
        buttonClearFilter.setDisable(!filtered);
    }
}
