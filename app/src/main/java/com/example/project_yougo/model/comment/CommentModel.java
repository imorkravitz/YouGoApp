package com.example.project_yougo.model.comment;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.project_yougo.model.user.UserModelFirebase;
import com.example.project_yougo.model.local.LocalDatabase;
import com.example.project_yougo.model.post.Comment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CommentModel {
    public interface CommentCreationListener {
        void onCreationSuccess();
        void onCreationFailed();
    }

    public interface CommentListUpdateListener {
        void onCommentListUpdated(List<Comment> commentList);
    }

    public interface CommentListLoadListener {
        void onCommentListLoaded(List<Comment> commentList);
    }

    public static CommentModel instance;

    private CommentModel() {

    }

    public static CommentModel getInstance() {
        if(instance == null) {
            instance = new CommentModel();
        }

        return instance;
    }

    public void loadCommentList(Context appContext, String postId, CommentListLoadListener commentListLoadListener) {
        // cannot access db on UI thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Comment> commentList = LocalDatabase.getInstance(appContext).commentDao().getOfPost(postId);
                commentListLoadListener.onCommentListLoaded(commentList);
            }
        }).start();
    }

    public void listenForCommentListUpdates(Context appContext, String postId,
                                         CommentListUpdateListener commentListUpdateListener) {
        DatabaseReference databaseReference = UserModelFirebase.getInstance().getDatabaseReference();;

        databaseReference.child("comments").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Comment> commentList = new ArrayList<>();

                for(DataSnapshot dsChild : snapshot.getChildren()) {
                    commentList.add(dsChild.getValue(Comment.class));
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        LocalDatabase.getInstance(appContext).commentDao().insertAll(commentList.toArray(new Comment[0]));
                    }
                }).start();
                commentListUpdateListener.onCommentListUpdated(commentList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void addComment(String publisherId, String postId, String content, CommentCreationListener creationListener) {
        DatabaseReference databaseReference = UserModelFirebase.getInstance().getDatabaseReference();

        DatabaseReference timestampReference = databaseReference.child("timestamp");
        timestampReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // timestamp set
                long timestamp = Long.parseLong(snapshot.getValue().toString());
                String commentId = databaseReference.child("comments").child(postId).push().getKey();
                Comment comment = new Comment(commentId, publisherId, postId, timestamp, content);
                databaseReference.child("comments").child(postId).child(commentId).setValue(comment).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            creationListener.onCreationSuccess();
                        } else {
                            creationListener.onCreationFailed();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        timestampReference.setValue(ServerValue.TIMESTAMP);
    }
}
