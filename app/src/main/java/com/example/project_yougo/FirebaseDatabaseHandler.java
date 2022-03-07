package com.example.project_yougo;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.project_yougo.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseDatabaseHandler {
    public interface SignUpCompleteListener {
        // maybe add parameter(s) to specify reason
        void onSignupSuccessful();
        void onSignupFailed();
    }

    private static FirebaseDatabaseHandler instance;

    private FirebaseDatabaseHandler() {

    }

    public static FirebaseDatabaseHandler getInstance() {
        if(instance == null) {
            instance = new FirebaseDatabaseHandler();
        }

        return instance;
    }

    public void signUpWithEmailAndPassword(Context context, FirebaseAuth firebaseAuth,
                                            String email, String password, String firstName,
                                            String lastName,
                                            SignUpCompleteListener signUpCompleteListener) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    User user = new User(firebaseAuth.getUid(), email, firstName, lastName);
                    DatabaseReference databaseReference = getDatabaseReference();
                    databaseReference.child("users").child(user.getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                // registered in auth database, extra data in database
                                signUpCompleteListener.onSignupSuccessful();
                            } else {
                                firebaseAuth.getCurrentUser().delete(); // remove user from auth database
                                signUpCompleteListener.onSignupFailed();
                            }
                        }
                    });
                } else {
                    signUpCompleteListener.onSignupFailed();
                    // no internet conection or duplicate email or password length < 6

                }
            }
        });
    }

    private DatabaseReference getDatabaseReference() {
        return FirebaseDatabase.getInstance("https://yougoapp-50cbe-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
    }
}
