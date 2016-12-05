package afterwind.lab1.controller;

import afterwind.lab1.Utils;
import afterwind.lab1.entity.Candidate;
import afterwind.lab1.exception.ValidationException;
import afterwind.lab1.service.CandidateService;
import afterwind.lab1.ui.CandidateView;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.Observable;
import java.util.Observer;

/**
 * MVC controller perntru Candidate
 */
public class CandidateController implements Observer {

    private CandidateService service;
    private CandidateView view;
    private ObservableList<Candidate> model;

    /**
     * Constructor pentru CandidateController
     * @param service Service-ul de candidate
     * @param view View-ul controlat
     */
    public CandidateController(CandidateService service, CandidateView view) {
        this.service = service;
        this.view = view;
        this.model = service.getRepo().getData();
    }

    /**
     * Afiseaza toti candidatii
     */
    public void showAll() {
        view.tableView.setItems(model);
    }

    /**
     * Afiseaza detaliile despre un candidat in TextField-uri
     * @param c
     */
    public void showDetails(Candidate c) {
        view.nameTextField.setText(c.getName());
        view.addressTextField.setText(c.getAddress());
        view.telTextField.setText(c.getTelephone());
    }

    /**
     * Goleste textul din toate TextField-uri
     */
    public void clearTextFields() {
        view.nameTextField.setText("");
        view.addressTextField.setText("");
        view.telTextField.setText("");
        view.tableView.getSelectionModel().clearSelection();
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
            Utils.setErrorBorder(view.nameTextField);
            errored = true;
        }
        if (address.equals("")) {
            Utils.setErrorBorder(view.addressTextField);
            errored = true;
        }
        if (tel.equals("")) {
            Utils.setErrorBorder(view.telTextField);
            errored = true;
        } else {
            for (char ch : tel.toCharArray()) {
                if (ch < '0' || ch > '9') {
                    Utils.setErrorBorder(view.telTextField);
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
        String name = view.nameTextField.getText();
        String address = view.addressTextField.getText();
        String tel = view.telTextField.getText();

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
        Candidate c = view.tableView.getSelectionModel().getSelectedItem();
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
        Candidate c = view.tableView.getSelectionModel().getSelectedItem();
        if (c == null) {
            Utils.showErrorMessage("Nu a fost selectat nici un candidat!");
            return;
        }

        String name = view.nameTextField.getText();
        String address = view.addressTextField.getText();
        String tel = view.telTextField.getText();

        if (checkFields(name, address, tel)) {
            return;
        }

        service.updateCandidate(c, name, tel, address);
        for (int i = 0; i < 4; i++) {
            view.tableView.getColumns().get(i).setVisible(false);
            view.tableView.getColumns().get(i).setVisible(true);
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
}
