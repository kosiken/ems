package controllers;
import com.encentral.ems.api.IAdmin;
import com.encentral.ems.models.*;
import com.encentral.scaffold.commons.util.MyObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.*;
import play.data.Form;
import play.data.FormFactory;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import javax.inject.Inject;
import java.util.Optional;


@Api(value = "/admin")
@Transactional
public class AdminController extends Controller {

    @Inject
    IAdmin iAdmin;

    @Inject
    FormFactory formFactory;

    @Inject
    MyObjectMapper objectMapper;

    @ApiOperation(value = "Create New Admin", httpMethod = "POST")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, response = Admin.class, message = "Newly created Admin"),
                    @ApiResponse(code = 400, response = ApiErrorReporter.class, message = "Email is already taken"),
                    @ApiResponse(code = 500, response = ApiErrorReporter.class, message = "Unexpected Error")}
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "name",
                    value = "Admin name",
                    paramType = "form",
                    required = true,
                    dataType = "string"
            ),

            @ApiImplicitParam(
                    name = "email",
                    value = "Admin email",
                    paramType = "form",
                    required = true,
                    dataType = "string"
            ),

            @ApiImplicitParam(
                    name = "password",
                    value = "Admin password",
                    paramType = "form",
                    required = true,
                    dataType = "string"
            )
    })
    public Result addAdmin() throws Exception {

        Form<Admin> libraryForm = formFactory.form(Admin.class).bindFromRequest();

        if (libraryForm.hasErrors()) {
            return badRequest(libraryForm.errorsAsJson());
        }
        Admin admin = null;
        try {
            Optional<Admin> optionalAdmin = iAdmin.addAdmin(libraryForm.get());
            if (optionalAdmin.isPresent()) admin = optionalAdmin.get();
            if (admin == null) {
                return badRequest("Email " + libraryForm.get().getEmail() + " is already taken");
            }
            return ok(objectMapper.writeValueAsString(admin));

        }
        catch (NullPointerException nullPointerException) {
                ApiErrorReporter errorReporter = new ApiErrorReporter();
                errorReporter.reason = nullPointerException.getMessage();
                return badRequest(objectMapper.writeValueAsString(errorReporter));
        }


    }



    @ApiOperation(value = "Login an admin", httpMethod = "POST")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, response = Admin.class, message = "Logged in admin"),
                    @ApiResponse(code = 400, response = ApiErrorReporter.class, message = "No user found with email"),
                    @ApiResponse(code = 500, response = ApiErrorReporter.class, message = "Unexpected Error")}
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "email",
                    value = "Admin email",
                    paramType = "form",
                    required = true,
                    dataType = "string"
            ),

            @ApiImplicitParam(
                    name = "password",
                    value = "Admin password",
                    paramType = "form",
                    required = true,
                    dataType = "string"
            )
    })

    public Result login()   throws Exception {

        Form<Admin> libraryForm = formFactory.form(Admin.class).bindFromRequest();

        if (libraryForm.hasErrors()) {
            return badRequest(libraryForm.errorsAsJson());
        }
        Admin admin1;
        admin1 = libraryForm.get();
        try {


            Admin admin = null;
            Optional<Admin> optionalAdmin = iAdmin.login(admin1);
            if (optionalAdmin.isPresent()) admin = optionalAdmin.get();
            if (admin == null) {
                return badRequest("No such user " + admin1.getEmail());
            }
            return ok(objectMapper.writeValueAsString(admin));
        }
        catch (NullPointerException nullPointerException) {
            ApiErrorReporter errorReporter = new ApiErrorReporter();
            errorReporter.reason = nullPointerException.getMessage();
            return badRequest(objectMapper.writeValueAsString(errorReporter));
        }
//          return  ok(libraryForm.get().toString());

    }

    @ApiOperation(value = "Create New Employee", httpMethod = "POST")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, response = Employee.class, message = "Newly created Employee"),
                    @ApiResponse(code = 400, response = ApiErrorReporter.class, message = "No user found with token"),
                    @ApiResponse(code = 400, response = ApiErrorReporter.class, message = "Email is already taken"),
                    @ApiResponse(code = 500, response = ApiErrorReporter.class, message = "Unexpected Error")
            }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "firstName",
                    value = "Employee first name",
                    paramType = "form",
                    required = true,
                    dataType = "string"
            ),
            @ApiImplicitParam(
                    name = "lastName",
                    value = "Employee last name",
                    paramType = "form",
                    required = true,
                    dataType = "string"
            ),
            @ApiImplicitParam(
                    name = "email",
                    value = "Employee email",
                    paramType = "form",
                    required = true,
                    dataType = "string"
            )

    })
    public Result addEmployee(@ApiParam(value = "token of the admin", name = "token") String token)  throws JsonProcessingException, Exception {

        Form<Employee> libraryForm = formFactory.form(Employee.class).bindFromRequest();

        if (libraryForm.hasErrors()) {
            return badRequest(libraryForm.errorsAsJson());
        }
        try {


            Employee employee = null;
            Optional<Employee> optionalAdmin = iAdmin.addEmployee(libraryForm.get(), token);
            if (optionalAdmin.isPresent()) employee = optionalAdmin.get();
            if (employee == null) {
                return badRequest("Email " + libraryForm.get().getEmail() + " is already taken");
            }
            return ok(objectMapper.writeValueAsString(employee));
        }
           catch (NullPointerException nullPointerException) {
            ApiErrorReporter errorReporter = new ApiErrorReporter();
            errorReporter.reason = nullPointerException.getMessage();
            return badRequest(objectMapper.writeValueAsString(errorReporter));
        }

//          return  ok(libraryForm.get().toString());

    }

    @ApiOperation(value = "get all employees", httpMethod = "GET")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "All the Employees",
                    response = Employee.class, responseContainer = "List"),

            @ApiResponse(code = 400, response = ApiErrorReporter.class, message = "No user found with token"),
            @ApiResponse(code = 500, response = ApiErrorReporter.class, message = "Unexpected Error")
    })
    public Result getEmployees(@ApiParam(value = "token of the admin")String token) throws Exception {
        try {
            return ok(objectMapper.writeValueAsString(iAdmin.getAllEmployees(token)));
        }
        catch (NullPointerException nullPointerException) {
            ApiErrorReporter errorReporter = new ApiErrorReporter();
            errorReporter.reason = nullPointerException.getMessage();
            return badRequest(objectMapper.writeValueAsString(errorReporter));
        }
    }

    @ApiOperation(value = "get employee Attendance", httpMethod = "GET")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "All the attendance of Employee with id",
                    response = Attendance.class, responseContainer = "List"),
            @ApiResponse(code = 400, response = ApiErrorReporter.class, message = "No user found with token"),
            @ApiResponse(code = 500, response = ApiErrorReporter.class, message = "Unexpected Error")
    })
    public Result getEmployeeAttendance(@ApiParam(value = "token of the admin", name = "token")String token, @ApiParam(value = "id of employee", name = "id", type = "string")String id) throws Exception {
       try {


           return ok(objectMapper.writeValueAsString(iAdmin.getEmployeeAttendance(token, id)));
       }
       catch (NullPointerException nullPointerException) {
           ApiErrorReporter errorReporter = new ApiErrorReporter();
           errorReporter.reason = nullPointerException.getMessage();
           return badRequest(objectMapper.writeValueAsString(errorReporter));
       }
    }

    @ApiOperation(value = "get employee Attendance of all employees", httpMethod = "GET")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "All the attendance of Employee with id",
                    response = Attendance.class, responseContainer = "List"),

            @ApiResponse(code = 400, response = ApiErrorReporter.class, message = "No user found with token"),
            @ApiResponse(code = 500, response = ApiErrorReporter.class, message = "Unexpected Error")
    })
    public Result getEmployeeAttendanceMultiple(@ApiParam(value = "token of the admin", name = "token")String token) throws Exception {
        try {


            return ok(objectMapper.writeValueAsString(iAdmin.getEmployeeAttendance(token)));
        }
        catch (NullPointerException nullPointerException) {
            ApiErrorReporter errorReporter = new ApiErrorReporter();
            errorReporter.reason = nullPointerException.getMessage();
            return badRequest(objectMapper.writeValueAsString(errorReporter));
        }
    }




    @ApiOperation(value = "delete an employee employee", httpMethod = "GET")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Deleted Employee",
                    response = Employee.class),
            @ApiResponse(code = 400, message = "Not find employee with id"),

            @ApiResponse(code = 400, response = ApiErrorReporter.class, message = "No user found with token"),
            @ApiResponse(code = 500, response = ApiErrorReporter.class, message = "Unexpected Error")
    })
    public Result deleteEmployee(@ApiParam(value = "token of the admin", name = "token") String token, @ApiParam(value = "id of the employee", name = "id", type = "string") String id) throws Exception {
      try {


          Optional<Employee> optionalEmployee = iAdmin.deleteEmployee(id, token);
          if (optionalEmployee.isEmpty()) {
              throw new Exception("No Employee with id " + id + " found");
          }
          return ok(objectMapper.writeValueAsString(optionalEmployee.get()));
      }
      catch (Exception nullPointerException) {
          ApiErrorReporter errorReporter = new ApiErrorReporter();
          errorReporter.reason = nullPointerException.getMessage();
          return badRequest(objectMapper.writeValueAsString(errorReporter));
      }

    }




    @ApiOperation(value = "Update password", httpMethod = "POST")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, response = Admin.class, message = "Newly updated Admin"),
                    @ApiResponse(code = 400, response = ApiErrorReporter.class, message = "No user found with token"),
                    @ApiResponse(code = 500, response = ApiErrorReporter.class, message = "Unexpected Error")}
    )
    @ApiImplicitParams({

            @ApiImplicitParam(
                    name = "password",
                    value = "The new password",
                    paramType = "form",
                    required = true,
                    dataType = "string"
            ),

            @ApiImplicitParam(
                    name = "oldPassword",
                    value = "The old password",
                    paramType = "form",
                    required = true,
                    dataType = "string"
            )

    })
    public Result updatePassword(@ApiParam(name = "token", value = "token of the admin") String token)  throws JsonProcessingException, Exception {

        Form<PasswordForm> libraryForm = formFactory.form(PasswordForm.class).bindFromRequest();

        if (libraryForm.hasErrors()) {
            return badRequest(libraryForm.errorsAsJson());
        }
        try {


            Admin admin = null;
            Optional<Admin> optionalAdmin = iAdmin.updatePassword(token, libraryForm.get());
            if (optionalAdmin.isPresent()) admin = optionalAdmin.get();

            return ok(objectMapper.writeValueAsString(admin));
        }
        catch (NullPointerException nullPointerException) {
            ApiErrorReporter errorReporter = new ApiErrorReporter();
            errorReporter.reason = nullPointerException.getMessage();
            return badRequest(objectMapper.writeValueAsString(errorReporter));
        }

//          return  ok(libraryForm.get().toString());

    }
}
