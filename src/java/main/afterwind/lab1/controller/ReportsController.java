package afterwind.lab1.controller;

import afterwind.lab1.entity.Section;
import afterwind.lab1.permission.Permission;
import afterwind.lab1.repository.IRepository;
import afterwind.lab1.service.CandidateService;
import afterwind.lab1.service.OptionService;
import afterwind.lab1.service.SectionService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.DragEvent;
import javafx.stage.Stage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class ReportsController {

    @FXML
    public TableView sectionReportsTable;
    @FXML
    public TableColumn<Section, String> idColumn, nameColumn, nrLocColumn, nrLocOccupiedColumn;
    @FXML
    public TextField textFieldTop;
    @FXML
    public Slider topSectionsSlider;
    @FXML
    public Label sliderValueLabel;

    private CandidateService candidateService;
    private SectionService sectionService;
    private OptionService optionService;

    private IRepository<Section, Integer> report;

    private Stage generateWindow;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nrLocColumn.setCellValueFactory(new PropertyValueFactory<>("nrLoc"));
        nrLocOccupiedColumn.setCellValueFactory(param -> new SimpleStringProperty(
                getOccupiedNrLoc(param.getValue()) + ""
        ));

        disableBasedOnPermissions();
    }

    protected void disableBasedOnPermissions() {
        sectionReportsTable.setDisable(!Permission.QUERY.check());
        topSectionsSlider.setDisable(!Permission.QUERY.check());
    }

    public void setServices(OptionService service, CandidateService candidateService, SectionService sectionService) {
        this.optionService = service;
        this.candidateService = candidateService;
        this.sectionService = sectionService;

        topSectionsSlider.setMin(1);
        topSectionsSlider.setMax(sectionService.getSize());
        topSectionsSlider.setBlockIncrement(1D);
        topSectionsSlider.setMajorTickUnit(Math.max(Math.floor(sectionService.getSize() / 4), 1));
        topSectionsSlider.setMinorTickCount(1);

        if (Permission.QUERY.check()) {
            populateTable((int) Math.floor(topSectionsSlider.getValue()));
        }
    }

    public void populateTable(int top) {
        report = sectionService.getMostOccupiedSections(optionService.getRepo(), top);
        sectionReportsTable.setItems(report.getData());
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
        int top = (int) Math.floor(topSectionsSlider.getValue());
        sliderValueLabel.setText("   " + top);
        populateTable(top);
    }
}
