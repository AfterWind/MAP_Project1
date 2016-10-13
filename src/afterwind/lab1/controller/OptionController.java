package afterwind.lab1.controller;

import afterwind.lab1.entity.Candidate;
import afterwind.lab1.entity.Option;
import afterwind.lab1.entity.Section;

public class OptionController extends AbstractController<Option> {

    /**
     * Contructor pentru OptionController
     */
    public OptionController() {
        super(Option.class);
        repo.setTableHeader(String.format("%3s | %20s | %20s", "ID", "Candidat", "Sectiune"));
    }

    /**
     * Updateaza o optiune din repository
     * @param option optiunea updatata
     * @param candidate noul candidat
     * @param section noua sectiune
     */
    public void updateOption(Option option, Candidate candidate, Section section) {
        option.setCandidate(candidate);
        option.setSection(section);
    }
}
