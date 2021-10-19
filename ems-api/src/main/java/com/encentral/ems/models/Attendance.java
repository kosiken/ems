package com.encentral.ems.models;

import java.util.Date;

public class Attendance implements IEmsModel {
    private String id;
    private Employee employee;
    private Date markedDate;

    public String getId() {
        return id;
    }

    public Date getMarkedDate() {
        return markedDate;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMarkedDate(Date markedDate) {
        this.markedDate = markedDate;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
