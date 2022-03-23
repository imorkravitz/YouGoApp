package com.example.project_yougo.feed;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import com.example.project_yougo.R;
import com.example.project_yougo.model.user.User;
import com.example.project_yougo.model.user.UserModel;
import com.squareup.picasso.Picasso;

import java.io.InputStream;



public class profileFragment extends Fragment {
    private TextView firstName;
    private TextView lastName;
    private TextView email;
    private ImageView profileImg;
    private Button edit;
    private UserViewModel userViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        edit = view.findViewById(R.id.profile_frag_edit_btn);
        setHasOptionsMenu(true);
        firstName=view.findViewById(R.id.profile_frag_name_tv);
        lastName=view.findViewById(R.id.profile_frag_Last_name_tv);
        email=view.findViewById(R.id.profile_frag_e_mail_tv);
        profileImg = view.findViewById(R.id.profile_frag_user_img);


        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        Observer<User> observer = new Observer<User>() {
            @Override
            public void onChanged(User user) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        // might be null if user has not been downloaded from firebase db into local db
                        if(user != null) {
                            firstName.setText(user.getFirstName());
                            lastName.setText(user.getLastName());
                            email.setText(UserModel.getInstance().getUserEmail());
                            Picasso.get()
                                    .load(user.getImageUrl())
                                    .into(profileImg);
                        }
                    }
                });
            }
        };

        userViewModel.getUserLiveData(UserModel.getInstance().getUid(), getViewLifecycleOwner(), this)
                .observe(getViewLifecycleOwner(), observer);

//        UserModel.getInstance().getUserById(UserModel.getInstance().getUid(), new UserModel.GetUserCompleteListener() {
//            @Override
//            public void onComplete(User user) {
//                firstName.setText(user.getFirstName());
//                lastName.setText(user.getLastName());
//                email.setText(UserModel.getInstance().getUserEmail());
//            }
//        });


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_profileFragment1_to_editUserFragment);
            }
        });

        return view;
    }

}
