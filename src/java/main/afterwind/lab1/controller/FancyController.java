package afterwind.lab1.controller;

import afterwind.lab1.FancyMain;
import afterwind.lab1.Utils;
import afterwind.lab1.config.Config;
import afterwind.lab1.database.SQLiteDatabase;
import afterwind.lab1.entity.Candidate;
import afterwind.lab1.entity.Option;
import afterwind.lab1.entity.Section;
import afterwind.lab1.repository.FileRepository;
import afterwind.lab1.repository.FileRepositoryNumeroDos;
import afterwind.lab1.repository.XMLRepository;
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
import afterwind.lab1.ui.control.StatusBar;
import afterwind.lab1.validator.CandidateValidator;
import afterwind.lab1.validator.OptionValidator;
import afterwind.lab1.validator.SectionValidator;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

public class FancyController {

    public static final int candidatesPerPage = 13;
    public static final int sectionsPerPage = 14;
    public static final int optionsPerPage = 9;

    private CandidateService serviceCandidate;
            //= new CandidateService(new SQLiteCandidateRepository(database, new CandidateValidator(), 13));//new XMLRepository<>(new CandidateValidator(), new Candidate.XMLSerializer(), "res/candidates.xml"));
    private SectionService serviceSection;
            //= new SectionService(new SQLiteSectionRepository(database, new SectionValidator(), 14));//new FileRepository<>(new SectionValidator(), new Section.Serializer(), "res/sections.txt"));
    private OptionService serviceOption;
            //= new OptionService(new SQLiteOptionRepository(database, new OptionValidator(), serviceCandidate.getRepo(), serviceSection.getRepo(), 9));//new FileRepository<>(new OptionValidator(), new Option.Serializer(serviceCandidate, serviceSection), "res/options.txt"));

    @FXML
    private CandidateView candidatesView;
    @FXML
    private SectionView sectionsView;
    @FXML
    private OptionView optionsView;
    @FXML
    private ReportsView reportsView;
    @FXML
    public StatusBar statusBar;
    @FXML
    public MenuItem menuExit, menuViewCandidates, menuViewSections, menuViewOptions, menuViewReports;
    @FXML
    public MenuBar menuBar;

    private Node current;

    public FancyController() {
        CandidateValidator validatorCandidate = new CandidateValidator();
        SectionValidator validatorSection = new SectionValidator();
        OptionValidator validatorOption = new OptionValidator();
        FancyMain.stage.setOnCloseRequest((c) -> handleMenuExit(null));
        switch (Config.datasourceType) {
            case "fileText":
                serviceCandidate = new CandidateService(new FileRepository<>(
                        validatorCandidate,
                        new Candidate.Serializer(),
                        Config.datasourcePath + "candidates.txt",
                        candidatesPerPage));
                serviceSection = new SectionService(new FileRepository<>(
                        validatorSection,
                        new Section.Serializer(),
                        Config.datasourcePath + "sections.txt",
                        sectionsPerPage));
                serviceOption = new OptionService(new FileRepository<>(
                        validatorOption,
                        new Option.Serializer(serviceCandidate, serviceSection),
                        Config.datasourcePath + "options.txt",
                        optionsPerPage));
                break;
//            case "fileBinary":
//                serviceCandidate = new CandidateService(new FileRepositoryNumeroDos<>(
//                        validatorCandidate,
//                        Config.datasourcePath + "candidates.bin",
//                        candidatesPerPage));
//                serviceSection = new SectionService(new FileRepositoryNumeroDos<>(
//                        validatorSection,
//                        Config.datasourcePath + "sections.bin",
//                        sectionsPerPage));
//                serviceOption = new OptionService(new FileRepositoryNumeroDos<>(
//                        validatorOption,
//                        Config.datasourcePath + "options.bin",
//                        optionsPerPage));
//                break;
            case "fileXML":
                serviceCandidate = new CandidateService(new XMLRepository<>(
                        validatorCandidate,
                        new Candidate.XMLSerializer(),
                        Config.datasourcePath + "candidates.xml",
                        candidatesPerPage));
                serviceSection = new SectionService(new XMLRepository<>(
                        validatorSection,
                        new Section.XMLSerializer(),
                        Config.datasourcePath + "sections.xml",
                        sectionsPerPage));
                serviceOption = new OptionService(new XMLRepository<>(
                        validatorOption,
                        new Option.XMLSerializer(serviceCandidate, serviceSection),
                        Config.datasourcePath + "options.xml",
                        optionsPerPage));
                break;
            case "sqlite":
                SQLiteDatabase database = new SQLiteDatabase(Config.datasourcePath + "data.db");
                serviceCandidate = new CandidateService(new SQLiteCandidateRepository(
                        database,
                        validatorCandidate,
                        candidatesPerPage));
                serviceSection = new SectionService(new SQLiteSectionRepository(
                        database,
                        validatorSection,
                        sectionsPerPage));
                serviceOption = new OptionService(new SQLiteOptionRepository(
                        database,
                        validatorOption,
                        serviceCandidate.getRepo(),
                        serviceSection.getRepo(),
                        optionsPerPage));
                break;
        }
    }

