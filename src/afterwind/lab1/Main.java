package afterwind.lab1;

import afterwind.lab1.old_controller.CandidateController;
import afterwind.lab1.entity.Candidate;
import afterwind.lab1.repository.FileRepository;
import afterwind.lab1.service.CandidateService;
import afterwind.lab1.ui.CandidateView;
import afterwind.lab1.validator.CandidateValidator;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {

    private CandidateService service = new CandidateService(new FileRepository<>(new CandidateValidator(), new Candidate.Serializer(), "/home/afterwind/IdeaProjects/MAP_Lab1/res/candidates.txt"));

    /*
        Verificarea datelor de intrare.
        Specificatii / Documentatie functii.
            - ce face functia, parametrii, output
            - cu un !STANDARD!
        Teste.
        Problema 2 - Concurs Admitere

        Se citeste un sir de nr de la tastatura sa se afiseze min si max
     */
    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage stage) throws Exception {
        HBox root = new HBox();
        root.setAlignment(Pos.CENTER);
        Scene scene = new Scene(root, 1000, 600, Color.ALICEBLUE);
        root.getChildren().add(new CandidateView(service));
        stage.setTitle("Candidate Management");
        stage.setScene(scene);
        stage.requestFocus();
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        service.getRepo().updateLinks();
        super.stop();
    }
}
