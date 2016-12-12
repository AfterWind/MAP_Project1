package afterwind.lab1.ui;

import afterwind.lab1.controller.NewCandidateController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.File;

public class CandidateView extends VBox {

    public final NewCandidateController controller;
    public CandidateView() {
        this.controller = new NewCandidateController();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(new File("/home/afterwind/IdeaProjects/MAP_Lab1/src/afterwind/lab1/ui/fxml/CandidateView.fxml").toURL());
            loader.setRoot(this);
            loader.setControllerFactory((param) -> controller);
            loader.load();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
