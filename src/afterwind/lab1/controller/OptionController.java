package afterwind.lab1.controller;

import afterwind.lab1.Utils;
import afterwind.lab1.entity.Candidate;
import afterwind.lab1.entity.Option;
import afterwind.lab1.entity.Section;
import afterwind.lab1.exception.ValidationException;
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
import javafx.scene.layout.HBox;

import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.function.Predicate;

/**
 * MVC Controller pentru Options
 */
public class OptionController {

    @FXML
    public TableView<Option> tableView;
    @FXML
    public TableColumn<Option, String> idColumn;
    @FXML
    public TableColumn<Option, Section> sectionColumn;
    @FXML
    public TableColumn<Option, Candidate> candidateColumn;
    @FXML
    public Button deleteButton, saveButton, refreshButton, clearFilterButton;
    @FXML
    public ListView<Candidate> candidateList;
    @FXML
    public ListView<Section> sectionList;
    @FXML
    public Button addButton;
    public TextField candidateFilterTextField, sectionFilterTextField;

    private OptionService service;
    private CandidateService candidateService;
    private SectionService sectionService;

    private Predicate<Option> filter = (o) -> true;

    private ObservableList<Option> model;

    public void showAll() {
        tableView.setItems(FXCollections.observableArrayList(service.filter(filter)));
        candidateList.setItems(candidateService.getRepo().getData());
        sectionList.setItems(sectionService.getRepo().getData());
    }

    public void setServices(OptionService service, CandidateService candidateService, SectionService sectionService) {
        this.service = service;
        this.candidateService = candidateService;
        this.sectionService = sectionService;
        this.model = service.getRepo().getData();
        showAll();
    }

    @FXML
    public void initialize() {
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

    public void handleDelete(ActionEvent ev) {
        Option o = tableView.getSelectionModel().getSelectedItem();
        if (o == null) {
            Utils.showErrorMessage("Nu ati selectat nicio Optiune!");
            return;
        }
        service.remove(o);
    }

    public void handleRefresh(ActionEvent ev) {
        tableView.setItems(FXCollections.observableArrayList(service.filter(filter)));
        candidateList.setItems(candidateService.getRepo().getData());
        sectionList.setItems(sectionService.getRepo().getData());
    }

    public void handleSave(ActionEvent ev) {
        Utils.showInfoMessage("Totul s-a salvat in fisier!");
        service.getRepo().updateLinks();
    }

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
        } catch (ValidationException e) {
            Utils.showErrorMessage(e.getMessage());
        }
    }

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
        clearFilterButton.setDisable(!filtered);
    }

    public void handleClearFilter(ActionEvent ev) {
        tableView.setItems(model);
        clearFilterTextFields();
        clearFilterButton.setDisable(true);
    }

    private void clearFilterTextFields() {
        candidateFilterTextField.setText("");
        sectionFilterTextField.setText("");
    }
}
