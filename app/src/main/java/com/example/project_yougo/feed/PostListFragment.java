package com.example.project_yougo.feed;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project_yougo.R;
import com.example.project_yougo.model.user.User;
import com.example.project_yougo.model.user.UserModel;
import com.example.project_yougo.model.post.Post;
import com.example.project_yougo.model.post.PostModel;
import com.squareup.picasso.Picasso;

import java.util.List;
import androidx.lifecycle.Observer;


public class PostListFragment extends Fragment {
    private RecyclerView postRecyclerView;
    private PostListViewModel postListViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_list, container, false);
        postRecyclerView = view.findViewById((R.id.postlist_rv));
        postRecyclerView.setHasFixedSize(true);
        postRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        initPostList();


        setHasOptionsMenu(true);

        return view;
    }

    private void initPostList() {
//        PostModel.getInstance().loadPostList(getActivity().getApplicationContext(), new PostModel.PostListLoadListener() {
//            @Override
//            public void onPostListLoaded(List<Post> postList) {
//
//            }
//        });
//        PostModel.getInstance().listenForPostListUpdates(getActivity().getApplicationContext(), new PostModel.PostListUpdateListener() {
//            @Override
//            public void onPostListUpdated(List<Post> postList) {
//                updatePostRecyclerView(postList);
//            }
//        });

        postListViewModel = new ViewModelProvider(this).get(PostListViewModel.class);
        Observer<List<Post>> observer = new Observer<List<Post>>() {
            @Override
            public void onChanged(List<Post> posts) {
                updatePostRecyclerView(posts);
            }
        };

        postListViewModel.getPostListLiveData(getViewLifecycleOwner(), this)
                .observe(getViewLifecycleOwner(), observer);
    }

    private void updatePostRecyclerView(List<Post> postList) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                MyAdapter adapter = new MyAdapter(postList);
                adapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        //TODO fixe that
                        // Navigation.findNavController(v).navigate(PostListFragmentDirections.actionPostListFragmentToProfileFragment());
                    }
                });
                postRecyclerView.setAdapter(adapter);
           //     postRecyclerView.invalidate();
            }
        });
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView userName;
        TextView rowPostFreeTextTextView;
        TextView rowPostDifficultyTextView;
        TextView rowPostTypeOfWorkoutTextView;
        ImageView userImg;
        ImageButton likeBtn;
        ImageButton commentBtn;
        ImageButton addCommentBtn;
        ImageView postImg;



        public MyViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName_list_row);
            rowPostFreeTextTextView = itemView.findViewById(R.id.rowPostFreeTextTextView);
            rowPostDifficultyTextView = itemView.findViewById(R.id.rowPostDifficultyTextView);
            rowPostTypeOfWorkoutTextView = itemView.findViewById(R.id.rowPostTypeOfWorkoutTextView);
            userImg = itemView.findViewById(R.id.userImg_list_row);
            likeBtn = itemView.findViewById(R.id.like_btn_row);
            commentBtn = itemView.findViewById(R.id.comment_btn_row);
            addCommentBtn = itemView.findViewById(R.id.add_comment_btn_row);
            postImg = itemView.findViewById(R.id.post_img_row);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    listener.onItemClick(v, pos);
                }
            });
        }
        public void bind(User user,Post post){
            userName.setText(user.fullname());
            rowPostFreeTextTextView.setText(post.getFreeText());
            rowPostTypeOfWorkoutTextView.setText(post.getTypeOfWorkout());
            rowPostDifficultyTextView.setText(post.getDifficulty());
            userImg.setImageResource(R.drawable.avatar);
            if(user.getImageUrl() != null) {
                Picasso.get()
                        .load(user.getImageUrl())
                        .into(userImg);
            }
            postImg.setImageResource(R.drawable.demo_map);
            if(post.getPostImgUrl() != null) {
                Picasso.get()
                        .load(post.getPostImgUrl())
                        .into(postImg);
            }
        }
    }

    interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private OnItemClickListener listener;
        private List<Post> postList;
        private UserViewModel userViewModel;

        public MyAdapter(List<Post> postList) {
            this.postList = postList;
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.postlist_row, parent, false);
            MyViewHolder holder = new MyViewHolder(view, listener);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Post post = postList.get(position);

            userViewModel = new ViewModelProvider(PostListFragment.this).get(UserViewModel.class);

            Observer<User> observer = new Observer<User>() {
                @Override
                public void onChanged(User user) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            // might be null if user has not been downloaded from firebase db into local db
                            if(user != null) {
                                holder.bind(user,post);
                            }
                        }
                    });
                }
            };

            userViewModel.getUserLiveData(post.getPublisherId(), getViewLifecycleOwner(), PostListFragment.this)
                    .observe(getViewLifecycleOwner(), observer);

//            UserModel.getInstance().getUserById(post.getPublisherId(), new UserModel.GetUserCompleteListener() {
//                @Override
//                public void onComplete(User user) {
//                    new Handler(Looper.getMainLooper()).post(new Runnable() {
//                        @Override
//                        public void run() {
//                            holder.userName.setText(user.fullname());
//                            holder.rowPostFreeTextTextView.setText(post.getFreeText());
//                            holder.rowPostTypeOfWorkoutTextView.setText(post.getTypeOfWorkout());
//                            holder.rowPostDifficultyTextView.setText(post.getDifficulty());
//                        }
//                    });
//                }
//            });

            holder.commentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavDirections navDirections = PostListFragmentDirections.actionPostListFragmentToCommentListFragment2(post.getId());
                    Navigation.findNavController(v).navigate(navDirections);
                }
            });
            holder.addCommentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavDirections navDirections = PostListFragmentDirections.actionPostListFragmentToAddCommentFragment(post.getId());
                    Navigation.findNavController(v).navigate(navDirections);
                }
            });
        }

        @Override
        public int getItemCount() {
            return postList.size();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public static class PostListViewModel extends ViewModel {
        private LiveData<List<Post>> postListLiveData;

        public LiveData<List<Post>> getPostListLiveData(LifecycleOwner lifecycleOwner,
                                                        ViewModelStoreOwner viewModelStoreOwner) {
            if(postListLiveData == null)
                postListLiveData = PostModel.getInstance().getPostListLiveData(viewModelStoreOwner,
                        lifecycleOwner);
            return postListLiveData;
        }
    }

}
