//package com.example.project_yougo.model.post;
//
//import androidx.annotation.NonNull;
//
//import com.example.project_yougo.model.user.User;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.QueryDocumentSnapshot;
//import com.google.firebase.firestore.QuerySnapshot;
//
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//
//public class PostModelFirebase {
//    FirebaseFirestore db = FirebaseFirestore.getInstance();
//    public void getAllPosts(PostModel.GetAllPostsListener listener) {
//        db.collection(Post.COLLECTION_NAME)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        List<Post> list=new LinkedList<>();
//                        if(task.isSuccessful()){
//                            QuerySnapshot querySnapshot = task.getResult();
//                            for(QueryDocumentSnapshot doc: querySnapshot){
//                                Map<String,Object> json=doc.getData();
//                                Post post=Post.create(json);
//                                if(post!=null){
//                                    list.add(post);
//                                }
//                            }
//                        }
//                        listener.onComplete(list);
//                    }
//                });
//    }
//
//    public void addPost(Post post, PostModel.AddPostListener listener) {
//        Map<String, Object> json=post.toJson();
//
//        db.collection(Post.COLLECTION_NAME)
//                .document(post.getPublisherId())
//                .set(json)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void unused) {
//                        listener.onComplete();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        listener.onComplete();
//                    }
//                });
//    }
//
//
//    public void GetPostByPublisherId(String publisherId, PostModel.GetPostByPublisherId listener) {
//        db.collection(User.COLLECTION_NAME)
//                .document(publisherId)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        Post post=null;
//                        if(task.isSuccessful() & task.getResult()!=null){
//                            post=Post.create(task.getResult().getData());
//                        }
//                        listener.onComplete(post);
//                    }
//                });
//
//    }
//}
