package afterwind.lab1.repository;

import afterwind.lab1.entity.IIdentifiable;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Repository<T extends IIdentifiable> implements IRepository<T> {

    protected String tableHeader = "";
    protected List<T> data = new ArrayList<>();

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
    public void add(T e) {
        data.add(e);
    }

    /**
     * Sterge o entitate din repository
     * @param e entitatea care va fi stearsa
     */
    @Override
    public void remove(T e) {
        Iterator<T> it = data.iterator();
        while (it.hasNext()) {
            if (it.equals(e)) {
                it.remove();
                return;
            }
        }
    }

    /**
     * Getter pentru o entitate cu id-ul dat
     * @param id id-ul entitatii cautate
     * @return entitatea cu id-ul dat sau null daca aceasta nu exista
     */
    @Override
    public T get(int id) {
        for (T e : data) {
            if (e.getId() == id) {
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
    public boolean contains(int id) {
        return get(id) != null;
    }

    /**
     * @return vectorul de entitati
     */
    @Override
    public List<T> getData() {
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
                if (i != size - 1) result += "\n";
                result += data.get(i).toString();
            }
        }
        return result;
    }
}
