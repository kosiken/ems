package com.encentral.entities;



import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="attendance")
public class JPAAttendance {
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private JPAEmployee employee;


    @Column(name = "posting_date")
    private Date markedDate;

    public void setMarkedDate(Date markedDate) {
        this.markedDate = markedDate;
    }

    public Date getMarkedDate() {
        return markedDate;
    }

    public void setEmployee(JPAEmployee employee) {
        this.employee = employee;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public JPAEmployee getEmployee() {
        return employee;
    }

    @Override
    public String toString() {
        return "Attendance{" +
                "id=" + id +
                ", employee=" + employee.getEmail() +
                ", markedDate=" + markedDate +
                '}';
    }
}
