package com.example.project_yougo.model.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.project_yougo.model.comment.Comment;

import java.util.List;

@Dao
public interface CommentDao {
    @Query("SELECT * FROM comment")
    LiveData<List<Comment>> getAll();

    @Query("SELECT * FROM comment where postId=:postId")
    LiveData<List<Comment>> getOfPost(String postId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Comment... comment);

    @Delete
    void delete(Comment comment);

    @Query("DELETE FROM comment")
    void deleteAll();
}
