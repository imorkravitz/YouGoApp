package com.example.project_yougo.feed;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_yougo.R;
import com.example.project_yougo.model.user.User;
import com.example.project_yougo.model.user.UserModelFirebase;


public class EditUserFragment extends Fragment {

    TextView firstName;
    TextView lastName;
    TextView email;
    TextView gender;
    TextView age;
    TextView password;
    TextView confirmPassword;
    ImageView profileImg;
    ImageButton profileBtn;
    Button save;
    Button deleteUser;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_GALLERY = 2;
    Bitmap imageBitmap;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_edit_user, container, false);
        firstName=view.findViewById(R.id.editUser_name_et);
        lastName=view.findViewById(R.id.editUser_lastname_et);
        email=view.findViewById(R.id.editUser_email_et);
        gender=view.findViewById(R.id.editUser_gender_et);
        age=view.findViewById(R.id.editUser_age_et);
        password=view.findViewById(R.id.editUser_password_et);
        confirmPassword=view.findViewById(R.id.editUser_conPassword_et);
        profileImg=view.findViewById(R.id.editUser_img);
        save=view.findViewById(R.id.editUser_save_btn);
        deleteUser=view.findViewById(R.id.editUser_deleteUser_btn);
        UserModelFirebase.getInstance().getUserById(UserModelFirebase.getInstance().getUid(), new UserModelFirebase.GetUserCompleteListener() {
            @Override
            public void onComplete(User user) {
                firstName.setText(user.getFirstName());
                lastName.setText(user.getLastName());
                email.setText(UserModelFirebase.getInstance().getUserEmail());
                gender.setText(user.getGender());
                age.setText(user.getAge());
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser();
                //Navigation.findNavController(v).navigateUp();
            }
        });
        deleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUser();
                Toast.makeText(getContext(),"User deleted!",Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }

    private void deleteUser() {
        UserModelFirebase.getInstance().deleteUser();
    }

    private void updateUser() {
        String firstName=this.firstName.getText().toString();
        String lastName=this.lastName.getText().toString();
        String gender=this.gender.getText().toString();
        String age=this.age.getText().toString();
        String emailNew=this.email.getText().toString();
        String password=this.password.getText().toString();
        if(!email.equals(emailNew)){
            if(validateEmail()) {
                UserModelFirebase.getInstance().updateUserEmail(emailNew);
            }
        }
        if(!password.equals("")){
            if(validateConfirmPassword()){
                UserModelFirebase.getInstance().updateUserPassword(password);
            }
        }
        UserModelFirebase.getInstance().updateUser(UserModelFirebase.getInstance().getUid(), emailNew, password, firstName, lastName, gender, age, new UserModelFirebase.UpdateUserCompleteListener() {
            @Override
            public void onComplete(User user) {
                Toast.makeText(getContext(),"user updated!",Toast.LENGTH_LONG).show();
            }
        });

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
}