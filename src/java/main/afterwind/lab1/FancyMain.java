package afterwind.lab1;

import afterwind.lab1.permission.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;

public class FancyMain extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public static Scene scene;
    public static Stage stage;
    public static User user;

    @Override
    public void start(Stage stage) throws Exception {
        FancyMain.stage = stage;
        FXMLLoader loader = new FXMLLoader();
//        loader.setLocation(new File("src/java/main/afterwind/lab1/ui/fxml/FancyView.fxml").toURL());
        loader.setLocation(new File("src/java/main/afterwind/lab1/ui/fxml/LoginView.fxml").toURL());
        scene = new Scene(loader.load(), 400, 200, Color.DARKBLUE);
        stage.setTitle("Management");
        stage.setScene(scene);
        stage.requestFocus();
        stage.show();
    }
}