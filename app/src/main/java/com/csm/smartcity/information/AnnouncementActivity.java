package com.csm.smartcity.information;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.csm.smartcity.R;
import com.csm.smartcity.adapter.RecycleViewAnnouncementAdapter;

import com.csm.smartcity.common.AppCommon;
import com.csm.smartcity.common.AppController;
import com.csm.smartcity.common.ColoredSnackbar;
import com.csm.smartcity.common.CommonDialogs;
import com.csm.smartcity.model.AnnouncementModel;

import com.csm.smartcity.utils.OnLoadMoreListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AnnouncementActivity extends AppCompatActivity {
    RecycleViewAnnouncementAdapter adapter;
    RecyclerView recyclerView;
    ArrayList<AnnouncementModel> announceList;
    private String LOAD_TYPE="";
    final String tag_json_obj = "json_obj_req";
    private JSONArray announcementData = null;
    protected Handler loadMorehandler;
    private ProgressBar progress_connect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement);
        recyclerView =  (RecyclerView)findViewById(R.id.myList);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        announceList = new ArrayList<AnnouncementModel>();
        adapter = new RecycleViewAnnouncementAdapter(announceList,recyclerView);
        recyclerView.setAdapter(adapter);
        progress_connect=(ProgressBar)findViewById(R.id.prog_connect);
 /* Load More Items on Infinite Scroll*/
        loadMorehandler=new Handler();
        adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                //add null , so the adapter will check view_type and show progress bar at bottom
                announceList.add(null);
                adapter.notifyItemInserted(announceList.size() - 1);
                loadMorehandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //   remove progress item
                        //changeAgentArrayList.remove(changeAgentArrayList.size() - 1);
                        announceList.remove(null);
                        adapter.notifyItemRemoved(announceList.size());
                        //add items one by one
                        int start = announceList.size();
                        // int end = start + 10;
                        if(!AppCommon.isNetworkAvailability(getApplicationContext())) {
                            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), CommonDialogs.INTERNET_UNAVAILABLE, Snackbar.LENGTH_LONG);
                            ColoredSnackbar.confirm(snackbar).show();
                        }
                        else {
                            String loadMr = AppCommon.getURL() + "getAnnouncementData/" + start;
                            getAnnouncementData(loadMr, "LOAD_MORE");
                        }

                        adapter.setLoaded();
                        //or you can add all at once but do not forget to call mAdapter.notifyDataSetChanged();
                    }
                }, 2000);
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        try{
            if(!AppCommon.isNetworkAvailability(getApplicationContext())) {
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), CommonDialogs.INTERNET_UNAVAILABLE, Snackbar.LENGTH_LONG);
                ColoredSnackbar.confirm(snackbar).show();
            }
            else {
                String url = AppCommon.getURL() + "getAnnouncementData/0";
                getAnnouncementData(url, "LOAD_DEFAULT");
            }


        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void getAnnouncementData(String url,String loadType){
        LOAD_TYPE=loadType;
            final JsonObjectRequest jsonObjReq = loadAnnouncementJSONData(url);
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(30000, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);


    }
    private JsonObjectRequest loadAnnouncementJSONData(String url) {
        // AppCommon.showDialog("Loading...",this);
        progress_connect.setVisibility(View.VISIBLE);
        Log.i("atag", url);
        announcementData=null;
        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            announcementData=response.getJSONArray("getAnnouncementDataResult");
                            Log.i("atag", "hello" + announcementData.toString());
                            bindDataToAdepter(announcementData);
                        } catch (Exception e) {
                            Log.i("atag",e.getMessage());
                            e.printStackTrace();
                        }
                        // AppCommon.hideDialog();
                        progress_connect.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress_connect.setVisibility(View.GONE);
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), CommonDialogs.SERVER_ERROR, Snackbar.LENGTH_LONG);
                ColoredSnackbar.confirm(snackbar).show();

            }
        });
        return jsonObjReq;
    }
    public void bindDataToAdepter(JSONArray arraydata){
        try {

            Gson converter = new Gson();
            Type type = new TypeToken<ArrayList<AnnouncementModel>>() {
            }.getType();
            ArrayList<AnnouncementModel> tempArrayList = converter.fromJson(String.valueOf(arraydata), type);
            switch (LOAD_TYPE) {
                case "LOAD_DEFAULT":
                    announceList.clear();
                    for (int i = 0; i < tempArrayList.size(); i++) {
                        announceList.add(tempArrayList.get(i));
                    }
                    adapter.notifyItemInserted(announceList.size());
                    adapter.notifyDataSetChanged();
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
                    for (int i = 0; i < tempArrayList.size(); i++) {
                        announceList.add(tempArrayList.get(i));
                    }
                    Log.i("atag", "MOre" + announceList.size());
                    adapter.notifyItemInserted(announceList.size());
                   // adapter.setLoaded();
                    break;
            }
        }catch (Exception e) {
            Log.i("atag","catch"+e.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_back, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.regdMenuIcon) {
            finish();
           /* Intent i = new Intent(UserProfileActivity.this, SettingActivity.class);
            startActivity(i);*/
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
