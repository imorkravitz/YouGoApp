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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends Fragment {

    EditText email, password;
    Button login;
    boolean log=false;
    private FirebaseDatabaseHandler firebaseDatabaseHandler;
    private FirebaseAuth firebaseAuth;

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
        this.firebaseDatabaseHandler = FirebaseDatabaseHandler.getInstance();
        this.firebaseAuth = FirebaseAuth.getInstance();

        this.email = view.findViewById(R.id.login_email);
        this.password = view.findViewById(R.id.login_password);
        this.login = (Button) view.findViewById(R.id.login_login_btn);

        login.setOnClickListener((v)->{
            loginApproved(v);
        });


        return view;
    }

    private void loginApproved(View v) {

        String email = this.email.getText().toString();
        String password = this.password.getText().toString();
        this.firebaseDatabaseHandler.loginWithEmailAndPassword(getContext(), firebaseAuth, email, password, new FirebaseDatabaseHandler.LoginCompleteListener() {
            @Override
            public void onLoginSuccessful() {
                Toast.makeText(getContext(), "login oved", Toast.LENGTH_LONG).show();
                Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_postListFragment);
            }

            @Override
            public void onLoginFailed() {
                Toast.makeText(getContext(), "Wrong password or email!", Toast.LENGTH_LONG).show();
            }
        });
    }
}
