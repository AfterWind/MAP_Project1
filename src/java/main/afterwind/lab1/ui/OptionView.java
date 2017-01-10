package afterwind.lab1.ui;

import afterwind.lab1.controller.NewOptionController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.File;

public class OptionView extends VBox {

    public final NewOptionController controller = new NewOptionController();
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
