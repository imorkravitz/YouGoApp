package com.example.project_yougo.feed;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.project_yougo.model.user.User;
import com.example.project_yougo.model.user.UserModel;

import java.util.List;

public class UserListViewModel extends ViewModel {
    private LiveData<List<User>> userListLiveData;

    public LiveData<List<User>> getUserListLiveData(LifecycleOwner lifecycleOwner,
                                          ViewModelStoreOwner viewModelStoreOwner) {
        if(userListLiveData == null)
            userListLiveData = UserModel.getInstance().getUserListLiveData(viewModelStoreOwner,
                    lifecycleOwner);
        return userListLiveData;
    }
}
