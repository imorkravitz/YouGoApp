package com.example.project_yougo.model;

import java.util.LinkedList;
import java.util.List;

public class Model {
    public static final Model instance = new Model();

    private Model(){
        for(int i=0;i<100;i++){
            Friend s = new Friend("name",""+i,false);
            data.add(s);
        }
    }

    List<Friend> data = new LinkedList<Friend>();

    public List<Friend> getAllFriends(){
        return data;
    }

    public void addFriends(Friend friend){
        data.add(friend);
    }

    public Friend getFriendById(String FriendId) {
        for (Friend s:data) {
            if (s.getId().equals(FriendId)){
                return s;
            }
        }
        return null;
    }
}
