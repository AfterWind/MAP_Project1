package afterwind.lab1.ui;

import afterwind.lab1.controller.OptionController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.File;

public class OptionView extends VBox {

    public final OptionController controller = new OptionController();
    public OptionView() {
        FXMLLoader loader = new FXMLLoader();
        try {
            loader.setLocation(new File("/home/afterwind/IdeaProjects/MAP_Lab1/src/afterwind/lab1/ui/fxml/OptionView.fxml").toURL());
            loader.setControllerFactory((param) -> controller);
            loader.setRoot(this);
            loader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
