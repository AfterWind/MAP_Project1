package afterwind.lab1.controller;

import afterwind.lab1.entity.Section;
import afterwind.lab1.repository.IRepository;
import afterwind.lab1.repository.Repository;
import afterwind.lab1.validator.SectionValidator;

/**
 * Stratul de "Controller" pentru entitatea "Section"
 * Folosita pentru schimbul de informatii dintre Repository si UI
 */
public class SectionController extends AbstractController<Section> {

    /**
     * Constructor pentru SectionController
     */
    public SectionController(IRepository<Section, Integer> repo) {
        super(repo);
        repo.setTableHeader(String.format("%3s | %20s | %5s", "ID", "Nume", "Numar locuri"));
    }

    public SectionController() {
        this(new Repository<>(new SectionValidator()));
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
    public IRepository<Section, Integer> filterByName(String name) {
        IRepository<Section, Integer> result = new Repository<>(new SectionValidator());
        for (Section s : repo.getData()) {
            if (s.getName().startsWith(name)) {
                result.add(s);
            }
        }
        result.setTableHeader(repo.getTableHeader());
        return result;
    }

    /**
     * Filtrare dupa numar de locuri
     * @param nrLoc transa
     * @param lower daca verifica daca e mai mic sau mai mare decat transa
     * @return un repository care contine toate datele filtrate
     */
    public IRepository<Section, Integer> filterByNrLoc(int nrLoc, boolean lower) {
        IRepository<Section, Integer> result = new Repository<>(new SectionValidator());
        for (Section s : repo.getData()) {
            if (lower && s.getNrLoc() <= nrLoc || !lower && s.getNrLoc() >= nrLoc) {
                result.add(s);
            }
        }
        result.setTableHeader(repo.getTableHeader());
        return result;
    }

    @Override
    public String toString() {
        return repo.toString();
    }
}
