package com.example.project_yougo.feed;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.ContactsContract;
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
import com.example.project_yougo.model.User;
import com.example.project_yougo.model.UserModelFirebase;

import java.io.InputStream;



public class profileFragment extends Fragment {
    private TextView firstName;
    private TextView lastName;
    private TextView email;
    private ImageView profileImg;
    private ImageButton profileBtn;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_GALLERY = 2;
    private Bitmap imageBitmap;
    private Button edit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        edit = view.findViewById(R.id.profile_frag_edit_btn);
        setHasOptionsMenu(true);
        firstName=view.findViewById(R.id.profile_frag_name_tv);
        lastName=view.findViewById(R.id.profile_frag_Last_name_tv);
        email=view.findViewById(R.id.profile_frag_e_mail_tv);
        profileImg = view.findViewById(R.id.profile_frag_user_img);
        profileBtn = view.findViewById(R.id.profile_frag_image_btn);

        UserModelFirebase.getInstance().getUserById(UserModelFirebase.getInstance().getUid(), new UserModelFirebase.GetUserCompleteListener() {
            @Override
            public void onComplete(User user) {
                firstName.setText(user.getFirstName());
                lastName.setText(user.getLastName());
                email.setText(user.getEmail());
            }
        });
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_profileFragment1_to_editUserFragment);
            }
        });

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
