package com.example.project_yougo;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class RegisterFragment extends Fragment {

    EditText name, lastname, email, password, confirmPassword;
    Button signup;


    public RegisterFragment(){
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
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_login,container, false);

        this.name = view.findViewById(R.id.register_name);
        this.lastname = view.findViewById(R.id.register_lastname);
        this.email = view.findViewById(R.id.register_email);
        this.password = view.findViewById(R.id.register_password);
        this.confirmPassword = view.findViewById(R.id.register_confirm_password);
        this.signup = view.findViewById(R.id.profile_frag_back_btn);


        return view;
    }
}