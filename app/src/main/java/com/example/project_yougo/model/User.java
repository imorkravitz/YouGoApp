package com.example.project_yougo.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String uid;
    private String email;
    private String firstName;
    private String lastName;
    private List<String> friendIds;

    public User(String uid, String email, String firstName, String lastName) {
        this.uid = uid;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.friendIds = new ArrayList<>();
    }

    // for firebase
    public User() {

    }

    public String getUid() {
        return uid;
    }

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
