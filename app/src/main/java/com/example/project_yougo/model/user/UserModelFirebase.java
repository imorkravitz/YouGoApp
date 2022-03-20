package com.example.project_yougo.model.user;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserModelFirebase {
    private FirebaseDatabase db;
    private DatabaseReference usersRef;
    private FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();



    public interface SignInCompleteListener {
        void onSignInSuccessful();
        void onSignInFailed();
    }

    public interface SignUpCompleteListener {
        // maybe add parameter(s) to specify reason
        void onSignupSuccessful();
        void onSignupFailed();
    }

    public interface GetUserCompleteListener {
        void onComplete(User user);
    }

    public interface UpdateUserCompleteListener {
        void onComplete(User user);
    }

    private static UserModelFirebase instance;

    public UserModelFirebase() {
        db=FirebaseDatabase.getInstance();
        usersRef=db.getReference("users");
    }

    public static UserModelFirebase getInstance() {
        if(instance == null) {
            instance = new UserModelFirebase();
        }

        return instance;
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getUid();
    }

    public void signUpWithEmailAndPassword(Context context,String email, String password, String firstName,
                                                                          String lastName, String gender, String age,
                                                                          SignUpCompleteListener signUpCompleteListener) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    User user = new User(firebaseAuth.getUid(), email, firstName, lastName, age, gender);
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
                    // no internet connection or duplicate email or password length < 6

                }
            }
        });
    }
    public DatabaseReference getDatabaseReference() {
        return FirebaseDatabase.getInstance("https://yougoapp-50cbe-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
    }
    public String getUserEmail(){
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        return user.getEmail();
    }

    public void getUserById(String userId,GetUserCompleteListener listener){
        usersRef.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    User user=snapshot.getValue(User.class);
                    listener.onComplete(user);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }
    public void deleteUser() {
        //FirebaseUser userAuth = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseAuth.getInstance().getCurrentUser().delete();
    }
    public void updateUserPassword(String password){
        FirebaseUser userAuth = FirebaseAuth.getInstance().getCurrentUser();
        userAuth.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d("TAG","User password is updated");
                }
            }
        });

    }
    public void updateUserEmail(String email){
        FirebaseUser userAuth = FirebaseAuth.getInstance().getCurrentUser();
        userAuth.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d("TAG","User email is updated");
                }
            }
        });
    }

    public void updateUser(String userId,String email,String password,String firstName,String lastName,
                           String gender,String age, UpdateUserCompleteListener listener) {
        User user = new User(userId, email, firstName, lastName, age, gender);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("users").child(userId).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    listener.onComplete(user);
                } else {
                    listener.onComplete(null);
                }
            }
        });
    }


    public void login(String email, String password, SignInCompleteListener signInCompleteListener) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
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


//    public User getUserById(GetUserById listener){
//        String userId=firebaseAuth.getCurrentUser().getUid();
//        userModelFirebase.getUserById(userId,listener);
//        return null;
//    }
//    public interface UpdateUser{
//        void onComplete();
//    }
//    public void updateUser(String email,String password,String firstName,String lastName,
//                           String gender,String age,UpdateUser listener){
//        String userId=firebaseAuth.getCurrentUser().getUid();
//        userModelFirebase.updateUser(userId,email,password,firstName,lastName,gender,age,listener);
//
//    }
    public boolean isLoggedIn() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

}
