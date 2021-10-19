package controllers;

import com.encentral.ems.api.IEmployee;
import com.encentral.ems.models.Admin;
import com.encentral.ems.models.Attendance;
import com.encentral.ems.models.Employee;
import com.encentral.ems.models.PasswordForm;
import com.encentral.scaffold.commons.util.MyObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.*;
import play.data.Form;
import play.data.FormFactory;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.Date;
import java.util.Optional;

@Transactional
public class EmployeeController extends Controller {

    @Inject
    IEmployee iEmployee;

    @Inject
    FormFactory formFactory;

    @Inject
    MyObjectMapper objectMapper;
    @ApiOperation(value = "Login an employee", httpMethod = "POST")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, response = Employee.class, message = "Logged in employee")}
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "email",
                    value = "Employee email",
                    paramType = "string",
                    required = true,
                    dataType = "java.lang.String"
            ),

            @ApiImplicitParam(
                    name = "password",
                    value = "Employee  password",
                    paramType = "string",
                    required = true,
                    dataType = "java.lang.String"
            )
    })

    public Result login()  throws JsonProcessingException, Exception {

        Form<Employee> libraryForm = formFactory.form(Employee.class).bindFromRequest();

        if (libraryForm.hasErrors()) {
            return badRequest(libraryForm.errorsAsJson());
        }
        Employee employee = null;
        Optional<Employee> optionalEmployee = iEmployee.login(libraryForm.get());
        if(optionalEmployee.isPresent() ) employee = optionalEmployee.get();
        if(employee == null) {
            return badRequest("Email " + libraryForm.get().getEmail() + " is already taken");
        }
        return ok(objectMapper.writeValueAsString(employee));

//          return  ok(libraryForm.get().toString());

    }


    @ApiOperation(value = "Update password", httpMethod = "POST")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, response = Admin.class, message = "Newly updated Admin")}
    )
    @ApiImplicitParams({

            @ApiImplicitParam(
                    name = "password",
                    value = "The new password",
                    paramType = "string",
                    required = true,
                    dataType = "java.lang.String"
            ),

            @ApiImplicitParam(
                    name = "oldPassword",
                    value = "The old password",
                    paramType = "string",
                    required = true,
                    dataType = "java.lang.String"
            )

    })
    public Result updatePassword(@ApiParam(name="token", value = "user token", type = "java.lang.String") String token)  throws JsonProcessingException, Exception {

        Form<PasswordForm> libraryForm = formFactory.form(PasswordForm.class).bindFromRequest();

        if (libraryForm.hasErrors()) {
            return badRequest(libraryForm.errorsAsJson());
        }
        Employee employee = null;
        Optional<Employee> optionalEmployee = iEmployee.updatePassword(token, libraryForm.get());
        if(optionalEmployee.isPresent() ) employee = optionalEmployee.get();

        return ok(objectMapper.writeValueAsString(employee));

//          return  ok(libraryForm.get().toString());

    }

    @ApiOperation(value = "Mark Attendance", httpMethod = "POST")
    public Result markAttendance(@ApiParam(value = "user token", name = "token", type = "java.lang.String") String token) throws Exception {

        Date date = new Date();

        int day = date.getDay();

        if(day >= 1 && day <= 5) {
            int time = date.getHours();
            if(time >= 9 && time <= 17 ) {
                Optional<Attendance> optionalAttendance =  iEmployee.markAttendance(token, date);
                if(optionalAttendance.isPresent()) {
                    return ok(objectMapper.writeValueAsString(optionalAttendance.get()));
                }
            }

        }


        return badRequest("Time " + date + " is not within timeframe 9a.m - 5p.m Mon - Fri");
    }



}
