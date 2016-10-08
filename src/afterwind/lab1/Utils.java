package afterwind.lab1;

import java.util.InputMismatchException;

/**
 * Diverse utilitati
 */
public class Utils {
    /**
     * Verifica daca string-ul dat poate fi convertit intr-un int
     * @param s string-ul verificat
     * @return daca string-ul dat poate fi convertit intr-un int
     */
    public static boolean tryParseInt(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public class InputOutOfRangeException extends RuntimeException {
        public InputOutOfRangeException() { super(); }
    }
}
