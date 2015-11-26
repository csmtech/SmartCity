package com.csm.smartcity.information;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.csm.smartcity.R;
import com.csm.smartcity.adapter.RecycleViewCardAdapter;
import com.csm.smartcity.adapter.RecycleVieweVartaAdapter;
import com.csm.smartcity.common.AppCommon;
import com.csm.smartcity.common.AppController;
import com.csm.smartcity.common.ColoredSnackbar;
import com.csm.smartcity.common.CommonDialogs;
import com.csm.smartcity.model.eVartaModel;
import com.csm.smartcity.utils.OnLoadMoreListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;

import java.util.ArrayList;
import java.util.List;

public class eVartaActivity extends AppCompatActivity {
    RecycleVieweVartaAdapter adapter;
    private SwipeRefreshLayout mSwipeRefreshLayout = null;

    RecyclerView recyclerView;
    ArrayList<eVartaModel> demoData;
    private String LOAD_TYPE="";
    final String tag_json_obj = "json_obj_req";
    private JSONArray eVartaData = null;
    protected Handler loadMorehandler;
    private ProgressBar progress_connect;
   // Context c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e_varta);
        recyclerView =  (RecyclerView)findViewById(R.id.evarta_recycle_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        demoData = new ArrayList<eVartaModel>();
        adapter = new RecycleVieweVartaAdapter(demoData,recyclerView);
        recyclerView.setAdapter(adapter);
        progress_connect=(ProgressBar)findViewById(R.id.prog_connect);
        mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.eVartaswipeRefresh);
        //Initialize swipe to refresh view
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                String loadTop = AppCommon.getURL() + "geteVartaData/0";
                geteVartaData(loadTop, "LOAD_TOP");

            }
        });
 /* Load More Items on Infinite Scroll*/
        loadMorehandler=new Handler();
        adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                //add null , so the adapter will check view_type and show progress bar at bottom
                demoData.add(null);
                adapter.notifyItemInserted(demoData.size() - 1);
                loadMorehandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //   remove progress item
                        //changeAgentArrayList.remove(changeAgentArrayList.size() - 1);
                        demoData.remove(null);
                        adapter.notifyItemRemoved(demoData.size());
                        //add items one by one
                        int start = demoData.size();
                        // int end = start + 10;
                        if(!AppCommon.isNetworkAvailability(getApplicationContext())) {
                            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), CommonDialogs.INTERNET_UNAVAILABLE, Snackbar.LENGTH_LONG);
                            ColoredSnackbar.confirm(snackbar).show();
                        }
                        else {
                            String loadMr = AppCommon.getURL() + "geteVartaData/" + start;
                            geteVartaData(loadMr, "LOAD_MORE");
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
                String url = AppCommon.getURL() + "geteVartaData/0";
                geteVartaData(url, "LOAD_DEFAULT");
            }


        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void geteVartaData(String url,String loadType){
        LOAD_TYPE=loadType;

            final JsonObjectRequest jsonObjReq = loadeVartaJSONData(url);
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(30000, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }
    private JsonObjectRequest loadeVartaJSONData(String url) {
        // AppCommon.showDialog("Loading...",this);
        progress_connect.setVisibility(View.VISIBLE);
        Log.i("atag",url);
        eVartaData=null;
        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            eVartaData=response.getJSONArray("geteVartaDataResult");
                            Log.i("atag", "hello" + eVartaData.toString());
                              bindDataToAdepter(eVartaData);
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
            Type type = new TypeToken<ArrayList<eVartaModel>>() {
            }.getType();
            ArrayList<eVartaModel> tempArrayList = converter.fromJson(String.valueOf(arraydata), type);
            switch (LOAD_TYPE) {
                case "LOAD_TOP":
                    demoData.clear();
                    for(int i=0; i<tempArrayList.size(); i++) {
                        demoData.add(tempArrayList.get(i));
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case "LOAD_DEFAULT":
                    demoData.clear();
                    for (int i = 0; i < tempArrayList.size(); i++) {
                        demoData.add(tempArrayList.get(i));
                    }
                    adapter.notifyItemInserted(demoData.size());
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
                        demoData.add(tempArrayList.get(i));
                    }
                    Log.i("atag", "MOre" + demoData.size());
                    adapter.notifyItemInserted(demoData.size());
                    //adapter.setLoaded();
                    break;
            }
            if(mSwipeRefreshLayout!=null) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }catch (Exception e) {
            Log.i("atag","catch"+e.getMessage());
        }
    }
    public void onHome(View v)

    {
        startActivity(new Intent(this, InformationActivity.class));
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
