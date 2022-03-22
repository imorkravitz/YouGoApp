package com.example.project_yougo.feed;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.project_yougo.R;
import com.example.project_yougo.model.post.PostModel;
import com.google.firebase.auth.FirebaseAuth;

import java.io.InputStream;


public class create_postFragment extends Fragment {
    private ImageButton addImg;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_GALLERY = 2;
    private Button createPostButton;
    private EditText freeTexEditText;
    private EditText difficultyEditText;
    private EditText typeOfWorkoutEditText;
    private ImageView postImg;
    Bitmap imageBitmap;
    Bitmap selectedImage;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_post, container, false);
        addImg = view.findViewById(R.id.create_post_frag_addImage_btn);
        addImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v);
            }
        });

        createPostButton = view.findViewById(R.id.create_post_frag_post_btn);
        createPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCreatePostButtonClicked();
            }
        });

        freeTexEditText = view.findViewById(R.id.create_post_frag_freeText_PL);
        difficultyEditText = view.findViewById(R.id.create_post_frag_difficulty);
        typeOfWorkoutEditText = view.findViewById(R.id.create_post_frag_TOW_PT);
        postImg = view.findViewById(R.id.post_camera_img);

        return view;

    }

    public void onCreatePostButtonClicked() {
        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
            // not signed in
            return;
        }
        // TODO: add validation
        // TODO: + add GPS fields in Post + PostModel
        String freeText = freeTexEditText.getText().toString();
        String difficulty = difficultyEditText.getText().toString();
        String typeOfWorkout = typeOfWorkoutEditText.getText().toString();
        String publisherId = FirebaseAuth.getInstance().getUid();
        String nameOfImg = publisherId + freeText + difficulty + typeOfWorkout;
        if(imageBitmap != null){
            Toast.makeText(getContext(), "Post with Bitmap Creation success",
                    Toast.LENGTH_LONG).show();
            PostModel.getInstance().saveImage(imageBitmap,  nameOfImg + ".jpg", url->{

                PostModel.getInstance().addPostWithImg(freeText, difficulty, typeOfWorkout,
                        publisherId, url, new PostModel.PostCreationListener() {
                            @Override
                            public void onCreationSuccess() {
                                // TODO: navigate to different frag?
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getContext(), "Post Creation success",
                                                Toast.LENGTH_LONG).show();
                                        Navigation.findNavController(getView()).
                                                navigate(R.id.action_global_postListFragment2);
                                    }
                                });
                            }

                            @Override
                            public void onCreationFailed() {

                            }
                        });
            });
        }else {
            PostModel.getInstance().addPost(freeText, difficulty, typeOfWorkout,
                    publisherId, new PostModel.PostCreationListener() {
                        @Override
                        public void onCreationSuccess() {
                            // TODO: navigate to different frag?
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(), "Post Creation success",
                                            Toast.LENGTH_LONG).show();
                                    Navigation.findNavController(getView()).
                                            navigate(R.id.action_global_postListFragment2);
                                }
                            });
                        }

                        @Override
                        public void onCreationFailed() {

                        }
                    });
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE_CAPTURE){
            if(resultCode == RESULT_OK){
                Bundle extras = data.getExtras();
                imageBitmap = (Bitmap) extras.get("data");
                postImg.setImageBitmap(imageBitmap);
            }
        }else if(requestCode == REQUEST_IMAGE_GALLERY){
            if(resultCode == RESULT_OK){
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContext().getContentResolver().openInputStream(imageUri);
                    //final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    selectedImage = BitmapFactory.decodeStream(imageStream);
                    postImg.setImageBitmap(selectedImage);
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
