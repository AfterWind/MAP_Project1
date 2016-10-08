package afterwind.lab1.repository;

import afterwind.lab1.entity.Section;

/**
 * Stratul de "Repository" pentru entitatea "Section"
 */
public class SectionRepository {
    public final Section[] data = new Section[100];
    private int size = 0;

    /**
     * Getter pentru size
     * @return numarul de elemente din repository
     */
    public int getSize() {
        return size;
    }

    /**
     * Adauga o sectiune in repository
     * @param section sectiunea care va fi adaugata
     */
    public void add(Section section) {
        if (size < data.length) {
            data[size++] = section;
        }
    }

    /**
     * Sterge o sectiune din repository
     * @param section sectiunea care va fi stearsa
     */
    public void remove(Section section) {
        for (int i = 0; i < size; i++) {
            if (section.equals(data[i])) {
                remove(i);
                return;
            }
        }
    }

    /**
     * Sterge elementul de pe pozitia data din repository
     * @param pos pozitia sectiunii in repository
     * @return sectiunea stearsa
     */
    public Section remove(int pos) {
        Section section = data[pos];
        for (int i = pos + 1; i < size; i++) {
            data[i - 1] = data[i];
        }
        size--;
        return section;
    }

    /**
     * Getter pentru o sectiune cu id-ul dat
     * @param id id-ul sectiunii cautate
     * @return sectiunea cu id-ul dat sau null daca aceasta nu exista
     */
    public Section get(int id) {
        for (int i = 0; i < size; i++) {
            if (data[i].getId() == id) {
                return data[i];
            }
        }
        return null;
    }
}
