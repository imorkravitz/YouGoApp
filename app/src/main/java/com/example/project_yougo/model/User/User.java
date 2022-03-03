package com.example.project_yougo.model.User;

import androidx.annotation.NonNull;
import androidx.room.PrimaryKey;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {

    final public static String COLLECTION_NAME = "users";
    String firstName="";
    String lastName="";
    String gender="";
    String email="";
    String id="";
    List<String> friends;//identify by user id
    int age;



    public User(String firstName, String lastName, String gender, String email, int age, String id, List<String> friends) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.email = email;
        this.age = age;
        this.id = id;
        this.friends=friends;
    }

    public User() {}

    public String getFirstName() {return firstName; }

    public void setFirstName(String firstName) {this.firstName = firstName; }

    public String getLastName() { return lastName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getGender() { return gender; }

    public void setGender(String gender) { this.gender = gender; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email;}

    public String getId() {return id;}

    public void setId(String id) {this.id = id;}

    public int getAge() {return age;}

    public void setAge(int age) {this.age = age;}

    public List<String> getFriends() {return friends; }

    public void setFriends(List<String> friends) { this.friends = friends; }

    public Map<String, Object> toJson() {
        Map<String, Object> json=new HashMap<String, Object>();
        json.put("id",id);
        json.put("first name",firstName);
        json.put("last name",lastName);
        json.put("gender",gender);
        json.put("email",email);
        json.put("age",age);
        json.put("friends",friends);
        return json;
    }
    public static User create(Map<String, Object> json) {
        String firstName= (String) json.get("first name");
        String lastName=(String) json.get("last name");
        String gender=(String) json.get("gender");
        String email=(String) json.get("email");
        String id=(String) json.get("id");
        List<String> friendsId= (List<String>) json.get("friends");
        int age= (int) json.get("age");
        User user=new User(firstName,lastName,gender,email,age,id,friendsId);
        return user;
    }

}
