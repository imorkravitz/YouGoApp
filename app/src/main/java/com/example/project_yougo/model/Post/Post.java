package com.example.project_yougo.model.Post;

import com.example.project_yougo.model.User.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Post {
    final public static String COLLECTION_NAME = "posts";
    //TODO: עבור כל משתמש שמסתכל כרגע על הפוסט רק לבדוק אם הוא עשה לייק על הפוסט או לא
    String publisherId="";
    String description="";
    String date="";
    String time="";
    List<Comment> commentList;
    int likes=0;

    public String getPublisherId() { return publisherId; }

    public void setPublisherId(String publisherId) { this.publisherId = publisherId; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getDate() { return date; }

    public void setDate(String date) { this.date = date; }

    public String getTime() { return time; }

    public void setTime(String time) { this.time = time; }

    public List<Comment> getCommentList() { return commentList; }

    public void setCommentList(List<Comment> commentList) { this.commentList = commentList; }

    public int getLikes() { return likes; }

    public void setLikes(int likes) { this.likes = likes; }

    public Post() { }

    public Post(String publisherId, String description, String date, String time, List<Comment> commentList, int likes) {
        this.publisherId = publisherId;
        this.description = description;
        this.date = date;
        this.time = time;
        this.commentList = commentList;
        this.likes = likes;
    }

    public Map<String, Object> toJson() {
        Map<String, Object> json=new HashMap<String, Object>();
        json.put("publisher id",publisherId);
        json.put("description",description);
        json.put("date",date);
        json.put("time",time);
        json.put("comment list",commentList);
        json.put("likes",likes);
        return json;
    }
    public static Post create(Map<String, Object> json) {
        String publisherId= (String) json.get("publisher id");
        String description=(String) json.get("description");
        String date=(String) json.get("date");
        String time=(String) json.get("time");
        String id=(String) json.get("id");
        List<Comment> commentList= (List<Comment>) json.get("comment list");
        int likes= (int) json.get("likes");
        Post post=new Post(publisherId,description,date,time,commentList,likes);
        return post;
    }
}

