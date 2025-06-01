package com.aoop.healthchain;

import com.aoop.healthchain.model.Appointment;
import com.aoop.healthchain.model.UserData;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Paint;
import javafx.scene.shape.SVGPath;
import javafx.scene.control.TableCell;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class PatientDashboardController implements Initializable {
    @FXML private Label userNameLabel;
    @FXML private Label userEmailLabel;
    @FXML private Label appointmentCountLabel;
    @FXML private Label prescriptionCountLabel;
    @FXML private Label testCountLabel;
    @FXML private ListView<String> recentActivityList;
    @FXML private TableView<Appointment> appointmentsTable;
    @FXML private TableColumn<Appointment, String> dateColumn;
    @FXML private TableColumn<Appointment, String> timeColumn;
    @FXML private TableColumn<Appointment, String> doctorColumn;
    @FXML private TableColumn<Appointment, String> departmentColumn;
    @FXML private TableColumn<Appointment, String> statusColumn;

    public void setUser(UserData userData) {
        // Set user data in the dashboard
        userNameLabel.setText(userData.fullName());
        userEmailLabel.setText(userData.email());

        // Refresh data for this user
        updateStats();
        initializeRecentActivity();
        initializeAppointmentsTable();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize with default data
        updateStats();
        initializeRecentActivity();
        initializeAppointmentsTable();

        // Apply custom cell factory for status column to display different styles
        statusColumn.setCellFactory(column -> {
            return new TableCell<Appointment, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setText(null);
                        setGraphic(null);
                        setStyle("");
                    } else {
                        setText(item);

                        // Apply different style classes based on status
                        getStyleClass().removeAll("status-upcoming", "status-confirmed", "status-completed", "status-cancelled");

                        switch (item.toLowerCase()) {
                            case "upcoming":
                                getStyleClass().add("status-upcoming");
                                break;
                            case "confirmed":
                                getStyleClass().add("status-confirmed");
                                break;
                            case "completed":
                                getStyleClass().add("status-completed");
                                break;
                            case "cancelled":
                                getStyleClass().add("status-cancelled");
                                break;
                        }
                    }
                }
            };
        });
    }

    private void updateStats() {
        // In a real application, fetch these from your database
        appointmentCountLabel.setText("3");
        prescriptionCountLabel.setText("5");
        testCountLabel.setText("2");
    }

    private void initializeRecentActivity() {
        // Sample activity data - in a real app, fetch this from your database
        ObservableList<String> activities = FXCollections.observableArrayList(
            "Prescription updated by Dr. Smith - 2 hours ago",
            "Lab results uploaded - Yesterday",
            "Appointment scheduled with Dr. Johnson - 2 days ago",
            "Prescription refill authorized - 3 days ago",
            "Completed checkup with Dr. Wilson - 1 week ago"
        );
        recentActivityList.setItems(activities);
    }

    private void initializeAppointmentsTable() {
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        doctorColumn.setCellValueFactory(new PropertyValueFactory<>("doctor"));
        departmentColumn.setCellValueFactory(new PropertyValueFactory<>("department"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Sample appointment data - in a real app, fetch this from your database
        ObservableList<Appointment> appointments = FXCollections.observableArrayList(
            new Appointment(LocalDate.now().plusDays(2).format(DateTimeFormatter.ofPattern("MMM dd, yyyy")), "10:00 AM", "Dr. Smith", "Cardiology", "Upcoming"),
            new Appointment(LocalDate.now().plusDays(5).format(DateTimeFormatter.ofPattern("MMM dd, yyyy")), "02:30 PM", "Dr. Johnson", "Neurology", "Confirmed"),
            new Appointment(LocalDate.now().plusDays(8).format(DateTimeFormatter.ofPattern("MMM dd, yyyy")), "11:15 AM", "Dr. Wilson", "Orthopedics", "Confirmed"),
            new Appointment(LocalDate.now().minusDays(5).format(DateTimeFormatter.ofPattern("MMM dd, yyyy")), "09:00 AM", "Dr. Brown", "General Medicine", "Completed"),
            new Appointment(LocalDate.now().minusDays(10).format(DateTimeFormatter.ofPattern("MMM dd, yyyy")), "03:45 PM", "Dr. Davis", "Ophthalmology", "Cancelled")
        );
        appointmentsTable.setItems(appointments);
    }

    @FXML
    private void handleOverview() {
        // Already on overview page
        System.out.println("Overview selected");
    }

    @FXML
    private void handleAppointments() {
        System.out.println("Navigating to Appointments");
        // In a real application, you would navigate to the appointments view
    }

    @FXML
    private void handleMedicalRecords() {
        System.out.println("Navigating to Medical Records");
        // In a real application, you would navigate to the medical records view
    }

    @FXML
    private void handlePrescriptions() {
        System.out.println("Navigating to Prescriptions");
        // In a real application, you would navigate to the prescriptions view
    }

    @FXML
    private void handleSettings() {
        System.out.println("Navigating to Settings");
        // In a real application, you would navigate to the settings view
    }

    @FXML
    private void handleLogout() {
        System.out.println("Logging out");
        // In a real application, you would implement proper logout functionality
    }

    @FXML
    private void handleNewAppointment() {
        System.out.println("Creating new appointment");
        // In a real application, you would open a dialog to create a new appointment
    }
}

