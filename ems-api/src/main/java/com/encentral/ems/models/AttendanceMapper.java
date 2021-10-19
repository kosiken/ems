package com.encentral.ems.models;

import com.encentral.entities.JPAAttendance;

public class AttendanceMapper {
    public static Attendance jpaAttendanceToAttendance(JPAAttendance jpaAttendance) {
        Attendance attendance = new Attendance();
        attendance.setId(jpaAttendance.getId());
        attendance.setEmployee(EmployeeMapper.jpaEmployeeToEmployee(jpaAttendance.getEmployee()));
        attendance.setMarkedDate(jpaAttendance.getMarkedDate());
        return attendance;
    }

    public static JPAAttendance attendanceToJPAAttendance(Attendance attendance) {
        JPAAttendance jpaAttendance = new JPAAttendance();
        jpaAttendance.setMarkedDate(attendance.getMarkedDate());
        jpaAttendance.setEmployee(EmployeeMapper.employeeToJPAEmployee(attendance.getEmployee()));
        jpaAttendance.setId(attendance.getId());
        return jpaAttendance;
    }
}
