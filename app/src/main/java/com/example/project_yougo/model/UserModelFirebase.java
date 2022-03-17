package com.example.project_yougo.model;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class UserModelFirebase {
    private FirebaseDatabase db;
    private DatabaseReference usersRef;



    public interface SignUpCompleteListener {
        // maybe add parameter(s) to specify reason
        void onSignupSuccessful();
        void onSignupFailed();
    }

    private static UserModelFirebase instance;

    UserModelFirebase() {
        db=FirebaseDatabase.getInstance();
        usersRef=db.getReference("users");
    }

    public static UserModelFirebase getInstance() {
        if(instance == null) {
            instance = new UserModelFirebase();
        }

        return instance;
    }

    public void signUpWithEmailAndPassword(Context context, FirebaseAuth firebaseAuth,
                                                                          String email, String password, String firstName,
                                                                          String lastName, String gender, String age,
                                                                          SignUpCompleteListener signUpCompleteListener) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    User user = new User(firebaseAuth.getUid(), email, firstName, lastName, age, gender,password);
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

    public void getUserById(String userId,UserModel.GetUserById listener){
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
    public void updateUser(String userId,String email,String password,String firstName,String lastName,
                           String gender,String age,UserModel.UpdateUser listener) {
        User user = new User(userId, email, firstName, lastName, age, gender, password);
//        HashMap map=new HashMap();
//        map.put("age",age);
//        map.put("email",email);
//        map.put("firstName",firstName);
//        map.put("gender",gender);
//        map.put("lastName",lastName);
//        map.put("password",password);
//        usersRef.child(userId).updateChildren(map).addOnCompleteListener(new OnCompleteListener() {
//            @Override
//            public void onComplete(@NonNull Task task) {
//                listener.onComplete();
//            }
//        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("users").child(userId).setValue(user);
    }

}
