package com.csm.smartcity.userList;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.csm.smartcity.R;
import com.csm.smartcity.adapter.CommentListAdapter;
import com.csm.smartcity.adapter.UserListAdapter;
import com.csm.smartcity.common.AppCommon;
import com.csm.smartcity.common.AppController;
import com.csm.smartcity.model.UserDataObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class UsersActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ArrayList<UserDataObject> usertArrayList;
    private RecyclerView.LayoutManager mLayoutManager;
    private UserListAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        mRecyclerView = (RecyclerView)findViewById(R.id.user_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        usertArrayList=new ArrayList<UserDataObject>();
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new UserListAdapter(usertArrayList,mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        getUserList(getIntent().getExtras().getString("ideaId"));
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getUserList(String idea_id){
        String url = AppCommon.getURL() + "getIdeaLikeDetail/"+idea_id;
        AppCommon.showDialog("Loading...",this);
        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                           // AppCommon.hideDialog();
                            JSONArray data=response.getJSONArray("getIdeaLikeDetailResult");
                            Log.i("atag", data.toString());
                            bindDatainAdepter(data);
                        } catch (Exception e) {
                            AppCommon.hideDialog();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                AppCommon.hideDialog();
            }
        });
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjReq, "user_list_call");
    }

    public void bindDatainAdepter(JSONArray list_data){
        Gson converter = new Gson();
        Type type = new TypeToken<ArrayList<UserDataObject>>(){}.getType();
        ArrayList<UserDataObject> tempArrayList=converter.fromJson(String.valueOf(list_data), type);

        usertArrayList.clear();
        for(int i=0; i<tempArrayList.size(); i++) {
            usertArrayList.add(tempArrayList.get(i));
        }
        mAdapter.notifyDataSetChanged();
        AppCommon.hideDialog();
    }
}
