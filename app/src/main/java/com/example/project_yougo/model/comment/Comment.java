package com.example.project_yougo.model.comment;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity
public class Comment {
    @PrimaryKey
    @NotNull
    private String id;

    @ColumnInfo(name = "publisherId")
    private String publisherId;

    @ColumnInfo(name = "postId")
    private String postId;

    @ColumnInfo(name = "timestamp")
    private long timestamp; // server timestamp in milliseconds

    @ColumnInfo(name = "content")
    private String content;

    public Comment() {

    }

    public Comment(@NotNull String id, String publisherId, String postId, long timestamp, String content) {
        this.id = id;
        this.publisherId = publisherId;
        this.postId = postId;
        this.timestamp = timestamp;
        this.content = content;
    }

    @NotNull
    public String getId() {
        return id;
    }

    public void setId(@NotNull String id) {
        this.id = id;
    }

    public String getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
