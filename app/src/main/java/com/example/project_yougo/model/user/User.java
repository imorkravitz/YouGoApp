package com.example.project_yougo.model.user;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User {
    @PrimaryKey
    @NotNull
    private String uid;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "firstName")
    private String firstName;


    @ColumnInfo(name = "lastName")
    private String lastName;

    @ColumnInfo(name = "gender")
    private String gender;

    @ColumnInfo(name = "age")
    private String age;

    @ColumnInfo(name = "active")
    private boolean active;

    @ColumnInfo(name = "imageUrl")
    private String imageUrl;

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
        this.active = true;
    }
    public User(String uid, String email, String firstName, String lastName,String age, String gender,
                boolean active) {
        this.uid = uid;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age=age;
        this.gender=gender;
        this.active = active;
    }

    public User(String uid, String email, String firstName, String lastName,String age, String gender,
                String imageUrl,boolean active) {
        this.uid = uid;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age=age;
        this.gender=gender;
        this.active = active;
        this.imageUrl = imageUrl;
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

    public void setUid(@NotNull String uid) {
        this.uid = uid;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String fullname() {
        return firstName + " " + lastName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
