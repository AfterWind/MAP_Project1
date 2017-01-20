package afterwind.lab1.controller;

import afterwind.lab1.FancyMain;
import afterwind.lab1.Utils;
import afterwind.lab1.config.Config;
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
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;

public class LoginController {

    private Node current;
    private SQLiteUserRepository repo;

    @FXML
    public Button buttonLogin;
    @FXML
    public Button buttonRegister;
    @FXML
    public Text statusBar;
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
        this.repo = new SQLiteUserRepository(new SQLiteDatabase(Config.datasourcePath + "users.db"), new UserValidator(), 100000);
    }

    @FXML
    public void initialize() {
        this.current = loginPanel;
        fieldPassword.setOnKeyPressed(this::handleEnter);
        Utils.moveRight(registerPanel, Duration.millis(1000));
    }

    public void handleEnter(KeyEvent ev) {
        if (ev.getCode() == KeyCode.ENTER) {
            handleLogIn(null);
        }
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

        fieldPassword.setText("");
        fieldUsername.setText("");

        fieldPassword.setDisable(true);
        fieldUsername.setDisable(true);
        buttonLogin.setDisable(true);
        buttonRegister.setDisable(true);

        statusBar.setFill(Color.DARKBLUE);
        statusBar.setFont(new Font("Arial", 24));
        statusBar.setText("You are now logged in!");

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
            if (user.getUsername().equals("AfterWind")) {
                repo.addPermission(user, Permission.MANAGE);
                repo.addPermission(user, Permission.MODIFY);
            }
        } catch (ValidationException e) {
            Utils.showErrorMessage(e.getMessage());
            return;
        }
        handleViewSwitch(null);
    }

    /**
     * Switches between the "Login" panel and the "Register" panel
     * @param ev can be null
     */
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
//            FancyMain.stage.close();
            FancyMain.user = user;
            FancyMain.stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(new File("src/java/main/afterwind/lab1/ui/fxml/FancyView.fxml").toURL());
//            loader.setLocation(new File("src/java/main/afterwind/lab1/ui/fxml/LoginView.fxml").toURL());
            Scene scene = new Scene(loader.load(), 800, 650 , Color.DARKBLUE);
            FancyMain.scene = scene;
            FancyMain.stage.setTitle("Candidates Management");
            FancyMain.stage.setScene(scene);
            FancyMain.stage.setResizable(false);
            FancyMain.stage.requestFocus();
            FancyMain.stage.show();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
