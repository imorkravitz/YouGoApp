package com.example.project_yougo.feed;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.project_yougo.R;
import com.example.project_yougo.model.User;
import com.example.project_yougo.model.UserModelFirebase;
import com.example.project_yougo.model.comment.CommentModel;
import com.example.project_yougo.model.post.Comment;

import java.util.List;


public class CommentListFragment extends Fragment {

    private String postId;
    private RecyclerView commentRecyclerView;

    public CommentListFragment() {
        // Required empty public constructor
    }
    public static CommentListFragment newInstance(String param1, String param2) {
        CommentListFragment fragment = new CommentListFragment();
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
        View view = inflater.inflate(R.layout.fragment_comment_list, container, false);
        postId = CommentListFragmentArgs.fromBundle(getArguments()).getPostIdArg();
        commentRecyclerView = view.findViewById(R.id.commentlist_rv);

        commentRecyclerView.setHasFixedSize(true);
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        setHasOptionsMenu(true);
        initCommentList();

        return view;
    }

    private void initCommentList() {
        CommentModel.getInstance().loadCommentList(getActivity().getApplicationContext(), postId, new CommentModel.CommentListLoadListener() {
            @Override
            public void onCommentListLoaded(List<Comment> commentList) {
                updateCommentRecyclerView(commentList);
            }
        });
        CommentModel.getInstance().listenForCommentListUpdates(getActivity().getApplicationContext(), postId, new CommentModel.CommentListUpdateListener()  {
            @Override
            public void onCommentListUpdated(List<Comment> commentList){
                updateCommentRecyclerView(commentList);
            }
        });
    }

    private void updateCommentRecyclerView(List<Comment> commentList) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                MyAdapter adapter = new MyAdapter(commentList);
                adapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        //TODO fixe that
                        // Navigation.findNavController(v).navigate(PostListFragmentDirections.actionPostListFragmentToProfileFragment());
                    }
                });
                commentRecyclerView.setAdapter(adapter);
            }
        });
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        TextView userName;
        TextView content;

        public MyViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName_comment_row);
            content = itemView.findViewById(R.id.content_comment_row);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    listener.onItemClick(v, pos);
                }
            });
        }
    }

    interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        OnItemClickListener listener;
        private List<Comment> commentList;

        public MyAdapter(List<Comment> commentList) {
            this.commentList = commentList;
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.commentlist_row, parent, false);
            MyViewHolder holder = new MyViewHolder(view, listener);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Comment comment = commentList.get(position);

            UserModelFirebase.getInstance().getUserById(comment.getPublisherId(), new UserModelFirebase.GetUserCompleteListener() {
                @Override
                public void onComplete(User user) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            holder.userName.setText(user.fullname());
                            holder.content.setText(comment.getContent());
                        }
                    });
                }
            });
        }

        @Override
        public int getItemCount() {
            return commentList.size();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}