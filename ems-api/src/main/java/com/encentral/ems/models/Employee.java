package com.encentral.ems.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;
import java.util.Locale;
import java.util.Random;

public class Employee implements IEmsModel {
    private String id;

    private String firstName;
    private String lastName;

    private String email;

    private String password;
    private String token;

    @JsonIgnore
    private List<Attendance> attendanceList;

    public void setId(String id) {
        this.id = id;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email.toLowerCase(Locale.ROOT);
    }

    public void setLastName(String lastName) {
        this.lastName = lastName.toLowerCase(Locale.ROOT);
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName.toLowerCase(Locale.ROOT);
    }

    public String getToken() {
        return token;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }
    
    public static String generatePin(int count) {
        Random rand = new Random();
        StringBuilder builder = new StringBuilder();
        int upperbound = 10;
        for (int i = 0; i < count; i++) {

            builder.append(rand.nextInt(upperbound));
        }
        return builder.toString();
    }

}
