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



public class RegisterFragment extends Fragment {

    EditText name, lastname, email, password, confirmPassword,gender,age;
    Button signup;

    public RegisterFragment(){
        //required empty public constructor.
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_register,container, false);

        this.age = view.findViewById(R.id.editUser_age_et);
        this.gender = view.findViewById(R.id.editUser_gender_et);
        this.name = view.findViewById(R.id.editUser_name_et);
        this.lastname = view.findViewById(R.id.editUser_lastname_et);
        this.email = view.findViewById(R.id.editUser_email_et);
        this.password = view.findViewById(R.id.editUser_password_et);
        this.confirmPassword = view.findViewById(R.id.editUser_conPassword_et);
        this.signup = view.findViewById(R.id.editUser_save_btn);

        this.signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register(v);
            }
        });

        return view;
    }

    public void register(View v) {
        if(!validateFirstname() | !validateLastname() | !validateEmail()
                | !validatePassword() | !validateConfirmPassword()){
            return;
        }
        String gender = this.gender.getText().toString();
        String age = this.age.getText().toString();
        String firstname = this.name.getText().toString();
        String lastname = this.lastname.getText().toString();
        String email = this.email.getText().toString();
        String password = this.password.getText().toString();
        UserModel.getInstance().signUpWithEmailAndPassword(getContext(),email, password, firstname, lastname,gender,age, new UserModel.SignUpCompleteListener() {
            @Override
            public void onSignupSuccessful() {
                toFeedActivity();
            }

            @Override
            public void onSignupFailed() {
                Toast.makeText(getContext(), "could not sign up", Toast.LENGTH_LONG).show();
            }
        });

    } //bluestacks

    private Boolean validateFirstname(){
        String val = this.name.getText().toString();
        String noWhiteSpace = "\\A[a-zA-Z]{1,20}\\z";

        if(val.isEmpty()){
            this.name.setError("Field cannot be empty");
            return false;
        }else if(!val.matches(noWhiteSpace)){
            this.name.setError("Invalid firstname address");
            return false;
        }
        else{
            this.name.setError(null);
            return true;
        }
    }
    // \A\w{4,20}\z
    private Boolean validateLastname(){
        String val = this.lastname.getText().toString();
        String noWhiteSpace = "\\A[a-zA-Z]{1,20}\\z";
        if(val.isEmpty()){
            this.lastname.setError("Field cannot be empty");
            return false;
        }else if(!val.matches(noWhiteSpace)){
            this.lastname.setError("Invalid lastname address");
            return false;
        }
        else{
            this.lastname.setError(null);
            return true;
        }
    }

    private Boolean validateEmail(){
        String val = this.email.getText().toString();
        String emailPattern = "[a-zA-Z0-9,_-]+@[a-z]+\\.+[a-z]+";

        if(val.isEmpty()){
            this.email.setError("Field cannot be empty");
            return false;
        }
        else if(!val.matches(emailPattern)){
            this.email.setError("Invalid email address");
            return false;
        }
        else{
            this.email.setError(null);
            return true;
        }
    }

    private Boolean validatePassword(){
        String val = this.password.getText().toString();
        String passwordval = "\\A[a-zA-Z0-9]{6,10}\\z";
        if(val.isEmpty()){
            this.password.setError("Field cannot be empty");
            return false;
        }else if(!val.matches(passwordval)){
            this.password.setError("Password must be only letters and number & between 6 to 10 characters");
            return false;
        }
        else{
            this.password.setError(null);
            return true;
        }
    }

    private Boolean validateConfirmPassword(){
        String val1 = this.password.getText().toString();
        String val2 = this.confirmPassword.getText().toString();

        if(val2.isEmpty()){
            this.confirmPassword.setError("Field cannot be empty");
            return false;
        }
        else if(!val2.equals(val1)){
            this.confirmPassword.setError("Different password");
            return false;
        }
        else{
            this.confirmPassword.setError(null);
            return true;
        }
    }

    public void toFeedActivity() {
        Intent intent = new Intent(getContext(), FeedActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}

