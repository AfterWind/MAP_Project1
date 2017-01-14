package afterwind.lab1.ui;

import afterwind.lab1.controller.ReportsController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.File;

public class ReportsView extends VBox {

    public final ReportsController controller;
    public ReportsView() {
        controller = new ReportsController();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(new File("src/java/main/afterwind/lab1/ui/fxml/ReportsView.fxml").toURL());
            loader.setRoot(this);
            loader.setControllerFactory((param) -> controller);
            loader.load();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
