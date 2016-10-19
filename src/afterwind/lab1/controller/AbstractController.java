package afterwind.lab1.controller;

import afterwind.lab1.entity.IIdentifiable;
import afterwind.lab1.repository.IRepository;
import afterwind.lab1.repository.Repository;
import afterwind.lab1.validator.IValidator;

public abstract class AbstractController<T extends IIdentifiable<Integer>> {
    protected IRepository<T, Integer> repo;

    public AbstractController(IRepository<T, Integer> repo) {
        this.repo = repo;
    }
    /**
     * Adauga o entitate in repository
     * @param e entitatea care va fi adaugata
     */
    public void add(T e) {
        repo.add(e);
    }

    /**
     * Sterge o entitate din repository
     * @param e entitatea care va fi stearsa
     */
    public void remove(T e) {
        repo.remove(e);
    }

    /**
     * Sterge entitatea cu id-ul dat din repository
     * @param id id-ul entitatii care va fi stearss
     */
    public T remove(int id) {
        T e = get(id);
        if (e != null) {
            repo.remove(e);
        }
        return e;
    }

    /**
     * Returneaza entitatea cu id-ul dat
     * @param id id-ul entitatii cautate
     * @return entitatea cu id-ul dat sau null daca acesta nu exista
     */
    public T get(int id) {
        return repo.get(id);
    }

    /**
     * Verifica daca o entitate cu id-ul dat exista
     * @param id identificatorul unic al entitatii
     * @return daca entitatea cu id-ul dat exista in repository
     */
    public boolean contains(int id) {
        return repo.contains(id);
    }

    /**
     * @return cate entitati se afla in repository
     */
    public int getSize() {
        return repo.getSize();
    }

    /**
     * Cauta un nou id care nu exista in repository
     * @return noul id
     */
    public int getNextId() {
        int max = -1;
        for (T e : repo.getData()) {
            if (e.getId() > max) {
                max = e.getId();
            }
        }
        return max + 1;
    }

    /**
     * @return vectorul de entitati din repository
     */
    public Iterable<T> getData() {
        return repo.getData();
    }
}
