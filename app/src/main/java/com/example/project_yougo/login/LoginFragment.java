package com.example.project_yougo.login;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.project_yougo.R;

import com.example.project_yougo.feed.FeedActivity;
import com.example.project_yougo.model.user.UserModel;


public class LoginFragment extends Fragment {

    EditText email, password;
    Button login;
    boolean log=false;


    public LoginFragment(){
        //required empty public constructor.
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_login, container, false);


        this.email = view.findViewById(R.id.login_email);
        this.password = view.findViewById(R.id.login_password);
        this.login = view.findViewById(R.id.login_login_btn);

        login.setOnClickListener((v)->{
            loginApproved(v);
        });


        return view;
    }

    private void loginApproved(View v) {
        String email = this.email.getText().toString();
        String password = this.password.getText().toString();
        UserModel.getInstance().login(email, password, new UserModel.SignInCompleteListener() {
            @Override
            public void onSignInSuccessful() {
                Toast.makeText(getContext(), "login", Toast.LENGTH_LONG).show();
              //  Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_postListFragment);
                /** switch between the activities **/
                toFeedActivity();
            }

            @Override
            public void onSignInFailed() {
                Toast.makeText(getContext(), "Wrong password or email!", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void toFeedActivity() {
        Intent intent = new Intent(getContext(), FeedActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}
