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

public class AdminDashboardController implements Initializable {
    @FXML private Label userNameLabel;
    @FXML private Label userEmailLabel;
    @FXML private Label userCountLabel;
    @FXML private Label hospitalCountLabel;
    @FXML private Label doctorCountLabel;
    @FXML private Label systemHealthLabel;

    @FXML private TableView<User> usersTable;
    @FXML private TableColumn<User, Integer> userIdColumn;
    @FXML private TableColumn<User, String> nameColumn;
    @FXML private TableColumn<User, String> emailColumn;
    @FXML private TableColumn<User, String> roleColumn;
    @FXML private TableColumn<User, String> statusColumn;
    @FXML private TableColumn<User, String> createdAtColumn;
    @FXML private TableColumn<User, Void> actionsColumn;

    @FXML private TableView<ActivityLog> activityTable;
    @FXML private TableColumn<ActivityLog, String> activityTimeColumn;
    @FXML private TableColumn<ActivityLog, String> activityUserColumn;
    @FXML private TableColumn<ActivityLog, String> activityTypeColumn;
    @FXML private TableColumn<ActivityLog, String> activityDetailsColumn;

    private UserData userData;

    public void setUser(UserData userData) {
        this.userData = userData;
        userNameLabel.setText(userData.fullName());
        userEmailLabel.setText(userData.email());

        loadData();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeUserTable();
        initializeActivityTable();
        loadData();
    }

    private void initializeUserTable() {
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        createdAtColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

        // Configure the action column with edit and delete buttons
        actionsColumn.setCellFactory(column -> {
            return new TableCell<User, Void>() {
                private final Button editButton = new Button("Edit");
                private final Button deleteButton = new Button("Delete");
                private final HBox pane = new HBox(5, editButton, deleteButton);

                {
                    editButton.getStyleClass().add("edit-button");
                    deleteButton.getStyleClass().add("delete-button");

                    editButton.setOnAction(event -> {
                        User user = getTableRow().getItem();
                        if (user != null) {
                            handleEditUser(user);
                        }
                    });

                    deleteButton.setOnAction(event -> {
                        User user = getTableRow().getItem();
                        if (user != null) {
                            handleDeleteUser(user);
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

    private void initializeActivityTable() {
        activityTimeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        activityUserColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        activityTypeColumn.setCellValueFactory(new PropertyValueFactory<>("actionType"));
        activityDetailsColumn.setCellValueFactory(new PropertyValueFactory<>("details"));
    }

    private void loadData() {
        // Load counts for dashboard stats
        userCountLabel.setText("1,425");
        hospitalCountLabel.setText("24");
        doctorCountLabel.setText("124");
        systemHealthLabel.setText("Good");

        // Sample user data - in a real app, fetch this from your database
        ObservableList<User> users = FXCollections.observableArrayList(
            new User(1, "John Doe", "john@example.com", "PATIENT", "Active", "2023-01-15"),
            new User(2, "Jane Smith", "jane@hospital.com", "DOCTOR", "Active", "2023-02-20"),
            new User(3, "General Hospital", "info@generalhospital.com", "HOSPITAL", "Active", "2023-03-10"),
            new User(4, "Admin User", "admin@healthchain.com", "ADMIN", "Active", "2023-01-01"),
            new User(5, "David Wilson", "david@example.com", "PATIENT", "Inactive", "2023-04-05")
        );
        usersTable.setItems(users);

        // Sample activity data - in a real app, fetch this from your database
        ObservableList<ActivityLog> activities = FXCollections.observableArrayList(
            new ActivityLog("2023-06-01 09:15:22", "admin", "LOGIN", "Admin user logged in"),
            new ActivityLog("2023-06-01 09:20:45", "admin", "CREATE", "Created new user: doctor1@hospital.com"),
            new ActivityLog("2023-06-01 10:05:33", "jane", "LOGIN", "Doctor logged in"),
            new ActivityLog("2023-06-01 10:15:10", "jane", "UPDATE", "Updated patient record: John Doe"),
            new ActivityLog("2023-06-01 11:30:22", "hospital", "CREATE", "Created new appointment: Jane Smith with John Doe")
        );
        activityTable.setItems(activities);
    }

    @FXML
    private void handleDashboard() {
        System.out.println("Dashboard section selected");
    }

    @FXML
    private void handleUsers() {
        System.out.println("Users section selected");
    }

    @FXML
    private void handleHospitals() {
        try {
            FXMLScene.switchScene(FXMLScene.load("hospital_list.fxml"), userNameLabel.getScene().getRoot());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading hospital list: " + e.getMessage());
        }
    }

    @FXML
    private void handleDoctors() {
        System.out.println("Doctors section selected");
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
            FXMLScene.switchScene(FXMLScene.load("auth/login.fxml"), userNameLabel.getScene().getRoot());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddUser() {
        System.out.println("Add user button clicked");
        // In a real app, you would open a dialog to add a new user
    }

    private void handleEditUser(User user) {
        System.out.println("Edit user: " + user.getName());
        // In a real app, you would open a dialog to edit the user
    }

    private void handleDeleteUser(User user) {
        System.out.println("Delete user: " + user.getName());
        // In a real app, you would show a confirmation dialog and then delete
    }

    // Helper classes for TableView
    public static class User {
        private final Integer id;
        private final String name;
        private final String email;
        private final String role;
        private final String status;
        private final String createdAt;

        public User(Integer id, String name, String email, String role, String status, String createdAt) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.role = role;
            this.status = status;
            this.createdAt = createdAt;
        }

        public Integer getId() { return id; }
        public String getName() { return name; }
        public String getEmail() { return email; }
        public String getRole() { return role; }
        public String getStatus() { return status; }
        public String getCreatedAt() { return createdAt; }
    }

    public static class ActivityLog {
        private final String time;
        private final String username;
        private final String actionType;
        private final String details;

        public ActivityLog(String time, String username, String actionType, String details) {
            this.time = time;
            this.username = username;
            this.actionType = actionType;
            this.details = details;
        }

        public String getTime() { return time; }
        public String getUsername() { return username; }
        public String getActionType() { return actionType; }
        public String getDetails() { return details; }
    }
}
