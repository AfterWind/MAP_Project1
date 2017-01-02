package afterwind.lab1.controller;

import afterwind.lab1.Utils;
import afterwind.lab1.database.SQLiteDatabase;
import afterwind.lab1.entity.Candidate;
import afterwind.lab1.entity.Option;
import afterwind.lab1.entity.Section;
import afterwind.lab1.repository.*;
import afterwind.lab1.service.CandidateService;
import afterwind.lab1.service.OptionService;
import afterwind.lab1.service.SectionService;
import afterwind.lab1.ui.CandidateView;
import afterwind.lab1.ui.OptionView;
import afterwind.lab1.ui.ReportsView;
import afterwind.lab1.ui.SectionView;
import afterwind.lab1.validator.CandidateValidator;
import afterwind.lab1.validator.OptionValidator;
import afterwind.lab1.validator.SectionValidator;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;

public class FancyController {

    private SQLiteDatabase database = new SQLiteDatabase("res/data.db");

    private CandidateService candidateService = new CandidateService(new SQLiteCandidateRepository(database, new CandidateValidator()));//new XMLRepository<>(new CandidateValidator(), new Candidate.XMLSerializer(), "/home/afterwind/IdeaProjects/MAP_Lab1/res/candidates.xml"));
    private SectionService sectionService = new SectionService(new SQLiteSectionRepository(database, new SectionValidator()));//new FileRepository<>(new SectionValidator(), new Section.Serializer(), "/home/afterwind/IdeaProjects/MAP_Lab1/res/sections.txt"));
    private OptionService optionService = new OptionService(new SQLiteOptionRepository(database, new OptionValidator(), candidateService.getRepo(), sectionService.getRepo()));//new FileRepository<>(new OptionValidator(), new Option.Serializer(candidateService, sectionService), "/home/afterwind/IdeaProjects/MAP_Lab1/res/options.txt"));

    @FXML
    private CandidateView candidatesView;
    @FXML
    private SectionView sectionsView;
    @FXML
    private OptionView optionsView;

    private Stage reportsWindow;

    public FancyController() { }

    @FXML
    public void initialize() {
        sectionsView.controller.setService(sectionService);
        optionsView.controller.setServices(optionService, candidateService, sectionService);
        candidatesView.controller.setService(candidateService);
        reportsWindow = new Stage();
        reportsWindow.setTitle("Reports");
        ReportsView reportsView = new ReportsView();
        reportsView.controller.setServices(optionService, candidateService, sectionService);
        reportsWindow.setScene(new Scene(reportsView, 800, 400));
    }

    @FXML
    public ToggleButton candidateButton, sectionsButton, optionsButton;

    public void resetViews() {
        sectionsView.setVisible(false);
        candidatesView.setVisible(false);
        optionsView.setVisible(false);
    }

    @FXML
    public void handleViewChange(ActionEvent ev) {
        resetViews();
        if (ev.getSource() == candidateButton) {
            candidatesView.setVisible(true);
        } else if(ev.getSource() == sectionsButton) {
            sectionsView.setVisible(true);
        } else if(ev.getSource() == optionsButton) {
            optionsView.setVisible(true);
        }
    }

    public void handleMenuSaveAll(ActionEvent ev) {
        candidateService.getRepo().updateLinks();
        sectionService.getRepo().updateLinks();
        optionService.getRepo().updateLinks();
        Utils.showInfoMessage("Totul s-a salvat in fisier!");
    }

    public void handleMenuExit(ActionEvent ev) {
//        ((Stage) candidateButton.getScene().getWindow()).close();
        Platform.exit();
    }

    public void handleMenuReports(ActionEvent ev) {
        if (!reportsWindow.isShowing()) {
            reportsWindow.show();
        }
    }
}
