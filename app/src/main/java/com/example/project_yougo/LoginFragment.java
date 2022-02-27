package com.example.project_yougo;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class LoginFragment extends Fragment {

    EditText email, password;
    Button login;
    ImageView btn;

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
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_register,container, false);

        this.email = view.findViewById(R.id.login_email);
        this.password = view.findViewById(R.id.login_password);
        this.login = view.findViewById(R.id.login_login_btn);
        this.btn = view.findViewById(R.id.back_to_register);



        return view;
    }

}