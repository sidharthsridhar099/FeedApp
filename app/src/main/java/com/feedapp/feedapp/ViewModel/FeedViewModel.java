package com.feedapp.feedapp.ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.feedapp.feedapp.Config;
import com.feedapp.feedapp.Model.PostModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class FeedViewModel extends ViewModel {
    FirebaseDatabase mFireBaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference mFireBaseRef;
    List<PostModel> mPost = new ArrayList<>();
    static String lastPostID = "";
    Query query1, query2;

    private MutableLiveData<List<PostModel>> posts;

    public FeedViewModel(String lastPostID) {
        this.lastPostID = lastPostID;
    }

    public FeedViewModel() {
        this.lastPostID = "";
    }


    public LiveData<List<PostModel>> getPosts() {
        //posts = null;
        if (posts == null) {
            posts = new MutableLiveData<List<PostModel>>();
            mFireBaseRef = mFireBaseDatabase.getReference(Config.POST_NODE);
            loadPost();
        }
        return posts;
    }

    private void loadPost() {
        if (lastPostID.equals("")) {
            query1 = mFireBaseRef.limitToLast(50);
            query1.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Log.d(" SID : ", "onChildAdded ");
                    mPost.add(dataSnapshot.getValue(PostModel.class));
                    lastPostID = dataSnapshot.getKey();
                    posts.postValue(mPost);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    String key = dataSnapshot.getKey();
                    for (int i = 0; i < mPost.size(); i++) {
                        if (key.equals(mPost.get(i).getPostId())) {
                            PostModel data = dataSnapshot.getValue(PostModel.class);
                            Log.d(" SID : ", "New Data : " + data.toString());
                            mPost.set(i, data);
                            posts.postValue(mPost);
                        }
                    }
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    String key = dataSnapshot.getKey();
                    for (int i = 0; i < mPost.size(); i++) {
                        if (key.equals(mPost.get(i).getPostId())) {
                            mPost.remove(i);
                        }
                    }
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d(" SID : ", "onCancelled " + databaseError.toString());

                }
            });

        } else {
            query2 = mFireBaseRef.limitToFirst(80);
            query2.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Log.d(" SID : ", "onChildAdded ");

                    mPost.add(dataSnapshot.getValue(PostModel.class));
                    lastPostID = dataSnapshot.getKey();
                    posts.postValue(mPost);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    String key = dataSnapshot.getKey();
                    for (int i = 0; i < mPost.size(); i++) {
                        if (key.equals(mPost.get(i).getPostId())) {
                            PostModel data = dataSnapshot.getValue(PostModel.class);
                            Log.d(" SID : ", "CHANGED ");
                            Log.d(" SID : ", "New Data : " + data.toString());
                            mPost.set(i, data);
                            posts.postValue(mPost);
                        }
                    }
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    String key = dataSnapshot.getKey();
                    for (int i = 0; i < mPost.size(); i++) {
                        if (key.equals(mPost.get(i).getPostId())) {
                            mPost.remove(i);
                        }
                    }
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d(" SID : ", "onCancelled " + databaseError.toString());

                }
            });
            Log.d(" SID : ", " Subsequent Load ");
        }

    }



}



