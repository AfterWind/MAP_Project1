package afterwind.lab1.controller;

import afterwind.lab1.repository.Repository;
import afterwind.lab1.entity.Section;

/**
 * Stratul de "Controller" pentru entitatea "Section"
 * Folosita pentru schimbul de informatii dintre Repository si UI
 */
public class SectionController extends AbstractController<Section> {

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
}
