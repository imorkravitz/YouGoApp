package com.example.project_yougo.feed;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_yougo.MyApplication;
import com.example.project_yougo.R;
import com.example.project_yougo.login.LoginActivity;
import com.example.project_yougo.model.user.User;
import com.example.project_yougo.model.user.UserModel;


public class EditUserFragment extends Fragment {
    private TextView firstName;
    private TextView lastName;
    private TextView email;
    private TextView gender;
    private TextView age;
    private TextView password;
    private TextView confirmPassword;
    private ImageView profileImg;
    private ImageButton profileBtn;
    private Button save;
    private Button deleteUser;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_GALLERY = 2;
    private Bitmap imageBitmap;
    private UserViewModel userViewModel;
    private boolean active;

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

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        Observer<User> observer = new Observer<User>() {
            @Override
            public void onChanged(User user) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        // might be null if user has not been downloaded from firebase db into local db
                        if(user != null) {
                            firstName.setText(user.getFirstName());
                            lastName.setText(user.getLastName());
                            email.setText(UserModel.getInstance().getUserEmail());
                            gender.setText(user.getGender());
                            age.setText(user.getAge());
                            active = user.isActive();
                        }
                    }
                });
            }
        };

        userViewModel.getUserLiveData(UserModel.getInstance().getUid(), getViewLifecycleOwner(), this)
                .observe(getViewLifecycleOwner(), observer);


//        UserModel.getInstance().getUserById(UserModel.getInstance().getUid(), new UserModel.GetUserCompleteListener() {
//            @Override
//            public void onComplete(User user) {
//                firstName.setText(user.getFirstName());
//                lastName.setText(user.getLastName());
//                email.setText(UserModel.getInstance().getUserEmail());
//                gender.setText(user.getGender());
//                age.setText(user.getAge());
//            }
//        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser();
            }
        });
        deleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               new AlertDialog.Builder(getContext(),R.style.MyDialogTheme)
                       .setTitle("Delete user")
                       .setMessage("This is it?")
                       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               deleteUser();
                           }
                       }).setNegativeButton("NO",null).show();
            }
        });

        return view;
    }

    private void deleteUser() {
        String firstName=this.firstName.getText().toString();
        String lastName=this.lastName.getText().toString();
        String gender=this.gender.getText().toString();
        String age=this.age.getText().toString();
        String emailNew=this.email.getText().toString();
        String password=this.password.getText().toString();

        UserModel.getInstance().updateUser(UserModel.getInstance().getUid(), emailNew, password, firstName, lastName, gender, age,
                false, new UserModel.UpdateUserCompleteListener() {
                    @Override
                    public void onUpdateSuccessful() {
                        Toast.makeText(getContext(),"User deleted!",Toast.LENGTH_LONG).show();
                        UserModel.getInstance().logOut();
                        startActivity(new Intent(getContext(), LoginActivity.class));
                        getActivity().finish();
                    }

                    @Override
                    public void onUpdateFailed() {
                        Toast.makeText(getContext(),"User cannot be deleted at this time, please try again later",
                                Toast.LENGTH_LONG).show();
                    }
                });

//
//        UserModel.getInstance().deleteUser(new UserModel.UserDeletionCompleteListener() {
//            @Override
//            public void onDeletionSuccessful() {
//                Toast.makeText(getContext(),"User deleted!",Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onDeletionFailed() {
//                Toast.makeText(getContext(),"User cannot be deleted at this time, please try again later",
//                        Toast.LENGTH_LONG).show();
//            }
//        });
    }

    private void updateUser() {
        String firstName=this.firstName.getText().toString();
        String lastName=this.lastName.getText().toString();
        String gender=this.gender.getText().toString();
        String age=this.age.getText().toString();
        String emailNew=this.email.getText().toString();
        String password=this.password.getText().toString();
        boolean emailFlag=false;
        boolean passwordFlag=false;
        if(!email.equals(emailNew)){
            if(validateEmail()) {
                emailFlag=true;
            }
        }
        if(!password.equals("")){
            if(validateConfirmPassword()){
                passwordFlag=true;
            }
        }
        if(emailFlag&&passwordFlag){
            UserModel.getInstance().updateUser(UserModel.getInstance().getUid(), emailNew, password, firstName, lastName, gender, age,
                    active, new UserModel.UpdateUserCompleteListener() {
                        @Override
                        public void onUpdateSuccessful() {
                            Toast.makeText(MyApplication.getContext(),"user updated!",Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onUpdateFailed() {
                            Toast.makeText(getContext(),"User cannot be deleted at this time, please try again later",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
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