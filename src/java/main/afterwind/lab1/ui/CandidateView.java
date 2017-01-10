package afterwind.lab1.ui;

import afterwind.lab1.controller.CandidateController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.File;

    public class CandidateView extends VBox {

    public final CandidateController controller;
    public CandidateView() {
        this.controller = new CandidateController();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(new File("src/java/main/afterwind/lab1/ui/fxml/CandidateView.fxml").toURL());
            loader.setRoot(this);
            loader.setControllerFactory((param) -> controller);
            loader.load();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
