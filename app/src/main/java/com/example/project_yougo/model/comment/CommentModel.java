package com.example.project_yougo.model.comment;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.project_yougo.model.firebase.FirebaseModel;
import com.example.project_yougo.model.firebase.FirebaseQueryLiveData;
import com.example.project_yougo.model.local.CommentDao;
import com.example.project_yougo.model.local.LocalDatabase;
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

    public interface CommentDeletionListener {
        void onDeletionSuccess();
        void onDeletionFailed();
    }

//    public interface CommentListUpdateListener {
//        void onCommentListUpdated(List<Comment> commentList);
//    }
//
//    public interface CommentListLoadListener {
//        void onCommentListLoaded(List<Comment> commentList);
//    }

    public static class CommentListDataSnapshotViewModel extends ViewModel {
        private FirebaseQueryLiveData queryLiveData;

        public LiveData<DataSnapshot> getSnapshotLiveData(String postId) {
            if(queryLiveData == null)
                queryLiveData = new FirebaseQueryLiveData(
                        FirebaseModel.getInstance().getDatabaseReference()
                                .child("comments").child(postId));
            return queryLiveData;
        }
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

    public LiveData<List<Comment>> getCommentListLiveData(String postId,
                                                       ViewModelStoreOwner viewModelStoreOwner,
                                                       LifecycleOwner lifecycleOwner) {
        CommentListDataSnapshotViewModel viewModel
                = new ViewModelProvider(viewModelStoreOwner)
                .get(CommentListDataSnapshotViewModel.class);
        Observer<DataSnapshot> observer = new Observer<DataSnapshot>() {
            @Override
            public void onChanged(DataSnapshot dataSnapshot) {
                List<Comment> commentList = new ArrayList<>();

                for(DataSnapshot dsChild: dataSnapshot.getChildren()) {
                    commentList.add(dsChild.getValue(Comment.class));
                }

                // cannot access db on UI thread
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        LocalDatabase.getInstance().commentDao().insertAll(commentList.toArray(new Comment[0]));
                    }
                }).start();
            }
        };

        viewModel.getSnapshotLiveData(postId).observe(lifecycleOwner, observer);

        return LocalDatabase.getInstance().commentDao().getOfPost(postId);
    }

    public void deleteCommentsByPostId(String postId, CommentDeletionListener listener) {
        DatabaseReference databaseReference = FirebaseModel.getInstance().getDatabaseReference();

        databaseReference.child("comments").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<DatabaseReference> commentRefsToDelete = new ArrayList<>();

                for(DataSnapshot child : snapshot.getChildren()) {
                    Comment c = child.getValue(Comment.class);
                    if(c.getPostId().equals(postId)) {
                        commentRefsToDelete.add(child.getRef());
                    }
                }

                for(DatabaseReference ref : commentRefsToDelete) {
                    ref.removeValue();
                }

                LocalDatabase.getInstance().commentDao().deleteByPostId(postId);
                listener.onDeletionSuccess();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onDeletionFailed();
            }
        });
    }
//
//    public void loadCommentList(Context appContext, String postId, CommentListLoadListener commentListLoadListener) {
//        // cannot access db on UI thread
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                List<Comment> commentList = LocalDatabase.getInstance().commentDao().getOfPost(postId);
//                commentListLoadListener.onCommentListLoaded(commentList);
//            }
//        }).start();
//    }
//
//    public void listenForCommentListUpdates(Context appContext, String postId,
//                                         CommentListUpdateListener commentListUpdateListener) {
//        DatabaseReference databaseReference = UserModelFirebase.getInstance().getDatabaseReference();;
//
//        databaseReference.child("comments").child(postId).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                List<Comment> commentList = new ArrayList<>();
//
//                for(DataSnapshot dsChild : snapshot.getChildren()) {
//                    commentList.add(dsChild.getValue(Comment.class));
//                }
//
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        LocalDatabase.getInstance().commentDao().insertAll(commentList.toArray(new Comment[0]));
//                    }
//                }).start();
//                commentListUpdateListener.onCommentListUpdated(commentList);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

    public void addComment(String publisherId, String postId, String content, CommentCreationListener creationListener) {
        DatabaseReference databaseReference = FirebaseModel.getInstance().getDatabaseReference();

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
