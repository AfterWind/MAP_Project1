package afterwind.lab1.controller;

import afterwind.lab1.Utils;
import afterwind.lab1.config.Config;
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
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import com.itextpdf.text.pdf.draw.LineSeparator;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

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

    public PdfPCell createCell(String text, Font font, boolean centered, float indent, float borderTop, float borderRight, float borderBottom, float borderLeft) {
        Phrase p = new Phrase(text, font);
        PdfPCell cell = new PdfPCell(p);
        if (centered) {
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        }
        cell.setIndent(indent);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setBorderWidthTop(borderTop);
        cell.setBorderWidthRight(borderRight);
        cell.setBorderWidthBottom(borderBottom);
        cell.setBorderWidthLeft(borderLeft);
        cell.setPaddingBottom(6f);

        return cell;
    }

    public PdfPTable generatePDFTable() throws DocumentException {
        PdfPTable table = new PdfPTable(4);
        table.setSpacingBefore(10f);
        table.setWidths(new float[] {1, 4, 3, 3});
        table.setHorizontalAlignment(Element.ALIGN_MIDDLE);

        Font fontHeader = new Font(Font.FontFamily.COURIER, 12f, Font.BOLD, BaseColor.GRAY);
        Font fontID = new Font(Font.FontFamily.COURIER);
        fontID.setColor(BaseColor.RED);
        float bottomBorder, insideBorder = 0.3f, outsideBorder = 2f;
        table.addCell(createCell("ID", fontHeader, true, 0, outsideBorder, outsideBorder, outsideBorder, outsideBorder));
        table.addCell(createCell("Name", fontHeader, true, 0, outsideBorder, insideBorder, outsideBorder, insideBorder));
        table.addCell(createCell("Seats", fontHeader, true, 0, outsideBorder, insideBorder, outsideBorder, insideBorder));
        table.addCell(createCell("Seats Occupied", fontHeader, true, 0, outsideBorder, outsideBorder, outsideBorder, insideBorder));
        for (Section s : report.getData()) {
            bottomBorder = insideBorder;
            if (table.getRows().size() == report.getSize()) {
                bottomBorder = outsideBorder;
            }
            table.addCell(createCell(s.getId() + "", fontID, true, 0, insideBorder, outsideBorder, bottomBorder, outsideBorder));
            table.addCell(createCell(s.getName(), new Font(Font.FontFamily.COURIER), false, 10F, insideBorder, insideBorder, bottomBorder, insideBorder));
            table.addCell(createCell(s.getNrLoc() + "", new Font(Font.FontFamily.COURIER), false, 10F, insideBorder, insideBorder, bottomBorder, insideBorder));
            table.addCell(createCell(getOccupiedNrLoc(s) + "", new Font(Font.FontFamily.COURIER), false, 10F, insideBorder, outsideBorder, bottomBorder, insideBorder));
        }
        return table;
    }

    public void handleMenuExportPDF(ActionEvent ev) {
        try {
            File file = new File(Config.reportsPath + "report_"+ ZonedDateTime.now().truncatedTo(ChronoUnit.SECONDS).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) +".pdf");

            Document doc = new Document();
            PdfWriter.getInstance(doc, new FileOutputStream(file));
            doc.open();
            doc.addTitle("Report with top " + report.getSize() + " most occupied sections");
            Font fp = new Font(Font.FontFamily.TIMES_ROMAN, 20f, Font.ITALIC, BaseColor.DARK_GRAY);
            Paragraph p = new Paragraph("Report with top " + report.getSize() + " most occupied sections", fp);
            p.setSpacingAfter(4f);
            doc.add(p);

            DottedLineSeparator separator = new DottedLineSeparator();
            separator.setGap(1.5f);
            doc.add(separator);
            doc.add(generatePDFTable());
            doc.close();

            Utils.showInfoMessage("The report has been generated and was saved: \nreports/" + file.getName());
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
