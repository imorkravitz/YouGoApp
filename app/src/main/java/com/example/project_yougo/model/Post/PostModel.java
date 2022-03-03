package com.example.project_yougo.model.Post;

import com.example.project_yougo.model.User.User;
import com.example.project_yougo.model.User.UserModel;
import com.example.project_yougo.model.User.UserModelFirebase;

import java.util.List;

public class PostModel {
    public static final PostModel instance = new PostModel();

    PostModelFirebase PostModelFirebase = new PostModelFirebase();
    private PostModel(){ }

    public interface GetAllPostsListener{
        void onComplete(List<Post> list);
    }

    public void getAllPosts(PostModel.GetAllPostsListener listener){
        PostModelFirebase.getAllPosts(listener);
    }

    public interface AddPostListener{
        void onComplete();
    }

    public void addPost(Post post, PostModel.AddPostListener listener){
        PostModelFirebase.addPost( post,  listener);
    }

    public interface GetPostByPublisherId{
        void onComplete(Post post);
    }
    public Post GetPostByPublisherId(String publisherId, PostModel.GetPostByPublisherId listener) {
        PostModelFirebase.GetPostByPublisherId(publisherId,listener);
        return null;
    }
}
