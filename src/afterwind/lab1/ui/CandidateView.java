package afterwind.lab1.ui;

import afterwind.lab1.controller.CandidateController;
import afterwind.lab1.entity.Candidate;
import afterwind.lab1.service.CandidateService;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.File;

public class CandidateView extends VBox {

    public final CandidateController controller;
    public CandidateView() {
        this.controller = new CandidateController();
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
