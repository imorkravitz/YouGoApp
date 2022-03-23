package com.example.project_yougo.feed;

import android.app.AlertDialog;
import android.content.DialogInterface;
import static android.app.Activity.RESULT_OK;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_yougo.MyApplication;
import com.example.project_yougo.R;
import com.example.project_yougo.login.LoginActivity;
import com.example.project_yougo.model.user.User;
import com.example.project_yougo.model.user.UserModel;
import com.squareup.picasso.Picasso;

import java.io.InputStream;


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
        profileBtn=view.findViewById(R.id.editUser_camera_btn);

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
                            if(user.getImageUrl() != null) {
                                Picasso.get()
                                        .load(user.getImageUrl())
                                        .into(profileImg);
                            }
                        }
                    }
                });
            }
        };

        userViewModel.getUserLiveData(UserModel.getInstance().getUid(), getViewLifecycleOwner(), this)
                .observe(getViewLifecycleOwner(), observer);

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
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v);
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
        if(emailFlag&&passwordFlag) {
            if (imageBitmap != null) {
                UserModel.getInstance().saveImage(imageBitmap, firstName + lastName + emailNew + ".jpg",url -> {
                    UserModel.getInstance().updateUserWithUrl(UserModel.getInstance().getUid(), emailNew, password, firstName, lastName, gender, age,
                            url,active, new UserModel.UpdateUserCompleteListener() {
                                @Override
                                public void onUpdateSuccessful() {
                                    Toast.makeText(MyApplication.getContext(), "user updated!", Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onUpdateFailed() {
                                    Toast.makeText(getContext(), "User cannot be deleted at this time, please try again later_img",
                                            Toast.LENGTH_LONG).show();
                                }
                    });
                });
                getActivity().onBackPressed();
            }else{
            UserModel.getInstance().updateUser(UserModel.getInstance().getUid(), emailNew, password, firstName, lastName, gender, age,
                    active, new UserModel.UpdateUserCompleteListener() {
                        @Override
                        public void onUpdateSuccessful() {
                            Toast.makeText(MyApplication.getContext(), "user updated!_NON", Toast.LENGTH_LONG).show();

                        }

                        @Override
                        public void onUpdateFailed() {
                            Toast.makeText(getContext(), "User cannot be deleted at this time, please try again later_NON",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                getActivity().onBackPressed();
            }
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

    public void showPopup(View v){
        PopupMenu popupMenu = new PopupMenu(getContext(), v);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.camera_popup_menu:
                        openCamera(v);
                        break;
                    case R.id.gallery_popup_menu:
                        openGallery(v);
                        Toast.makeText(getContext(), "Gallery", Toast.LENGTH_LONG).show();
                        break;
                }
                return false;
            }
        });
    }
    private void openCamera(View v){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE_CAPTURE){
            if(resultCode == RESULT_OK){
                Bundle extras = data.getExtras();
                imageBitmap = (Bitmap) extras.get("data");
                profileImg.setImageBitmap(imageBitmap);
            }
        }else if(requestCode == REQUEST_IMAGE_GALLERY){
            if(resultCode == RESULT_OK){
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContext().getContentResolver().openInputStream(imageUri);
                    imageBitmap = BitmapFactory.decodeStream(imageStream);
                    profileImg.setImageBitmap(imageBitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void openGallery(View v){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_IMAGE_GALLERY);
    }

}