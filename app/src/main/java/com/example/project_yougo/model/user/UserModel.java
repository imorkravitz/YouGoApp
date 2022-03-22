package com.example.project_yougo.model.user;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.project_yougo.model.firebase.FirebaseModel;
import com.example.project_yougo.model.firebase.FirebaseQueryLiveData;
import com.example.project_yougo.model.local.LocalDatabase;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserModel {
    private FirebaseDatabase db;
    private DatabaseReference usersRef;
    private FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    private Set<Integer> userListLivedataLifeCycleOwnerSet;

    public static class UserListDataSnapshotViewModel extends ViewModel {
        private final FirebaseQueryLiveData queryLiveData;

        public UserListDataSnapshotViewModel() {
            queryLiveData = new FirebaseQueryLiveData(
                    FirebaseModel.getInstance().getDatabaseReference().child("users")
            );
        }

        public LiveData<DataSnapshot> getSnapshotLiveData() {
            return queryLiveData;
        }
    }

    public interface SignInCompleteListener {
        void onSignInSuccessful();
        void onSignInFailed();
    }

    public interface SignUpCompleteListener {
        // maybe add parameter(s) to specify reason
        void onSignupSuccessful();
        void onSignupFailed();
    }

    public interface UserDeletionCompleteListener {
        // maybe add parameter(s) to specify reason
        void onDeletionSuccessful();
        void onDeletionFailed();
    }

//    public interface GetUserCompleteListener {
//        void onComplete(User user);
//    }

    public interface UpdateUserCompleteListener {
        void onUpdateSuccessful();
        void onUpdateFailed();
    }

    private static UserModel instance;


    public UserModel() {
        db=FirebaseDatabase.getInstance();
        usersRef=db.getReference("users");
        userListLivedataLifeCycleOwnerSet = new HashSet<>();
    }

    public static UserModel getInstance() {
        if(instance == null) {
            instance = new UserModel();
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
                    User user = new User(firebaseAuth.getUid(), email, firstName, lastName, age, gender, true);
                    DatabaseReference databaseReference = FirebaseModel.getInstance().getDatabaseReference();
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

    public String getUserEmail(){
        FirebaseUser user= FirebaseModel.getInstance().getFirebaseAuthInstance().getCurrentUser();
        return user.getEmail();
    }

    public LiveData<User> getUserLiveData(String uid, ViewModelStoreOwner viewModelStoreOwner,
                                                    LifecycleOwner lifecycleOwner) {

        if(!userListLivedataLifeCycleOwnerSet.contains(lifecycleOwner.hashCode())) {
            userListLivedataLifeCycleOwnerSet.add(lifecycleOwner.hashCode());


            UserListDataSnapshotViewModel viewModel
                    = new ViewModelProvider(viewModelStoreOwner)
                    .get(UserListDataSnapshotViewModel.class);
            Observer<DataSnapshot> observer = new Observer<DataSnapshot>() {
                @Override
                public void onChanged(DataSnapshot dataSnapshot) {
                    List<User> userList = new ArrayList<>();

                    for(DataSnapshot dsChild: dataSnapshot.getChildren()) {
                        userList.add(dsChild.getValue(User.class));
                    }

                    // cannot access db on UI thread
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            LocalDatabase.getInstance().userDao().insertAll(userList.toArray(new User[0]));
                        }
                    }).start();
                }
            };

            viewModel.getSnapshotLiveData().observe(lifecycleOwner, observer);
        }

        return LocalDatabase.getInstance().userDao().getById(uid);
    }

    public LiveData<List<User>> getUserListLiveData(ViewModelStoreOwner viewModelStoreOwner,
                                          LifecycleOwner lifecycleOwner) {
        if(!userListLivedataLifeCycleOwnerSet.contains(lifecycleOwner.hashCode())) {
            userListLivedataLifeCycleOwnerSet.add(lifecycleOwner.hashCode());

            UserListDataSnapshotViewModel viewModel
                    = new ViewModelProvider(viewModelStoreOwner)
                    .get(UserListDataSnapshotViewModel.class);
            Observer<DataSnapshot> observer = new Observer<DataSnapshot>() {
                @Override
                public void onChanged(DataSnapshot dataSnapshot) {
                    List<User> userList = new ArrayList<>();

                    for(DataSnapshot dsChild: dataSnapshot.getChildren()) {
                        userList.add(dsChild.getValue(User.class));
                    }

                    // cannot access db on UI thread
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            LocalDatabase.getInstance().userDao().insertAll(userList.toArray(new User[0]));
                        }
                    }).start();
                }
            };

            viewModel.getSnapshotLiveData().observe(lifecycleOwner, observer);
        }

        return LocalDatabase.getInstance().userDao().getAll();
    }
//
//    public void getUserById(String userId,GetUserCompleteListener listener){
//        usersRef.child(userId).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()){
//                    User user=snapshot.getValue(User.class);
//                    listener.onComplete(user);
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) { }
//        });
//    }

