package afterwind.lab1;

import afterwind.lab1.entity.Candidate;
import afterwind.lab1.entity.Section;
import afterwind.lab1.exception.ValidationException;
import afterwind.lab1.ui.Console;

public class Main {

    /*
        Verificarea datelor de intrare.
        Specificatii / Documentatie functii.
            - ce face functia, parametrii, output
            - cu un !STANDARD!
        Teste.
        Problema 2 - Concurs Admitere

        Se citeste un sir de nr de la tastatura sa se afiseze min si max
     */
    public static void main(String[] args) {
        Console c = new Console();
        try {
            c.candidateController.add(new Candidate(0, "Sergiu", "000111", "Dunarii 12"));
            c.candidateController.add(new Candidate(1, "Catalin", "100", "Centru"));
            c.candidateController.add(new Candidate(2, "Andrei", "0259888888", "Napoca 100"));
            c.sectionController.add(new Section(0, "Info", 120));
            c.sectionController.add(new Section(1, "Mate", 60));
            c.sectionController.add(new Section(2, "Engleza", 100));
        } catch (ValidationException ex) {
            System.out.printf(ex.getMessage());
        }
        c.run();
    }
}
