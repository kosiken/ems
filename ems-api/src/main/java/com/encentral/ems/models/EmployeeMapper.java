package com.encentral.ems.models;

import com.encentral.entities.JPAEmployee;

public class EmployeeMapper {
    public static JPAEmployee employeeToJPAEmployee(Employee employee) {
        JPAEmployee jpaEmployee = new JPAEmployee();
        jpaEmployee.setEmail(employee.getEmail());
        jpaEmployee.setPassword(employee.getPassword());
        jpaEmployee.setFirstName(employee.getFirstName());
        jpaEmployee.setLastName(employee.getLastName());
        jpaEmployee.setId(employee.getId());
        jpaEmployee.setToken(employee.getToken());
        return jpaEmployee;
    }

    public static Employee jpaEmployeeToEmployee(JPAEmployee jpaEmployee) {
        Employee employee = new Employee();
         employee.setEmail(jpaEmployee.getEmail());
         employee.setToken(jpaEmployee.getToken());
         employee.setId(jpaEmployee.getId());
         employee.setPassword(jpaEmployee.getPassword());
         employee.setFirstName(jpaEmployee.getFirstName());
         employee.setLastName(jpaEmployee.getLastName());
         return employee;
//         employee.setToken(jpaEmployee.);
    }
}
