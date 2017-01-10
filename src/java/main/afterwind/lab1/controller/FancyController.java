package afterwind.lab1.controller;

import afterwind.lab1.FancyMain;
import afterwind.lab1.Utils;
import afterwind.lab1.database.SQLiteDatabase;
import afterwind.lab1.repository.FileRepository;
import afterwind.lab1.repository.sql.SQLiteCandidateRepository;
import afterwind.lab1.repository.sql.SQLiteOptionRepository;
import afterwind.lab1.repository.sql.SQLiteSectionRepository;
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
import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.stage.Stage;
import javafx.util.Duration;

public class FancyController {

    private SQLiteDatabase database = new SQLiteDatabase("res/data.db");
    private CandidateService candidateService = new CandidateService(new SQLiteCandidateRepository(database, new CandidateValidator()));//new XMLRepository<>(new CandidateValidator(), new Candidate.XMLSerializer(), "res/candidates.xml"));

    private SectionService sectionService = new SectionService(new SQLiteSectionRepository(database, new SectionValidator()));//new FileRepository<>(new SectionValidator(), new Section.Serializer(), "res/sections.txt"));
    private OptionService optionService = new OptionService(new SQLiteOptionRepository(database, new OptionValidator(), candidateService.getRepo(), sectionService.getRepo()));//new FileRepository<>(new OptionValidator(), new Option.Serializer(candidateService, sectionService), "res/options.txt"));

    @FXML
    private MenuItem menuFileSaveAll;
    @FXML
    private CandidateView candidatesView;
    @FXML
    private SectionView sectionsView;
    @FXML
    private OptionView optionsView;
    @FXML
    private ReportsView reportsView;

    private Node current;
    private Stage reportsWindow;

    public FancyController() { }

    @FXML
    public void initialize() {
        sectionsView.controller.setService(sectionService);
        optionsView.controller.setServices(optionService, candidateService, sectionService);
        candidatesView.controller.setService(candidateService);
//        reportsWindow = new Stage();
//        reportsWindow.setTitle("Reports");
//        ReportsView reportsView = new ReportsView();
        reportsView.controller.setServices(optionService, candidateService, sectionService);
//        reportsWindow.setScene(new Scene(reportsView, 800, 400));

        if (!(candidateService.getRepo() instanceof FileRepository || sectionService.getRepo() instanceof FileRepository || optionService.getRepo() instanceof FileRepository)) {
            menuFileSaveAll.setDisable(true);
        }

        Utils.moveRight(optionsView, Duration.millis(1000));
        Utils.moveRight(sectionsView, Duration.millis(1000));
        Utils.moveRight(reportsView, Duration.millis(1000));
        current = candidatesView;
    }

    @FXML
    public void handleViewChange(ActionEvent ev) {
//        resetViews();
        if (ev.getSource() instanceof RadioMenuItem) {
            FancyMain.stage.setTitle(((RadioMenuItem) ev.getSource()).getText() + " Management");
            if (((RadioMenuItem) ev.getSource()).getText().equals("Candidates")) {
                Utils.transition(current, candidatesView, Duration.millis(2000));
                current = candidatesView;
            } else if (((RadioMenuItem) ev.getSource()).getText().equals("Sections")) {
                Utils.transition(current, sectionsView, Duration.millis(2000));
                current = sectionsView;
            } else if (((RadioMenuItem) ev.getSource()).getText().equals("Options")) {
                Utils.transition(current, optionsView, Duration.millis(2000));
                current = optionsView;
            } else if (((RadioMenuItem) ev.getSource()).getText().equals("Reports")) {
                Utils.transition(current, reportsView, Duration.millis(2000));
                current = reportsView;
            }
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

    public void handleAnimationMoveAbout(ActionEvent ev) {
        Path p = new Path();
        p.getElements().add(new MoveTo(20, 20));
        p.getElements().add(new CubicCurveTo(380, 0, 380, 120, 300, 120));
        p.getElements().add(new CubicCurveTo(0, 120, 0, 240, 380, 240));

        PathTransition pt = new PathTransition(Duration.millis(2000), p, FancyMain.scene.getRoot());
        pt.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pt.setCycleCount(Timeline.INDEFINITE);
        pt.setAutoReverse(true);
        pt.play();
    }

    public void handleAnimationFade(ActionEvent ev) {
        FadeTransition ft = new FadeTransition(Duration.millis(3000), FancyMain.scene.getRoot());
        ft.setFromValue(1.0);
        ft.setToValue(0.1);
        ft.setCycleCount(Timeline.INDEFINITE);
        ft.setAutoReverse(true);
        ft.play();
    }

    public void handleAnimationPopOut(ActionEvent ev) {
        Timeline t = new Timeline();
        t.setCycleCount(Timeline.INDEFINITE);
//        t.setAutoReverse(true);
        KeyValue kv1 = new KeyValue(FancyMain.stage.minHeightProperty(), 1080);
        KeyFrame kf1 = new KeyFrame(Duration.millis(1000), kv1);
        KeyValue kv2 = new KeyValue(FancyMain.stage.minHeightProperty(), 400);
        KeyFrame kf2 = new KeyFrame(Duration.ZERO, kv2);
        KeyValue kv3 = new KeyValue(FancyMain.stage.maxHeightProperty(), 400);
        KeyFrame kf3 = new KeyFrame(Duration.millis(1000), kv3);
        KeyValue kv4 = new KeyValue(FancyMain.stage.maxHeightProperty(), 1080);
        KeyFrame kf4 = new KeyFrame(Duration.ZERO, kv4);
        t.getKeyFrames().add(kf1);
        t.getKeyFrames().add(kf2);
        t.getKeyFrames().add(kf3);
        t.getKeyFrames().add(kf4);
        t.play();

//        ScaleTransition st = new ScaleTransition(Duration.millis(2000), FancyMain.stage.hei);
//        st.setAutoReverse(true);
//        st.setCycleCount(Timeline.INDEFINITE);
//        st.setFromY(1);
//        st.setByY(3);
//        st.play();

    }
}
