package com.aoop.healthchain;

import com.aoop.healthchain.model.Hospital;
import com.aoop.healthchain.model.HospitalData;
import com.aoop.healthchain.util.CustomAlertBox;
import com.aoop.healthchain.util.DatabaseConnection;
import com.aoop.healthchain.util.FXMLScene;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class HospitalListController implements Initializable {
    @FXML private TextField searchField;
    @FXML private TableView<HospitalData> hospitalsTable;
    @FXML private TableColumn<HospitalData, Integer> idColumn;
    @FXML private TableColumn<HospitalData, String> nameColumn;
    @FXML private TableColumn<HospitalData, String> emailColumn;
    @FXML private TableColumn<HospitalData, String> phoneColumn;
    @FXML private TableColumn<HospitalData, String> addressColumn;
    @FXML private TableColumn<HospitalData, String> cityColumn;
    @FXML private TableColumn<HospitalData, String> statusColumn;
    @FXML private TableColumn<HospitalData, Void> actionsColumn;

    private ObservableList<HospitalData> hospitalsList = FXCollections.observableArrayList();
    private FilteredList<HospitalData> filteredHospitals;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTableColumns();
        setupSearchField();
        loadHospitals();
    }

    private void setupTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Configure the actions column with edit and view buttons
        actionsColumn.setCellFactory(column -> {
            return new TableCell<HospitalData, Void>() {
                private final Button viewButton = new Button("View");
                private final Button editButton = new Button("Edit");
                private final HBox pane = new HBox(5, viewButton, editButton);

                {
                    viewButton.getStyleClass().add("view-button");
                    editButton.getStyleClass().add("edit-button");

                    viewButton.setOnAction(event -> {
                        HospitalData hospital = getTableRow().getItem();
                        if (hospital != null) {
                            viewHospitalDetails(hospital);
                        }
                    });

                    editButton.setOnAction(event -> {
                        HospitalData hospital = getTableRow().getItem();
                        if (hospital != null) {
                            editHospital(hospital);
                        }
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    setGraphic(empty ? null : pane);
                }
            };
        });
    }

    private void setupSearchField() {
        filteredHospitals = new FilteredList<>(hospitalsList, p -> true);
        hospitalsTable.setItems(filteredHospitals);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredHospitals.setPredicate(createPredicate(newValue));
        });
    }

    private Predicate<HospitalData> createPredicate(String searchText) {
        return hospital -> {
            if (searchText == null || searchText.isEmpty()) {
                return true;
            }

            String lowerCaseFilter = searchText.toLowerCase();

            if (hospital.name().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            } else if (hospital.email().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            } else if (hospital.city().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            } else if (hospital.status().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            }

            return false;
        };
    }

    private void loadHospitals() {
        try {
            DatabaseConnection connection = new DatabaseConnection();
            Hospital hospitalModel = new Hospital(connection.getConnection());
            List<HospitalData> hospitals = hospitalModel.findAll();

            hospitalsList.clear();
            hospitalsList.addAll(hospitals);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            CustomAlertBox.showCustomAlert("Error loading hospitals: " + e.getMessage(), "error");
        }
    }

    @FXML
    private void handleCreateHospital() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("hospital_form.fxml"));
            Parent root = loader.load();

            HospitalFormController controller = loader.getController();
            controller.setHospitalListController(this);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Create Hospital");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            CustomAlertBox.showCustomAlert("Error opening hospital form: " + e.getMessage(), "error");
        }
    }

    private void viewHospitalDetails(HospitalData hospital) {
        // In a real application, you would open a detailed view of the hospital
        CustomAlertBox.showCustomAlert("Viewing hospital: " + hospital.name(), "info");
    }

    private void editHospital(HospitalData hospital) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("hospital_form.fxml"));
            Parent root = loader.load();

            HospitalFormController controller = loader.getController();
            controller.setHospitalListController(this);
            controller.setHospital(hospital);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Edit Hospital");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            CustomAlertBox.showCustomAlert("Error opening hospital form: " + e.getMessage(), "error");
        }
    }

    public void refreshTable() {
        loadHospitals();
    }

    @FXML
    private void handleBack() {
        try {
            Node source = hospitalsTable.getScene().getRoot();
            FXMLScene.switchScene(FXMLScene.load("admin_dashboard.fxml"), source);
        } catch (Exception e) {
            e.printStackTrace();
            CustomAlertBox.showCustomAlert("Error returning to dashboard: " + e.getMessage(), "error");
        }
    }
}
