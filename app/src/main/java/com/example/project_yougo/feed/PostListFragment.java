package com.example.project_yougo.feed;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project_yougo.R;


public class PostListFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_list, container, false);
        RecyclerView list = view.findViewById((R.id.postlist_rv));
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        MyAdapter adapter = new MyAdapter();
        list.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                //TODO fix that
               // Navigation.findNavController(v).navigate(PostListFragmentDirections.actionPostListFragmentToProfileFragment());
            }
        });
        setHasOptionsMenu(true);
        return view;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView userName;
        TextView description;
        ImageView userImg;
        ImageButton likeBtn;
        ImageButton commentBtn;

        public MyViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName_list_row);
            description = itemView.findViewById(R.id.postdescription_row);
            userImg = itemView.findViewById(R.id.userImg_list_row);
            likeBtn = itemView.findViewById(R.id.like_btn_row);
            commentBtn = itemView.findViewById(R.id.comment_btn_row);
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

    class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

        OnItemClickListener listener;

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
//            Student student = data.get(position);
//            holder.nameTv.setText(student.getName());
//            holder.idTv.setText(student.getId());
//            holder.cb.setChecked(student.isFlag());
        }

        @Override
        public int getItemCount() {
            return 5;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
