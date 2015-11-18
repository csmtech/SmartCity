package com.csm.smartcity.idea;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.csm.smartcity.R;
import com.csm.smartcity.adapter.RecycleViewCardAdapter;
import com.csm.smartcity.catagoryIdeas.CatagoryIdeaActivity;

import java.util.ArrayList;

public class CatagoryFragment extends Fragment implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout = null;
//    private ProgressBar progress_connect;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecycleViewCardAdapter mAdapter;
    private ArrayList<String> catagoryArrayList;
    LinearLayout soldWaste;
    LinearLayout streetLight;
    LinearLayout drinkWater;
    LinearLayout swerage;
    LinearLayout miscGriev;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_catagory, container, false);

//        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycle_view_catagory);
//        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.catagoryswipeRefresh);
////        progress_connect=(ProgressBar)rootView.findViewById(R.id.prog_connect);
//        mRecyclerView.setHasFixedSize(true);

        soldWaste=(LinearLayout)rootView.findViewById(R.id.soldWaste);
        soldWaste.setOnClickListener(this);
        streetLight=(LinearLayout)rootView.findViewById(R.id.streetLight);
        streetLight.setOnClickListener(this);
        drinkWater=(LinearLayout)rootView.findViewById(R.id.drinkWater);
        drinkWater.setOnClickListener(this);
        swerage=(LinearLayout)rootView.findViewById(R.id.swerage);
        swerage.setOnClickListener(this);
        miscGriev=(LinearLayout)rootView.findViewById(R.id.miscGriev);
        miscGriev.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View v) {
       // Toast.makeText(getActivity(),"hello",Toast.LENGTH_LONG).show();
        String tag=v.getTag().toString();
        Log.i("atag", tag);
        Intent intent=new Intent(getActivity(), CatagoryIdeaActivity.class);
        intent.putExtra("catagory_id",tag);
        startActivity(intent);
        //Adding transition animation to open the page
        getActivity().overridePendingTransition(R.animator.push_up_in, R.animator.push_up_out);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        catagoryArrayList=new ArrayList<String>();
//        catagoryArrayList.add("1");
//        catagoryArrayList.add("2");
//        catagoryArrayList.add("3");
//        catagoryArrayList.add("4");
//        catagoryArrayList.add("5");
//        catagoryArrayList.add("6");
//
//        for(int i=0;i<catagoryArrayList.size();i++){
//            mRecyclerView.setAccessibilityLiveRegion(R.layout.catagoryList);
//        }
//        mLayoutManager = new LinearLayoutManager(getActivity());
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        mAdapter = new RecycleViewCardAdapter(catagoryArrayList,mRecyclerView);
//        mRecyclerView.setAdapter(mAdapter);
    }
//    public void getCatagoryData(View v){
//        Toast.makeText(getActivity(),"hello",Toast.LENGTH_LONG).show();
//    }
}
