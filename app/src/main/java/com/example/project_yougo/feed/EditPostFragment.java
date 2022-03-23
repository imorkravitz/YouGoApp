package com.example.project_yougo.feed;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_yougo.R;
import com.example.project_yougo.model.post.Post;
import com.example.project_yougo.model.post.PostModel;
import com.example.project_yougo.model.user.UserModel;

import java.util.Date;
import java.util.List;

public class EditPostFragment extends Fragment {
    EditText FreeText;
    EditText Difficulty;
    EditText TypeOfWorkout;
    Button savePost;
    Button deletePost;
    String postPos;
    Post post;
    ImageView postImg;
    TextView postDate;
    TextView postTime;
    private PostListViewModel postListViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.edit_post_fragemnt, container, false);
        TypeOfWorkout=view.findViewById(R.id.create_post_frag_TOW_PT);
        FreeText=view.findViewById(R.id.create_post_frag_freeText_PL);
        Difficulty=view.findViewById(R.id.create_post_frag_difficulty);
        savePost=view.findViewById(R.id.edit_post_save_btn);
        deletePost=view.findViewById(R.id.edit_post_delete_btn);

        postListViewModel=new ViewModelProvider(this).get(PostListViewModel.class);

        postPos=EditPostFragmentArgs.fromBundle(getArguments()).getPostId();

        postObserver();

        savePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePost();
            }
        });
        deletePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext(),R.style.MyDialogTheme)
                        .setTitle("Delete post")
                        .setMessage("Are you sure you?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deletePost();
                            }
                        }).setNegativeButton("NO",null).show();
            }
        });
        return view;
    }
    public void postObserver(){
        Observer<List<Post>> postObserver=new Observer<List<Post>>() {

            @Override
            public void onChanged(List<Post> posts) {
                for(int i=0;i<posts.size();i++){
                    if(posts.get(i).getId().equals(postPos)){
                        post=posts.get(i);
                        break;
                    }
                }
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if(post!=null){
                            TypeOfWorkout.setText(post.getTypeOfWorkout());
                            Difficulty.setText(post.getDifficulty());
                            FreeText.setText(post.getFreeText());
                        }
                    }
                });
            }

        };
        postListViewModel.getPostListLiveData(getViewLifecycleOwner(),this).observe(getViewLifecycleOwner(),postObserver);
    }
    private void updatePost() {
        String freeText=FreeText.getText().toString();
        String two=TypeOfWorkout.getText().toString();
        String diff=Difficulty.getText().toString();

        PostModel.getInstance().updatePost(post.getId(),post.getPublisherId(),freeText,two,diff,post.getLongitude(),post.getLatitude(),new PostModel.UpdatePostCompleteListener(){
            @Override
            public void onUpdateSuccessful() {
                Toast.makeText(getContext(),"post updated!",Toast.LENGTH_LONG).show();
            }
            @Override
            public void onUpdateFailed() {
                Toast.makeText(getContext(),"You are not allowed to edit this post",Toast.LENGTH_LONG).show();
            }
        });
    }
    private void deletePost() {
        PostModel.getInstance().deletePost(post.getId(),post.getPublisherId(),post,new PostModel.DeletePostCompleteListener(){

            @Override
            public void onDeleteSuccessful() {
                Toast.makeText(getContext(),"post deleted!",Toast.LENGTH_LONG).show();
                //PostModel.getInstance().getPostListLiveData(EditPostFragment.this,getViewLifecycleOwner());
                //postListViewModel.getPostListLiveData(getViewLifecycleOwner(),this).observe(getViewLifecycleOwner(),postObserver);
                //postObserver();
            }

            @Override
            public void onDeleteFailed() {
                Toast.makeText(getContext(),"You are not allowed to delete this post",Toast.LENGTH_LONG).show();
            }
        });
//        PostModel.getInstance().refreshList()


    }


}