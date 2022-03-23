package com.example.project_yougo.model.local;

import androidx.room.Database;

import com.example.project_yougo.MyApplication;
import com.example.project_yougo.model.comment.Comment;
import com.example.project_yougo.model.post.Post;
import com.example.project_yougo.model.user.User;

import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Post.class, Comment.class, User.class}, version=3)
public abstract class LocalDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "LOCAL_DATABASE";
    private static LocalDatabase instance;

    public abstract PostDao postDao();
    public abstract CommentDao commentDao();
    public abstract UserDao userDao();

    public static LocalDatabase getInstance() {
        if(instance == null) {
            instance = Room.databaseBuilder(MyApplication.getContext(),
                    LocalDatabase.class, DATABASE_NAME).
                    fallbackToDestructiveMigration().build();
        }

        return instance;
    }
}
