package afterwind.lab1.ui;

import afterwind.lab1.entity.Candidate;
import afterwind.lab1.entity.Option;
import afterwind.lab1.entity.Section;
import afterwind.lab1.exception.ValidationException;
import afterwind.lab1.repository.FileRepository;
import afterwind.lab1.repository.FileRepositoryNumeroDos;
import afterwind.lab1.repository.IRepository;
import afterwind.lab1.service.CandidateService;
import afterwind.lab1.service.OptionService;
import afterwind.lab1.service.SectionService;
import afterwind.lab1.validator.CandidateValidator;
import afterwind.lab1.validator.SectionValidator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

/**
 * Stratul de "UI"
 */
public class Console {
    public final CandidateService candidateService = new CandidateService(new FileRepository<>(new CandidateValidator(), new Candidate.Serializer(), "/home/afterwind/IdeaProjects/MAP_Lab1/res/candidates.txt", 13));
    public final SectionService sectionService = new SectionService(new FileRepositoryNumeroDos<>(new SectionValidator(), "/home/afterwind/IdeaProjects/MAP_Lab1/res/sections2.txt", 13));
    public final OptionService optionService = new OptionService();
    private Scanner scanner = new Scanner(System.in);

    /**
     * Scurtatura pentru afisarea unui String pe ecran
     * @param s String-ul afisat
     */
    private void print(String s) {
        System.out.println(s);
    }

    /**
     * Afiseaza toate optiunile pe ecran
     */
    private void showOptions() {
        print("1. Show all candidates");
        print("2. Show all sections");
        print("3. Show all options");

        print("4. Add candidate");
        print("5. Add section");
        print("6. Add option");

        print("7. Delete candidate");
        print("8. Delete section");
        print("9. Delete option");

        print("10. Update candidate");
        print("11. Update section");
        print("12. Update option");

        print("13. Filter candidates by name");
        print("14. Filter candidates by telephone");
        print("15. Filter candidates by address");

        print("16. Filter sections by name");
        print("17. Filter sections by nrLoc");

        print("18. Most occupied sections");
        print("19. Save");

        print("0. Exit");
        System.out.print("Choose an option: ");
    }

    /**
     * Afiseaza toti candidatii
     */
    public void uiShowCandidates() {
        print(candidateService.toString());
    }

    /**
     * Afiseaza toate sectiunile
     */
    public void uiShowSections() {
        print(sectionService.toString());
    }

    /**
     * Afiseaza toate optiunile
     */
    public void uiShowOptions() {
        print(optionService.toString());
    }

    /**
     * Afiseaza meniul ui pentru adaugarea unui candidat
     */
    public void uiAddCandidate() throws ValidationException {
        System.out.print("Dati numele candidatului: ");
        String name = scanner.nextLine();
        if (name.equals("")) {
            print("Nume invalid!");
            return;
        }
        System.out.print("Dati numarul de telefon al candidatului: ");
        String tel = scanner.nextLine();
        if (tel.equals("")) {
            print("Telefon invalid!");
            return;
        }
        System.out.print("Dati adresa candidatului: ");
        String address = scanner.nextLine();
        if (address.equals("")) {
            print("Adresa invalida!");
            return;
        }
        candidateService.add(new Candidate(candidateService.getNextId(), name, tel, address));
    }

    /**
     * Afiseaza meniul ui pentru adaugarea unei sectii
     */
    public void uiAddSection() throws ValidationException {
        System.out.print("Dati numele sectiei: ");
        String name = scanner.nextLine();
        if (name.equals("")) {
            print("Nume invalid!");
            return;
        }
        System.out.print("Dati numarul de locuri al sectiei: ");
        int nrLoc = scanner.nextInt(); scanner.nextLine();
        sectionService.add(new Section(sectionService.getNextId(), name, nrLoc));
    }

    /**
     * Meniul ui pentru adaugarea unei optiuni
     */
    public void uiAddOption() throws ValidationException {
        System.out.print("Dati id-ul candidatului: ");
        int candidateId = scanner.nextInt(); scanner.nextLine();
        Candidate candidate = candidateService.get(candidateId);
        if (candidate == null) {
            print("Candidatul cu id-ul dat nu exista!");
            return;
        }

        System.out.print("Dati id-ul sectiunii: ");
        int sectionId = scanner.nextInt(); scanner.nextLine();
        Section section = sectionService.get(sectionId);
        if (section == null) {
            print("Sectiunea cu id-ul dat nu exista!");
            return;
        }

        optionService.add(new Option(optionService.getNextId(), section, candidate));
    }

    /**
     * Afiseaza meniul ui pentru stergerea unui candidat
     */
    public void uiDeleteCandidate() {
        System.out.print("Dati id-ul candidatului: ");
        int id = scanner.nextInt(); scanner.nextLine();
        if (candidateService.get(id) == null) {
            print("Candidatul cu id-ul dat nu exista!");
            return;
        }
        candidateService.remove(id);
    }

