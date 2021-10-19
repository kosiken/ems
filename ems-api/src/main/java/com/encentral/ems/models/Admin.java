package com.encentral.ems.models;

import java.util.Locale;

public class Admin implements IEmsModel {

    private String id;

    private String name;

    private String email;

    private String password;
    private String token;

    public Admin() {

    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email.toLowerCase(Locale.ROOT);
    }

    public void setName(String name) {
        this.name = name.toLowerCase(Locale.ROOT);
    }


    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password.trim();
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }


    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
