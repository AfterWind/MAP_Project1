package afterwind.lab1.service;

import afterwind.lab1.entity.Candidate;
import afterwind.lab1.entity.Option;
import afterwind.lab1.entity.Section;
import afterwind.lab1.repository.IRepository;
import afterwind.lab1.repository.Repository;
import afterwind.lab1.validator.OptionValidator;

/**
 * Service pentru manipularea optiunilor
 */
public class OptionService extends AbstractService<Option> {
    /**
     * Contructor pentru OptionController
     */
    public OptionService(IRepository<Option, Integer> repo) {
        super(repo);
    }

    public OptionService() {
        this(new Repository<>(new OptionValidator()));
    }

    @Override
    public String toString() {
        return repo.toString();
    }

}
