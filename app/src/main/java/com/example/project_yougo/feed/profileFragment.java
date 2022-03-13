package com.example.project_yougo.feed;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

import com.example.project_yougo.R;
import com.example.project_yougo.model.AuthenticationHandler;
import com.example.project_yougo.model.User;
import com.example.project_yougo.model.user.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.InputStream;

public class profileFragment extends Fragment {
    TextView FirstName;
    TextView LastName;
    TextView e_mail;
    ImageView profileImg;
    ImageButton profileBtn;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_GALLERY = 2;
    Bitmap imageBitmap;
    private AuthenticationHandler authenticationHandler;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //   FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        Button edit = view.findViewById(R.id.profile_frag_edit_btn);
        setHasOptionsMenu(true);


        FirstName = view.findViewById(R.id.profile_frag_name_tv);
        LastName = view.findViewById(R.id.profile_frag_Last_name_tv);
        e_mail = view.findViewById(R.id.profile_frag_e_mail_tv);
        profileImg = view.findViewById(R.id.profile_frag_user_img);
        profileBtn = view.findViewById(R.id.profile_frag_image_btn);

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v);
            }
        });

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


//        UserModel.GetUserById(uid,new UserModel.GetUserById(){
//
//            @Override
//            public void onComplete(User user) {
//
//            }
//        });


        return view;
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
                        Toast.makeText(getContext(), "gallery oved", Toast.LENGTH_LONG).show();
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

    /**
     * TODO: Add the image to profile and to database
     */
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
