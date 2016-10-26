package afterwind.lab1.entity;

import java.io.Serializable;

/**
 * Clasa pentru a retine si gestiona datele unui candidat
 */
public class Candidate implements IIdentifiable<Integer>, Serializable {
    private final static long serialVersionUID = 1L;

    private int id;
    private String name;
    private String tel;
    private String address;

    /**
     * Constructor pentru un obiect Candidate
     * @param id Identificatorul unic
     * @param name Numele
     * @param tel Numarul de telefon
     * @param address Adresa
     */
    public Candidate(int id, String name, String tel, String address) {
        this.name = name;
        this.id = id;
        this.tel = tel;
        this.address = address;
    }

    /**
     * Getter pentru nume
     * @return numele candidatului
     */
    public String getName() {
        return name;
    }

    /**
     * Getter pentru id
     * @return id-ul candidatului
     */
    @Override
    public Integer getId() {
        return id;
    }

    /**
     * Getter pentru numarul de telefon
     * @return numarul de telefon al candidatului
     */
    public String getTel() {
        return tel;
    }

    /**
     * Getter pentru adresa
     * @return adresa candidatului
     */
    public String getAddress() {
        return address;
    }

    /**
     * Setter pentru nume
     * @param name noul nume al candidatului
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Setter pentru numarul de telefon
     * @param tel noul numar de telefon al candidatului
     */
    public void setTel(String tel) {
        this.tel = tel;
    }

    /**
     * Setter pentru adresa
     * @param address noua adresa a candidatului
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Converteste obiectul intr-un String pentru afisare
     * @return un string care contine datele candidatului
     */
    @Override
    public String toString() {
        return String.format("%3s | %20s | %15s | %15s", id, name, tel, address);
    }

    /**
     * Verifica daca acest obiect este egal cu un altul dat
     * @param obj obiectul cu care se va compara
     * @return daca acesta si obiectul dat sunt egale
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Candidate) {
            return id == ((Candidate) obj).id;
        }
        return false;
    }

    public static class Serializer implements ISerializer<Candidate> {

        @Override
        public Candidate deserialize(String s) {
            String[] data = s.split("\\|");
            return new Candidate(Integer.parseInt(data[0]), data[1], data[2], data[3]);
        }

        @Override
        public String serialize(Candidate e) {
            return String.format("%d|%s|%s|%s", e.getId(), e.getName(), e.getTel(), e.getAddress());
        }
    }
}
