package com.example.project_yougo.feed;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelStoreOwner;
import com.example.project_yougo.model.user.User;
import com.example.project_yougo.model.user.UserModel;

public class UserViewModel extends ViewModel {
    private LiveData<User> userLiveData;

    public LiveData<User> getUserLiveData(String uid, LifecycleOwner lifecycleOwner,
                                                    ViewModelStoreOwner viewModelStoreOwner) {
        if(userLiveData == null)
            userLiveData = UserModel.getInstance().getUserLiveData(uid, viewModelStoreOwner,
                    lifecycleOwner);
        return userLiveData;
    }
}
