package com.csm.smartcity.idea;


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

public class MostLikedFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout = null;
    private ProgressBar progress_connect;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecycleViewCardAdapter mAdapter;
    private ArrayList<String> mostLikedArrayList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       // return super.onCreateView(inflater, container, savedInstanceState);


        View rootView = inflater.inflate(R.layout.fragment_most_liked, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycle_view_mostliked);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.mostlikedswipeRefresh);
        progress_connect=(ProgressBar)rootView.findViewById(R.id.prog_connect);
        mRecyclerView.setHasFixedSize(true);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mostLikedArrayList=new ArrayList<String>();
        mostLikedArrayList.add("1");
        mostLikedArrayList.add("2");
        mostLikedArrayList.add("3");
        mostLikedArrayList.add("4");
        mostLikedArrayList.add("5");
        mostLikedArrayList.add("6");
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecycleViewCardAdapter(mostLikedArrayList,mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
    }

}
