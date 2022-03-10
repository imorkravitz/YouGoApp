package com.example.project_yougo.model.post;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Post {
    final public static String COLLECTION_NAME = "posts";
    //TODO: עבור כל משתמש שמסתכל כרגע על הפוסט רק לבדוק אם הוא עשה לייק על הפוסט או לא
    private String id;
    private String publisherId;
    private String description;
    private Map<String, String> timestamp;
    private int likes;

    public Post() { }


    public Post(String id, String publisherId, String description, Map<String, String> timestamp) {
        this.id = id;
        this.publisherId = publisherId;
        this.description = description;
        this.timestamp = timestamp;
        this.likes = 0;
    }

    public String getPublisherId() { return publisherId; }

    public void setPublisherId(String publisherId) { this.publisherId = publisherId; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public void setId(String id) {
        this.id = id;
    }

    public void setTimestamp(Map<String, String> timestamp) {
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public Map<String, String> getTimestamp() {
        return timestamp;
    }

    public int getLikes() { return likes; }

    public void setLikes(int likes) { this.likes = likes; }



    public Map<String, Object> toJson() {
        Map<String, Object> json=new HashMap<String, Object>();
        json.put("publisher id",publisherId);
        json.put("description",description);
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
      //  Post post=new Post(publisherId,description,date,time,likes);
        return null;
    }
}

