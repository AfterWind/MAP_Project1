package afterwind.lab1.entity;

import afterwind.lab1.Utils;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.Serializable;

/**
 * Clasa pentru a retine si gestiona datele unui candidat
 */
public class Candidate implements IIdentifiable<Integer>, Serializable, Comparable<Candidate>{
    private final static long serialVersionUID = 1L;

    private SimpleIntegerProperty id;
    private SimpleStringProperty name;
    private SimpleStringProperty telephone;
    private SimpleStringProperty address;

    /**
     * Constructor pentru un obiect Candidate
     * @param id Identificatorul unic
     * @param name Numele
     * @param telephone Numarul de telefon
     * @param address Adresa
     */
    public Candidate(int id, String name, String telephone, String address) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.telephone = new SimpleStringProperty(telephone);
        this.address = new SimpleStringProperty(address);
    }

    /**
     * Getter pentru nume
     * @return numele candidatului
     */
    public String getName() {
        return name.get();
    }

    /**
     * Getter pentru id
     * @return id-ul candidatului
     */
    @Override
    public Integer getId() {
        return id.get();
    }

    /**
     * Getter pentru numarul de telefon
     * @return numarul de telefon al candidatului
     */
    public String getTelephone() {
        return telephone.get();
    }

    /**
     * Getter pentru adresa
     * @return adresa candidatului
     */
    public String getAddress() {
        return address.get();
    }

    /**
     * Setter pentru nume
     * @param name noul nume al candidatului
     */
    public void setName(String name) {
        this.name.set(name);
    }

    /**
     * Setter pentru numarul de telefon
     * @param telephone noul numar de telefon al candidatului
     */
    public void setTelephone(String telephone) {
        this.telephone.set(telephone);
    }

    /**
     * Setter pentru adresa
     * @param address noua adresa a candidatului
     */
    public void setAddress(String address) {
        this.address.set(address);
    }

    /**
     * Converteste obiectul intr-un String pentru afisare
     * @return un string care contine datele candidatului
     */
    @Override
    public String toString() {
        return String.format("%3s | %20s | %15s | %15s", getId(), getName(), getTelephone(), getAddress());
    }

    /**
     * Verifica daca acest obiect este egal cu un altul dat
     * @param obj obiectul cu care se va compara
     * @return daca acesta si obiectul dat sunt egale
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof Candidate && getId().equals(((Candidate) obj).getId());
    }

    @Override
    public int compareTo(Candidate o) {
        return getName().compareTo(o.getName());
    }

    public static class Serializer implements ISerializer<Candidate> {

        @Override
        public Candidate deserialize(String s) {
            String[] data = s.split("\\|");
            if (!Utils.tryParseInt(data[0])) {
                return null;
            }
            return new Candidate(Integer.parseInt(data[0]), data[1], data[2], data[3]);
        }

        @Override
        public String serialize(Candidate e) {
            return String.format("%d|%s|%s|%s", e.getId(), e.getName(), e.getTelephone(), e.getAddress());
        }
    }
}
