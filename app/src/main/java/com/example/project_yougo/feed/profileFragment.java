package com.example.project_yougo.feed;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.project_yougo.R;
import com.example.project_yougo.model.User;
import com.example.project_yougo.model.UserModel;
import com.example.project_yougo.model.UserModelFirebase;
import com.google.firebase.auth.FirebaseAuth;

public class profileFragment extends Fragment {
    private UserModelFirebase userModelFirebase;
    private FirebaseAuth firebaseAuth;
    TextView firstName;
    TextView lastName;
    TextView email;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        Button edit = view.findViewById(R.id.profile_frag_edit_btn);
        setHasOptionsMenu(true);
        firstName=view.findViewById(R.id.profile_frag_name_tv);
        lastName=view.findViewById(R.id.profile_frag_Last_name_tv);
        email=view.findViewById(R.id.profile_frag_e_mail_tv);
        UserModel.getInstance().getUserById(new UserModel.GetUserById() {
            @Override
            public void onComplete(User user) {
//                firstName.setText(user.getFirstName());
//                lastName.setText(user.getLastName());
//                email.setText(user.getEmail());
            }
        });
        return view;
    }


}
