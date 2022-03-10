package com.example.project_yougo.model;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AuthenticationHandler {
    public interface SignInCompleteListener {
        void onSignInSuccessful();
        void onSignInFailed();
    }

    private static AuthenticationHandler instance;

    private final FirebaseAuth firebaseAuth;

    private AuthenticationHandler() {
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    public static AuthenticationHandler getInstance() {
        if(instance == null) {
            instance = new AuthenticationHandler();
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

    public boolean isLoggedIn() {
        return firebaseAuth.getCurrentUser() != null;
    }
}
