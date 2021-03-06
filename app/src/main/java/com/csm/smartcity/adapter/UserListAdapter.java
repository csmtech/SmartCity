package com.csm.smartcity.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.csm.smartcity.R;
import com.csm.smartcity.common.AppCommon;
import com.csm.smartcity.common.UtilityMethods;
import com.csm.smartcity.model.UserDataObject;

import java.util.ArrayList;
/**
 * Created by arundhati on 11/21/2015.
 */

public class UserListAdapter extends RecyclerView.Adapter {
    private ArrayList<UserDataObject> mDataset;

    public UserListAdapter(ArrayList<UserDataObject> myDataset, RecyclerView recyclerView) {
        mDataset=myDataset;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_listitem, parent, false);
        vh = new UserListViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof UserListViewHolder) {
            ((UserListViewHolder)holder).txtName.setText(UtilityMethods.getSentencecaseString(mDataset.get(position).getUSER_NAME()));
            ((UserListViewHolder)holder).txtArea.setText(mDataset.get(position).getUSER_AREA_NAME());
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
        TextView txtArea;

        public UserListViewHolder(View itemView) {
            super(itemView);
            imgUserImage=(ImageView)itemView.findViewById(R.id.imgUserImage);
            txtName=(TextView)itemView.findViewById(R.id.txtName);
            txtArea=(TextView)itemView.findViewById(R.id.txtArea);
        }
    }

}





