package com.example.project_yougo.model.post;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.project_yougo.model.user.UserModelFirebase;
import com.example.project_yougo.model.local.LocalDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PostModel {
    public interface PostCreationListener {
        void onCreationSuccess();
        void onCreationFailed();
    }

    public interface PostListUpdateListener {
        void onPostListUpdated(List<Post> postList);
    }

    public interface PostListLoadListener {
        void onPostListLoaded(List<Post> postList);
    }

    public static PostModel instance;

    private PostModel() {

    }

    public static PostModel getInstance() {
        if(instance == null) {
            instance = new PostModel();
        }

        return instance;
    }

    public void loadPostList(Context appContext, PostListLoadListener postListLoadListener) {
        // cannot access db on UI thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Post> postList = LocalDatabase.getInstance(appContext).postDao().getAll();
                postListLoadListener.onPostListLoaded(postList);
            }
        }).start();
    }

    public void listenForPostListUpdates(Context appContext,
                                         PostListUpdateListener postListUpdateListener) {
        DatabaseReference databaseReference = UserModelFirebase.getInstance().getDatabaseReference();;

        databaseReference.child("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Post> postList = new ArrayList<>();

                for(DataSnapshot dsChild : snapshot.getChildren()) {
                    postList.add(dsChild.getValue(Post.class));
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        LocalDatabase.getInstance(appContext).postDao().insertAll(postList.toArray(new Post[0]));
                    }
                }).start();
                postListUpdateListener.onPostListUpdated(postList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void addPost(String freeText, String difficulty, String typeOfWorkout,
                        String publisherId, PostCreationListener creationListener) {
        DatabaseReference databaseReference = UserModelFirebase.getInstance().getDatabaseReference();

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
