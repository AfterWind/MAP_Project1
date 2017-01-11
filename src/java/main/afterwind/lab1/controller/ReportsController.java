package afterwind.lab1.controller;

import afterwind.lab1.entity.Section;
import afterwind.lab1.permission.Permission;
import afterwind.lab1.repository.IRepository;
import afterwind.lab1.service.CandidateService;
import afterwind.lab1.service.OptionService;
import afterwind.lab1.service.SectionService;
import afterwind.lab1.ui.control.StatusBar;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.FileOutputStream;

public class ReportsController {

    @FXML
    public TableView tableReports;
    @FXML
    public TableColumn<Section, String> columnID, columnName, columnSeats, columnSeatsOccupied;
    @FXML
    public Slider sliderTopSections;
    @FXML
    public Label labelSliderValue;

    public FancyController baseController;
    private CandidateService candidateService;
    private SectionService sectionService;
    private OptionService optionService;

    private IRepository<Section, Integer> report;

    private Stage generateWindow;

    @FXML
    public void initialize() {
        columnID.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnSeats.setCellValueFactory(new PropertyValueFactory<>("nrLoc"));
        columnSeatsOccupied.setCellValueFactory(param -> new SimpleStringProperty(
                getOccupiedNrLoc(param.getValue()) + ""
        ));

        disableBasedOnPermissions();
    }

    void generateStatusBarMessages(StatusBar statusBar) {
        statusBar.addMessage(tableReports, "Showing the top most occupied sections");
        statusBar.addMessage(sliderTopSections, "How many sections should be in the report (ordered by number of slots occupied)");
    }

    protected void disableBasedOnPermissions() {
        tableReports.setDisable(!Permission.QUERY.check());
        sliderTopSections.setDisable(!Permission.QUERY.check());
    }

    public void setServices(OptionService service, CandidateService candidateService, SectionService sectionService) {
        this.optionService = service;
        this.candidateService = candidateService;
        this.sectionService = sectionService;

        sliderTopSections.setMin(1);
        sliderTopSections.setMax(sectionService.getSize());
        sliderTopSections.setBlockIncrement(1D);
        sliderTopSections.setMajorTickUnit(Math.max(Math.floor(sectionService.getSize() / 4), 1));
        sliderTopSections.setMinorTickCount(1);

        if (Permission.QUERY.check()) {
            populateTable((int) Math.floor(sliderTopSections.getValue()));
        }
    }

    public void populateTable(int top) {
        report = sectionService.getMostOccupiedSections(optionService.getRepo(), top);
        tableReports.setItems(report.getData());
    }

    private int getOccupiedNrLoc(Section s) {
        return (int) optionService.getRepo().getData().stream().filter(o -> o.getSection().getId().equals(s.getId())).count();
    }

    public PdfPCell createCell(String text, Font font, boolean centered, float indent) {
        Phrase p = new Phrase(text);
        p.setFont(font);
        PdfPCell cell = new PdfPCell(p);
        if (centered) {
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        }
        cell.setIndent(indent);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        return cell;
    }

    public PdfPTable generatePDFTable() {
        PdfPTable table = new PdfPTable(4);
        table.setHorizontalAlignment(Element.ALIGN_MIDDLE);
        Font headerFont = null;
        try {
            headerFont = new Font();
            headerFont.setStyle(Font.BOLD);
        } catch (Exception e) {
            e.printStackTrace();
        }
        table.addCell(createCell("ID", headerFont, true, 0));
        table.addCell(createCell("Name", headerFont, true, 0));
        table.addCell(createCell("NrLoc", headerFont, true, 0));
        table.addCell(createCell("NrLocOccupied", headerFont, true, 0));
        for (Section s : report.getData()) {
            table.addCell(createCell(s.getId() + "", new Font(), false, 10F));
            table.addCell(createCell(s.getName(), new Font(), false, 10F));
            table.addCell(createCell(s.getNrLoc() + "", new Font(), false, 10F));
            table.addCell(createCell(getOccupiedNrLoc(s) + "", new Font(), false, 10F));
        }
        return table;
    }

    public void handleApply(ActionEvent ev) {

    }

    public void handleMenuExportPDF(ActionEvent ev) {
        try {
            Document doc = new Document();
            PdfWriter.getInstance(doc, new FileOutputStream("res/report.pdf"));
            doc.open();
            doc.addTitle("Report on top " + report.getSize() + " most occupied sections");
            doc.add(generatePDFTable());
            doc.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void handleMenuGenerate(ActionEvent ev) {

    }

    public void handleSliderDrag(Event ev) {
        int top = (int) Math.floor(sliderTopSections.getValue());
        labelSliderValue.setText("   " + top);
        populateTable(top);
    }

    public void handleMouseEntered(MouseEvent ev) {
        baseController.handleMouseEntered(ev);
    }

    public void handleMouseExited(MouseEvent ev) {
        baseController.handleMouseExited(ev);
    }
}
