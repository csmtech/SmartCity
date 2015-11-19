package com.csm.smartcity.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.csm.smartcity.R;
import com.csm.smartcity.ideaComment.IdeaCommentActivity;
import com.joanzapata.iconify.widget.IconTextView;

import java.util.ArrayList;

/**
 * Created by arundhati on 11/18/2015.
 */
//fa-thumbs-up
//fa-comments
/**
 * Created by arundhati on 11/12/2015.
 */
public class UserListAdapter extends RecyclerView.Adapter {
    private ArrayList<String> mDataset;

    public UserListAdapter(ArrayList<String> myDataset, RecyclerView recyclerView) {
        mDataset=myDataset;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.invite_listitem, parent, false);
        vh = new UserListViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof UserListViewHolder) {



        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    public static class UserListViewHolder extends RecyclerView.ViewHolder
    {

        public UserListViewHolder(View itemView) {
            super(itemView);

        }
    }

}




