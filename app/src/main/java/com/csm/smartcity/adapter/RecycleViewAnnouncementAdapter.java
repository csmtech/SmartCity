package com.csm.smartcity.adapter;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.csm.smartcity.information.AnnouncementDtlActivity;
import com.csm.smartcity.model.AnnouncementModel;
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
public class RecycleViewAnnouncementAdapter extends RecyclerView.Adapter {
    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
Context c;
    private ProgressDialog mProgressDialog;
    private OnLoadMoreListener onLoadMoreListener;
    CustomVolleyRequestQueue customvolley;

    private ArrayList<AnnouncementModel> mDataset;
    private SparseBooleanArray selectedItems;
    private boolean loading;
    private int visibleThreshold = 10; //number of items remain to the recycler before reaching the end
    private int lastVisibleItem, totalItemCount;
    public RecycleViewAnnouncementAdapter(ArrayList<AnnouncementModel> myDataset, RecyclerView recyclerView) {
        mDataset=myDataset;
    }
    public void setLoaded() {
        loading = false;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        c=parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.announcement, parent, false);
        vh = new RecentViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ((RecentViewHolder) holder).txtNews.setText(mDataset.get(position).getANNOUNCEMENT_NAME());
        ((RecentViewHolder) holder).txtAnnouncementDate.setText(mDataset.get(position).getANNOUNCEMENT_DATE());
        ((RecentViewHolder) holder).txtNews.setOnClickListener(announcementOnclickListener(mDataset.get(position).getANNOUNCEMENT_ID()));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }
    /*COMMENT click*/
    private View.OnClickListener announcementOnclickListener(final String announcementID){

        return  new View.OnClickListener(){
            @Override
            public void onClick(View v) {
               // c.startActivity(new Intent(c, AnnouncementDtlActivity.class));
                Intent intent = new Intent(v.getContext(), AnnouncementDtlActivity.class);
                Bundle b = new Bundle();
                b.putString("announcementId", announcementID); //Your id
                intent.putExtras(b); //Put your id to your next Intent
                v.getContext().startActivity(intent);
            }

        };
    }



    public static class RecentViewHolder extends RecyclerView.ViewHolder
    {
        TextView txtNews;// ei text view achhi hmm okk run
        TextView txtAnnouncementDate;
       ;
        public RecentViewHolder(View itemView) {
            super(itemView);
            txtNews = (TextView) itemView.findViewById(R.id.txtNews);
            txtAnnouncementDate = (TextView) itemView.findViewById(R.id.txtAnnouncementDate);

        }
    }

}



