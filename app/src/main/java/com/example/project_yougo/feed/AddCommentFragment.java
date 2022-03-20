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
import com.example.project_yougo.model.user.UserModelFirebase;
import com.example.project_yougo.model.comment.CommentModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddCommentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddCommentFragment extends Fragment {
//
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
    private String postId;
    private EditText contentEditText;
    private Button addButton;

    public AddCommentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddCommentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddCommentFragment newInstance(String param1, String param2) {
        AddCommentFragment fragment = new AddCommentFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
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