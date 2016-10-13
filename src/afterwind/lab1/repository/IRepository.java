package afterwind.lab1.repository;

import afterwind.lab1.entity.IIdentifiable;

public interface IRepository<T extends IIdentifiable> {

    /**
     * Getter pentru size
     * @return numarul de elemente
     */
    int getSize();

    /**
     * Getter pentru o entitate cu id-ul dat
     * @param id id-ul entitatii cautate
     * @return entitatea cu id-ul dat sau null daca aceasta nu exista
     */
    T get(int id);

    /**
     * Verifica daca o entitate cu id-ul dat exista
     * @param id identificatorul unic al entitatii
     * @return daca acesta exista in repository
     */
    boolean contains(int id);

    /**
     * Adauga o entitate in repository
     * @param e entitatea care va fi adaugata
     */
    void add(T e);

    /**
     * Sterge o entitate din repository
     * @param e entitatea care va fi stearsa
     */
    void remove(T e);

    /**
     * Sterge elementul de pe pozitia data din repository
     * @param pos pozitia entitatii in repository
     * @return entitatea stearsa
     */
    T remove(int pos);
}