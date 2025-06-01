package com.aoop.healthchain.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Appointment {
    private final StringProperty date;
    private final StringProperty time;
    private final StringProperty doctor;
    private final StringProperty department;
    private final StringProperty status;

    public Appointment(String date, String time, String doctor, String department, String status) {
        this.date = new SimpleStringProperty(date);
        this.time = new SimpleStringProperty(time);
        this.doctor = new SimpleStringProperty(doctor);
        this.department = new SimpleStringProperty(department);
        this.status = new SimpleStringProperty(status);
    }

    // Property getters
    public StringProperty dateProperty() { return date; }
    public StringProperty timeProperty() { return time; }
    public StringProperty doctorProperty() { return doctor; }
    public StringProperty departmentProperty() { return department; }
    public StringProperty statusProperty() { return status; }

    // Value getters
    public String getDate() { return date.get(); }
    public String getTime() { return time.get(); }
    public String getDoctor() { return doctor.get(); }
    public String getDepartment() { return department.get(); }
    public String getStatus() { return status.get(); }

    // Value setters
    public void setDate(String date) { this.date.set(date); }
    public void setTime(String time) { this.time.set(time); }
    public void setDoctor(String doctor) { this.doctor.set(doctor); }
    public void setDepartment(String department) { this.department.set(department); }
    public void setStatus(String status) { this.status.set(status); }
}