    @FXML
    public void initialize() {
        sectionsView.controller.setService(serviceSection);
        sectionsView.controller.baseController = this;
        optionsView.controller.setServices(serviceOption, serviceCandidate, serviceSection);
        optionsView.controller.baseController = this;
        candidatesView.controller.setService(serviceCandidate);
        candidatesView.controller.baseController = this;
        reportsView.controller.setServices(serviceOption, serviceCandidate, serviceSection);
        reportsView.controller.baseController = this;
        generateStatusBarMessages(statusBar);

        Utils.moveRight(optionsView, Duration.millis(1000));
        Utils.moveRight(sectionsView, Duration.millis(1000));
        Utils.moveRight(reportsView, Duration.millis(1000));
        current = candidatesView;
    }

    private void generateStatusBarMessages(StatusBar statusBar) {
        candidatesView.controller.generateStatusBarMessages(statusBar);
        sectionsView.controller.generateStatusBarMessages(statusBar);
        optionsView.controller.generateStatusBarMessages(statusBar);
        reportsView.controller.generateStatusBarMessages(statusBar);

        statusBar.addMessage(statusBar, "That's me. Hi!");
        statusBar.addMessage(menuExit, "Exits the application");
        statusBar.addMessage(menuViewCandidates, "Switches to the Candidates view");
        statusBar.addMessage(menuViewSections, "Switches to the Sections view");
        statusBar.addMessage(menuViewOptions, "Switches to the Options view");
        statusBar.addMessage(menuViewReports, "Switches to the Reports view");
    }

    @FXML
    public void handleViewChange(ActionEvent ev) {
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

    public void handleMenuExit(ActionEvent ev) {
        Platform.exit();
    }

    public void handleAnimationMoveAbout(ActionEvent ev) {
        Path p = new Path();
        p.getElements().add(new MoveTo(20, 20));
        p.getElements().add(new CubicCurveTo(380, 0, 380, 120, 300, 120));
        p.getElements().add(new CubicCurveTo(0, 120, 0, 240, 380, 240));

        PathTransition pt = new PathTransition(Duration.millis(2000), p, FancyMain.scene.getRoot());
        pt.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pt.setCycleCount(4);
        pt.setAutoReverse(true);
        pt.play();
    }

    public void handleAnimationFade(ActionEvent ev) {
        FadeTransition ft = new FadeTransition(Duration.millis(3000), FancyMain.scene.getRoot());
        ft.setFromValue(1.0);
        ft.setToValue(0.1);
        ft.setCycleCount(4);
        ft.setAutoReverse(true);
        ft.play();
    }

    public void handleAnimationPopOut(ActionEvent ev) {
        Timeline t = new Timeline();
        t.setCycleCount(4);
//        t.setAutoReverse(true);
        KeyValue kv1 = new KeyValue(FancyMain.stage.minHeightProperty(), 650);
        KeyFrame kf1 = new KeyFrame(Duration.millis(1000), kv1);
        KeyValue kv2 = new KeyValue(FancyMain.stage.minHeightProperty(), 400);
        KeyFrame kf2 = new KeyFrame(Duration.ZERO, kv2);
        KeyValue kv3 = new KeyValue(FancyMain.stage.maxHeightProperty(), 400);
        KeyFrame kf3 = new KeyFrame(Duration.millis(1000), kv3);
        KeyValue kv4 = new KeyValue(FancyMain.stage.maxHeightProperty(), 650);
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

    public void handleAnimationRotate(ActionEvent ev) {
        RotateTransition rt = new RotateTransition(Duration.millis(2000));
        rt.setFromAngle(0);
        rt.setToAngle(355);
        rt.setCycleCount(1);
//        rt.setAutoReverse(true);
        rt.setNode(FancyMain.scene.getRoot());
        rt.play();
    }

    public void handleMouseEntered(MouseEvent ev) {
        statusBar.setMessage(ev.getTarget());
    }

    public void handleMouseExited(MouseEvent ev) {
        statusBar.setText("");
    }
}
