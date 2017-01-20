package afterwind.lab1.entity;

import afterwind.lab1.service.CandidateService;
import afterwind.lab1.service.SectionService;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * O legatura intre candidat si sectiune
 */
public class Option implements IIdentifiable<Integer> {

    private SimpleIntegerProperty id;
    private SimpleObjectProperty<Section> section;
    private SimpleObjectProperty<Candidate> candidate;

    /**
     * Constructorul unei optiuni
     * @param id identificatorul unic
     * @param section sectiunea
     * @param candidate candidatul
     */
    public Option(int id, Section section, Candidate candidate) {
        this.id = new SimpleIntegerProperty(id);
        this.section = new SimpleObjectProperty<>(section, section.getName(), section);
        this.candidate = new SimpleObjectProperty<>(candidate, candidate.getName(), candidate);
    }

    /**
     * Getter pentru sectiune
     * @return sectiunea alesa de candidat
     */
    public Section getSection() {
        return section.getValue();
    }

    /**
     * Getter pentru candidat
     * @return candidatul care a ales sectiunea
     */
    public Candidate getCandidate() {
        return candidate.getValue();
    }

    /**
     * Setter pentru sectiune
     * @param section noua sectiune aleasa de candidat
     */
    public void setSection(Section section) {
        this.section.set(section);
    }

    /**
     * Setter pentru candidat
     * @param candidate noul candidat
     */
    public void setCandidate(Candidate candidate) {
        this.candidate.set(candidate);
    }

    /**
     * Getter pentru id
     * @return identificatorul unic al optiunii
     */
    @Override
    public Integer getId() {
        return id.getValue();
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
            return ((Option) obj).getId().equals(getId());
        } else {
            return false;
        }
    }

    /**
     * A custom serializer
     */
    public static class Serializer implements ISerializer<Option> {
        private CandidateService candidateService;
        private SectionService sectionService;

        public Serializer(CandidateService candidateService, SectionService sectionService) {
            this.candidateService = candidateService;
            this.sectionService =  sectionService;
        }

        @Override
        public Option deserialize(String s) {
            String[] data = s.split("\\|");
            Section section = sectionService.get(Integer.parseInt(data[1]));
            Candidate candidate = candidateService.get(Integer.parseInt(data[2]));
            return new Option(Integer.parseInt(data[0]), section, candidate);
        }

        @Override
        public String serialize(Option e) {
            return String.format("%d|%d|%d", e.getId(), e.getSection().getId(), e.getCandidate().getId());
        }
    }

    /**
     * A serializer for XML files
     */
    public static class XMLSerializer implements afterwind.lab1.entity.XMLSerializer<Option> {

        private CandidateService candidateService;
        private SectionService sectionService;

        public XMLSerializer(CandidateService candidateService, SectionService sectionService) {
            this.candidateService = candidateService;
            this.sectionService = sectionService;
        }

        @Override
        public Node serialize(Document doc, Option c) {
            Element element = doc.createElement("option");
            element.setAttribute("id", c.getId().toString());
            element.setAttribute("candidateID", c.getCandidate().getId() + "");
            element.setAttribute("sectionID", c.getSection().getId() + "");
            return element;
        }

        @Override
        public Option deserialize(Document doc, Node node) {
            try {
                Element element = (Element) node;
                int id = Integer.parseInt(element.getAttribute("id"));
                Candidate c = candidateService.get(Integer.parseInt(element.getAttribute("candidateID")));
                Section s = sectionService.get(Integer.parseInt(element.getAttribute("sectionID")));
                return new Option(id, s, c);
            } catch (Exception ex) {
                return null;
            }
        }
    }
}
