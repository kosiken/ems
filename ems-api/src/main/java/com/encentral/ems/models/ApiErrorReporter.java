package com.encentral.ems.models;

public class ApiErrorReporter {
    public  Integer code = 400;
    public String reason;

    @Override
    public String toString() {
        return "{" +
                "\"code\": " + code +
                ", \"reason\": \"" + reason + '"' +
                '}';
    }
}
