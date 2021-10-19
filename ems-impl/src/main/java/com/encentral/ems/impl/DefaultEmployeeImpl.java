package com.encentral.ems.impl;

import com.encentral.ems.api.IEmployee;
import com.encentral.ems.models.*;
import com.encentral.entities.*;
import com.google.common.base.Preconditions;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

public class DefaultEmployeeImpl extends AbstractImpl implements IEmployee {
    private final JPAApi jpaApi;


    @Inject
    public DefaultEmployeeImpl(JPAApi jpaApi) {
        this.jpaApi = jpaApi;

    }


    @Override
    public Optional<Employee> login(Employee employee) throws Exception {
        reinitialize(jpaApi.em());
        JPAEmployee jpaEmployee = getEmployeeEmail(employee.getEmail());
        Preconditions.checkNotNull(jpaEmployee, "No user found with email "+ employee.getEmail());
        Preconditions.checkArgument(jpaEmployee.getPassword().equals(employee.getPassword()), "Passwords do not match");

        jpaEmployee.setToken(UUID.randomUUID().toString());

        reinitialize(jpaApi.em());
        QJPAEmployee qjpaEmployee = QJPAEmployee.jPAEmployee;//.jPAAdmin;
        queryFactory.update(qjpaEmployee)
                .set(qjpaEmployee.token, jpaEmployee.getToken())
                .where(qjpaEmployee.id.eq(jpaEmployee.getId()))
                .execute();
        return Optional.of(EmployeeMapper.jpaEmployeeToEmployee(jpaEmployee));

    }

    private JPAEmployee getEmployeeToken(String value) {
        Preconditions.checkNotNull(value, "Email cannot be null");
        reinitialize(jpaApi.em());
        QJPAEmployee qjpaEmployee = QJPAEmployee.jPAEmployee;
        JPAEmployee jpaEmployee = null;


        jpaEmployee = queryFactory.selectFrom(qjpaEmployee)
                .where(qjpaEmployee.token.eq(value))
                .fetchOne();



        return jpaEmployee;
    }
    private JPAEmployee checkToken(String token) throws Exception {
        JPAEmployee jpaEmployee = getEmployeeToken(token);

        Preconditions.checkNotNull(jpaEmployee, "Invalid token");
        return jpaEmployee;
    }

    @Override
    public Optional<Attendance> markAttendance(String token, Date date) throws Exception {
       JPAEmployee jpaEmployee = checkToken(token);

        JPAAttendance jpaAttendance = new JPAAttendance();
        jpaAttendance.setMarkedDate(date);
        jpaAttendance.setEmployee(jpaEmployee);
        jpaAttendance.setId(UUID.randomUUID().toString());
        reinitialize(jpaApi.em());
        Attendance attendance = null;

        try {
            reinitialize(jpaApi.em());



            entityManager.persist(jpaAttendance);
            attendance = AttendanceMapper.jpaAttendanceToAttendance(jpaAttendance);

        } catch (Exception runtimeException) {

            attendance = null;
            return Optional.empty();
        }
        finally {
            return Optional.ofNullable(attendance);
        }

    }

    @Override
    public Optional<Employee> updatePassword(String token, PasswordForm passwordForm) throws Exception {
        Preconditions.checkNotNull(passwordForm.getOldPassword(), "Old password cannot be empty");
        Preconditions.checkNotNull(passwordForm.getPassword(), "Password cannot be empty");

        JPAEmployee jpaEmployee = checkToken(token);
        Preconditions.checkArgument(jpaEmployee.getPassword().equals(passwordForm.getOldPassword()), "Passwords do not match");
        jpaEmployee.setPassword(passwordForm.getPassword());
        reinitialize(jpaApi.em());
        QJPAEmployee qjpaEmployee = QJPAEmployee.jPAEmployee;//.jPAAdmin;
        queryFactory.update(qjpaEmployee)
                .set(qjpaEmployee.password, jpaEmployee.getPassword())
                .where(qjpaEmployee.id.eq(jpaEmployee.getId()))
                .execute();

        return Optional.of(EmployeeMapper.jpaEmployeeToEmployee(jpaEmployee));


    }
}
