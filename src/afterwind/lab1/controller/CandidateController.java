package afterwind.lab1.controller;

import afterwind.lab1.entity.Candidate;
import afterwind.lab1.exception.ValidationException;
import afterwind.lab1.repository.FileRepository;
import afterwind.lab1.repository.IRepository;
import afterwind.lab1.repository.Repository;
import afterwind.lab1.validator.CandidateValidator;
import afterwind.lab1.validator.IValidator;

/**
 * Stratul de "Controller" pentru entitatea "Candidate"
 * Folosita pentru schimbul de informatii dintre Repository si UI
 */
public class CandidateController extends AbstractController<Candidate> {

    /**
     * Constructor pentru CandidateController
     */
    public CandidateController(IRepository<Candidate, Integer> repo) {
        super(repo);
        repo.setTableHeader(String.format("%3s | %20s | %15s | %15s", "ID", "Nume", "Telefon", "Adresa"));
    }

    public CandidateController() {
        this(new Repository<>(new CandidateValidator()));
    }

    /**
     * Updateaza datele aflate in entitatea candidat
     * @param candidate candidatul updatat
     * @param name noul nume al candidatului
     * @param tel noul numar de telefon al candidatului
     * @param address noua adresa a candidatului
     */
    public void updateCandidate(Candidate candidate, String name, String tel, String address) {
        candidate.setName(name);
        candidate.setAddress(address);
        candidate.setTel(tel);
        repo.markDirty();
    }

    /**
     * Filtrare dupa nume
     * @param name stringul cu care este comparat numele prin startsWith
     * @return un repository care contine toate datele filtrate
     */
    public IRepository<Candidate, Integer> filterByName(String name) {
        IRepository<Candidate, Integer> result = new Repository<>(new CandidateValidator());
        for (Candidate candidate : filter((c) -> c.getName().startsWith(name))) {
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
        for (Candidate candidate : filter((c) -> c.getTel().startsWith(telephone))) {
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
        for (Candidate candidate : filter((c) -> c.getAddress().startsWith(address))) {
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
