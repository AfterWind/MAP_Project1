package afterwind.lab1;

import afterwind.lab1.permission.Permission;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.List;
import java.util.stream.Collectors;

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

    public static void setErrorBorder(TextField t) {
        t.borderProperty().set(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
    }

    public static void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Eroare");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void showInfoMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informatie");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static String toMD5(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.reset();
            md.update(text.getBytes());
            byte[] digest = md.digest();
            BigInteger digestBigInt = new BigInteger(1, digest);
            String hashText = digestBigInt.toString(16);
            while (hashText.length() < 32) {
                hashText = "0" + hashText;
            }
            return hashText;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void transition(Node from, Node to, Duration duration) {
        int offsetX = 1500;
        TranslateTransition first = new TranslateTransition(duration.divide(2), from);
        first.setCycleCount(1);
        first.setByX(offsetX);
        first.play();
        TranslateTransition second = new TranslateTransition(duration.divide(2), to);
        second.setCycleCount(1);
        second.setByX(-offsetX);
        second.play();
    }

    public static void  moveRight(Node node, Duration duration) {
        int offsetX = 1500;
        TranslateTransition second = new TranslateTransition(duration, node);
        second.setCycleCount(1);
        second.setByX(offsetX);
        second.play();
    }

    public class InputOutOfRangeException extends RuntimeException {
        public InputOutOfRangeException() { super(); }

    }
}
