package com.csm.smartcity.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.csm.smartcity.R;
import com.joanzapata.iconify.widget.IconTextView;
import java.util.ArrayList;
//fa-thumbs-up
//fa-comments
/**
 * Created by arundhati on 11/12/2015.
 */
public class RecycleViewCardAdapter extends RecyclerView.Adapter {
    private ArrayList<String> mDataset;

    public RecycleViewCardAdapter(ArrayList<String> myDataset, RecyclerView recyclerView) {
        mDataset=myDataset;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_complaint, parent, false);
        vh = new RecentViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    public static class RecentViewHolder extends RecyclerView.ViewHolder
    //        implements View.OnClickListener
    {
        IconTextView compDetail;
//        CircularNetworkImageView imgUserImage;
        TextView txtCompUser;
        TextView txtTime;
        TextView txtCompArea;
        TextView txtCompStatus;
        TextView txtSupportCount;
//        NetworkImageView imgCompImage;
        TextView txtSourceIcon;
        TextView txtSupport;
        TextView txtSupportIcon;
        TextView txtShareIcon;
        TextView txtInviteIcon;
        LinearLayout layoutShare;
        LinearLayout layoutSupport;
        LinearLayout layoutInvite;
        LinearLayout layoutSupportShare;
        Button btnLocation;
        TextView txtClapCount;
        TextView txtClapIcon;
        TextView txtClap;
        LinearLayout layoutClap;
//        NetworkImageView imgResolveCompImage;
        LinearLayout layoutResolveAction;
        TextView txtUpdatedBy;
        TextView txtUpdatedOn;
        TextView txtResolvedRemark;


//        public TextView getCompDetail() {
//            return compDetail;
//        }

        public RecentViewHolder(View itemView) {
            super(itemView);
//            imgUserImage = (CircularNetworkImageView) itemView.findViewById(R.id.imgUserimage);
            txtCompUser = (TextView) itemView.findViewById(R.id.txtCompUser);
            compDetail = (IconTextView) itemView.findViewById(R.id.txtCompDetail);
            txtTime = (TextView) itemView.findViewById(R.id.txtTime);
            txtCompArea = (TextView) itemView.findViewById(R.id.txtCompArea);
           // txtCompStatus = (TextView) itemView.findViewById(R.id.txtCompStatus);
            txtSupportCount = (TextView) itemView.findViewById(R.id.txtSupportCount);
//            imgCompImage = (NetworkImageView) itemView.findViewById(R.id.imgCompImage);
           // txtSourceIcon = (TextView) itemView.findViewById(R.id.txtSourceIcon);
            //txtSupportIcon = (TextView) itemView.findViewById(R.id.txt_icon_support);
            //txtShareIcon = (TextView) itemView.findViewById(R.id.txt_icon_share);
           // txtInviteIcon = (TextView) itemView.findViewById(R.id.txt_icon_invite);
            layoutShare = (LinearLayout) itemView.findViewById(R.id.layoutShare);
            layoutSupport = (LinearLayout) itemView.findViewById(R.id.layoutSupport);
            txtSupport = (TextView) itemView.findViewById(R.id.txtSupport);
           // btnLocation = (Button) itemView.findViewById(R.id.btnLocation);
            layoutInvite = (LinearLayout) itemView.findViewById(R.id.layoutInvite);
            layoutSupportShare = (LinearLayout) itemView.findViewById(R.id.layout_support_share_bar);
            txtClapCount = (TextView) itemView.findViewById(R.id.txt_clap_count);
            layoutClap = (LinearLayout) itemView.findViewById(R.id.layout_clap);
            txtClapIcon = (TextView) itemView.findViewById(R.id.txt_clap_icon);
            txtClap = (TextView) itemView.findViewById(R.id.txt_clap);
//            imgResolveCompImage = (NetworkImageView) itemView.findViewById(R.id.img_resolve_image);
            layoutResolveAction = (LinearLayout) itemView.findViewById(R.id.layout_resolve_action);
            txtUpdatedBy = (TextView) itemView.findViewById(R.id.txt_updated_by);
            txtUpdatedOn = (TextView) itemView.findViewById(R.id.txt_updated_on);
            txtResolvedRemark = (TextView) itemView.findViewById(R.id.txt_resolve_remark);
            // dateTime = (TextView) itemView.findViewById(R.id.textView2);
            // Log.i(LOG_TAG, "Adding Listener");
           // itemView.setOnClickListener(this);
        }

//        @Override
//        public void onClick(View v) {
//            //  myClickListener.onItemClick(getAdapterPosition(), v);
//        }
    }


}



