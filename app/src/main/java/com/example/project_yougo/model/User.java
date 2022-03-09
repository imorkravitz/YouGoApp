package com.example.project_yougo.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String uid;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private List<String> friendIds;

    public User(String uid, String email, String firstName, String lastName,String password) {
        this.uid = uid;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password=password;
        this.friendIds = new ArrayList<>();
    }

    // for firebase

    public User() {

    }
    public String getUid() {
        return uid;
    }

    public String getPassword() {return password; }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public List<String> getFriendIds() {
        return friendIds;
    }
}
