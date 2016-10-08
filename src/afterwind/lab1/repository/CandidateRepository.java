package afterwind.lab1.repository;

import afterwind.lab1.entity.Candidate;

/**
 * Stratul de "repository" pentru entitatea Candidate
 */
public class CandidateRepository {
    public final Candidate[] data = new Candidate[100];
    private int size = 0;

    /**
     * Getter pentru size
     * @return cate elemente se afla in repository
     */
    public int getSize() {
        return size;
    }

    /**
     * Adauga un candidat in repository daca mai este spatiu
     * @param candidate candidatul care va fi adaugat
     */
    public void add(Candidate candidate) {
        if (size < data.length) {
            data[size++] = candidate;
        }
    }

    /**
     * Getter pentru un candidat din repository
     * @param id id-ul candidatului cautat
     * @return un candidat cu id-ul dat sau null daca nu exista
     */
    public Candidate get(int id) {
        for (int i = 0; i < size; i++) {
            if (data[i].getId() == id) {
                return data[i];
            }
        }
        return null;
    }

    /**
     * Sterge candidatul dat din repository
     * @param candidate candidatul care va fi sters
     */
    public void remove(Candidate candidate) {
        for (int i = 0; i < size; i++) {
            if (candidate.equals(data[i])) {
                remove(i);
                return;
            }
        }
    }

    /**
     * Sterge candidatul de pe pozitia data din vector
     * @param pos pozitia candidatului care va fi sters din repository
     * @return candidatul sters din repository
     */
    public Candidate remove(int pos) {
        Candidate c = data[pos];
        for (int i = pos + 1; i < size; i++) {
            data[i - 1] = data[i];
        }
        size--;
        return c;
    }
}
