package afterwind.lab1;

import afterwind.lab1.controller.OptionController;
import afterwind.lab1.entity.Candidate;
import afterwind.lab1.entity.Option;
import afterwind.lab1.entity.Section;
import afterwind.lab1.repository.FileRepository;
import afterwind.lab1.repository.FileRepositoryNumeroDos;
import afterwind.lab1.service.CandidateService;
import afterwind.lab1.service.OptionService;
import afterwind.lab1.service.SectionService;
import afterwind.lab1.validator.CandidateValidator;
import afterwind.lab1.validator.OptionValidator;
import afterwind.lab1.validator.SectionValidator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;

public class OptionMain extends Application {

    private CandidateService candidateService = new CandidateService(new FileRepository<>(new CandidateValidator(), new Candidate.Serializer(), "/home/afterwind/IdeaProjects/MAP_Lab1/res/candidates.txt"));
    private SectionService sectionService = new SectionService(new FileRepository<>(new SectionValidator(), new Section.Serializer(), "/home/afterwind/IdeaProjects/MAP_Lab1/res/sections.txt"));
    private OptionService optionService = new OptionService(new FileRepository<>(new OptionValidator(), new Option.Serializer(candidateService, sectionService), "/home/afterwind/IdeaProjects/MAP_Lab1/res/options.txt"));

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
//        optionService.add(new Option(0, sectionService.get(0), candidateService.get(0)));
//        optionService.add(new Option(1, sectionService.get(0), candidateService.get(1)));
//        optionService.add(new Option(2, sectionService.get(1), candidateService.get(1)));
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(new File("/home/afterwind/IdeaProjects/MAP_Lab1/src/afterwind/lab1/ui/OptionView.fxml").toURL());
        HBox root = loader.load();
        OptionController controller = loader.getController();
        controller.setServices(optionService, candidateService, sectionService);
        Scene scene = new Scene(root, 1000, 600, Color.ALICEBLUE);
        stage.setTitle("Options Management");
        stage.setScene(scene);
        stage.requestFocus();
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        optionService.getRepo().updateLinks();
        super.stop();
    }
}
