package com.example.project_yougo;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class LoginFragment extends Fragment {

    EditText email, password;
    Button login;

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
        this.login = (Button) view.findViewById(R.id.login_login_btn);

        login.setOnClickListener((v)->{
            Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_postListFragment);
        });


        return view;
    }
}
