package com.example.project_yougo.feed;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project_yougo.R;
import com.example.project_yougo.model.AuthenticationHandler;
import com.example.project_yougo.model.User;
import com.example.project_yougo.model.user.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class profileFragment extends Fragment {
    TextView FirstName;
    TextView LastName;
    TextView e_mail;
    ImageView profileImg;
    private AuthenticationHandler authenticationHandler;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //   FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        Button edit = view.findViewById(R.id.profile_frag_edit_btn);
        setHasOptionsMenu(true);


        FirstName = view.findViewById(R.id.profile_frag_name_tv);
        LastName = view.findViewById(R.id.profile_frag_Last_name_tv);
        e_mail = view.findViewById(R.id.profile_frag_e_mail_tv);
        profileImg = view.findViewById(R.id.profile_frag_user_img);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


        UserModel.GetUserById(uid,new UserModel.GetUserById(){

            @Override
            public void onComplete(User user) {

            }
        });


        return view;
    }

}
