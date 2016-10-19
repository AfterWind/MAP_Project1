package afterwind.lab1.entity;

/**
 * O legatura intre candidat si sectiune
 */
public class Option implements IIdentifiable<Integer> {

    private int id;
    private Section section;
    private Candidate candidate;

    /**
     * Constructorul unei optiuni
     * @param id identificatorul unic
     * @param section sectiunea
     * @param candidate candidatul
     */
    public Option(int id, Section section, Candidate candidate) {
        this.id = id;
        this.section = section;
        this.candidate = candidate;
    }

    /**
     * Getter pentru sectiune
     * @return sectiunea alesa de candidat
     */
    public Section getSection() {
        return section;
    }

    /**
     * Getter pentru candidat
     * @return candidatul care a ales sectiunea
     */
    public Candidate getCandidate() {
        return candidate;
    }

    /**
     * Setter pentru sectiune
     * @param section noua sectiune aleasa de candidat
     */
    public void setSection(Section section) {
        this.section = section;
    }

    /**
     * Setter pentru candidat
     * @param candidate noul candidat
     */
    public void setCandidate(Candidate candidate) {
        this.candidate = candidate;
    }

    /**
     * Getter pentru id
     * @return identificatorul unic al optiunii
     */
    @Override
    public Integer getId() {
        return id;
    }

    /**
     * Converteste obiectul intr-un String pentru afisare
     * @return un string care contine datele candidatului
     */
    @Override
    public String toString() {
        return String.format("%3s | %20s | %20s", id, candidate.getName(), section.getName());
    }

    /**
     * Verifica daca acest obiect este egal cu un altul dat
     * @param obj obiectul cu care se va compara
     * @return daca acesta si obiectul dat sunt egale
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Option) {
            return ((Option) obj).getId() == id;
        } else {
            return false;
        }
    }
}
