package afterwind.lab1.ui;

import afterwind.lab1.controller.SectionController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.File;

public class SectionView extends VBox {

    public final SectionController controller = new SectionController();
    public SectionView() {
        FXMLLoader loader = new FXMLLoader();
        try {
            loader.setLocation(new File("src/java/main/afterwind/lab1/ui/fxml/SectionView.fxml").toURL());
            loader.setControllerFactory((param) -> controller);
            loader.setRoot(this);
            loader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
