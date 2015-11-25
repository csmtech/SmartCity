package com.csm.smartcity.idea;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.csm.smartcity.R;
import com.csm.smartcity.adapter.CatagoryListAdapter;
import com.csm.smartcity.adapter.CommentListAdapter;
import com.csm.smartcity.adapter.RecycleViewCardAdapter;
import com.csm.smartcity.catagoryIdeas.CatagoryIdeaActivity;
import com.csm.smartcity.common.AppCommon;
import com.csm.smartcity.common.AppController;
import com.csm.smartcity.common.ColoredSnackbar;
import com.csm.smartcity.common.CommonDialogs;
import com.csm.smartcity.model.CatagoryDataObject;
import com.csm.smartcity.model.IdeaDataObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class CatagoryFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout = null;
    private ProgressBar progress_connect;
    private RecyclerView.LayoutManager mLayoutManager;
    private CatagoryListAdapter mAdapter;
    private ArrayList<CatagoryDataObject> catagoryArrayList;
    JSONArray catagoryList=null;
    LinearLayout networkUnavailable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_catagory, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycle_view_catagory);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.catagoryswipeRefresh);
        progress_connect=(ProgressBar)rootView.findViewById(R.id.prog_connect);
        mRecyclerView.setHasFixedSize(true);
        networkUnavailable=(LinearLayout)rootView.findViewById(R.id.networkUnavailable);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        catagoryArrayList=new ArrayList<CatagoryDataObject>();
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new CatagoryListAdapter(catagoryArrayList,mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);

        //getCatagoryList();
       // callServiceMethd("getIdeas/M/" + strCitizenID + "/" + catagory_id + "/0", "LOAD_DEFAULT");
        callServiceMethd("getCategoryWiseIdeas/", "LOAD_DEFAULT");

        //Initialize swipe to refresh view
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                if ((AppCommon.isSoundEnable(getActivity())).equals("true")) {
//                    slide_mp.start();
//                }
                //Refreshing data on server
                callServiceMethd("getCategoryWiseIdeas/", "LOAD_TOP");
            }
        });




    }

    private void callServiceMethd(String url,String loadType){
        networkUnavailable.setVisibility(View.GONE);
        if(AppCommon.isNetworkAvailability(getActivity().getApplicationContext())==true) {

            //Log.i("atag", "connection available");
            loadOnlineIdeaData(url,loadType);

        }else{
            bindOfflineData(url,loadType);

            Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), CommonDialogs.INTERNET_UNAVAILABLE, Snackbar.LENGTH_LONG);
            ColoredSnackbar.confirm(snackbar).show();
        }

    }


    private void bindOfflineData(String url,final String load_type){
        String fullUrl=AppCommon.getURL()+url;
        catagoryList=null;
        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(fullUrl);
        cache.invalidate(fullUrl, true);
        if (entry != null) {
            // fetch the data from cache
            try {
                String data = new String(entry.data, "UTF-8");
                try {

                    catagoryList= new JSONObject(data).getJSONArray("getCategoryWiseIdeasResult");
                    bindDataInAdapter(load_type, catagoryList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }else{

            if(mSwipeRefreshLayout!=null) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
            progress_connect.setVisibility(View.GONE);
            networkUnavailable.setVisibility(View.VISIBLE);

        }
    }


    private void loadOnlineIdeaData(String url,final String load_type){

        String fullUrl=AppCommon.getURL()+url;
        // Log.i("atag",fullUrl);

        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,fullUrl, new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            // Log.i("atag",response.toString());
                            catagoryList=response.getJSONArray("getCategoryWiseIdeasResult");
                            bindDataInAdapter(load_type,catagoryList);
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
                Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), CommonDialogs.SERVER_ERROR, Snackbar.LENGTH_LONG);
                ColoredSnackbar.confirm(snackbar).show();
            }
        });
        //Retry policy of request queue
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjReq, "catagorylist_obj_request");

    }



    private void bindDataInAdapter(String load_type,JSONArray catagoryListDtl){

        Gson converter = new Gson();
        Type type = new TypeToken<ArrayList<CatagoryDataObject>>(){}.getType();
        ArrayList<CatagoryDataObject> tempArrayList=converter.fromJson(String.valueOf(catagoryListDtl), type);
        // compArrayList = converter.fromJson(String.valueOf(response), type);

        switch(load_type){
            case "LOAD_TOP":
                catagoryArrayList.clear();
                for(int i=0; i<tempArrayList.size(); i++) {
                    catagoryArrayList.add(tempArrayList.get(i));
                }
                mAdapter.notifyDataSetChanged();
                break;
            case "LOAD_DEFAULT":
                catagoryArrayList.clear();
                for(int i=0; i<tempArrayList.size(); i++) {
                    catagoryArrayList.add(tempArrayList.get(i));
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

        }

        if(mSwipeRefreshLayout!=null) {
            mSwipeRefreshLayout.setRefreshing(false);
            progress_connect.setVisibility(View.GONE);
        }

    }

}
