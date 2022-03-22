package com.example.project_yougo.feed;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.project_yougo.model.post.Post;
import com.example.project_yougo.model.post.PostModel;

import java.util.List;

public class PostListViewModel extends ViewModel {
    private LiveData<List<Post>> postListLiveData;

    public LiveData<List<Post>> getPostListLiveData(LifecycleOwner lifecycleOwner,
                                                    ViewModelStoreOwner viewModelStoreOwner) {
        if(postListLiveData == null)
            postListLiveData = PostModel.getInstance().getPostListLiveData(viewModelStoreOwner,
                    lifecycleOwner);
        return postListLiveData;
    }
}
