package com.encentral.ems.api;
import com.encentral.ems.models.Attendance;
import com.encentral.ems.models.Employee;
import com.encentral.ems.models.PasswordForm;

import java.util.Date;
import java.util.Optional;

public interface IEmployee {
    Optional<Employee> login(Employee employee) throws Exception;

    Optional<Attendance> markAttendance(String token, Date date) throws Exception;

    Optional<Employee> updatePassword(String token, PasswordForm passwordForm) throws Exception;
}
