package afterwind.lab1.controller;

import afterwind.lab1.entity.Candidate;

/**
 * Stratul de "Controller" pentru entitatea "Candidate"
 * Folosita pentru schimbul de informatii dintre Repository si UI
 */
public class CandidateController extends AbstractController<Candidate> {
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
}
