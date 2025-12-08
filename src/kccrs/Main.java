package kccrs;

import kccrs.models.Complaint;
import kccrs.services.ComplaintService;
import kccrs.services.ReportService;
import kccrs.services.UndoService;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;

public class Main extends Application {

    private ComplaintService complaintService;
    private UndoService undoService;
    private TableView<Complaint> complaintTable;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        complaintService = new ComplaintService();
        undoService = new UndoService(complaintService.getUndoStack());

        primaryStage.setTitle("🌆 Karachi City Complaint & Response System (KCCRS)");

        TabPane tabPane = new TabPane();
        tabPane.getTabs().addAll(
                createAddComplaintTab(),
                createProcessTab(),
                createUndoTab(),
                createReportTab()
        );

        Scene scene = new Scene(tabPane, 1100, 650);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /** TAB 1: Add Complaint with GridPane */
    private Tab createAddComplaintTab() {
        Tab tab = new Tab("📝 Add Complaint");
        tab.setClosable(false);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(20);
        grid.setVgap(15);

        Label lblName = new Label("Citizen Name:");
        TextField tfName = new TextField();
        tfName.setPromptText("Enter Name");

        Label lblArea = new Label("Area:");
        ComboBox<String> cbArea = new ComboBox<>();
        cbArea.getItems().addAll("Korangi", "Saddar", "Clifton", "Gulshan");
        cbArea.setPromptText("Select Area");

        Label lblType = new Label("Type:");
        ComboBox<String> cbType = new ComboBox<>();
        cbType.getItems().addAll("Water 💧", "Garbage 🗑️", "Electricity ⚡", "Gas 🔥", "Traffic 🚦");
        cbType.setPromptText("Select Type");

        Label lblSeverity = new Label("Severity:");
        ComboBox<String> cbSeverity = new ComboBox<>();
        cbSeverity.getItems().addAll("Low", "Medium", "High", "Critical");
        cbSeverity.setPromptText("Select Severity");

        Button btnAdd = new Button("Add Complaint");
        btnAdd.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        btnAdd.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        btnAdd.setTooltip(new Tooltip("Click to add complaint"));
        btnAdd.setOnMouseEntered(e -> btnAdd.setStyle("-fx-background-color: #45a049; -fx-text-fill: white; -fx-font-weight: bold;"));
        btnAdd.setOnMouseExited(e -> btnAdd.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;"));

        btnAdd.setOnAction(e -> {
            String name = tfName.getText();
            String area = cbArea.getValue();
            String type = cbType.getValue();
            String severity = cbSeverity.getValue();

            if (name.isEmpty() || area == null || type == null || severity == null) {
                showAlert(Alert.AlertType.ERROR, "Please fill all fields!");
                return;
            }

            complaintService.addComplaint(name, area, type, severity);
            updateComplaintTable();
            tfName.clear();
            cbArea.getSelectionModel().clearSelection();
            cbType.getSelectionModel().clearSelection();
            cbSeverity.getSelectionModel().clearSelection();
        });

        grid.add(lblName, 0, 0);
        grid.add(tfName, 1, 0);
        grid.add(lblArea, 0, 1);
        grid.add(cbArea, 1, 1);
        grid.add(lblType, 0, 2);
        grid.add(cbType, 1, 2);
        grid.add(lblSeverity, 0, 3);
        grid.add(cbSeverity, 1, 3);
        grid.add(btnAdd, 1, 4);

        // Complaint Table
        complaintTable = new TableView<>();
        complaintTable.setPrefHeight(350);
        setupTableColumns();
        updateComplaintTable();

        VBox vbox = new VBox(20);
        vbox.setPadding(new Insets(15));
        Label lblTable = new Label("All Complaints:");
        lblTable.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        vbox.getChildren().addAll(grid, lblTable, complaintTable);

        tab.setContent(vbox);
        return tab;
    }

    /** TAB 2: Process Complaint */
    private Tab createProcessTab() {
        Tab tab = new Tab("⚡ Process Complaint");
        tab.setClosable(false);

        VBox vbox = new VBox(25);
        vbox.setPadding(new Insets(20));

        Button btnNext = new Button("Process Next Complaint (FIFO)");
        styleButton(btnNext, "#2196F3");

        btnNext.setOnAction(e -> {
            Complaint c = complaintService.processNextComplaint();
            if (c != null)
                showAlert(Alert.AlertType.INFORMATION, "Processed Complaint ID: " + c.getId());
            else
                showAlert(Alert.AlertType.INFORMATION, "No complaints to process.");
            updateComplaintTable();
        });

        Button btnUrgent = new Button("Process Next Urgent Complaint");
        styleButton(btnUrgent, "#f44336");

        btnUrgent.setOnAction(e -> {
            Complaint c = complaintService.processNextUrgentComplaint();
            if (c != null)
                showAlert(Alert.AlertType.INFORMATION, "Processed Urgent Complaint ID: " + c.getId());
            else
                showAlert(Alert.AlertType.INFORMATION, "No urgent complaints to process.");
            updateComplaintTable();
        });

        vbox.getChildren().addAll(btnNext, btnUrgent);
        tab.setContent(vbox);
        return tab;
    }

    /** TAB 3: Undo Operations */
    private Tab createUndoTab() {
        Tab tab = new Tab("↩️ Undo");
        tab.setClosable(false);

        VBox vbox = new VBox(25);
        vbox.setPadding(new Insets(20));

        Button btnUndo = new Button("Undo Last Operation");
        styleButton(btnUndo, "#FF9800");
        btnUndo.setOnAction(e -> {
            Complaint c = undoService.undoLast();
            if (c != null)
                showAlert(Alert.AlertType.INFORMATION, "Undo successful for Complaint ID: " + c.getId());
            updateComplaintTable();
        });

        Button btnReverse = new Button("Display Undo Stack in Reverse");
        styleButton(btnReverse, "#9C27B0");
        btnReverse.setOnAction(e -> undoService.displayReverse());

        vbox.getChildren().addAll(btnUndo, btnReverse);
        tab.setContent(vbox);
        return tab;
    }

    /** TAB 4: Daily Reports */
    private Tab createReportTab() {
        Tab tab = new Tab("📊 Daily Report");
        tab.setClosable(false);

        VBox vbox = new VBox(20);
        vbox.setPadding(new Insets(20));

        Button btnSortArea = new Button("Sort by Area");
        styleButton(btnSortArea, "#3F51B5");
        btnSortArea.setOnAction(e -> {
            ReportService report = new ReportService(complaintService.getAllActiveComplaints());
            report.sortByArea();
            report.displayReport();
        });

        Button btnSortSeverity = new Button("Sort by Severity");
        styleButton(btnSortSeverity, "#009688");
        btnSortSeverity.setOnAction(e -> {
            ReportService report = new ReportService(complaintService.getAllActiveComplaints());
            report.sortBySeverity();
            report.displayReport();
        });

        Button btnSortTime = new Button("Sort by Time");
        styleButton(btnSortTime, "#795548");
        btnSortTime.setOnAction(e -> {
            ReportService report = new ReportService(complaintService.getAllActiveComplaints());
            report.sortByTime();
            report.displayReport();
        });

        vbox.getChildren().addAll(btnSortArea, btnSortSeverity, btnSortTime);
        tab.setContent(vbox);
        return tab;
    }

    /** TableView Columns with Priority Highlighting */
    private void setupTableColumns() {
        TableColumn<Complaint, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Complaint, String> nameCol = new TableColumn<>("Citizen");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("citizenName"));

        TableColumn<Complaint, String> areaCol = new TableColumn<>("Area");
        areaCol.setCellValueFactory(new PropertyValueFactory<>("area"));

        TableColumn<Complaint, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<Complaint, String> severityCol = new TableColumn<>("Severity");
        severityCol.setCellValueFactory(new PropertyValueFactory<>("severity"));
        severityCol.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String severity, boolean empty) {
                super.updateItem(severity, empty);
                if (empty || severity == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(severity);
                    switch (severity) {
                        case "Critical" -> setTextFill(Color.RED);
                        case "High" -> setTextFill(Color.ORANGE);
                        case "Medium" -> setTextFill(Color.GOLDENROD);
                        case "Low" -> setTextFill(Color.GREEN);
                        default -> setTextFill(Color.BLACK);
                    }
                    setFont(Font.font("Arial", FontWeight.BOLD, 14));
                }
            }
        });

        TableColumn<Complaint, String> timeCol = new TableColumn<>("Timestamp");
        timeCol.setCellValueFactory(new PropertyValueFactory<>("timestamp"));

        complaintTable.getColumns().addAll(idCol, nameCol, areaCol, typeCol, severityCol, timeCol);
    }

    /** Update TableView */
    private void updateComplaintTable() {
        complaintTable.getItems().clear();
        complaintTable.getItems().addAll(complaintService.getAllActiveComplaints());
    }

    /** Show alert */
    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.showAndWait();
    }

    /** Button Styling */
    private void styleButton(Button button, String color) {
        button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14;");
        button.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        button.setTooltip(new Tooltip("Click to perform action"));
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: derive(" + color + ", -20%); -fx-text-fill: white; -fx-font-weight: bold;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-weight: bold;"));
    }
}
