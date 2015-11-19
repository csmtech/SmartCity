package com.csm.smartcity.ideaComment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.csm.smartcity.R;
import com.csm.smartcity.adapter.RecycleViewCardAdapter;
import com.csm.smartcity.adapter.UserListAdapter;

import java.util.ArrayList;


public class IdeaCommentActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ArrayList<String> recentArrayList;
    private RecyclerView.LayoutManager mLayoutManager;
    private UserListAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idea_comment);

        mRecyclerView = (RecyclerView)findViewById(R.id.comment_recycler_view);
        mRecyclerView.setHasFixedSize(true);
    }

    @Override
    protected void onStart() {
        super.onStart();

        recentArrayList=new ArrayList<String>();
        recentArrayList.add("1");
        recentArrayList.add("2");
        recentArrayList.add("3");
        recentArrayList.add("4");
        recentArrayList.add("5");
        recentArrayList.add("6");
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new UserListAdapter(recentArrayList,mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_catagory_idea, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.crossicon) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }





}
