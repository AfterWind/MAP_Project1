package afterwind.lab1.repository;

import afterwind.lab1.entity.Candidate;
import afterwind.lab1.entity.IIdentifiable;
import afterwind.lab1.exception.ValidationException;
import javafx.collections.ObservableList;

public interface IRepository<T extends IIdentifiable<K>, K> {

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
    T get(K id);

    /**
     * Verifica daca o entitate cu id-ul dat exista
     * @param id identificatorul unic al entitatii
     * @return daca acesta exista in repository
     */
    boolean contains(K id);

    /**
     * Adauga o entitate in repository
     * @param e entitatea care va fi adaugata
     */
    void add(T e) throws ValidationException;

    /**
     * Sterge o entitate din repository
     * @param e entitatea care va fi stearsa
     */
    void remove(T e);

    /**
     * Returneaza elementele
     * @return un obiect iterabil care contine toate elementele
     */
    ObservableList<T> getData();

    /**
     * Getter pentru tableHeader
     * @return tableHeader-ul
     */
    String getTableHeader();

    /**
     * Setter pentru tableHeader
     * @param tableHeader noul tableHeader
     */
    void setTableHeader(String tableHeader);

    /**
     * Updates the entity with the data given
     * @param k The key of the existing entity in the repository
     * @param data The data to update with
     */
    void update(K k, T data);

    /**
     * Marks the repository as being dirty which means:
     *  - The repository has been modified AND
     *  - Linked objects haven't been notified OR
     *  - Files haven't been updated
     */
    void markDirty();

    /**
     * If the Repository is dirty all the linked objects should be notified and updated
     */
    void updateLinks();
}
