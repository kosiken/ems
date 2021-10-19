package com.encentral.ems.impl;

import com.encentral.ems.api.IAdmin;
import com.encentral.ems.models.*;
import com.encentral.entities.*;
import com.google.common.base.Preconditions;
import org.apache.commons.collections.CollectionUtils;
import play.db.jpa.JPAApi;
import javax.inject.Inject;
import javax.persistence.EntityTransaction;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DefaultAdminImpl extends AbstractImpl implements IAdmin {

    private final JPAApi jpaApi;


    @Inject
    public DefaultAdminImpl(JPAApi jpaApi) {
        this.jpaApi = jpaApi;

    }


    @Override
    public Optional<Admin> addAdmin(Admin admin) throws Exception {
        Preconditions.checkNotNull(admin, "admin cannot be null");
        JPAAdmin jpaAdmin1 = getAdminEmail(admin.getEmail());
        Preconditions.checkArgument(jpaAdmin1 == null, "Email already exists");
        Admin admin1 = null;
       EntityTransaction transaction = null;
       try {
           reinitialize(jpaApi.em());
           setModelId(admin);
           JPAAdmin jpaAdmin = AdminMapper.adminToJPAAdmin(admin);
        
           entityManager.persist(jpaAdmin);
           admin1 = AdminMapper.jpaAdmintoAdmin(jpaAdmin);
       } catch (Exception runtimeException) {

           admin1 = null;
           return Optional.empty();
       }
       finally {
           return Optional.ofNullable(admin1);
       }


    }


    @Override
    public Optional<Admin> getAdmin(Integer id) {
        return Optional.empty();
    }

    @Override
    public Optional<Admin> updatePassword(String token, PasswordForm passwordForm) throws Exception {
        Preconditions.checkNotNull(passwordForm.getOldPassword(), "Old password cannot be empty");
        Preconditions.checkNotNull(passwordForm.getPassword(), "Password cannot be empty");
        JPAAdmin jpaAdmin = checkToken(token);
        Preconditions.checkArgument(jpaAdmin.getPassword().equals(passwordForm.getOldPassword()), "Passwords do not match");
        jpaAdmin.setPassword(passwordForm.getPassword());
        reinitialize(jpaApi.em());
        QJPAAdmin admin = QJPAAdmin.jPAAdmin;//.jPAAdmin;
        queryFactory.update(admin)
                .set(admin.password, passwordForm.getPassword())
                .where(admin.id.eq(jpaAdmin.getId()))
                .execute();

       return Optional.of(AdminMapper.jpaAdmintoAdmin(jpaAdmin));

    }

    @Override
    public Optional<Admin> getAdmin(String crieteria, String value) {
        return Optional.empty();
    }

    private JPAAdmin getAdminEmail(String value) {
        Preconditions.checkNotNull(value, "Email cannot be null");
        reinitialize(jpaApi.em());
        QJPAAdmin qjpaAdmin = QJPAAdmin.jPAAdmin;
        JPAAdmin jpaAdmin = null;


        jpaAdmin = queryFactory.selectFrom(qjpaAdmin)
                    .where(qjpaAdmin.email.eq(value))
                    .fetchOne();



        return jpaAdmin;
    }

    private JPAAdmin getAdminToken(String value) {
        Preconditions.checkNotNull(value, "Email cannot be null");
        reinitialize(jpaApi.em());
        QJPAAdmin qjpaAdmin = QJPAAdmin.jPAAdmin;
        JPAAdmin jpaAdmin = null;


        jpaAdmin = queryFactory.selectFrom(qjpaAdmin)
                .where(qjpaAdmin.token.eq(value))
                .fetchOne();



        return jpaAdmin;
    }

    private JPAAdmin checkToken(String token) throws Exception {
        JPAAdmin jpaAdmin = getAdminToken(token);
        Preconditions.checkNotNull(jpaAdmin, "Invalid token");
        return jpaAdmin;
    }
    @Override
    public Optional<Admin> login(Admin admin) throws Exception {
        Preconditions.checkNotNull(admin.getEmail(), "email is required");
        JPAAdmin jpaAdmin = getAdminEmail(admin.getEmail());
        Preconditions.checkNotNull(jpaAdmin, "No user with email " + admin.getEmail() + " exists");

        if(jpaAdmin == null) return Optional.empty();
        Preconditions.checkArgument(jpaAdmin.getPassword().equals(admin.getPassword()), "Passwords do not match");

        jpaAdmin.setToken(UUID.randomUUID().toString());
        reinitialize(jpaApi.em());
        QJPAAdmin qjpaAdmin = QJPAAdmin.jPAAdmin;
        queryFactory.update(qjpaAdmin)
                .set(qjpaAdmin.token, jpaAdmin.getToken())
                .where(qjpaAdmin.id.eq(jpaAdmin.getId()))
                .execute();

        return Optional.of(AdminMapper.jpaAdmintoAdmin(jpaAdmin));
    }

    @Override
    public Optional<Employee> addEmployee(Employee employee, String token) throws Exception {

        checkToken(token);
        JPAEmployee jpaEmployee = getEmployeeEmail(employee.getEmail());
        Preconditions.checkArgument(jpaEmployee == null, "employee with Email " + employee.getEmail() + " already exists");
        setModelId(employee);
        employee.setPassword(Employee.generatePin(4));
        jpaEmployee = EmployeeMapper.employeeToJPAEmployee(employee);
        reinitialize(jpaApi.em());
        entityManager.persist(jpaEmployee);



        return Optional.of(EmployeeMapper.jpaEmployeeToEmployee(jpaEmployee));
    }

    @Override
    public Optional<Employee> deleteEmployee(String id, String token) throws Exception {
        checkToken(token);
        reinitialize(jpaApi.em());
        QJPAEmployee employee = QJPAEmployee.jPAEmployee;
        JPAEmployee jpaEmployee = getEmployeeId(id);
        Preconditions.checkNotNull(jpaEmployee, "No employee found with id " + id);
        queryFactory.delete(employee)
                .where(employee.id.eq(id))
                .execute();
        return Optional.of(EmployeeMapper.jpaEmployeeToEmployee(jpaEmployee));
    }

    @Override
    public Optional<Employee> getEmployee(String id, String token) throws Exception {
        checkToken(token);
        JPAEmployee jpaEmployee = getEmployeeId(id);
        Preconditions.checkNotNull(jpaEmployee, "No employee found with id " + id);

        return Optional.of(EmployeeMapper.jpaEmployeeToEmployee(jpaEmployee));

    }

    @Override
    public List<Employee> getAllEmployees(String token) throws Exception {
        checkToken(token);
        QJPAEmployee employee = QJPAEmployee.jPAEmployee;
        List employeeList;
        employeeList = queryFactory.selectFrom(employee)
                .where().fetch();
        CollectionUtils.transform(employeeList, e -> EmployeeMapper.jpaEmployeeToEmployee((JPAEmployee) e));
        return employeeList;
    }

    @Override
    public List<Attendance> getEmployeeAttendance(String token, String id) throws Exception {
        checkToken(token);
        JPAEmployee jpaEmployee = getEmployeeId(id);
        Preconditions.checkNotNull(jpaEmployee, "No employee found with id " + id);
        QJPAAttendance attendance = QJPAAttendance.jPAAttendance;
        List attendances = queryFactory.selectFrom(attendance)
                .where(attendance.employee.id.eq(id))
                .fetch();
        CollectionUtils.transform(attendances, t -> AttendanceMapper.jpaAttendanceToAttendance((JPAAttendance) t));
        return attendances;
    }

    @Override
    public List<Attendance> getEmployeeAttendance(String token) throws Exception {
        checkToken(token);
        QJPAAttendance attendance = QJPAAttendance.jPAAttendance;
        List attendances =  queryFactory.selectFrom(attendance)
                .where().fetch();
       CollectionUtils.transform(attendances, t -> AttendanceMapper.jpaAttendanceToAttendance((JPAAttendance) t));
        return attendances;
    }
}
