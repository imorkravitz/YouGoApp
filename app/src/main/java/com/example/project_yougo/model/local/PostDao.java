package com.example.project_yougo.model.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.project_yougo.model.post.Post;

import java.util.List;

@Dao
public interface PostDao {
    @Query("SELECT * FROM post")
    LiveData<List<Post>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Post... posts);

    @Delete
    void delete(Post post);

    @Query("DELETE FROM post where id=:postId")
    void deleteById(String postId);

    @Query("DELETE FROM post")
    void deleteAll();
}
