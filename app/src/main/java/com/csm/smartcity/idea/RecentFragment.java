package com.csm.smartcity.idea;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.csm.smartcity.R;
import com.csm.smartcity.adapter.RecycleViewCardAdapter;

import java.util.ArrayList;


public class RecentFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout = null;
    private ProgressBar progress_connect;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecycleViewCardAdapter mAdapter;
    private ArrayList<String> recentArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_recent, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycle_view_recent);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.recentswipeRefresh);
        progress_connect=(ProgressBar)rootView.findViewById(R.id.prog_connect);
        mRecyclerView.setHasFixedSize(true);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recentArrayList=new ArrayList<String>();
        recentArrayList.add("1");
        recentArrayList.add("2");
        recentArrayList.add("3");
        recentArrayList.add("4");
        recentArrayList.add("5");
        recentArrayList.add("6");
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecycleViewCardAdapter(recentArrayList,mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
    }


}
