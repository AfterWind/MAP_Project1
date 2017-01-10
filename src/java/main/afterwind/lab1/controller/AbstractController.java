package afterwind.lab1.controller;

import afterwind.lab1.Utils;
import afterwind.lab1.entity.IIdentifiable;
import afterwind.lab1.entity.Option;
import afterwind.lab1.repository.FileRepository;
import afterwind.lab1.service.AbstractService;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.function.Predicate;


public abstract class AbstractController<T extends IIdentifiable<Integer>> {

    @FXML
    public TableView<T> tableView;

    @FXML
    public Button buttonDelete, buttonSave, buttonRefresh,
            buttonAdd, buttonUpdate, buttonClear,
            buttonClearFilter;

    protected AbstractService<T> service;

    protected Predicate<T> filter = (e) -> true;

    @FXML
    public void initialize() {
        buttonAdd.setOnAction(this::handleAdd);
        buttonUpdate.setOnAction(this::handleUpdate);
        buttonClear.setOnAction(this::handleClear);
        buttonDelete.setOnAction(this::handleDelete);
        buttonRefresh.setOnAction(this::handleRefresh);
        buttonSave.setOnAction(this::handleSave);
        buttonClearFilter.setOnAction(this::handleClearFilter);

        tableView.getSelectionModel().selectedItemProperty().addListener(this::handleSelectionChanged);
    }

    public void applyFilter() {
        List<T> filteredList = service.filter(filter);
        tableView.setItems(FXCollections.observableArrayList(filteredList));
        buttonClearFilter.setDisable(false);
    }

    /**
     * Afiseaza toti candidatii
     */
    public void showAll() {
        tableView.setItems(FXCollections.observableArrayList(service.filter(filter)));
    }

    protected abstract void showDetails(T t);

    public abstract void updateFilter(ObservableValue<? extends String> observable, String oldValue, String newValue);

    public abstract void clearModificationTextFields();
    public abstract void clearFilterTextFields();

    public abstract void handleAdd(ActionEvent ev);
    public abstract void handleUpdate(ActionEvent ev);
    public abstract void handleDelete(ActionEvent ev);

    /**
     * Apelat daca selectia se modifica
     * @param oldValue valoarea veche
     * @param newValue valoarea noua
     */
    public void handleSelectionChanged(ObservableValue<? extends T> o, T oldValue, T newValue) {
        if (newValue != null) {
            showDetails(newValue);
            buttonUpdate.setDisable(false);
        } else {
            buttonUpdate.setDisable(true);
        }
    }

    public void handleClear(ActionEvent ev) {
        clearModificationTextFields();
    }

    public void handleClearFilter(ActionEvent ev) {
        showAll();
        clearFilterTextFields();
        buttonClearFilter.setDisable(true);
    }

    public void handleRefresh(ActionEvent ev) {
        showAll();
    }

    public void handleSave(ActionEvent ev) {
        if (service.getRepo() instanceof FileRepository) {
            Utils.showInfoMessage("Totul s-a salvat in fisier!");
            service.getRepo().updateLinks();
        } else {
            Utils.showInfoMessage("Tipul de Repository nu suporta salvarea in fisier!");
        }
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
}
