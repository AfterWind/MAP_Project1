package afterwind.lab1;

import afterwind.lab1.controller.NewSectionController;
import afterwind.lab1.entity.Section;
import afterwind.lab1.repository.FileRepository;
import afterwind.lab1.service.SectionService;
import afterwind.lab1.validator.SectionValidator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;

public class SectionMain extends Application {

    private SectionService sectionService = new SectionService(new FileRepository<>(new SectionValidator(), new Section.Serializer(), "/home/afterwind/IdeaProjects/MAP_Lab1/res/sections.txt"));

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
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(new File("/home/afterwind/IdeaProjects/MAP_Lab1/src/afterwind/lab1/ui/SectionView.fxml").toURL());
        HBox root = loader.load();
        NewSectionController controller = loader.getController();
        controller.setService(sectionService);
        Scene scene = new Scene(root, 1000, 600, Color.ALICEBLUE);
        stage.setTitle("Sections Management");
        stage.setScene(scene);
        stage.requestFocus();
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        sectionService.getRepo().updateLinks();
        super.stop();
    }
}
