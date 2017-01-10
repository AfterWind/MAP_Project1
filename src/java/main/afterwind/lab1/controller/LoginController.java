package afterwind.lab1.controller;

import afterwind.lab1.FancyMain;
import afterwind.lab1.Utils;
import afterwind.lab1.database.SQLiteDatabase;
import afterwind.lab1.exception.ValidationException;
import afterwind.lab1.permission.Permission;
import afterwind.lab1.permission.User;
import afterwind.lab1.repository.sql.SQLiteUserRepository;
import afterwind.lab1.validator.UserValidator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;

public class LoginController {

    private SQLiteUserRepository repo;
    private Node current;
    @FXML
    public HBox loginPanel;
    @FXML
    public HBox registerPanel;
    @FXML
    public PasswordField fieldRegisterPassword1;
    @FXML
    public PasswordField fieldRegisterPassword2;
    @FXML
    public PasswordField fieldPassword;
    @FXML
    public TextField fieldUsername;
    @FXML
    public TextField fieldRegisterUsername;

    public LoginController() {
        this.repo = new SQLiteUserRepository(new SQLiteDatabase("res/users.db"), new UserValidator());
    }

    @FXML
    public void initialize() {
        this.current = loginPanel;
        Utils.moveRight(registerPanel, Duration.millis(1000));
    }

    public void handleLogIn(ActionEvent ev) {
        User user = repo.get(fieldUsername.getText());
        if (user == null) {
            Utils.showErrorMessage("Username-ul " + fieldUsername.getText() + " nu exista! Va rog inregistrati-va!");
            return;
        }
        String password = Utils.toMD5(fieldPassword.getText());
        System.out.println(password);
        if (!user.getPassword().equals(password)) {
            Utils.showErrorMessage("Parola incorecta!");
            return;
        }
        launchApp(user);
    }

    public void handleRegister(ActionEvent ev) {
        if (fieldRegisterPassword1.getText().length() < 10) {
            Utils.showErrorMessage("Parola prea scurta, macar 10 caractere!");
            return;
        }
        if (!fieldRegisterPassword1.getText().equals(fieldRegisterPassword2.getText())) {
            Utils.showErrorMessage("Parolele nu sunt la fel!");
            return;
        }
        User user = repo.get(fieldRegisterUsername.getText());
        if (user != null) {
            Utils.showErrorMessage("Username-ul " + user.getUsername() + " deja exista!");
            return;
        }
        try {
            user = new User(-1, fieldRegisterUsername.getText(), Utils.toMD5(fieldRegisterPassword1.getText()));
            repo.add(user);
            repo.addPermission(user, Permission.QUERY);
            if (user.getUsername().equals("Admin")) {
                repo.addPermission(user, Permission.MANAGE);
                repo.addPermission(user, Permission.MODIFY);
            }
        } catch (ValidationException e) {
            Utils.showErrorMessage(e.getMessage());
            return;
        }
        handleViewSwitch(null);
    }

    public void handleViewSwitch(ActionEvent ev) {
        if (current == loginPanel) {
            Utils.transition(current, registerPanel, Duration.millis(2000));
            current = registerPanel;
        } else {
            Utils.transition(current, loginPanel, Duration.millis(2000));
            current = loginPanel;
        }
    }

    private void launchApp(User user) {
        try {
            FancyMain.stage.close();
            FancyMain.user = user;
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(new File("src/java/main/afterwind/lab1/ui/fxml/FancyView.fxml").toURL());
//            loader.setLocation(new File("src/java/main/afterwind/lab1/ui/fxml/LoginView.fxml").toURL());
            Scene scene = new Scene(loader.load(), 1000, 600, Color.DARKBLUE);
            FancyMain.scene = scene;
            stage.setTitle("Candidates Management");
            stage.setScene(scene);
            stage.requestFocus();
            stage.show();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
