package com.example.project_yougo.feed;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.project_yougo.R;
import com.example.project_yougo.model.UserModelFirebase;
import com.example.project_yougo.model.comment.CommentModel;

public class AddCommentFragment extends Fragment {

    private String postId;
    private EditText contentEditText;
    private Button addButton;

    public AddCommentFragment() {
        // Required empty public constructor
    }

    public static AddCommentFragment newInstance(String param1, String param2) {
        AddCommentFragment fragment = new AddCommentFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_comment, container, false);

        postId = AddCommentFragmentArgs.fromBundle(getArguments()).getPostIdArg();
        addButton = view.findViewById(R.id.add_comment_btn);
        contentEditText = view.findViewById(R.id.add_comment_content_edit_text);

        addButton.setOnClickListener((v) -> onAddCommentButtonClicked());

        return view;
    }

    private void onAddCommentButtonClicked() {
        String publisherId = UserModelFirebase.getInstance().getUid();
        String content = contentEditText.getText().toString().trim();
        CommentModel.getInstance().addComment(publisherId, postId, content, new CommentModel.CommentCreationListener() {
            @Override
            public void onCreationSuccess() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "Comment added", Toast.LENGTH_SHORT).show();
                        getActivity().onBackPressed(); // close current fragment
                    }
                });
            }

            @Override
            public void onCreationFailed() {
                Toast.makeText(getContext(), "Could not add comment, please try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }
}