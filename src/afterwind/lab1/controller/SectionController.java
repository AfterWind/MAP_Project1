package afterwind.lab1.controller;

import afterwind.lab1.entity.Option;
import afterwind.lab1.entity.Section;
import afterwind.lab1.exception.ValidationException;
import afterwind.lab1.repository.IRepository;
import afterwind.lab1.repository.Repository;
import afterwind.lab1.validator.SectionValidator;
import com.sun.deploy.util.OrderedHashSet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                try {
                    result.add(s);
                } catch (ValidationException ex) {
                    System.out.print(ex.getMessage());
                }
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
                try {
                    result.add(s);
                } catch (ValidationException ex) {
                    System.out.print(ex.getMessage());
                }
            }
        }
        result.setTableHeader(repo.getTableHeader());
        return result;
    }

    /**
     * Calculeaza cele mai solicitate sectii
     * @param options repository cu toate optiunile
     * @param amount primele cate sectiuni
     * @return un repository cu sectiunile cele mai solicitate
     */
    public IRepository<Section, Integer> getMostOccupiedSections(IRepository<Option, Integer> options, int amount) {
        IRepository<Section, Integer> repo = new Repository<>(new SectionValidator());
        if (getSize() == 0 || amount <= 0) {
            return repo;
        }

        Map<Section, Integer> v = new HashMap<>();
        for (Option o : options.getData()) {
            int a = v.containsKey(o.getSection()) ? v.get(o.getSection()) : 0;
            v.put(o.getSection(), a + 1);
        }
        for (int i = 0; i < amount; i++) {
            Section found = null;
            int max = 0;
            for (Map.Entry<Section, Integer> entry : v.entrySet()) {
                if (max < entry.getValue()) {
                    found = entry.getKey();
                }
            }
            v.remove(found);
            try {
                repo.add(found);
            } catch (ValidationException ex) { ex.printStackTrace(); }
        }
        return repo;
    }

    @Override
    public String toString() {
        return repo.toString();
    }
}
