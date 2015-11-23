package com.csm.smartcity.idea;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.csm.smartcity.R;
import com.csm.smartcity.adapter.RecycleViewCardAdapter;
import com.csm.smartcity.common.AppCommon;
import com.csm.smartcity.common.AppController;
import com.csm.smartcity.common.ColoredSnackbar;
import com.csm.smartcity.common.CommonDialogs;
import com.csm.smartcity.model.IdeaDataObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MostLikedFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout = null;
    private ProgressBar progress_connect;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecycleViewCardAdapter mAdapter;
    private ArrayList<IdeaDataObject> mostLikedArrayList;
    private String strCitizenID="0";
    private String catagory_id="0";
    JSONArray ideaData=null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       // return super.onCreateView(inflater, container, savedInstanceState);


        View rootView = inflater.inflate(R.layout.fragment_most_liked, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycle_view_mostliked);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.mostlikedswipeRefresh);
        progress_connect=(ProgressBar)rootView.findViewById(R.id.prog_connect);
        mRecyclerView.setHasFixedSize(true);

        if(AppCommon.isLogin(getActivity().getApplicationContext())){
            strCitizenID=AppCommon.getLoginPrefData(getActivity().getApplicationContext()).getCITIZEN_ID();
        }

        Log.i("atag", strCitizenID);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mostLikedArrayList=new ArrayList<IdeaDataObject>();
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecycleViewCardAdapter(mostLikedArrayList,mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);

        callServiceMethd("getIdeas/M/" + strCitizenID + "/" + catagory_id + "/0", "LOAD_DEFAULT");


        //Initialize swipe to refresh view
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                if ((AppCommon.isSoundEnable(getActivity())).equals("true")) {
//                    slide_mp.start();
//                }
                //Refreshing data on server
                // new DownloadFilesTask().execute(feedUrl);
                //callServiceMethd("allComplaint/T/0/0/0/0","LOAD_TOP");
                callServiceMethd("getIdeas/M/" + strCitizenID + "/" + catagory_id + "/0", "LOAD_TOP");

            }
        });
    }

    private void callServiceMethd(String url,String loadType){
        if(AppCommon.isNetworkAvailability(getActivity().getApplicationContext())==true) {

            Log.i("atag", "connection available");
            loadOnlineIdeaData(url,loadType);

        }else{
            Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), CommonDialogs.INTERNET_UNAVAILABLE, Snackbar.LENGTH_LONG);
            ColoredSnackbar.confirm(snackbar).show();
        }

    }

    private void loadOnlineIdeaData(String url,final String load_type){

        String fullUrl=AppCommon.getURL()+url;
        Log.i("atag",fullUrl);

        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,fullUrl, new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            Log.i("atag",response.toString());
                            ideaData=response.getJSONArray("getIdeasResult");

                            bindDataInAdapter(load_type,ideaData);
                        } catch (Exception e) {
                            //  AppCommon.hideDialog();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //AppCommon.hideDialog();//Hidding dialog on PostJsonArrayRequest server side error
                //Custom message for server error
//                Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), CommonDialogs.SERVER_ERROR, Snackbar.LENGTH_LONG);
                //  ColoredSnackbar.confirm(snackbar).show();
            }
        });
        //Retry policy of request queue
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjReq, "recent_obj_request");

    }


    private void bindDataInAdapter(String load_type,JSONArray ideaData){


        Gson converter = new Gson();
        Type type = new TypeToken<ArrayList<IdeaDataObject>>(){}.getType();
        ArrayList<IdeaDataObject> tempArrayList=converter.fromJson(String.valueOf(ideaData), type);
        // compArrayList = converter.fromJson(String.valueOf(response), type);

        switch(load_type){
            case "LOAD_TOP":
                mostLikedArrayList.clear();
                for(int i=0; i<tempArrayList.size(); i++) {
                    mostLikedArrayList.add(tempArrayList.get(i));
                }
                mAdapter.notifyDataSetChanged();
                break;
            case "LOAD_DEFAULT":
                mostLikedArrayList.clear();
                for(int i=0; i<tempArrayList.size(); i++) {
                    mostLikedArrayList.add(tempArrayList.get(i));
                }
                mAdapter.notifyDataSetChanged();
                progress_connect.animate()
                        .translationY(0)
                        .alpha(0.0f)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                progress_connect.setVisibility(View.GONE);
                            }
                        });
                break;
            case "LOAD_MORE":
                for(int i=0; i<tempArrayList.size(); i++) {
                    mostLikedArrayList.add(tempArrayList.get(i));
                }
                mAdapter.notifyItemInserted(mostLikedArrayList.size());
                // mAdapter.setLoaded();
                break;
        }

        Log.i("atag", mostLikedArrayList.size() + ":::::::::::::");

        if(mSwipeRefreshLayout!=null) {
            mSwipeRefreshLayout.setRefreshing(false);
        }

    }

}
