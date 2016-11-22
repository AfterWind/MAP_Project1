package afterwind.lab1.repository;

import afterwind.lab1.entity.IIdentifiable;
import afterwind.lab1.exception.ValidationException;
import afterwind.lab1.validator.IValidator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Repository<T extends IIdentifiable<K>, K> implements IRepository<T, K> {

    protected String tableHeader = "";
    protected ObservableList<T> data = FXCollections.observableArrayList();
    protected IValidator<T> validator;

    public Repository(IValidator<T> validator) {
        this.validator = validator;
    }

    /**
     * Getter pentru size
     * @return numarul de elemente din repository
     */
    @Override
    public int getSize() {
        return data.size();
    }

    /**
     * Setter pentru tableHeader
     * @param tableHeader noul tableHeader
     */
    @Override
    public void setTableHeader(String tableHeader) {
        this.tableHeader = tableHeader;
    }

    /**
     * Getter pentru tableHeader
     * @return tableHeader-ul curent
     */
    @Override
    public String getTableHeader() {
        return tableHeader;
    }

    /**
     * Adauga o entitate in repository
     * @param e entitatea care va fi adaugata
     */
    @Override
    public void add(T e) throws ValidationException {
        validator.validate(e);
        data.add(e);
        markDirty();
    }

    /**
     * Sterge o entitate din repository
     * @param e entitatea care va fi stearsa
     */
    @Override
    public void remove(T e) {
        Iterator<T> it = data.iterator();
        while (it.hasNext()) {
            if (it.next().equals(e)) {
                it.remove();
                break;
            }
        }
        markDirty();
    }

    /**
     * Getter pentru o entitate cu id-ul dat
     * @param id id-ul entitatii cautate
     * @return entitatea cu id-ul dat sau null daca aceasta nu exista
     */
    @Override
    public T get(K id) {
        for (T e : data) {
            if (e.getId().equals(id)) {
                return e;
            }
        }
        return null;
    }

    /**
     * Verifica daca o entitate cu id-ul dat exista
     * @param id identificatorul unic al entitatii
     * @return daca acesta exista in repository
     */
    @Override
    public boolean contains(K id) {
        return get(id) != null;
    }

    /**
     * @return vectorul de entitati
     */
    @Override
    public ObservableList<T> getData() {
        return data;
    }

    @Override
    public String toString() {
        String result = "";
        if (getSize() == 0) {
            result += "Nu exista entitati!";
        } else {
            result += tableHeader + "\n";
            int size = getSize();
            for (int i = 0; i < size; i++) {
                result += data.get(i).toString();
                if (i != size - 1) result += "\n";
            }
        }
        return result;
    }

    @Override
    public void markDirty() {
        // No meaning. Has no links whatsoever.
    }

    @Override
    public void updateLinks() {
        // No meaning. Has no links whatsoever.
    }
}
