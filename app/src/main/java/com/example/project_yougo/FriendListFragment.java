package com.example.project_yougo;

import com.example.project_yougo.model.Model;
import com.example.project_yougo.model.Friend;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project_yougo.model.Friend;
import com.example.project_yougo.model.Model;

import java.util.List;


public class FriendListFragment extends Fragment {
    MyAdapterListFriends adapterListFriends;
    List<Friend> data;
        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_friend_list, container, false);
        data = Model.instance.getAllFriends();
        RecyclerView list = view.findViewById(R.id.friendslist_rv);


        list.setLayoutManager(new LinearLayoutManager(getContext()));

        list.setHasFixedSize(true);
        adapterListFriends = new MyAdapterListFriends();

        list.setAdapter(adapterListFriends);
        adapterListFriends.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Log.d("Tag", "click on position" + position);
                }
        });
        return view;
    }

    class MyViewHolderListFriends extends RecyclerView.ViewHolder{
        TextView nameTv;
        Button followBtn;
        ImageView avatarImg;

        public MyViewHolderListFriends(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.friendsList_name_tv);
            followBtn = itemView.findViewById(R.id.friendsList_follow_btn);
            avatarImg = itemView.findViewById(R.id.friendsList_avatar_img);
            followBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(followBtn.getText() == "Follow") {
                        followBtn.setText("Unfollow");
                        data.get(pos).setFollow(true);
                    }
                    else {
                        followBtn.setText("Follow");
                        data.get(pos).setFollow(false);
                    }
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    listener.onItemClick(pos);
                }
            });
        }


    }

    interface OnItemClickListener{
        void onItemClick(int position);
    }

    class MyAdapterListFriends extends RecyclerView.Adapter<MyViewHolderListFriends>{

        OnItemClickListener listener;
        public void setOnItemClickListener(OnItemClickListener listener){
            this.listener = listener;
        }

        @NonNull
        @Override
        public MyViewHolderListFriends onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.find_friends_row,parent,false);
            MyViewHolderListFriends holder = new MyViewHolderListFriends(view,listener);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolderListFriends holder, int position) {
            Friend friend = data.get(position);
            holder.nameTv.setText(friend.getName());
            if(friend.isFollow()){
                holder.followBtn.setText("Unfollow");

            }
            else
                holder.followBtn.setText("Follow");

        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }
}