//    public void deleteUser(UserDeletionCompleteListener deletionCompleteListener) {
//        String uid = FirebaseModel.getInstance().getFirebaseAuthInstance().getUid();
//        FirebaseAuth.getInstance().getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if(task.isSuccessful()) {
//                    FirebaseModel.getInstance().getDatabaseReference().child("users")
//                            .child(uid).removeValue();
//                    deletionCompleteListener.onDeletionSuccessful();
//                } else {
//                    deletionCompleteListener.onDeletionFailed();
//                }
//            }
//        });
//    }
//    public void updateUserPassword(String password, UpdateUserCompleteListener completeListener){
//        FirebaseUser userAuth = FirebaseModel.getInstance().getFirebaseAuthInstance().getCurrentUser();
//        userAuth.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if(task.isSuccessful()){
//                    completeListener.onUpdateSuccessful();
//                } else {
//                    completeListener.onUpdateFailed();
//                }
//            }
//        });
//    }
//
//    public void updateUserEmail(String email, UpdateUserCompleteListener completeListener){
//        FirebaseUser userAuth = FirebaseAuth.getInstance().getCurrentUser();
//        userAuth.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if(task.isSuccessful()){
//                    completeListener.onUpdateSuccessful();
//                } else {
//                    completeListener.onUpdateFailed();
//                }
//            }
//        });
//    }

    public void updateUser(String userId,String email,String password,String firstName,String lastName,
                           String gender,String age, boolean active, UpdateUserCompleteListener listener) {
        User user = new User(userId, email, firstName, lastName, age, gender, active);
        DatabaseReference databaseReference = FirebaseModel.getInstance().getDatabaseReference();

        databaseReference.child("users").child(userId).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    FirebaseModel.getInstance().getFirebaseAuthInstance().getCurrentUser().updatePassword(password).addOnCompleteListener(
                            new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        FirebaseModel.getInstance().getFirebaseAuthInstance().getCurrentUser().updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()) {
                                                    listener.onUpdateSuccessful();
                                                }
                                                else
                                                {
                                                    listener.onUpdateFailed();
                                                }
                                            }
                                        });
                                    } else {
                                        listener.onUpdateFailed();
                                    }
                                }
                            }
                    );
                    listener.onUpdateSuccessful();
                } else {
                    listener.onUpdateFailed();
                }
            }
        });
    }

    public void updateUserWithUrl(String userId,String email,String password,String firstName,String lastName,
                           String gender,String age, String url,boolean active, UpdateUserCompleteListener listener) {
        User user = new User(userId, email, firstName, lastName, age, gender,url,active);
        DatabaseReference databaseReference = FirebaseModel.getInstance().getDatabaseReference();

        databaseReference.child("users").child(userId).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    FirebaseModel.getInstance().getFirebaseAuthInstance().getCurrentUser().updatePassword(password).addOnCompleteListener(
                            new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        FirebaseModel.getInstance().getFirebaseAuthInstance().getCurrentUser().updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()) {
                                                    listener.onUpdateSuccessful();
                                                }
                                                else
                                                {
                                                    listener.onUpdateFailed();
                                                }
                                            }
                                        });
                                    } else {
                                        listener.onUpdateFailed();
                                    }
                                }
                            }
                    );
                    listener.onUpdateSuccessful();
                } else {
                    listener.onUpdateFailed();
                }
            }
        });
    }


    public void login(String email, String password, SignInCompleteListener signInCompleteListener) {
        DatabaseReference databaseReference = FirebaseModel.getInstance().getDatabaseReference();
        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean active = true;

                for(DataSnapshot dataSnapshotChild : snapshot.getChildren()) {
                    User user = dataSnapshotChild.getValue(User.class);

                    if(user.getEmail().equals(email)) {
                        if (!user.isActive()) {
                            active = false;
                        }

                        break;
                    }
                }

                if(active) {
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
                } else {
                    signInCompleteListener.onSignInFailed();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                signInCompleteListener.onSignInFailed();
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
        return FirebaseModel.getInstance().getFirebaseAuthInstance().getCurrentUser() != null;
    }

    public void logOut() {
        FirebaseModel.getInstance().getFirebaseAuthInstance().signOut();
    }

    FirebaseStorage storage = FirebaseStorage.getInstance();
    public void saveImage1(Bitmap imageBitmap, String imageName, saveImageListener listener) {
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();

        // Create a reference to "mountains.jpg"
        StorageReference imgUserRef = storageRef.child("/user_avatars/" + imageName);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imgUserRef.putBytes(data);
        uploadTask.addOnFailureListener(exception -> {
            listener.onComplete(null);
        }).addOnSuccessListener(taskSnapshot -> {
            imgUserRef.getDownloadUrl().addOnSuccessListener(uri -> {
                Uri downloadUrl = uri;
                listener.onComplete(downloadUrl.toString());
            });
        });
    }

    public interface saveImageListener{
        void onComplete(String url);
    }
    public void saveImage(Bitmap imageBitmap, String imageName,saveImageListener listener) {
        saveImage1(imageBitmap,imageName,listener);
    }

}
