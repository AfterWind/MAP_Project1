package afterwind.lab1.controller;

import afterwind.lab1.repository.Repository;
import afterwind.lab1.entity.Section;

/**
 * Stratul de "Controller" pentru entitatea "Section"
 * Folosita pentru schimbul de informatii dintre Repository si UI
 */
public class SectionController {
    private final Repository<Section> repo = new Repository<>(Section.class);

    /**
     * Adauga o sectiune in repository
     * @param section sectionuea care va fi adaugata
     */
    public void addSection(Section section) {
        repo.add(section);
    }

    /**
     * Getter pentru sectiunea cu id-ul dat
     * @param id id-ul sectiunii cautate
     * @return sectiunea cu id-ul dat sau null daca aceasta nu exista
     */
    public Section getSection(int id) {
        return repo.get(id);
    }

    /**
     * Sterge o sectiune din repository
     * @param section sectiunea care va fi stearsa
     */
    public void removeSection(Section section) {
        repo.remove(section);
    }

    /**
     * Sterge o sectiune cu id-ul dat
     * @param id id-ul sectiunii care va fi stearsa
     */
    public Section removeSection(int id) {
        Section s = getSection(id);
        if (s != null) {
            removeSection(s);
        }
        return s;
    }

    /**
     * Updateaza datele unei sectiuni
     * @param section sectiunea care va fi updatata
     * @param name noul nume
     * @param nrLoc noul numar de locuri disponibile
     */
    public void updateSection(Section section, String name, int nrLoc) {
        section.setName(name);
        section.setNrLoc(nrLoc);
    }

    /**
     * @return numarul de sectiuni
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
        for (Section c : repo.data) {
            if (c != null && c.getId() > max) {
                max = c.getId();
            }
        }
        return max + 1;
    }

    /**
     * @return vectorul de sectiuni
     */
    public Section[] getSections() {
        return repo.data;
    }
}
