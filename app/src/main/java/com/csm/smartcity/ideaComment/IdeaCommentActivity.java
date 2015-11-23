package com.csm.smartcity.ideaComment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.csm.smartcity.R;
import com.csm.smartcity.adapter.CommentListAdapter;
import com.csm.smartcity.common.AppCommon;
import com.csm.smartcity.common.AppController;
import com.csm.smartcity.common.ColoredSnackbar;
import com.csm.smartcity.common.CommonDialogs;
import com.csm.smartcity.connection.PostJsonStringRequest;
import com.csm.smartcity.model.CommentDataObject;
import com.csm.smartcity.model.UserDataObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.joanzapata.iconify.widget.IconButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class IdeaCommentActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ArrayList<CommentDataObject> commentArrayList;
    private RecyclerView.LayoutManager mLayoutManager;
    private CommentListAdapter mAdapter;
    EditText commentetxt;
    IconButton postBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idea_comment);
        commentetxt=(EditText)findViewById(R.id.commentetxt);
        postBtn=(IconButton)findViewById(R.id.postBtn);

        mRecyclerView = (RecyclerView)findViewById(R.id.comment_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        commentArrayList=new ArrayList<CommentDataObject>();

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new CommentListAdapter(commentArrayList,mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        String idea_id=getIntent().getExtras().getString("ideaId");
        getCommentList(idea_id);

        postBtn.setOnClickListener(postComment(idea_id));

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

    private void getCommentList(String idea_id){
        String url = AppCommon.getURL() + "getIdeaCommentDetail/"+idea_id;
        AppCommon.showDialog("Loading...",this);
        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // AppCommon.hideDialog();
                            JSONArray data=response.getJSONArray("getIdeaCommentDetailResult");
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
        Type type = new TypeToken<ArrayList<CommentDataObject>>(){}.getType();
        ArrayList<CommentDataObject> tempArrayList=converter.fromJson(String.valueOf(list_data), type);

        commentArrayList.clear();
        for(int i=0; i<tempArrayList.size(); i++) {
            commentArrayList.add(tempArrayList.get(i));
        }
        mAdapter.notifyDataSetChanged();
        AppCommon.hideDialog();
    }

    public View.OnClickListener postComment(final String ideaId){
        return new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                if (AppCommon.isNetworkAvailability(v.getContext())) {
                    if (AppCommon.isLogin(v.getContext())) {
                        AppCommon.showDialog("Registering....", v.getContext()); //Showing registering dialog
                        Map<String, String> params = new HashMap<String, String>();
                        Log.i("atag",AppCommon.getLoginPrefData(v.getContext()).getCITIZEN_ID()+":::::::"+ideaId+"::::::::"+commentetxt.getText().toString());
                        try {

                            params.put("strUser_ID", AppCommon.getLoginPrefData(v.getContext()).getCITIZEN_ID());
                            params.put("streIdea_ID", ideaId);
                            params.put("strComments", commentetxt.getText().toString());

                        } catch (Exception e) {
                        }

                        String url = AppCommon.getURL() + "insertIdeaComment";
                        PostJsonStringRequest jsonObjReq = new PostJsonStringRequest(
                                url, new JSONObject(params),
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                          AppCommon.hideDialog();
                                          Log.i("atag",response);
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                AppCommon.hideDialog();
                            }
                        });
                        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                                90000,
                                -1,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        AppController.getInstance().addToRequestQueue(jsonObjReq, "comment_click_request");
                    }else {
                        Snackbar snackbar = Snackbar.make(v, "Please login to post complaint", Snackbar.LENGTH_LONG);
                        ColoredSnackbar.confirm(snackbar).show();
                    }
                    }else {
//                      /*If Network is not Available*/
                        Snackbar snackbar = Snackbar.make(v, CommonDialogs.INTERNET_UNAVAILABLE, Snackbar.LENGTH_LONG);
                        ColoredSnackbar.confirm(snackbar).show();
                    }
                }
        };
    }


}
