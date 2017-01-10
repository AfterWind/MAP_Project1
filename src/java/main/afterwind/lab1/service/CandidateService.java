package afterwind.lab1.service;

import afterwind.lab1.entity.Candidate;
import afterwind.lab1.exception.ValidationException;
import afterwind.lab1.repository.IRepository;
import afterwind.lab1.repository.Repository;
import afterwind.lab1.validator.CandidateValidator;

import java.util.List;

/**
 * Candidate service in MVC
 */
public class CandidateService extends AbstractService<Candidate> {

    /**
     * Constructor pentru CandidateController
     */
    public CandidateService(IRepository<Candidate, Integer> repo) {
        super(repo);
        repo.setTableHeader(String.format("%3s | %20s | %15s | %15s", "ID", "Nume", "Telefon", "Adresa"));
    }

    public CandidateService() {
        this(new Repository<>(new CandidateValidator()));
    }

    /**
     * Filtrare dupa nume
     * @param name stringul cu care este comparat numele prin startsWith
     * @return un repository care contine toate datele filtrate
     */
    public IRepository<Candidate, Integer> filterByName(String name) {
        IRepository<Candidate, Integer> result = new Repository<>(new CandidateValidator());
        List<Candidate> sortedList = sort(filter((c) -> c.getName().startsWith(name)), (c1, c2) -> c1.getName().compareTo(c2.getName()));
        for (Candidate candidate : sortedList) {
            try {
                result.add(candidate);
            } catch (ValidationException ex) {
                System.out.print(ex.getMessage());
            }
        }
        result.setTableHeader(repo.getTableHeader());
        return result;
    }

    /**
     * Filtrare dupa numarul de telefon
     * @param telephone stringul cu care este comparat numele prin startsWith
     * @return un repository care contine toate datele filtrate
     */
    public IRepository<Candidate, Integer> filterByTelephone(String telephone) {
        IRepository<Candidate, Integer> result = new Repository<>(new CandidateValidator());
        List<Candidate> sortedList = sort(filter((c) -> c.getTelephone().startsWith(telephone)), (c1, c2) -> c1.getTelephone().compareTo(c2.getTelephone()));
        for (Candidate candidate : sortedList) {
            try {
                result.add(candidate);
            } catch (ValidationException ex) {
                System.out.println(ex.getMessage());
            }
        }

        result.setTableHeader(repo.getTableHeader());
        return result;
    }

    /**
     * Filtrare dupa adresa
     * @param address stringul cu care este comparat numele prin startsWith
     * @return un repository care contine toate datele filtrate
     */
    public Repository<Candidate, Integer> filterByAddress(String address) {
        Repository<Candidate, Integer> result = new Repository<>(new CandidateValidator());
        List<Candidate> sortedList = sort(filter((c) -> c.getAddress().startsWith(address)), (c1, c2) -> c1.getName().compareTo(c2.getName()));
        for (Candidate candidate : sortedList) {
            try {
                result.add(candidate);
            } catch (ValidationException ex) {
                System.out.print(ex.getMessage());
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
