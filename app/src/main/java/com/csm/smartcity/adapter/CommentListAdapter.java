package com.csm.smartcity.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.csm.smartcity.R;
import com.csm.smartcity.common.AppCommon;
import com.csm.smartcity.common.UtilityMethods;
import com.csm.smartcity.ideaComment.IdeaCommentActivity;
import com.csm.smartcity.model.CommentDataObject;
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
public class CommentListAdapter extends RecyclerView.Adapter {
    private ArrayList<CommentDataObject> mDataset;

    public CommentListAdapter(ArrayList<CommentDataObject> myDataset, RecyclerView recyclerView) {
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
            ((UserListViewHolder)holder).txtName.setText(UtilityMethods.getSentencecaseString(mDataset.get(position).getUSER_NAME()));
            ((UserListViewHolder)holder).txtDesc.setText(mDataset.get(position).getCOMMENTS());
            ((UserListViewHolder)holder).txtday.setText(mDataset.get(position).getCOMMENT_DATE());
            String userUrl= AppCommon.getUserPhotoURL()+mDataset.get(position).getUSER_PIC();
            new UtilityMethods.LoadProfileImage(((UserListViewHolder)holder).imgUserImage).execute(userUrl);
        }

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    public static class UserListViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imgUserImage;
        TextView txtName;
        TextView txtDesc;
        TextView txtday;
        public UserListViewHolder(View itemView) {
            super(itemView);
            imgUserImage=(ImageView)itemView.findViewById(R.id.imgUserImage);
            txtName=(TextView)itemView.findViewById(R.id.txtName);
            txtDesc=(TextView)itemView.findViewById(R.id.txtDesc);
            txtday=(TextView)itemView.findViewById(R.id.txtday);

        }
    }

}




