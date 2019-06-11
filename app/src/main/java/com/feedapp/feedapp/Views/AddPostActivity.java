package com.feedapp.feedapp.Views;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.feedapp.feedapp.Config;
import com.feedapp.feedapp.Model.PostModel;
import com.feedapp.feedapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

public class AddPostActivity extends AppCompatActivity {
    private Button mSubmit ;
    private EditText mPostTitle, mPostContent;
    FirebaseDatabase database;
    DatabaseReference postReference;
    PostModel newPost;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        mPostTitle = (EditText) findViewById(R.id.postTitle);
        mPostContent = (EditText) findViewById(R.id.postContent);
        userId = getIntent().getStringExtra("userId");

        mSubmit = (Button) findViewById(R.id.button);
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String postContent = mPostContent.getText().toString();
                String postTitle = mPostTitle.getText().toString();
                String postId = postReference.push().getKey();
                newPost = new PostModel();
                newPost.setPostId(postId);
                newPost.setPostData(postContent);
                newPost.setPostTitle(postTitle);
                newPost.setUserid(userId);
                newPost.setTimeStamp(ServerValue.TIMESTAMP);
                postReference.child(postId).setValue(newPost).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        finish();
                    }
                });
            }
        });

        database = FirebaseDatabase.getInstance();
         postReference = database.getReference(Config.POST_NODE);
    }
}
