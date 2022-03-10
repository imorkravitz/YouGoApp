package com.example.project_yougo.model.post;

import com.example.project_yougo.model.FirebaseDatabaseHandler;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;

import java.util.List;

public class PostModel {
    public static PostModel instance;

    private PostModel() {

    }

    public static PostModel getInstance() {
        if(instance == null) {
            instance = new PostModel();
        }

        return instance;
    }

    public void addPost(String description, String publisherId) {
        DatabaseReference databaseReference = FirebaseDatabaseHandler.getInstance().getDatabaseReference();
        String postId = databaseReference.child("posts").push().getKey();
        Post post = new Post(postId, publisherId, description, ServerValue.TIMESTAMP);
        databaseReference.child("posts").child(postId).setValue(post);
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
