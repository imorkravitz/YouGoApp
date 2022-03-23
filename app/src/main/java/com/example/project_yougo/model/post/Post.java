package com.example.project_yougo.model.post;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Post {
    final public static String COLLECTION_NAME = "posts";
    //TODO: עבור כל משתמש שמסתכל כרגע על הפוסט רק לבדוק אם הוא עשה לייק על הפוסט או לא
    @PrimaryKey
    @NotNull
    private String id;

    @ColumnInfo(name = "publisherId")
    private String publisherId;

    @ColumnInfo(name = "freeText")
    private String freeText;

    @ColumnInfo(name = "difficulty")
    private String difficulty;

    @ColumnInfo(name = "typeOfWorkout")
    private String typeOfWorkout;

    @ColumnInfo(name = "timestamp")
    private long timestamp; // server timestamp in milliseconds

    @ColumnInfo(name = "postImgUrl")
    private String postImgUrl;

    @ColumnInfo(name = "longitude")
    private double longitude;

    @ColumnInfo(name = "latitude")
    private double latitude;

    public Post() { }

    public Post(String id, String publisherId, String freeText,
                String difficulty, String typeOfWorkout,  double longitude, double latitude, long timestamp) {
        this.id = id;
        this.publisherId = publisherId;
        this.freeText = freeText;
        this.difficulty = difficulty;
        this.typeOfWorkout = typeOfWorkout;
        this.timestamp = timestamp;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Post(String id, String publisherId, String freeText,
                String difficulty, String typeOfWorkout, long timestamp, double longitude, double latitude, String url) {
        this.id = id;
        this.publisherId = publisherId;
        this.freeText = freeText;
        this.difficulty = difficulty;
        this.typeOfWorkout = typeOfWorkout;
        this.timestamp = timestamp;
        this.postImgUrl = url;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public static String getCollectionName() {
        return COLLECTION_NAME;
    }

    public String getId() {
        return id;
    }

    public String getPublisherId() {
        return publisherId;
    }

    public String getFreeText() {
        return freeText;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getTypeOfWorkout() {
        return typeOfWorkout;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }

    public void setFreeText(String freeText) {
        this.freeText = freeText;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public void setTypeOfWorkout(String typeOfWorkout) {
        this.typeOfWorkout = typeOfWorkout;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getPostImgUrl() {
        return this.postImgUrl;
    }

    public void setPostImgUrl(String postImgUrl) {
        this.postImgUrl = postImgUrl;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    //
//    public Map<String, Object> toJson() {
//        Map<String, Object> json=new HashMap<String, Object>();
//        json.put("publisher id",publisherId);
//        json.put("description",description);
//   //     json.put("likes",likes);
//        return json;
//    }

//    public static Post create(Map<String, Object> json) {
//        String publisherId= (String) json.get("publisher id");
//        String description=(String) json.get("description");
//        String date=(String) json.get("date");
//        String time=(String) json.get("time");
//        String id=(String) json.get("id");
//        List<Comment> commentList= (List<Comment>) json.get("comment list");
//        int likes= (int) json.get("likes");
//      //  Post post=new Post(publisherId,description,date,time,likes);
//        return null;
//    }
}

