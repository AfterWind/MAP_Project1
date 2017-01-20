package afterwind.lab1.ui;

import afterwind.lab1.controller.OptionController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.File;

/**
 * Simple View for encapsulating the Options
 */
public class OptionView extends VBox {

    public final OptionController controller = new OptionController();
    public OptionView() {
        FXMLLoader loader = new FXMLLoader();
        try {
            loader.setLocation(new File("src/java/main/afterwind/lab1/ui/fxml/OptionView.fxml").toURL());
            loader.setControllerFactory((param) -> controller);
            loader.setRoot(this);
            loader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
