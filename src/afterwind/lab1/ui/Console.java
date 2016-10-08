package afterwind.lab1.ui;

import afterwind.lab1.Utils;
import afterwind.lab1.controller.CandidateController;
import afterwind.lab1.controller.SectionController;
import afterwind.lab1.entity.Candidate;
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
        print("3. Add candidate");
        print("4. Add section");
        print("5. Delete candidate");
        print("6. Delete section");
        print("7. Update candidate");
        print("8. Update section");
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
        String nrLoc_string = scanner.nextLine();
        int nrLoc;
        if (!Utils.tryParseInt(nrLoc_string)) {
            print("Numar de locuri invalid!");
            return;
        }
        nrLoc = Integer.parseInt(nrLoc_string);
        sectionController.add(new Section(sectionController.getNextId(), name, nrLoc));
    }

    /**
     * Afiseaza meniul ui pentru stergerea unui candidat
     */
    public void uiDeleteCandidate() {
        System.out.print("Dati id-ul candidatului: ");
        String id_string = scanner.nextLine();
        if (!Utils.tryParseInt(id_string)) {
            print("ID invalid!");
            return;
        }
        int id = Integer.parseInt(id_string);
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
        String id_string = scanner.nextLine();
        if (!Utils.tryParseInt(id_string)) {
            print("ID invalid!");
            return;
        }
        int id = Integer.parseInt(id_string);
        if (sectionController.get(id) == null) {
            print("Sectiunea cu id-ul dat nu exista!");
            return;
        }
        sectionController.remove(id);
    }

    /**
     * Afiseaza meniul ui pentru updatarea unui candidat
     */
    public void uiUpdateCandidate() {
        System.out.print("Dati id-ul candidatului: ");
        String id_string = scanner.nextLine();
        if (!Utils.tryParseInt(id_string)) {
            print("ID invalid!");
            return;
        }
        int id = Integer.parseInt(id_string);
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
        String id_string = scanner.nextLine();
        if (!Utils.tryParseInt(id_string)) {
            print("ID invalid!");
            return;
        }
        int id = Integer.parseInt(id_string);
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
        String nrLoc_string = scanner.nextLine();
        int nrLoc;
        if (!Utils.tryParseInt(nrLoc_string)) {
            print("Numar de locuri invalid!");
            return;
        }
        nrLoc = Integer.parseInt(nrLoc_string);

        sectionController.updateSection(section, name, nrLoc);
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
            uis.put(3, this.getClass().getDeclaredMethod("uiAddCandidate"));
            uis.put(4, this.getClass().getDeclaredMethod("uiAddSection"));
            uis.put(5, this.getClass().getDeclaredMethod("uiDeleteCandidate"));
            uis.put(6, this.getClass().getDeclaredMethod("uiDeleteSection"));
            uis.put(7, this.getClass().getDeclaredMethod("uiUpdateCandidate"));
            uis.put(8, this.getClass().getDeclaredMethod("uiUpdateSection"));
        } catch (Exception e) {
            print(e.getMessage());
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

                print(" ");
            } catch (InputMismatchException ex1) {
                print("Eroare: Tip de date invalid!");
            } catch (IndexOutOfBoundsException ex2) {
                print("Eroare: Optiune invalida!");
            } catch (Exception ex3) {
                ex3.printStackTrace();
            }
        }
        print("Goodbye!");
    }
}
