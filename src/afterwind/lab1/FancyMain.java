package afterwind.lab1;

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

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(new File("/home/afterwind/IdeaProjects/MAP_Lab1/src/afterwind/lab1/ui/fxml/FancyView.fxml").toURL());
        Scene scene = new Scene(loader.load(), 1000, 800, Color.ALICEBLUE);
        stage.setTitle("Fancy Management");
        stage.setScene(scene);
        stage.requestFocus();
        stage.show();
    }
}
