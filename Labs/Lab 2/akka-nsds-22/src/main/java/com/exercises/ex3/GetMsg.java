package com.exercises.ex3;

public class GetMsg {
    private String name;
    private String email;
    public GetMsg(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}
