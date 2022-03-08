package com.example.project_yougo;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;


public class RegisterFragment extends Fragment {

    EditText name, lastname, email, password, confirmPassword;
    Button signup;
    private FirebaseDatabaseHandler firebaseDatabaseHandler;
    private FirebaseAuth firebaseAuth;

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
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_register,container, false);
        this.firebaseDatabaseHandler = FirebaseDatabaseHandler.getInstance();
        this.firebaseAuth = FirebaseAuth.getInstance();

        this.name = view.findViewById(R.id.register_name);
        this.lastname = view.findViewById(R.id.register_lastname);
        this.email = view.findViewById(R.id.register_email);
        this.password = view.findViewById(R.id.register_password);
        this.confirmPassword = view.findViewById(R.id.register_confirm_password);
        this.signup = view.findViewById(R.id.profile_frag_back_btn);

        this.signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                register();
            }
        });

        return view;
    }

    public void register() {
        String firstname = this.name.getText().toString();
        String lastname = this.lastname.getText().toString();
        String email = this.email.getText().toString();
        String password = this.password.getText().toString();
        this.firebaseDatabaseHandler.signUpWithEmailAndPassword(getContext(), firebaseAuth,
                email, password, firstname, lastname, new FirebaseDatabaseHandler.SignUpCompleteListener() {
                    @Override
                    public void onSignupSuccessful() {
                        Toast.makeText(getContext(), "oved", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onSignupFailed() {
                        Toast.makeText(getContext(), "could not sign up", Toast.LENGTH_LONG).show();
                    }
            }
        );
    } //bluestacks
}
