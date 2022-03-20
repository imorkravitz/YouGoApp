package com.example.project_yougo.model.user;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String uid;
    private String email;
    private String firstName;
    private String lastName;
    private String gender;
    private String age;
    private List<String> friendIds;

    public String getGender() {
        return gender;
    }

    public String getAge() {
        return age;
    }

    public User(String uid, String email, String firstName, String lastName) {
        this.uid = uid;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.friendIds = new ArrayList<>();
    }
    public User(String uid, String email, String firstName, String lastName,String age, String gender) {
        this.uid = uid;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age=age;
        this.gender=gender;
        this.friendIds = new ArrayList<>();
    }

    // for firebase

    public User() { }

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

    public String fullname() {
        return firstName + " " + lastName;
    }
}
