package afterwind.lab1.controller;

import afterwind.lab1.repository.CandidateRepository;
import afterwind.lab1.entity.Candidate;

/**
 * Stratul de "Controller" pentru entitatea "Candidate"
 * Folosita pentru schimbul de informatii dintre Repository si UI
 */
public class CandidateController {
    private CandidateRepository repo = new CandidateRepository();

    /**
     * Adauga un candidat in repository
     * @param candidate candidatul care va fi adaugat
     */
    public void addCandidate(Candidate candidate) {
        repo.add(candidate);
    }

    /**
     * Sterge un candidat din repository
     * @param candidate candidatul care va fi sters
     */
    public void removeCandidate(Candidate candidate) {
        repo.remove(candidate);
    }

    /**
     * Sterge candidatul cu id-ul dat din repository
     * @param id id-ul candidatului care va fi sters
     */
    public void removeCandidate(int id) {
        Candidate c = getCandidate(id);
        if (c != null) {
            repo.remove(c);
        }
    }

    /**
     * Returneaza candidatul cu id-ul dat
     * @param id id-ul candidatului cautat
     * @return candidatul cu id-ul dat sau null daca acesta nu exista
     */
    public Candidate getCandidate(int id) {
        return repo.get(id);
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
     * @return cati candidati se afla in repository
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
        for (Candidate c : repo.data) {
            if (c != null && c.getId() > max) {
                max = c.getId();
            }
        }
        return max + 1;
    }

    /**
     * @return vectorul de candidati din repository
     */
    public Candidate[] getCandidates() {
        return repo.data;
    }
}
