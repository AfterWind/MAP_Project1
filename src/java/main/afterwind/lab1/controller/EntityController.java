package afterwind.lab1.controller;

import afterwind.lab1.entity.IIdentifiable;
import afterwind.lab1.permission.Permission;
import afterwind.lab1.repository.PaginatedRepository;
import afterwind.lab1.service.AbstractService;
import afterwind.lab1.ui.control.BetterPagination;
import afterwind.lab1.ui.control.StatusBar;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.function.Predicate;


public abstract class EntityController<T extends IIdentifiable<Integer>> {

    @FXML
    public TableView<T> tableView;
    @FXML
    public BetterPagination pagination;
    @FXML
    public Button buttonDelete, buttonRefresh,
            buttonAdd, buttonUpdate, buttonClear,
            buttonClearFilter;

    protected AbstractService<T> service;
    protected Predicate<T> filter = (e) -> true;
    protected PaginatedRepository<T, Integer> filteredEntities = null;
    public FancyController baseController;

    @FXML
    public void initialize() {
        buttonAdd.setOnAction(this::handleAdd);
        buttonUpdate.setOnAction(this::handleUpdate);
        buttonClear.setOnAction(this::handleClear);
        buttonDelete.setOnAction(this::handleDelete);
        buttonRefresh.setOnAction(this::handleRefresh);
        buttonClearFilter.setOnAction(this::handleClearFilter);

        tableView.getSelectionModel().selectedItemProperty().addListener(this::handleSelectionChanged);
    }

    protected void handlePageChange(int currentPage) {
        if (filteredEntities == null) {
            tableView.setItems(((PaginatedRepository) service.getRepo()).getPage(currentPage));
        } else {
            tableView.setItems(filteredEntities.getPage(currentPage));
        }
    }

    protected void updateNumberOfPages() {
        int pages;
        if (filteredEntities == null) {
            pages = ((PaginatedRepository) service.getRepo()).getPages();
        } else {
            pages = filteredEntities.getPages();
        }
        pagination.setMaxPages(pages);
    }

    protected void disableBasedOnPermissions() {
        buttonAdd.setDisable(!Permission.MODIFY.check());
        buttonUpdate.setDisable(!Permission.MODIFY.check());
        buttonClear.setDisable(!Permission.MODIFY.check());
        buttonDelete.setDisable(!Permission.MODIFY.check());

        tableView.setDisable(!Permission.QUERY.check());
        buttonRefresh.setDisable(!Permission.QUERY.check());
        buttonClearFilter.setDisable(!Permission.QUERY.check());
    }

    void generateStatusBarMessages(StatusBar statusBar) {
        statusBar.addMessage(buttonAdd, "Adds an entity to the repository");
        statusBar.addMessage(buttonDelete, "Removes the selected entity from the repository");
        statusBar.addMessage(buttonClear, "Clears the fields above");
        statusBar.addMessage(buttonClearFilter, "Clears the filter text fields");
        statusBar.addMessage(buttonRefresh, "Rebinds the data to the table view");
        statusBar.addMessage(buttonUpdate, "Updates the selected entity with the data in the fields above");
        statusBar.addMessage(tableView, "Shows all the entities from the repository and is updated real time");
    }

    /**
     * Afiseaza toti candidatii
     */
    public void showAll() {
//        tableView.setItems(FXCollections.observableArrayList(service.filter(filter)));
        handlePageChange(0);
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
            if (Permission.MODIFY.check()) {
                buttonUpdate.setDisable(false);
            }
        } else {
            buttonUpdate.setDisable(true);
        }
    }

    public void handleClear(ActionEvent ev) {
        clearModificationTextFields();
    }

    public void handleClearFilter(ActionEvent ev) {
        filteredEntities = null;
        updateNumberOfPages();
        showAll();
        clearFilterTextFields();
        buttonClearFilter.setDisable(true);
    }

    public void handleRefresh(ActionEvent ev) {
        showAll();
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

    public void handleMouseEntered(MouseEvent ev) {
        baseController.handleMouseEntered(ev);
    }

    public void handleMouseExited(MouseEvent ev) {
        baseController.handleMouseExited(ev);
    }
}
