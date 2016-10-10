package afterwind.lab1.controller;

import afterwind.lab1.entity.Section;
import afterwind.lab1.repository.Repository;

/**
 * Stratul de "Controller" pentru entitatea "Section"
 * Folosita pentru schimbul de informatii dintre Repository si UI
 */
public class SectionController extends AbstractController<Section> {

    /**
     * Constructor pentru SectionController
     */
    public SectionController() {
        super(Section.class);
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
     * Filtrare dupa nume
     * @param name stringul cu care este comparat numele prin startsWith
     * @return un repository care contine toate datele filtrate
     */
    public Repository<Section> filterByName(String name) {
        Repository<Section> repo = new Repository<>(Section.class);
        for (Section s : repo.getData()) {
            if (s != null && s.getName().startsWith(name)) {
                repo.add(s);
            }
        }
        return repo;
    }

    /**
     * Filtrare dupa numar de locuri
     * @param nrLoc transa
     * @param lower daca verifica daca e mai mic sau mai mare decat transa
     * @return un repository care contine toate datele filtrate
     */
    public Repository<Section> filterByNrLoc(int nrLoc, boolean lower) {
        Repository<Section> repo = new Repository<>(Section.class);
        for (Section s : repo.getData()) {
            if (s != null) {
                if (lower && s.getNrLoc() <= nrLoc || !lower && s.getNrLoc() >= nrLoc) {
                    repo.add(s);
                }
            }
        }
        return repo;
    }
}
