package afterwind.lab1.controller;

import afterwind.lab1.entity.Candidate;
import afterwind.lab1.repository.Repository;

/**
 * Stratul de "Controller" pentru entitatea "Candidate"
 * Folosita pentru schimbul de informatii dintre Repository si UI
 */
public class CandidateController extends AbstractController<Candidate> {

    /**
     * Constructor pentru CandidateController
     */
    public CandidateController() {
        super(Candidate.class);
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
    }

    /**
     * Filtrare dupa nume
     * @param name stringul cu care este comparat numele prin startsWith
     * @return un repository care contine toate datele filtrate
     */
    public Repository<Candidate> filterByName(String name) {
        Repository<Candidate> repo = new Repository<>(Candidate.class);
        for (Candidate c : repo.getData()) {
            if (c != null && c.getName().startsWith(name)) {
                repo.add(c);
            }
        }
        return repo;
    }

    /**
     * Filtrare dupa numarul de telefon
     * @param telephone stringul cu care este comparat numele prin startsWith
     * @return un repository care contine toate datele filtrate
     */
    public Repository<Candidate> filterByTelephone(String telephone) {
        Repository<Candidate> repo = new Repository<>(Candidate.class);
        for (Candidate c : repo.getData()) {
            if (c != null && c.getTel().startsWith(telephone)) {
                repo.add(c);
            }
        }
        return repo;
    }

    /**
     * Filtrare dupa adresa
     * @param address stringul cu care este comparat numele prin startsWith
     * @return un repository care contine toate datele filtrate
     */
    public Repository<Candidate> filterByAddress(String address) {
        Repository<Candidate> repo = new Repository<>(Candidate.class);
        for (Candidate c : repo.getData()) {
            if (c != null && c.getAddress().startsWith(address)) {
                repo.add(c);
            }
        }
        return repo;
    }
}
