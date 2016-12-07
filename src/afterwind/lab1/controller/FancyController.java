package afterwind.lab1.controller;

import afterwind.lab1.entity.Candidate;
import afterwind.lab1.entity.Option;
import afterwind.lab1.entity.Section;
import afterwind.lab1.repository.FileRepository;
import afterwind.lab1.service.CandidateService;
import afterwind.lab1.service.OptionService;
import afterwind.lab1.service.SectionService;
import afterwind.lab1.ui.CandidateView;
import afterwind.lab1.ui.OptionView;
import afterwind.lab1.ui.SectionView;
import afterwind.lab1.validator.CandidateValidator;
import afterwind.lab1.validator.OptionValidator;
import afterwind.lab1.validator.SectionValidator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class FancyController {

    private CandidateService candidateService = new CandidateService(new FileRepository<>(new CandidateValidator(), new Candidate.Serializer(), "/home/afterwind/IdeaProjects/MAP_Lab1/res/candidates.txt"));
    private SectionService sectionService = new SectionService(new FileRepository<>(new SectionValidator(), new Section.Serializer(), "/home/afterwind/IdeaProjects/MAP_Lab1/res/sections.txt"));
    private OptionService optionService = new OptionService(new FileRepository<>(new OptionValidator(), new Option.Serializer(candidateService, sectionService), "/home/afterwind/IdeaProjects/MAP_Lab1/res/options.txt"));

    @FXML
    private CandidateView candidatesView;
    @FXML
    private SectionView sectionsView;
    @FXML
    private OptionView optionsView;

    public FancyController() { }

    @FXML
    public void initialize() {
        sectionsView.controller.setService(sectionService);
        optionsView.controller.setServices(optionService, candidateService, sectionService);
        candidatesView.controller.setService(candidateService);
    }

    @FXML
    public ToggleButton candidateButton, sectionsButton, optionsButton;
    @FXML
    public StackPane shown;

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
}
