package com.csm.smartcity.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.csm.smartcity.R;
import com.csm.smartcity.common.AppCommon;
import com.csm.smartcity.connection.CustomVolleyRequestQueue;
import com.csm.smartcity.ideaComment.IdeaCommentActivity;
import com.csm.smartcity.model.AnnouncementDtlModel;
import com.csm.smartcity.model.eVartaModel;
import com.csm.smartcity.utils.OnLoadMoreListener;
import com.joanzapata.iconify.widget.IconTextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
//fa-thumbs-up
//fa-comments

/**
 * Created by samarekha on 11/12/2015.
 */
public class RecycleVieweAnnouncementDtlAdapter extends RecyclerView.Adapter {
    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
Context c;
    private ProgressDialog mProgressDialog;
    private OnLoadMoreListener onLoadMoreListener;
    CustomVolleyRequestQueue customvolley;
    private ArrayList<AnnouncementDtlModel> mDataset;
    private SparseBooleanArray selectedItems;
    private boolean loading;
    private int visibleThreshold = 10; //number of items remain to the recycler before reaching the end
    private int lastVisibleItem, totalItemCount;
    public RecycleVieweAnnouncementDtlAdapter(ArrayList<AnnouncementDtlModel> myDataset, RecyclerView recyclerView) {
        mDataset=myDataset;
    }
    public void setLoaded() {
        loading = false;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        c=parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_announcement, parent, false);
        vh = new RecentViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ((RecentViewHolder) holder).txtAnnouncement.setText(mDataset.get(position).getANNOUNCEMENT_DTL_NAME());

        if(mDataset.get(position).getGetAnnouncementCommentCount().equals("")) {
            ((RecentViewHolder) holder).txtCmntcount.setVisibility(View.GONE);
        }
       else{
            if(mDataset.get(position).getGetAnnouncementCommentCount().equals("1")){
                ((RecentViewHolder) holder).txtCmntcount.setText(mDataset.get(position).getGetAnnouncementCommentCount()+" Comment");
            }else{
                ((RecentViewHolder) holder).txtCmntcount.setText(mDataset.get(position).getGetAnnouncementCommentCount()+" Comments");
            }
        }
        ((RecentViewHolder) holder).textAnnounceCmnt.setOnClickListener(cmntOnclickListener(mDataset.get(position).getAnnouncement_ID()));
        ((RecentViewHolder) holder).textAnnouncementShare.setOnClickListener(shareOnclickListener(mDataset.get(position).getAnnouncement_ID()));
        ((RecentViewHolder) holder).txtCmntcount.setOnClickListener(cmntOnclickListener(mDataset.get(position).getAnnouncement_ID()));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }
    /*COMMENT click*/
    private View.OnClickListener cmntOnclickListener(final String id){

        return  new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(),IdeaCommentActivity.class);
                intent.putExtra("announcementId",id);
                v.getContext().startActivity(intent);
                ((Activity) v.getContext()).overridePendingTransition(R.animator.push_up_in, R.animator.push_up_out);
            }

        };
    }
    /*SHARE click*/
    private View.OnClickListener shareOnclickListener(final String eVartaID){

        return  new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }

        };
    }

    public static class RecentViewHolder extends RecyclerView.ViewHolder
    {
        TextView txtAnnouncement;// ei text view achhi hmm okk run
        TextView txtCmntcount;
        IconTextView textAnnounceCmnt;
        IconTextView textAnnouncementShare;
        public RecentViewHolder(View itemView) {
            super(itemView);
            txtAnnouncement = (TextView) itemView.findViewById(R.id.txtAnnouncement);
            txtCmntcount=(TextView) itemView.findViewById(R.id.txtCmntcount);
            textAnnounceCmnt=(IconTextView) itemView.findViewById(R.id.textAnnounceCmnt);
            textAnnouncementShare=(IconTextView) itemView.findViewById(R.id.textAnnouncementShare);

        }
    }

}



