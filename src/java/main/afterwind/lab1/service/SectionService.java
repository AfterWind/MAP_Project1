package afterwind.lab1.service;

import afterwind.lab1.entity.Option;
import afterwind.lab1.entity.Section;
import afterwind.lab1.exception.ValidationException;
import afterwind.lab1.repository.IRepository;
import afterwind.lab1.repository.Repository;
import afterwind.lab1.validator.SectionValidator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Section service in MVC
 */
public class SectionService extends AbstractService<Section> {
    /**
     * Constructor pentru SectionController
     */
    public SectionService(IRepository<Section, Integer> repo) {
        super(repo);
    }

    public SectionService() {
        this(new Repository<>(new SectionValidator()));
    }

    /**
     * Filtrare dupa nume
     * @param name stringul cu care este comparat numele prin startsWith
     * @return un repository care contine toate datele filtrate
     */
    public IRepository<Section, Integer> filterByName(String name) {
        IRepository<Section, Integer> result = new Repository<>(new SectionValidator());
        List<Section> sortedList = sort(filter((s) -> s.getName().startsWith(name)), (s1, s2) -> s1.getNrLoc() - s2.getNrLoc());
        for (Section section : sortedList) {
            try {
                result.add(section);
            } catch (ValidationException ex) {
                System.out.print(ex.getMessage());
            }
        }
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
        List<Section> sortedList = sort(filter((s) -> lower && s.getNrLoc() <= nrLoc || !lower && s.getNrLoc() >= nrLoc), (s1, s2) -> s1.getNrLoc() - s2.getNrLoc());
        for (Section section : sortedList) {
            try {
                result.add(section);
            } catch (ValidationException ex) {
                System.out.print(ex.getMessage());
            }
        }
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
        if (amount > this.repo.getSize()) {
            return getRepo();
        }

        Map<Section, Integer> v = new HashMap<>();
        for (Section s : getData()) {
            v.put(s, (int) options.getData().stream().filter(o -> o.getSection().getId() == s.getId()).count());
        }
        for (int i = 0; i < amount; i++) {
            Section found = null;
            int max = -1;
            for (Map.Entry<Section, Integer> entry : v.entrySet()) {
                if (max < entry.getValue()) {
                    found = entry.getKey();
                    max = entry.getValue();
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
