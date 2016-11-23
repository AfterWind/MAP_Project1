package afterwind.lab1.entity;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.Serializable;

/**
 * Retine si gestioneaza datele unei sectiuni
 */
public class Section implements IIdentifiable<Integer>, Serializable, Comparable<Section> {
    private final static long serialVersionUID = 1L;

    private SimpleIntegerProperty id;
    private SimpleStringProperty name;
    private SimpleIntegerProperty nrLoc;

    /**
     * Constructor pentru o sectiune
     * @param id Identificatorul unic
     * @param name Numele
     * @param nrLoc Numarul de locuri disponibile
     */
    public Section(int id, String name, int nrLoc) {
        this.name = new SimpleStringProperty(name);
        this.id = new SimpleIntegerProperty(id);
        this.nrLoc = new SimpleIntegerProperty(nrLoc);
    }

    /**
     * Getter pentru nume
     * @return numele sectiunii
     */
    public String getName() {
        return name.getValue();
    }

    /**
     * Getter pentru id
     * @return id-ul sectiunii
     */
    @Override
    public Integer getId() {
        return id.getValue();
    }

    /**
     * Getter pentru nrLoc
     * @return numarul de locuri disponibile al sectiunii
     */
    public int getNrLoc() {
        return nrLoc.getValue();
    }

    /**
     * Setter pentru nume
     * @param name noul nume al sectiunii
     */
    public void setName(String name) {
        this.name.set(name);
    }

    /**
     * Setter pentru nrLoc
     * @param nrLoc nou numar de locuri disponibile al sectiunii
     */
    public void setNrLoc(int nrLoc) {
        this.nrLoc.set(nrLoc);
    }

    /**
     * Converteste obiectul intr-un String pentru afisare
     * @return un string care contine datele sectiunii
     */
    @Override
    public String toString() {
        return String.format("%3s | %20s | %5s", id, name, nrLoc);
    }

    /**
     * Verifica daca acest obiect este egal cu un altul dat
     * @param obj obiectul cu care se va compara
     * @return daca acesta si obiectul dat sunt egale
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Section) {
            return id == ((Section) obj).id;
        }
        return false;
    }

    @Override
    public int compareTo(Section o) {
        return getName().compareTo(o.getName());
    }

    public static class Serializer implements ISerializer<Section> {

        @Override
        public Section deserialize(String s) {
            String[] data = s.split("\\|");
            return new Section(Integer.parseInt(data[0]), data[1], Integer.parseInt(data[2]));
        }

        @Override
        public String serialize(Section e) {
            return String.format("%d|%s|%d", e.getId(), e.getName(), e.getNrLoc());
        }
    }
}
