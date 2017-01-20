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
import com.sun.javafx.scene.control.skin.TableHeaderRow;
import com.sun.javafx.scene.control.skin.TableViewSkin;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FancyController {

    public static final int candidatesPerPage = 13;
    public static final int sectionsPerPage = 14;
    public static final int optionsPerPage = 9;

    private CandidateService serviceCandidate;
    private SectionService serviceSection;
    private OptionService serviceOption;

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
        FancyMain.stage.setTitle(FancyMain.stage.getTitle() + " (RAVE mode activated)");
        Random r = new Random(System.currentTimeMillis());

        List<Node> nodes = getAllNodes(FancyMain.scene.getRoot());

        for (int i = 0; i < nodes.size(); i++) {
            Path p = new Path();
            p.getElements().add(new MoveTo(300, 100));
            p.getElements().add(new CubicCurveTo(r.nextInt(300), 0, 100, 120, r.nextInt(500), r.nextInt(500)));
            p.getElements().add(new CubicCurveTo(0, r.nextInt(250), 0, 240, r.nextInt(500), r.nextInt(500)));

            Path p2 = new Path();
            p2.getElements().add(new MoveTo(r.nextInt(300), 100));
            p2.getElements().add(new MoveTo(500, 300));

            Path p3 = new Path();
            p3.getElements().add(new MoveTo(300, r.nextInt(300)));
            p3.getElements().add(new MoveTo(r.nextInt(500), 100));

            Path p4 = new Path();
            p4.getElements().add(new MoveTo(300, r.nextInt(300)));
            p4.getElements().add(new MoveTo(500, r.nextInt(1000)));

            Path p5 = new Path();
            p5.getElements().add(new MoveTo(500, r.nextInt(300)));
            p5.getElements().add(new MoveTo(r.nextInt(500), 100));

            List<Path> paths = new ArrayList<>();
            paths.add(p);
            paths.add(p);
            paths.add(p);
            paths.add(p2);
            paths.add(p3);
            paths.add(p4);
            paths.add(p5);

            Node n = nodes.get(i);//nodes.get(r.nextInt(nodes.size()));
            if (!(n instanceof TableView)) {
                PathTransition pt = new PathTransition(Duration.millis(2000), paths.get(r.nextInt(paths.size())), n);
                pt.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
                pt.setCycleCount(Animation.INDEFINITE);
                pt.setAutoReverse(true);
                pt.play();
            }

            if (r.nextBoolean()) {
                FadeTransition ft = new FadeTransition(Duration.millis(r.nextInt(2000)), n);
                ft.setFromValue(1.0);
                ft.setToValue(0.5);
                ft.setCycleCount(Animation.INDEFINITE);
                ft.setAutoReverse(true);
                ft.play();
            }
            if (r.nextBoolean()) {
                RotateTransition rt = new RotateTransition(Duration.millis(2000));
                rt.setFromAngle(0);
                rt.setToAngle(r.nextInt(340));
                rt.setCycleCount(Animation.INDEFINITE);
                rt.setAutoReverse(true);
                rt.setNode(n);
                rt.play();
            }

            if (r.nextBoolean() || n instanceof TableView) {
                ScaleTransition st = new ScaleTransition(Duration.millis(300), n);
                st.setByX(Math.min(n instanceof TableView ? 0.5 : r.nextDouble(), 3));
                st.setByY(Math.min(n instanceof TableView ? 1 : r.nextDouble(), 2));
                st.setAutoReverse(true);
                st.setCycleCount(Animation.INDEFINITE);
                st.play();
            }
        }
    }

    public void handleMouseEntered(MouseEvent ev) {
        statusBar.setMessage(ev.getTarget());
    }

    public void handleMouseExited(MouseEvent ev) {
        statusBar.setText("");
    }

    public List<Node> getAllNodes(Parent root) {
        List<Node> nodes = new ArrayList<>();
        nodes.add(root);
        boolean hadParents;
        do {
            hadParents = false;
            int size = nodes.size();
            for (int i = 0; i < size; i++) {
                if (nodes.get(i) instanceof Parent && !(nodes.get(i) instanceof TableView)) {
                    boolean added = false;
                    for (Node n : ((Parent) nodes.get(i)).getChildrenUnmodifiable()) {
                        if (n instanceof Button || n instanceof Slider || n instanceof ListView || n instanceof TableView
                                || n instanceof VBox || n instanceof HBox || n instanceof Pane) {
                            nodes.add(n);
                            added = true;


//                            if (!(n instanceof VBox || n instanceof HBox)) {
//                                added = true;
//                            }
                        }
                    }
                    if (added) {
                        hadParents = true;
                        nodes.remove(i);
                        i--;
                        size--;
                    }
                }
            }
        } while (hadParents);
        return nodes;
    }
}
