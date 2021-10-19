package com.encentral.ems.api;

import com.encentral.ems.models.Admin;
import com.encentral.ems.models.Attendance;
import com.encentral.ems.models.Employee;
import com.encentral.ems.models.PasswordForm;

import java.util.List;
import java.util.Optional;

public interface IAdmin {
    Optional<Admin>  addAdmin(Admin admin) throws Exception;

    Optional<Admin> getAdmin(Integer id);
    Optional<Admin> updatePassword(String token, PasswordForm passwordForm) throws Exception;
    Optional<Admin> getAdmin(String crieteria, String value);
    Optional<Admin> login(Admin admin) throws Exception;
    Optional<Employee> addEmployee(Employee employee, String token) throws Exception;
    Optional<Employee> deleteEmployee(String id, String token) throws Exception;
    Optional<Employee> getEmployee(String id, String token) throws Exception;
    List<Employee> getAllEmployees(String token)  throws Exception;

    List<Attendance> getEmployeeAttendance(String token, String id) throws Exception;
    List<Attendance> getEmployeeAttendance(String token) throws Exception;
}
