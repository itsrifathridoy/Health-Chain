package com.aoop.healthchain;

import com.aoop.healthchain.model.UserData;
import com.aoop.healthchain.util.FXMLScene;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class HospitalDashboardController implements Initializable {
    @FXML private Label hospitalNameLabel;
    @FXML private Label hospitalEmailLabel;
    @FXML private Label doctorCountLabel;
    @FXML private Label patientCountLabel;
    @FXML private Label appointmentCountLabel;
    @FXML private Label departmentCountLabel;

    @FXML private TableView<Doctor> doctorsTable;
    @FXML private TableColumn<Doctor, Integer> doctorIdColumn;
    @FXML private TableColumn<Doctor, String> nameColumn;
    @FXML private TableColumn<Doctor, String> specializationColumn;
    @FXML private TableColumn<Doctor, String> departmentColumn;
    @FXML private TableColumn<Doctor, String> contactColumn;
    @FXML private TableColumn<Doctor, String> statusColumn;
    @FXML private TableColumn<Doctor, Void> actionsColumn;

    @FXML private TableView<Appointment> appointmentsTable;
    @FXML private TableColumn<Appointment, String> timeColumn;
    @FXML private TableColumn<Appointment, String> patientNameColumn;
    @FXML private TableColumn<Appointment, String> doctorNameColumn;
    @FXML private TableColumn<Appointment, String> purposeColumn;
    @FXML private TableColumn<Appointment, String> appointmentStatusColumn;
    @FXML private TableColumn<Appointment, Void> appointmentActionsColumn;

    private UserData userData;

    public void setUser(UserData userData) {
        this.userData = userData;
        hospitalNameLabel.setText(userData.fullName());
        hospitalEmailLabel.setText(userData.email());

        loadData();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeDoctorsTable();
        initializeAppointmentsTable();
        loadData();
    }

    private void initializeDoctorsTable() {
        doctorIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        specializationColumn.setCellValueFactory(new PropertyValueFactory<>("specialization"));
        departmentColumn.setCellValueFactory(new PropertyValueFactory<>("department"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contact"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Configure the action column with edit and delete buttons
        actionsColumn.setCellFactory(column -> {
            return new TableCell<Doctor, Void>() {
                private final Button editButton = new Button("Edit");
                private final Button deleteButton = new Button("Delete");
                private final HBox pane = new HBox(5, editButton, deleteButton);

                {
                    editButton.getStyleClass().add("edit-button");
                    deleteButton.getStyleClass().add("delete-button");

                    editButton.setOnAction(event -> {
                        Doctor doctor = getTableRow().getItem();
                        if (doctor != null) {
                            handleEditDoctor(doctor);
                        }
                    });

                    deleteButton.setOnAction(event -> {
                        Doctor doctor = getTableRow().getItem();
                        if (doctor != null) {
                            handleDeleteDoctor(doctor);
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

    private void initializeAppointmentsTable() {
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        patientNameColumn.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        doctorNameColumn.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
        purposeColumn.setCellValueFactory(new PropertyValueFactory<>("purpose"));
        appointmentStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Style the status column
        appointmentStatusColumn.setCellFactory(column -> {
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

        // Configure the action column with buttons
        appointmentActionsColumn.setCellFactory(column -> {
            return new TableCell<Appointment, Void>() {
                private final Button confirmButton = new Button("Confirm");
                private final Button cancelButton = new Button("Cancel");
                private final HBox pane = new HBox(5, confirmButton, cancelButton);

                {
                    confirmButton.getStyleClass().add("confirm-button");
                    cancelButton.getStyleClass().add("cancel-button");

                    confirmButton.setOnAction(event -> {
                        Appointment appointment = getTableRow().getItem();
                        if (appointment != null) {
                            handleConfirmAppointment(appointment);
                        }
                    });

                    cancelButton.setOnAction(event -> {
                        Appointment appointment = getTableRow().getItem();
                        if (appointment != null) {
                            handleCancelAppointment(appointment);
                        }
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty) {
                        setGraphic(null);
                    } else {
                        Appointment appointment = getTableRow().getItem();
                        if (appointment != null && appointment.getStatus().equalsIgnoreCase("upcoming")) {
                            setGraphic(pane);
                        } else {
                            setGraphic(null);
                        }
                    }
                }
            };
        });
    }

    private void loadData() {
        // Load counts for dashboard stats
        doctorCountLabel.setText("45");
        patientCountLabel.setText("325");
        appointmentCountLabel.setText("28");
        departmentCountLabel.setText("12");

        // Sample doctor data - in a real app, fetch this from your database
        ObservableList<Doctor> doctors = FXCollections.observableArrayList(
            new Doctor(1, "Dr. John Smith", "Cardiology", "Cardiac Care", "john.smith@hospital.com", "Active"),
            new Doctor(2, "Dr. Jane Doe", "Neurology", "Neuroscience", "jane.doe@hospital.com", "Active"),
            new Doctor(3, "Dr. Robert Johnson", "Orthopedics", "Bone & Joint", "robert.j@hospital.com", "Active"),
            new Doctor(4, "Dr. Maria Garcia", "Pediatrics", "Children's Health", "maria.g@hospital.com", "On Leave"),
            new Doctor(5, "Dr. William Brown", "Oncology", "Cancer Care", "william.b@hospital.com", "Active")
        );
        doctorsTable.setItems(doctors);

        // Sample appointment data - in a real app, fetch this from your database
        ObservableList<Appointment> appointments = FXCollections.observableArrayList(
            new Appointment("09:00 AM", "Alice Johnson", "Dr. John Smith", "Routine Checkup", "Upcoming"),
            new Appointment("10:30 AM", "Bob Williams", "Dr. Jane Doe", "Consultation", "Confirmed"),
            new Appointment("11:15 AM", "Carol Davis", "Dr. Robert Johnson", "Follow-up", "Upcoming"),
            new Appointment("01:45 PM", "David Miller", "Dr. Maria Garcia", "Vaccination", "Confirmed"),
            new Appointment("03:30 PM", "Eve Wilson", "Dr. William Brown", "Initial Consultation", "Upcoming")
        );
        appointmentsTable.setItems(appointments);
    }

    @FXML
    private void handleDashboard() {
        System.out.println("Dashboard section selected");
    }

    @FXML
    private void handleDoctors() {
        System.out.println("Doctors section selected");
    }

    @FXML
    private void handlePatients() {
        System.out.println("Patients section selected");
    }

    @FXML
    private void handleAppointments() {
        System.out.println("Appointments section selected");
    }

    @FXML
    private void handleDepartments() {
        System.out.println("Departments section selected");
    }

    @FXML
    private void handleReports() {
        System.out.println("Reports section selected");
    }

    @FXML
    private void handleSettings() {
        System.out.println("Settings section selected");
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLScene.switchScene(FXMLScene.load("auth/login.fxml"), hospitalNameLabel.getScene().getRoot());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddDoctor() {
        System.out.println("Add doctor button clicked");
        // In a real app, you would open a dialog to add a new doctor
    }

    private void handleEditDoctor(Doctor doctor) {
        System.out.println("Edit doctor: " + doctor.getName());
        // In a real app, you would open a dialog to edit the doctor
    }

    private void handleDeleteDoctor(Doctor doctor) {
        System.out.println("Delete doctor: " + doctor.getName());
        // In a real app, you would show a confirmation dialog and then delete
    }

    private void handleConfirmAppointment(Appointment appointment) {
        System.out.println("Confirm appointment: " + appointment.getPatientName() + " with " + appointment.getDoctorName());
        // In a real app, you would update the appointment status in the database
    }

    private void handleCancelAppointment(Appointment appointment) {
        System.out.println("Cancel appointment: " + appointment.getPatientName() + " with " + appointment.getDoctorName());
        // In a real app, you would update the appointment status in the database
    }

    // Helper classes for TableView
    public static class Doctor {
        private final Integer id;
        private final String name;
        private final String specialization;
        private final String department;
        private final String contact;
        private final String status;

        public Doctor(Integer id, String name, String specialization, String department, String contact, String status) {
            this.id = id;
            this.name = name;
            this.specialization = specialization;
            this.department = department;
            this.contact = contact;
            this.status = status;
        }

        public Integer getId() { return id; }
        public String getName() { return name; }
        public String getSpecialization() { return specialization; }
        public String getDepartment() { return department; }
        public String getContact() { return contact; }
        public String getStatus() { return status; }
    }

    public static class Appointment {
        private final String time;
        private final String patientName;
        private final String doctorName;
        private final String purpose;
        private final String status;

        public Appointment(String time, String patientName, String doctorName, String purpose, String status) {
            this.time = time;
            this.patientName = patientName;
            this.doctorName = doctorName;
            this.purpose = purpose;
            this.status = status;
        }

        public String getTime() { return time; }
        public String getPatientName() { return patientName; }
        public String getDoctorName() { return doctorName; }
        public String getPurpose() { return purpose; }
        public String getStatus() { return status; }
    }
}
