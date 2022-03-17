package com.example.project_yougo.feed;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_yougo.R;
import com.example.project_yougo.model.User;
import com.example.project_yougo.model.UserModel;


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
    Button cancel;
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
        cancel=view.findViewById(R.id.editUser_cancel_btn);
        UserModel.getInstance().getUserById(new UserModel.GetUserById() {
            @Override
            public void onComplete(User user) {
                firstName.setText(user.getFirstName());
                lastName.setText(user.getLastName());
                email.setText(user.getEmail());
                gender.setText(user.getGender());
                age.setText(user.getAge());
                password.setText(user.getPassword());
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser();
                //Navigation.findNavController(v).navigateUp();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigateUp();
            }
        });

        return view;
    }

    private void updateUser() {
        String firstName=this.firstName.getText().toString();
        String lastName=this.lastName.getText().toString();
        String gender=this.gender.getText().toString();
        String age=this.age.getText().toString();
        String email=this.email.getText().toString();
        String password=this.password.getText().toString();
        String confirmPass=this.confirmPassword.getText().toString();
        UserModel.getInstance().updateUser(email, password, firstName, lastName, gender, age, new UserModel.UpdateUser() {
            @Override
            public void onComplete() {
                Toast.makeText(getContext(),"user updated!",Toast.LENGTH_LONG).show();
            }
        });


    }
}