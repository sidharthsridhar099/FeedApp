package com.feedapp.feedapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.feedapp.feedapp.Model.PostModel;
import com.feedapp.feedapp.R;

import java.util.ArrayList;

public class PostDataAdapter extends RecyclerView.Adapter<PostDataAdapter.FeedViewHolder> {

    private TextView mPostTitle, mPostDescription;
    private ImageView mEditIcon, mDeleteICon;
    private ArrayList<PostModel> mPostDataArraylist;
    Context mContext;
    private EventListener eventListener;



    public PostDataAdapter(Context context, ArrayList<PostModel> mAllPostsArraylist){
        this.mPostDataArraylist = mAllPostsArraylist;
        this.mContext = context;
    }

    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_feed, viewGroup, false);
        return new FeedViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedViewHolder feedViewHolder, final int i) {

        Log.e("POSTADAPTERDATA","" + mPostDataArraylist.size());
        mPostTitle.setText(mPostDataArraylist.get(i).getPostTitle());
        mPostDescription.setText(mPostDataArraylist.get(i).getPostData());

        mDeleteICon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventListener.OnDeleteButtonClick(i);
            }
        });

        mEditIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventListener.OnEditButtonClick(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mPostDataArraylist.size();
    }


    class FeedViewHolder extends RecyclerView.ViewHolder {
        public FeedViewHolder(@NonNull View itemView) {
            super(itemView);

            mPostTitle = (TextView) itemView.findViewById(R.id.xFeedTitle);
            mPostDescription = (TextView) itemView.findViewById(R.id.xFeedContents);
            mEditIcon = (ImageView) itemView.findViewById(R.id.editIcon);
            mDeleteICon = (ImageView) itemView.findViewById(R.id.deleteIcon);
        }
    }


    public void addEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }


    public interface EventListener {
        void OnEditButtonClick(int position);

        void OnDeleteButtonClick(int position);
    }

}
