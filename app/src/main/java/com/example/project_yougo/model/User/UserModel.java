package com.example.project_yougo.model.User;

import java.util.List;

public class UserModel {
    public static final UserModel instance = new UserModel();

    UserModelFirebase modelFirebase = new UserModelFirebase();
    private UserModel(){ }

    public interface GetAllUsersListener{
        void onComplete(List<User> list);
    }

    public void getAllUsers(GetAllUsersListener listener){
        modelFirebase.getAllUsers(listener);
    }

    public interface AddUserListener{
        void onComplete();
    }

    public void addUser(User student, AddUserListener listener){
        modelFirebase.addUser( student,  listener);
    }

    public interface GetUserById{
        void onComplete(User user);
    }
    public User getUserById(String userId, GetUserById listener) {
        modelFirebase.getUserById(userId,listener);
        return null;
    }

}