    /**
     * Afiseaza meniul ui pentru stergerea unei sectii
     */
    public void uiDeleteSection() {
        System.out.print("Dati id-ul sectiei: ");
        int id = scanner.nextInt(); scanner.nextLine();
        if (sectionService.get(id) == null) {
            print("Sectiunea cu id-ul dat nu exista!");
            return;
        }
        sectionService.remove(id);
    }

    /**
     * Afiseaza meniul ui pentru stergerea unei optiuni
     */
    public void uiDeleteOption() {
        System.out.print("Dati id-ul optiunii: ");
        int id = scanner.nextInt(); scanner.nextLine();
        if (sectionService.contains(id)) {
            print("Optiunea cu id-ul dat nu exista!");
            return;
        }
        sectionService.remove(id);
    }

    /**
     * Afiseaza meniul ui pentru updatarea unui candidat
     */
    public void uiUpdateCandidate() {
        System.out.print("Dati id-ul candidatului: ");
        int id = scanner.nextInt(); scanner.nextLine();
        Candidate candidate = candidateService.get(id);
        if (candidate == null) {
            print("Candidatul cu id-ul dat nu exista!");
            return;
        }
        System.out.print("Dati numele candidatului: ");
        String name = scanner.nextLine();
        if (name.equals("")) {
            name = candidate.getName();
        }
        System.out.print("Dati numarul de telefon al candidatului: ");
        String tel = scanner.nextLine();
        if (tel.equals("")) {
            tel = candidate.getTelephone();
        }
        System.out.print("Dati adresa candidatului: ");
        String address = scanner.nextLine();
        if (address.equals("")) {
            address = candidate.getAddress();
        }

        if (!(address.equals(candidate.getAddress()) && tel.equals(candidate.getTelephone()) && name.equals(candidate.getName()))) {
            candidateService.update(candidate.getId(), new Candidate(-1, name, tel, address));
        }
    }

    /**
     * Afiseaza meniul ui pentru updatarea unei sectii
     */
    public void uiUpdateSection() {
        System.out.print("Dati id-ul sectiei: ");
        int id = scanner.nextInt(); scanner.nextLine();
        Section section = sectionService.get(id);
        if (section == null) {
            print("Sectiunea cu id-ul dat nu exista!");
            return;
        }
        System.out.print("Dati numele sectiei: ");
        String name = scanner.nextLine();
        if (name.equals("")) {
            name = section.getName();
        }
        System.out.print("Dati numarul de locuri al sectiei: ");
        String nrLocString = scanner.nextLine();
        int nrLoc;
        if (nrLocString.equals("")) {
            nrLoc = section.getNrLoc();
        } else {
            nrLoc = Integer.parseInt(nrLocString);
        }

        if (!(name.equals(section.getName()) && nrLoc == section.getNrLoc())) {
            sectionService.update(section.getId(), new Section(-1, name, nrLoc));
        }
    }

    /**
     * Afiseaza meniul ui pentru updatarea unei optiuni
     */
    public void uiUpdateOption() {
        System.out.println("Dati id-ul optiunii: ");
        int optionId = scanner.nextInt(); scanner.nextLine();
        Option option = optionService.get(optionId);
        if (option == null) {
            print("Nu exista optiunea cu acel ID");
            return;
        }

        System.out.print("Dati id-ul candidatului: ");
        String candidateIdString = scanner.nextLine();
        Candidate candidate;
        int candidateId;
        if (candidateIdString.equals("")) {
            candidateId = option.getCandidate().getId();
            candidate = option.getCandidate();
        } else {
            candidateId = Integer.parseInt(candidateIdString);
            candidate = candidateService.get(candidateId);
        }
        if (candidate == null) {
            print("Candidatul cu id-ul dat nu exista!");
            return;
        }

        System.out.println("Dati id-ul sectiunii: ");
        String sectionIdString = scanner.nextLine();
        Section section;
        int sectionId;
        if (sectionIdString.equals("")) {
            sectionId = option.getSection().getId();
            section = option.getSection();
        } else {
            sectionId = Integer.parseInt(sectionIdString);
            section = sectionService.get(sectionId);
        }
        if (section == null) {
            print("Sectiunea cu id-ul dat nu exista!");
            return;
        }

        if (!(candidate.equals(option.getCandidate()) && section.equals(option.getSection()))) {
            optionService.update(option.getId(), new Option(-1, section, candidate));
        }
    }

    /**
     * Meniul ui pentru filtrarea candidatilor dupa nume
     */
    public void uiFilterCandidatesByName() {
        System.out.print("Dati numele cautat: ");
        String name = scanner.nextLine();
        IRepository<Candidate, Integer> result = candidateService.filterByName(name);
        print("Rezultatul filtrarii:\n" + result);
    }

