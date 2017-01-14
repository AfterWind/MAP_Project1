package afterwind.lab1;

import afterwind.lab1.config.Config;
import afterwind.lab1.permission.Permission;
import afterwind.lab1.permission.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
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
        Config.init();

        FancyMain.stage = stage;
        FXMLLoader loader = new FXMLLoader();
        user = new User(5, "Blah", "asdf");
        user.permissions.add(Permission.QUERY);
        user.permissions.add(Permission.MANAGE);
        user.permissions.add(Permission.MODIFY);

        loader.setLocation(new File("src/java/main/afterwind/lab1/ui/fxml/LoginView.fxml").toURL());
//        loader.setLocation(new File("src/java/main/afterwind/lab1/ui/fxml/FancyView.fxml").toURL());
        scene = new Scene(loader.load(), 400.0D, 200.0D, Color.DARKBLUE);
//        scene = new Scene(loader.load(), 1000, 600, Color.DARKBLUE);

        stage.setResizable(false);
//        stage.getIcons().add(new Image(String.valueOf(new File("res/images.jpeg").toURL())));
        stage.setTitle("Management");
        stage.setScene(scene);
        stage.requestFocus();
        stage.show();
    }
}
