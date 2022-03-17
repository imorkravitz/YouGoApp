package com.example.project_yougo.model;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserModel {
    public interface SignInCompleteListener {
        void onSignInSuccessful();
        void onSignInFailed();
    }

    private static UserModel instance;

    private final FirebaseAuth firebaseAuth;
    UserModelFirebase userModelFirebase=new UserModelFirebase();

    private UserModel() {
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    public static UserModel getInstance() {
        if(instance == null) {
            instance = new UserModel();
        }

        return instance;
    }

    public void login(String email, String password, SignInCompleteListener signInCompleteListener) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    // signed in
                    signInCompleteListener.onSignInSuccessful();
                } else {
                    // not signed in
                    signInCompleteListener.onSignInFailed();
                }
            }
        });
    }
    public interface GetUserById{
        void onComplete(User user);
    }

    public User getUserById(GetUserById listener){
        String userId=firebaseAuth.getCurrentUser().getUid();
        userModelFirebase.getUserById(userId,listener);
        return null;
    }
    public interface UpdateUser{
        void onComplete();
    }
    public void updateUser(String email,String password,String firstName,String lastName,
                           String gender,String age,UpdateUser listener){
        String userId=firebaseAuth.getCurrentUser().getUid();
        userModelFirebase.updateUser(userId,email,password,firstName,lastName,gender,age,listener);

    }
    public boolean isLoggedIn() {
        return firebaseAuth.getCurrentUser() != null;
    }
}