    /**
     * Meniul ui pentru filtrarea candidatilor dupa numarul de telefon
     */
    public void uiFilterCandidatesByTelephone() {
        System.out.print("Dati numarul de telefon cautat: ");
        String tel = scanner.nextLine();
        IRepository<Candidate, Integer> result = candidateService.filterByTelephone(tel);
        print("Rezultatul filtrarii:\n" + result);
    }

    /**
     * Meniul ui pentru filtrarea candidatilor dupa adresa
     */
    public void uiFilterCandidatesByAddress() {
        System.out.print("Dati adresa cautata: ");
        String address = scanner.nextLine();
        IRepository<Candidate, Integer> result = candidateService.filterByAddress(address);
        print("Rezultatul filtrarii:\n" + result);
    }

    /**
     * Meniul ui pentru filtrarea sectiilor dupa nume
     */
    public void uiFilterSectionsByName() {
        System.out.print("Dati numele cautat: ");
        String name = scanner.nextLine();
        IRepository<Section, Integer> result = sectionService.filterByName(name);
        print("Rezultatul filtrarii:\n" + result);
    }

    /**
     * Meniul ui pentru filtrarea sectiilor dupa numarul de locuri
     */
    public void uiFilterSectionsByNrLoc() {
        System.out.print("Dati transa de numar de locuri: ");
        int nrLoc = scanner.nextInt(); scanner.nextLine();
        System.out.print("Cautam sectii sub transa (true/false)? ");
        boolean lower = scanner.nextBoolean();
        IRepository<Section, Integer> result = sectionService.filterByNrLoc(nrLoc, lower);
        print("Rezultatul filtrarii:\n" + result);
    }

    /**
     * Meniul ui pentru afisarea celor mai solicitate sectii
     */
    public void uiMostOccupiedSections() {
        System.out.println("Dati cate sectii doriti in top: ");
        int amount = scanner.nextInt(); scanner.nextLine();
        System.out.println("Sectiile din top: ");
        IRepository<Section, Integer> result = sectionService.getMostOccupiedSections(optionService.getRepo(), amount);
        System.out.print(result.toString());
    }

    /**
     * Locul de pornirea a zonei de ui
     */
    public void run() {
        Map<Integer, Method> uis = new HashMap<>();
        int o;

        try {
            uis.put(1, this.getClass().getDeclaredMethod("uiShowCandidates"));
            uis.put(2, this.getClass().getDeclaredMethod("uiShowSections"));
            uis.put(3, this.getClass().getDeclaredMethod("uiShowOptions"));
            uis.put(4, this.getClass().getDeclaredMethod("uiAddCandidate"));
            uis.put(5, this.getClass().getDeclaredMethod("uiAddSection"));
            uis.put(6, this.getClass().getDeclaredMethod("uiAddOption"));
            uis.put(7, this.getClass().getDeclaredMethod("uiDeleteCandidate"));
            uis.put(8, this.getClass().getDeclaredMethod("uiDeleteSection"));
            uis.put(9, this.getClass().getDeclaredMethod("uiDeleteOption"));
            uis.put(10, this.getClass().getDeclaredMethod("uiUpdateCandidate"));
            uis.put(11, this.getClass().getDeclaredMethod("uiUpdateSection"));
            uis.put(12, this.getClass().getDeclaredMethod("uiUpdateOption"));
            uis.put(13, this.getClass().getDeclaredMethod("uiFilterCandidatesByName"));
            uis.put(14, this.getClass().getDeclaredMethod("uiFilterCandidatesByTelephone"));
            uis.put(15, this.getClass().getDeclaredMethod("uiFilterCandidatesByAddress"));
            uis.put(16, this.getClass().getDeclaredMethod("uiFilterSectionsByName"));
            uis.put(17, this.getClass().getDeclaredMethod("uiFilterSectionsByNrLoc"));
            uis.put(18, this.getClass().getDeclaredMethod("uiMostOccupiedSections"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        while(true) {
            try {
                    showOptions();
                    o = scanner.nextInt();
                    if (o == 0) {
                        break;
                    } else if (!uis.containsKey(o)) {
                        throw new IndexOutOfBoundsException();
                    }
                    scanner = new Scanner(System.in);
                    uis.get(o).invoke(this);
                    print(" ");
            } catch (InputMismatchException ex1) {
                print("Eroare: Tip de date invalid!");
                scanner.nextLine();
            } catch (IndexOutOfBoundsException ex2) {
                print("Eroare: Optiune invalida!");
            } catch (InvocationTargetException ex3) {
                System.out.print(ex3.getTargetException().getMessage());
            } catch (Exception ex5) {
                ex5.printStackTrace();
            }
        }
        print("Goodbye!");
    }
}
