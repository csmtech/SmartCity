package com.csm.smartcity.catagoryIdeas;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.csm.smartcity.R;
import com.csm.smartcity.adapter.RecycleViewCardAdapter;

import java.util.ArrayList;

public class CatagoryIdeaActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout = null;
    private ProgressBar progress_connect;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecycleViewCardAdapter mAdapter;
    private ArrayList<String> catagoryArrayList;
    String heading="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catagory_idea);
        String catg_id=getIntent().getExtras().getString("catagory_id");
        if(catg_id.equals("6")){
            heading="Solid Waste Mgmt.";
        }else if(catg_id.equals("8")){
            heading="Street Light";
        }else if(catg_id.equals("10")){
            heading="Drinking Water";
        }else if(catg_id.equals("11")){
            heading="Sewage";
        }else if(catg_id.equals("12")){
            heading="Misc. Grievance";
        }
        setTitle(heading);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_view_catagory);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.catagoryswipeRefresh);
        progress_connect=(ProgressBar)findViewById(R.id.prog_connect);
        mRecyclerView.setHasFixedSize(true);

    }

    @Override
    protected void onStart() {
        super.onStart();

        catagoryArrayList=new ArrayList<String>();
        catagoryArrayList.add("1");
        catagoryArrayList.add("2");
        catagoryArrayList.add("3");
        catagoryArrayList.add("4");
        catagoryArrayList.add("5");
        catagoryArrayList.add("6");

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecycleViewCardAdapter(catagoryArrayList,mRecyclerView);
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
            //overridePendingTransition(R.animator.push_up_in, R.animator.push_up_out);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
