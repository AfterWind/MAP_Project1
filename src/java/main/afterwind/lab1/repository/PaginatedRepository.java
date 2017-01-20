package afterwind.lab1.repository;

import afterwind.lab1.Utils;
import afterwind.lab1.entity.Candidate;
import afterwind.lab1.entity.IIdentifiable;
import afterwind.lab1.entity.Option;
import afterwind.lab1.entity.Section;
import afterwind.lab1.exception.ValidationException;
import afterwind.lab1.validator.IValidator;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

public class PaginatedRepository<T extends IIdentifiable<K>, K> implements IRepository<T, K> {

    protected Comparator<T> comparator = (t1, t2) -> 0;
    protected ObservableList<ObservableList<T>> data = FXCollections.observableArrayList();
    protected ObservableList<T> cachedData = null;
    protected IValidator<T> validator;
    protected int amount = 0;
    public final int entitiesPerPage;

    public PaginatedRepository(IValidator<T> validator, int entitiesPerPage) {
        this.validator = validator;
        this.entitiesPerPage = entitiesPerPage;
        data.add(FXCollections.observableArrayList());
    }

    public ObservableList<T> getPage(int page) {
        return data.get(page);
    }

    public int getPages() {
        return data.size();
    }

    @Override
    public int getSize() {
        return amount;
    }

    @Override
    public T get(K id) {
        for (ObservableList<T> list : data) {
            for (T t : list) {
                if (t.getId().equals(id)) {
                    return t;
                }
            }
        }
        return null;
    }

    @Override
    public boolean contains(K id) {
        for (ObservableList<T> list : data) {
            for (T t : list) {
                if (t.getId().equals(id)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void add(T e) throws ValidationException {
        validator.validate(e);
        for (int k = 0; k < data.size(); k++) {
            ObservableList<T> current = data.get(k);
            int posToInsert = 0;
            for (int i = 0; i < current.size(); i++) {
                T t = current.get(i);
                if (comparator.compare(e, t) > 0) {
                    posToInsert = i + 1;
                }
            }
            if (posToInsert != entitiesPerPage || posToInsert == entitiesPerPage && k == data.size() - 1) {
                current.add(posToInsert, e);
                amount++;
                break;
            }
        }
        normalize();
        cachedData = null;
    }

    public void addAll(Collection<T> c) throws ValidationException {
        for(T t : c) {
            add(t);
        }
    }

    @Override
    public void remove(T e) {
        for (ObservableList<T> list : data) {
            for (Iterator<T> it = list.iterator(); it.hasNext();) {
                T t = it.next();
                if (t == e) {
                    it.remove();
                    normalize();
                    cachedData = null;
                    return;
                }
            }
        }
    }

    private void normalize() {
        int k;
        /**
         * -1: lists are normalized
         * 0 : one list has one extra entity
         * 1 : one list has one fewer entity
         */
        int mode = -1;
        for (k = 0; k < data.size(); k++) {
            if (k == data.size() - 1) {
                ObservableList<T> first = data.get(k);
                if (first.size() > entitiesPerPage) {
                    mode = 0;
                    break;
                }
            } else {
                ObservableList<T> first = data.get(k);
                if (first.size() == entitiesPerPage - 1) {
                    mode = 1;
                    break;
                }
                if (first.size() > entitiesPerPage) {
                    mode = 0;
                    break;
                }
            }
        }
        ObservableList<T> list = null;
        if (mode == 0 && data.get(data.size() - 1).size() >= entitiesPerPage) {
            list = FXCollections.observableArrayList();
            data.add(list);
        }

        k++;
        for (; k < data.size(); k++) {
            ObservableList<T> previous = data.get(k - 1);
//            if (previous.size() <= entitiesPerPage) {
//                continue;
//            }
            ObservableList<T> current = data.get(k);

            if (mode == 0) {
                T t = previous.remove(entitiesPerPage);
                current.add(0, t);
            } else if(mode == 1) {
                T t = current.remove(0);
                previous.add(entitiesPerPage - 1, t);
            }
        }
        if (data.get(data.size() - 1).size() == 0) {
            data.remove(data.size() - 1);
        }
        if (list != null) {
//            list.addListener((ListChangeListener<? super T>) (c) -> normalize());
        }
    }

    @Override
    public ObservableList<T> getData() {
        if (cachedData != null) {
            return cachedData;
        } else {
            cachedData = FXCollections.observableArrayList();
            for (ObservableList<T> list : data) {
                cachedData.addAll(list);
            }
            return cachedData;
        }
    }

    @Override
    public void update(K k, T data) {
        Utils.genericUpdate(this, k, data);
    }

    public void clear() {
        amount = 0;
        data.clear();
        data.add(FXCollections.observableArrayList());
    }

    public void sortBy(Comparator<T> comparator) {
        this.comparator = comparator;
        ObservableList<T> copy = getData();
        clear();
        for(T t : copy) {
            try {
                add(t);
            } catch (ValidationException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
