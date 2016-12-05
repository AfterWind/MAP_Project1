package afterwind.lab1.ui;

import afterwind.lab1.controller.CandidateController;
import afterwind.lab1.entity.Candidate;
import afterwind.lab1.service.CandidateService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class CandidateView extends BorderPane {

    private final CandidateController controller;

    public final TableView<Candidate> tableView;

    public final TextField nameTextField;
    public final TextField addressTextField;
    public final TextField telTextField;

    private final Pane left, right, top, bottom, center;

    public CandidateView(CandidateService service) {
        this.controller = new CandidateController(service, this);
        this.tableView = new TableView<>();
        this.setPadding(new Insets(15D));

        this.left = new AnchorPane();
        this.right = new AnchorPane();
        this.top = new HBox();
        ((HBox) top).setAlignment(Pos.CENTER);
        this.bottom = new AnchorPane();
        this.center = new AnchorPane();

        this.telTextField = new TextField();
        this.nameTextField = new TextField();
        this.addressTextField = new TextField();

        this.setLeft(left);
        this.setRight(right);
        this.setCenter(center);
        this.setTop(top);
        this.setBottom(bottom);

        initCenter();
        initTop();
        controller.showAll();
    }

    private void initTop() {
        Label label = new Label("Student Management");
        label.setFont(new Font(20));
        label.setPadding(new Insets(10, 0, 20, 0));
//        AnchorPane.setTopAnchor(label, 20D);
//        AnchorPane.setLeftAnchor(label, 50D);
//        AnchorPane.setRightAnchor(label, 50D);
//        AnchorPane.setBottomAnchor(label, 30D);
        top.getChildren().add(label);
    }

    private void initCenter() {
        HBox main = new HBox(5);
        center.getChildren().add(main);

        // --- Table ---

        VBox tableBox = new VBox(10);
        main.getChildren().add(tableBox);
        tableBox.getChildren().add(tableView);

        TableColumn<Candidate, Integer> columnID = new TableColumn<>("ID");
        TableColumn<Candidate, String> columnName = new TableColumn<>("Name");
        TableColumn<Candidate, String> columnTel = new TableColumn<>("Telephone");
        TableColumn<Candidate, String> columnAddress = new TableColumn<>("Address");

        columnID.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnTel.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        columnAddress.setCellValueFactory(new PropertyValueFactory<>("address"));

        tableView.getColumns().setAll(columnID, columnName, columnTel, columnAddress);

        tableView.getSelectionModel().selectedItemProperty().addListener(controller::handleSelectionChanged);

        HBox buttons = new HBox(5);
        tableBox.getChildren().add(buttons);

        Button buttonDelete = new Button("Delete");
        buttonDelete.setMinSize(80, 0);
        Button buttonRefresh = new Button("Refresh");
        buttonRefresh.setMinSize(80, 0);
        Button buttonSave = new Button("Save");
        buttonSave.setMinSize(80, 0);
        buttons.getChildren().addAll(buttonDelete, buttonRefresh, buttonSave);

        AnchorPane.setBottomAnchor(buttons, 10D);
        buttons.setAlignment(Pos.CENTER);

        buttonDelete.setOnAction(controller::handleDelete);
        buttonRefresh.setOnAction(controller::handleRefresh);
        buttonSave.setOnAction(controller::handleSave);

        // --- Details ---

        VBox detailsBox = new VBox(5);
        main.getChildren().add(detailsBox);

        GridPane details = new GridPane();
        details.setHgap(5D);
        details.setVgap(5D);
        details.setPadding(new Insets(20, 0, 0, 20D));
        Label nameLabel = new Label("Name: ");
        Label addressLabel = new Label("Address: ");
        Label telLabel = new Label("Telephone: ");

        details.add(nameLabel, 0, 0);
        details.add(nameTextField, 1, 0);
        details.add(addressLabel, 0, 1);
        details.add(addressTextField, 1, 1);
        details.add(telLabel, 0, 2);
        details.add(telTextField, 1, 2);

        nameTextField.borderProperty().set(new Border(new BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        addressTextField.borderProperty().set(new Border(new BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        telTextField.borderProperty().set(new Border(new BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        AnchorPane.setRightAnchor(details, 20D);
        detailsBox.getChildren().add(details);

        nameTextField.textProperty().addListener(controller::handleTextChanged);
        addressTextField.textProperty().addListener(controller::handleTextChanged);
        telTextField.textProperty().addListener(controller::handleTextChanged);

        HBox buttons2 = new HBox(5);
        detailsBox.getChildren().add(buttons2);
        buttons2.setPadding(new Insets(20, 0, 0, 0));

        Button buttonAdd = new Button("Add");
        Button buttonUpdate = new Button("Update");
        Button buttonClear = new Button("Clear");
        buttons2.getChildren().addAll(buttonAdd, buttonUpdate, buttonClear);

        buttonAdd.setMinSize(80, 0);
        buttonUpdate.setMinSize(80, 0);
        buttonClear.setMinSize(80, 0);

        buttonAdd.setOnAction(controller::handleAdd);
        buttonUpdate.setOnAction(controller::handleUpdate);
        buttonClear.setOnAction(controller::handleClear);

        AnchorPane.setBottomAnchor(buttons2, 10D);
        buttons2.setAlignment(Pos.CENTER);
    }
}
