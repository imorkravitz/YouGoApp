package com.example.project_yougo.model.post;

import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.project_yougo.feed.EditPostFragment;
import com.example.project_yougo.feed.PostListViewModel;
import com.example.project_yougo.model.comment.CommentModel;
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
    PostListViewModel postListViewModel;

    public interface PostCreationListener {
        void onCreationSuccess();

        void onCreationFailed();
    }

    public interface PostDeletionListener {
        void onDeletionSuccess();

        void onDeletionFailed();
    }

    public interface UpdatePostCompleteListener {
        void onUpdateSuccessful();

        void onUpdateFailed();
    }

    public interface DeletePostCompleteListener {
        void onDeleteSuccessful();

        void onDeleteFailed();
    }

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


    public static PostModel instance;

    private PostModel() {

    }

    public static PostModel getInstance() {
        if (instance == null) {
            instance = new PostModel();
        }

        return instance;
    }

    public LiveData<List<Post>> getPostListLiveData(ViewModelStoreOwner viewModelStoreOwner,
                                                    LifecycleOwner lifecycleOwner) {
        PostListDataSnapshotViewModel viewModel
                = new ViewModelProvider(viewModelStoreOwner)
                .get(PostListDataSnapshotViewModel.class);
        Observer<DataSnapshot> observer = new Observer<DataSnapshot>() {
            @Override
            public void onChanged(DataSnapshot dataSnapshot) {
                List<Post> postList = new ArrayList<>();

                for (DataSnapshot dsChild : dataSnapshot.getChildren()) {
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

    public void deletePostById(String postId, String publisherId, PostDeletionListener listener) {
        DatabaseReference databaseReference = FirebaseModel.getInstance().getDatabaseReference();

        databaseReference.child("posts").child(postId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    LocalDatabase.getInstance().postDao().deleteById(postId);
                    CommentModel.getInstance().deleteCommentsByPostId(postId, new CommentModel.CommentDeletionListener() {
                        @Override
                        public void onDeletionSuccess() {

                            listener.onDeletionSuccess();
                        }

                        @Override
                        public void onDeletionFailed() {
                            listener.onDeletionFailed();
                        }
                    });
                } else {
                    listener.onDeletionFailed();
                    ;
                }
            }
        });
    }


    public void addPost(String freeText, String difficulty, String typeOfWorkout,
                        String publisherId, double longitude, double latitude, PostCreationListener creationListener) {
        DatabaseReference databaseReference = FirebaseModel.getInstance().getDatabaseReference();

        DatabaseReference timestampReference = databaseReference.child("timestamp");
        timestampReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // timestamp set
                long timestamp = Long.parseLong(snapshot.getValue().toString());
                String postId = databaseReference.child("posts").push().getKey();
                Post post = new Post(postId, publisherId, freeText, difficulty, typeOfWorkout, longitude, latitude, timestamp);
                databaseReference.child("posts").child(postId).setValue(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
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
                               String publisherId, String url, double longitude, double latitude, PostCreationListener creationListener) {
        DatabaseReference databaseReference = FirebaseModel.getInstance().getDatabaseReference();

        DatabaseReference timestampReference = databaseReference.child("timestamp");
        timestampReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // timestamp set
                long timestamp = Long.parseLong(snapshot.getValue().toString());
                String postId = databaseReference.child("posts").push().getKey();
                Post post = new Post(postId, publisherId, freeText, difficulty, typeOfWorkout, timestamp, longitude, latitude, url);
                databaseReference.child("posts").child(postId).setValue(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
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

    public interface saveImageListener {
        void onComplete(String url);
    }

    public void saveImage(Bitmap imageBitmap, String imageName, saveImageListener listener) {
        saveImagePost(imageBitmap, imageName, listener);
    }

    public void updatePost(String postId, String publisherId, String freeText, String two, String diff, double longitude, double latitude, UpdatePostCompleteListener updatePostCompleteListener) {
        DatabaseReference databaseReference = FirebaseModel.getInstance().getDatabaseReference();

        DatabaseReference timestampReference = databaseReference.child("timestamp");
        timestampReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long timestamp = Long.parseLong(snapshot.getValue().toString());
                Post post = new Post(postId, publisherId, freeText, two, diff, longitude, latitude, timestamp);
                String currentUser = FirebaseModel.getInstance().getFirebaseAuthInstance().getCurrentUser().getUid();
                if (publisherId.equals(currentUser)) {
                    databaseReference.child("posts").child(postId).setValue(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                updatePostCompleteListener.onUpdateSuccessful();
                            }
                        }
                    });
                } else {
                    updatePostCompleteListener.onUpdateFailed();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        timestampReference.setValue(ServerValue.TIMESTAMP);
    }

    public void deletePost(String postId, String publisherId, Post post, DeletePostCompleteListener deletePostCompleteListener) {
        List<Post> newPosts = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseModel.getInstance().getDatabaseReference();
        String currentUser = FirebaseModel.getInstance().getFirebaseAuthInstance().getCurrentUser().getUid();
        if (publisherId.equals(currentUser)) {
            databaseReference.child("comments").child(postId).removeValue();
            databaseReference.child("posts").child(postId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        deletePostCompleteListener.onDeleteSuccessful();
                    }
                }
            });
        } else {
            deletePostCompleteListener.onDeleteFailed();
        }
    }
}