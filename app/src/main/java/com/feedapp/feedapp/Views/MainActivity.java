package com.feedapp.feedapp.Views;

import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.feedapp.feedapp.Config;
import com.feedapp.feedapp.Adapter.PostDataAdapter;
import com.feedapp.feedapp.Model.PostModel;
import com.feedapp.feedapp.R;
import com.feedapp.feedapp.ViewModel.FeedViewModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Spinner mSpinner;
    private String[] spinnerArray = {"All Feeds", "My Feeds"};
    private FeedViewModel mViewModel;
    private ArrayList<PostModel> mAllPostsArraylist, mMyPostArraylist;
    private PostDataAdapter mAdapter;
    private RecyclerView mPostRecyclerView;
    private String userId;
    private FirebaseDatabase database;
    private DatabaseReference postDataRef;
    public static String postID = "";
    private LinearLayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mInItObjects();
        mInItWidgets();


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                Intent intent = new Intent(MainActivity.this, AddPostActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });


        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (id == 0) {
                    setDataToAdapter(mAllPostsArraylist);
                } else {
                    setDataToAdapter(mMyPostArraylist);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        mPostRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (mSpinner.getSelectedItemId() == 0) {
                    if (!mPostRecyclerView.canScrollVertically(1)) {
                        Log.e("LAST", "" + mAllPostsArraylist.size());
                        new FeedViewModel(postID).getPosts();
                    }
                } else {
                    if (!mPostRecyclerView.canScrollVertically(1)) {
                        Log.e("LAST", "" + mMyPostArraylist.size());
                        new FeedViewModel(postID).getPosts();
                    }
                }
            }
        });

        mPostRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


            }
        });
    }


    private void mInItObjects() {
        userId = getIntent().getStringExtra("userId");
        mAllPostsArraylist = new ArrayList<>();
        mMyPostArraylist = new ArrayList<>();


        mViewModel = ViewModelProviders.of(this).get(FeedViewModel.class);
        mViewModel.getPosts().observe(this, new Observer<List<PostModel>>() {
            @Override
            public void onChanged(@Nullable List<PostModel> posts) {

                mAllPostsArraylist.clear();
                mMyPostArraylist.clear();
                Log.d("POST", "" + posts);
                for (int i = 0; i < posts.size(); i++) {
                    if (posts.get(i).getUserid().equals(userId)) {
                        mMyPostArraylist.add(posts.get(i));
                        mAllPostsArraylist.add(posts.get(i));
                        postID = posts.get(i).getPostId();
                    } else {
                        mAllPostsArraylist.add(posts.get(i));
                    }

                }
                setDataToAdapter(mAllPostsArraylist);
            }
        });


    }

    private void mInItWidgets() {

        mSpinner = (Spinner) findViewById(R.id.xSpinner);
        mPostRecyclerView = (RecyclerView) findViewById(R.id.xRecyclerView);
        mLayoutManager = new LinearLayoutManager(MainActivity.this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mPostRecyclerView.setLayoutManager(mLayoutManager);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, spinnerArray);
        mSpinner.setAdapter(adapter); // this will set list of values to spinner
    }

    private void setDataToAdapter(final ArrayList<PostModel> postArralist) {
        mAdapter = new PostDataAdapter(MainActivity.this, postArralist);

        database = FirebaseDatabase.getInstance();
        postDataRef = database.getReference(Config.POST_NODE);
        mAdapter.addEventListener(new PostDataAdapter.EventListener() {
            @Override
            public void OnEditButtonClick(int position) {
                String content = postArralist.get(position).getPostData();
                showContentEditAlert(content, postArralist.get(position).getPostId());
            }

            @Override
            public void OnDeleteButtonClick(int position) {
                Log.e("POSITION", "" + postArralist.get(position).getPostId());
                String postIdKey = postArralist.get(position).getPostId();
                postDataRef.child(postIdKey).removeValue();
            }
        });
        mPostRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void showContentEditAlert(final String content, final String postId) {

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.layout_dialog_edit_content);
        dialog.setCancelable(false);
        Button confirm_btn = (Button) dialog.findViewById(R.id.xConfirmBtn);
        Button cancel_btn = (Button) dialog.findViewById(R.id.xCancelBtn);
        final EditText et_name = (EditText) dialog.findViewById(R.id.content);
        et_name.setText(content);

        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String updatedText = et_name.getText().toString();
                Map<String, Object> updateMap = new HashMap<>();
                updateMap.put("postData", updatedText);
                postDataRef.child(postId).updateChildren(updateMap);
                dialog.dismiss();

            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

}
