package com.example.project_yougo.model.user;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class UserModelFirebase {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public void getAllUsers(UserModel.GetAllUsersListener listener) {
            db.collection(User.COLLECTION_NAME)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            List<User> list=new LinkedList<>();
                            if(task.isSuccessful()){
                                QuerySnapshot querySnapshot = task.getResult();
                                for(QueryDocumentSnapshot doc: querySnapshot){
                                    Map<String,Object> json=doc.getData();
                                    User user=User.create(json);
                                    if(user!=null){
                                        list.add(user);
                                    }
                                }
                            }
                            listener.onComplete(list);
                        }
                    });
    }

    public void addUser(User user, UserModel.AddUserListener listener) {
        // Create a new user with a first and last name
        Map<String, Object> json=user.toJson();

        db.collection(User.COLLECTION_NAME)
                .document(user.getId())
                .set(json)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        listener.onComplete();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onComplete();
                    }
                });
    }

    public void getUserById(String userId, UserModel.GetUserById listener) {
        db.collection(User.COLLECTION_NAME)
                .document(userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        User user=null;
                        if(task.isSuccessful() & task.getResult()!=null){
                            user=User.create(task.getResult().getData());
                        }
                        listener.onComplete(user);
                    }
                });

    }
}
