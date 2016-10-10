package afterwind.lab1.ui;

import afterwind.lab1.Utils;
import afterwind.lab1.controller.CandidateController;
import afterwind.lab1.controller.OptionController;
import afterwind.lab1.controller.SectionController;
import afterwind.lab1.entity.Candidate;
import afterwind.lab1.entity.Option;
import afterwind.lab1.entity.Section;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

/**
 * Stratul de "UI"
 */
public class Console {
    private CandidateController candidateController = new CandidateController();
    private SectionController sectionController = new SectionController();
    private OptionController optionController = new OptionController();
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

        print("0. Exit");
        System.out.print("Choose an option: ");
    }

    /**
     * Afiseaza toti candidatii
     */
    public void uiShowCandidates() {
        print("The list of candidates: ");
        if (candidateController.getSize() == 0) {
            print("Nu exista candidati!");
        } else {
            print(String.format("%3s | %20s | %15s | %15s", "ID", "Nume", "Telefon", "Adresa"));
            for (Candidate c : candidateController.getData()) {
                if (c != null) {
                    print(c.toString());
                }
            }
        }
    }

    /**
     * Afiseaza toate sectiunile
     */
    public void uiShowSections() {
        print("The list of sections: ");
        if (sectionController.getSize() == 0) {
            print("Nu exista sectii!");
        } else {
            print(String.format("%3s | %20s | %5s", "ID", "Nume", "Numar locuri"));
            for (Section s : sectionController.getData()) {
                if (s != null) {
                    print(s.toString());
                }
            }
        }
    }

    /**
     * Afiseaza toate optiunile
     */
    public void uiShowOptions() {
        print("The list of options: ");
        if (optionController.getSize() == 0) {
            print("Nu exista optiuni!");
        } else {
            print(String.format("%3s | %20s | %20s", "ID", "Candidat", "Sectiune"));
            for (Option o : optionController.getData()) {
                if (o != null) {
                    print(o.toString());
                }
            }
        }
    }

    /**
     * Afiseaza meniul ui pentru adaugarea unui candidat
     */
    public void uiAddCandidate() {
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
        candidateController.add(new Candidate(candidateController.getNextId(), name, tel, address));
    }

    /**
     * Afiseaza meniul ui pentru adaugarea unei sectii
     */
    public void uiAddSection() {
        System.out.print("Dati numele sectiei: ");
        String name = scanner.nextLine();
        if (name.equals("")) {
            print("Nume invalid!");
            return;
        }
        System.out.print("Dati numarul de locuri al sectiei: ");
        int nrLoc = scanner.nextInt();
        sectionController.add(new Section(sectionController.getNextId(), name, nrLoc));
    }

    public void uiAddOption() {
        System.out.print("Dati id-ul candidatului: ");
        int candidateId = scanner.nextInt();
        Candidate candidate = candidateController.get(candidateId);
        if (candidate == null) {
            print("Candidatul cu id-ul dat nu exista!");
            return;
        }

        System.out.println("Dati id-ul sectiunii: ");
        int sectionId = scanner.nextInt();
        Section section = sectionController.get(sectionId);
        if (section == null) {
            print("Sectiunea cu id-ul dat nu exista!");
            return;
        }

        optionController.add(new Option(optionController.getNextId(), section, candidate));
    }

    /**
     * Afiseaza meniul ui pentru stergerea unui candidat
     */
    public void uiDeleteCandidate() {
        System.out.print("Dati id-ul candidatului: ");
        int id = scanner.nextInt();
        if (candidateController.get(id) == null) {
            print("Candidatul cu id-ul dat nu exista!");
            return;
        }
        candidateController.remove(id);
    }

    /**
     * Afiseaza meniul ui pentru stergerea unei sectii
     */
    public void uiDeleteSection() {
        System.out.print("Dati id-ul sectiei: ");
        int id = scanner.nextInt();
        if (sectionController.get(id) == null) {
            print("Sectiunea cu id-ul dat nu exista!");
            return;
        }
        sectionController.remove(id);
    }

    /**
     * Afiseaza meniul ui pentru stergerea unei optiuni
     */
    public void uiDeleteOption() {
        System.out.print("Dati id-ul optiunii: ");
        int id = scanner.nextInt();
        if (sectionController.contains(id)) {
            print("Optiunea cu id-ul dat nu exista!");
            return;
        }
        sectionController.remove(id);
    }

    /**
     * Afiseaza meniul ui pentru updatarea unui candidat
     */
    public void uiUpdateCandidate() {
        System.out.print("Dati id-ul candidatului: ");
        int id = scanner.nextInt();
        Candidate candidate = candidateController.get(id);
        if (candidate == null) {
            print("Candidatul cu id-ul dat nu exista!");
            return;
        }

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
        candidateController.updateCandidate(candidate, name, tel, address);
    }

    /**
     * Afiseaza meniul ui pentru updatarea unei sectii
     */
    public void uiUpdateSection() {
        System.out.print("Dati id-ul sectiei: ");
        int id = scanner.nextInt();
        Section section = sectionController.get(id);
        if (section == null) {
            print("Sectiunea cu id-ul dat nu exista!");
            return;
        }
        System.out.print("Dati numele sectiei: ");
        String name = scanner.nextLine();
        if (name.equals("")) {
            print("Nume invalid!");
            return;
        }
        System.out.print("Dati numarul de locuri al sectiei: ");
        int nrLoc = scanner.nextInt();
        sectionController.updateSection(section, name, nrLoc);
    }

    /**
     * Afiseaza meniul ui pentru updatarea unei optiuni
     */
    public void uiUpdateOption() {
        System.out.println("Dati id-ul optiunii: ");
        int optionId = scanner.nextInt();
        Option option = optionController.get(optionId);
        if (option == null) {
            print("Nu exista optiunea cu acel ID");
            return;
        }
        System.out.print("Dati id-ul candidatului: ");
        int candidateId = scanner.nextInt();
        Candidate candidate = candidateController.get(candidateId);
        if (candidate == null) {
            print("Candidatul cu id-ul dat nu exista!");
            return;
        }

        System.out.println("Dati id-ul sectiunii: ");
        int sectionId = scanner.nextInt();
        Section section = sectionController.get(sectionId);
        if (section == null) {
            print("Sectiunea cu id-ul dat nu exista!");
            return;
        }

        optionController.updateOption(option, candidate, section);
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
            } catch (IndexOutOfBoundsException ex2) {
                print("Eroare: Optiune invalida!");
            } catch (Exception ex3) {
                ex3.printStackTrace();
                print("eroare");
            }
        }
        print("Goodbye!");
    }
}
