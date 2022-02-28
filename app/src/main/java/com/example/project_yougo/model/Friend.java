package com.example.project_yougo.model;

public class Friend {
    String name = "";
    String id = "";
    boolean follow = false;


    public Friend() {}

    public Friend(String name, String id, boolean flag) {
        this.name = name;
        this.id = id;
        this.follow = flag;
    }

    public void setId(String id) {
        this.id = id;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name+getId();
    }

    public String getId() {
        return id;
    }

    public boolean isFollow() {
        return follow;
    }

    public void setFollow(boolean follow) {
        this.follow = follow;
    }

}
