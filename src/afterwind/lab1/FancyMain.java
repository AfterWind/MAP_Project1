package afterwind.lab1;

import afterwind.lab1.database.SQLiteDatabase;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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

    @Override
    public void start(Stage stage) throws Exception {
        FancyMain.stage = stage;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(new File("/home/afterwind/IdeaProjects/MAP_Lab1/src/afterwind/lab1/ui/fxml/FancyView.fxml").toURL());
        scene = new Scene(loader.load(), 800, 650, Color.DARKBLUE);
        stage.setTitle("Candidates Management");
        stage.setScene(scene);
        stage.requestFocus();
        stage.show();
    }
}
