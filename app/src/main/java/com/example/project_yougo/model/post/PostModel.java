package com.example.project_yougo.model.post;

import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.project_yougo.model.firebase.FirebaseModel;
import com.example.project_yougo.model.firebase.FirebaseQueryLiveData;
import com.example.project_yougo.model.local.LocalDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import androidx.lifecycle.Observer;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


public class PostModel {
    public interface PostCreationListener {
        void onCreationSuccess();
        void onCreationFailed();
    }

//    public interface PostListUpdateListener {
//        void onPostListUpdated(List<Post> postList);
//    }

    public static class PostListDataSnapshotViewModel extends ViewModel {
        private final FirebaseQueryLiveData queryLiveData;

        public PostListDataSnapshotViewModel() {
            queryLiveData = new FirebaseQueryLiveData(
                    FirebaseModel.getInstance().getDatabaseReference().child("posts")
            );
        }

        public LiveData<DataSnapshot> getSnapshotLiveData() {
            return queryLiveData;
        }
    }

//    public interface PostListLoadListener {
//        void onPostListLoaded(List<Post> postList);
//    }

    public static PostModel instance;

    private PostModel() {

    }

    public static PostModel getInstance() {
        if(instance == null) {
            instance = new PostModel();
        }

        return instance;
    }

//    public void loadPostList(Context appContext, PostListLoadListener postListLoadListener) {
//        // cannot access db on UI thread
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                List<Post> postList = LocalDatabase.getInstance(appContext).postDao().getAll();
//                postListLoadListener.onPostListLoaded(postList);
//            }
//        }).start();
//    }


    public LiveData<List<Post>> getPostListLiveData(ViewModelStoreOwner viewModelStoreOwner,
                                                    LifecycleOwner lifecycleOwner) {
        PostListDataSnapshotViewModel viewModel
                = new ViewModelProvider(viewModelStoreOwner)
                    .get(PostListDataSnapshotViewModel.class);
        Observer<DataSnapshot> observer = new Observer<DataSnapshot>() {
            @Override
            public void onChanged(DataSnapshot dataSnapshot) {
                List<Post> postList = new ArrayList<>();

                for(DataSnapshot dsChild: dataSnapshot.getChildren()) {
                    postList.add(dsChild.getValue(Post.class));
                }

                // cannot access db on UI thread
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        LocalDatabase.getInstance().postDao().insertAll(postList.toArray(new Post[0]));
                    }
                }).start();
            }
        };

        viewModel.getSnapshotLiveData().observe(lifecycleOwner, observer);

        return LocalDatabase.getInstance().postDao().getAll();
    }
//
//
//    public void listenForPostListUpdates(Context appContext,
//                                         PostListUpdateListener postListUpdateListener) {
//        DatabaseReference databaseReference = UserModelFirebase.getInstance().getDatabaseReference();;
//
//        databaseReference.child("posts").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                List<Post> postList = new ArrayList<>();
//
//                for(DataSnapshot dsChild : snapshot.getChildren()) {
//                    postList.add(dsChild.getValue(Post.class));
//                }
//
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        LocalDatabase.getInstance(appContext).postDao().insertAll(postList.toArray(new Post[0]));
//                    }
//                }).start();
//                postListUpdateListener.onPostListUpdated(postList);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

    public void addPost(String freeText, String difficulty, String typeOfWorkout,
                        String publisherId, PostCreationListener creationListener) {
        DatabaseReference databaseReference = FirebaseModel.getInstance().getDatabaseReference();

        DatabaseReference timestampReference = databaseReference.child("timestamp");
        timestampReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // timestamp set
                long timestamp = Long.parseLong(snapshot.getValue().toString());
                String postId = databaseReference.child("posts").push().getKey();
                Post post = new Post(postId, publisherId, freeText, difficulty, typeOfWorkout, timestamp);
                databaseReference.child("posts").child(postId).setValue(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            creationListener.onCreationSuccess();
                        } else {
                            creationListener.onCreationFailed();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        timestampReference.setValue(ServerValue.TIMESTAMP);
    }

    public void addPostWithImg(String freeText, String difficulty, String typeOfWorkout,
                               String publisherId,String url ,PostCreationListener creationListener) {
        DatabaseReference databaseReference = FirebaseModel.getInstance().getDatabaseReference();

        DatabaseReference timestampReference = databaseReference.child("timestamp");
        timestampReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // timestamp set
                long timestamp = Long.parseLong(snapshot.getValue().toString());
                String postId = databaseReference.child("posts").push().getKey();
                Post post = new Post(postId, publisherId, freeText, difficulty, typeOfWorkout, timestamp,url);
                databaseReference.child("posts").child(postId).setValue(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            creationListener.onCreationSuccess();
                        } else {
                            creationListener.onCreationFailed();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        timestampReference.setValue(ServerValue.TIMESTAMP);
    }

    /**
     * Storage implementation
     */
    FirebaseStorage storage = FirebaseStorage.getInstance();
    public void saveImagePost(Bitmap imageBitmap, String imageName, saveImageListener listener) {
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();

        // Create a reference to "mountains.jpg"
        StorageReference imgPostRef = storageRef.child("/post_images/" + imageName);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imgPostRef.putBytes(data);
        uploadTask.addOnFailureListener(exception -> {
            listener.onComplete(null);
        }).addOnSuccessListener(taskSnapshot -> {
            imgPostRef.getDownloadUrl().addOnSuccessListener(uri -> {
                Uri downloadUrl = uri;
                listener.onComplete(downloadUrl.toString());
            });
        });
    }

    public interface saveImageListener{
        void onComplete(String url);
    }
    public void saveImage(Bitmap imageBitmap, String imageName, saveImageListener listener) {
        saveImagePost(imageBitmap,imageName,listener);
    }

//    PostModelFirebase PostModelFirebase = new PostModelFirebase();
//    private PostModel(){ }
//
//    public interface GetAllPostsListener{
//        void onComplete(List<Post> list);
//    }
//
//    public void getAllPosts(PostModel.GetAllPostsListener listener){
//        PostModelFirebase.getAllPosts(listener);
//    }
//
//    public interface AddPostListener{
//        void onComplete();
//    }
//
//    public void addPost(Post post, PostModel.AddPostListener listener){
//        PostModelFirebase.addPost( post,  listener);
//    }
//
//    public interface GetPostByPublisherId{
//        void onComplete(Post post);
//    }
//    public Post GetPostByPublisherId(String publisherId, PostModel.GetPostByPublisherId listener) {
//        PostModelFirebase.GetPostByPublisherId(publisherId,listener);
//        return null;
//    }
}
