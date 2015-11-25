package com.csm.smartcity.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.csm.smartcity.R;
import com.csm.smartcity.catagoryIdeas.CatagoryIdeaActivity;
import com.csm.smartcity.model.CatagoryDataObject;
import com.joanzapata.iconify.widget.IconTextView;

import java.util.ArrayList;

/**
 * Created by arundhati on 11/23/2015.
 */
public class CatagoryListAdapter extends RecyclerView.Adapter {
        private ArrayList<CatagoryDataObject> mDataset;

        public CatagoryListAdapter(ArrayList<CatagoryDataObject> myDataset, RecyclerView recyclerView) {
                mDataset=myDataset;
                }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                RecyclerView.ViewHolder vh;
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.catagory_listitem, parent, false);
                vh = new CatagoryListViewHolder(v);
                return vh;
                }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
                if (holder instanceof CatagoryListViewHolder) {
                ((CatagoryListViewHolder)holder).catagoryName.setText(mDataset.get(position).getStrCategoryDesc());

                    String ideaCount=mDataset.get(position).getStrIdeaCount();
                    if(ideaCount.equals("")){
                        ideaCount="No";
                    }

                ((CatagoryListViewHolder)holder).catagoryCount.setText(ideaCount);
                ((CatagoryListViewHolder)holder).cardLinearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent=new Intent(v.getContext(), CatagoryIdeaActivity.class);
                        intent.putExtra("catagory_id", mDataset.get(position).getStrCategoryID());
                        intent.putExtra("catagory_desc", mDataset.get(position).getStrCategoryDesc());
                        v.getContext().startActivity(intent);
                        //Adding transition animation to open the page
                        ((Activity)v.getContext()).overridePendingTransition(R.animator.push_up_in, R.animator.push_up_out);

                    }
                });


                }
                }

        @Override
        public int getItemCount() {
                return mDataset.size();
                }


        public static class CatagoryListViewHolder extends RecyclerView.ViewHolder
        {
            IconTextView catagoryName;
            TextView catagoryCount;
            LinearLayout cardLinearLayout;

            public CatagoryListViewHolder(View itemView) {
                super(itemView);
                catagoryName=(IconTextView)itemView.findViewById(R.id.catagoryName);
                catagoryCount=(TextView)itemView.findViewById(R.id.catagoryCount);
                cardLinearLayout=(LinearLayout)itemView.findViewById(R.id.cardLinearLayout);
            }
        }
}
