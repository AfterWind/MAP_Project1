package afterwind.lab1.controller;

import afterwind.lab1.entity.IIdentifiable;
import afterwind.lab1.exception.ValidationException;
import afterwind.lab1.repository.IRepository;
import afterwind.lab1.repository.Repository;
import afterwind.lab1.validator.IValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public abstract class AbstractController<T extends IIdentifiable<Integer>> {
    protected IRepository<T, Integer> repo;

    public AbstractController(IRepository<T, Integer> repo) {
        this.repo = repo;
    }
    /**
     * Adauga o entitate in repository
     * @param e entitatea care va fi adaugata
     */
    public void add(T e) throws ValidationException {
        repo.add(e);
        repo.markDirty();
    }

    /**
     * Sterge o entitate din repository
     * @param e entitatea care va fi stearsa
     */
    public void remove(T e) {
        repo.remove(e);
        repo.markDirty();
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
        repo.markDirty();
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
     * Filtreaza elementele bazandu-se pe pedicatul dat.
     * @param pred predicatul folosit pentru testare
     * @return o noua lista cu toate elementele care au trecut testul predicatului
     */
    public List<T> filter(Predicate<T> pred) {
        return StreamSupport.stream(getData().spliterator(), false)
                .filter(pred)
                .sorted()
                .collect(Collectors.toList());
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

    /**
     * @return repository-ul cu toate elementele
     */
    public IRepository<T, Integer> getRepo() {
        return repo;
    }
}
