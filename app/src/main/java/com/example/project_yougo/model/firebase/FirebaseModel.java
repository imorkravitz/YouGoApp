package com.example.project_yougo.model.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseModel {
    private static FirebaseModel instance;

    private FirebaseModel() {

    }

    public static FirebaseModel getInstance() {
        if(instance == null)
            instance = new FirebaseModel();

        return instance;
    }

    public DatabaseReference getDatabaseReference() {
        return FirebaseDatabase.getInstance("https://yougoapp-50cbe-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
    }

    public FirebaseAuth getFirebaseAuthInstance() {
        return FirebaseAuth.getInstance();
    }
}
