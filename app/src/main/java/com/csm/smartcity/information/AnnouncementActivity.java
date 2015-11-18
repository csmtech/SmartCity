package com.csm.smartcity.information;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.csm.smartcity.R;


import java.util.ArrayList;
import java.util.List;

public class AnnouncementActivity extends AppCompatActivity {
    RecyclerViewAnnouncementAdapter adapter;
    RecyclerView recyclerView;
    private static List<Model> demoData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement);
        recyclerView =  (RecyclerView)findViewById(R.id.myList);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        demoData = new ArrayList<Model>();

        for (byte i = 0; i < 20; i++) {
            Model model = new Model();
            //model.txtNews = Html.fromHtml("<p>This is the first line</p>\n" + "<p>This is the second line</p><p>Third line...</p>").toString();
                     model.txtNews=Html.fromHtml("Geek Snack\n" +"\t\n" +"Android M and Mediatek's Helio P10 team up for Elephone").toString();
            model.txtAnnouncementDate="Oct 5 2015 at 9:30 PM";
            demoData.add(model);
        }
        adapter = new RecyclerViewAnnouncementAdapter(demoData);
        recyclerView.setAdapter(adapter);
    }
    /*public void onHome(View v)

    {
        startActivity(new Intent(this, InformationActivity.class));
    }*/
    public void onForward(View v)

    {
        startActivity(new Intent(this, AnnouncementDtlActivity.class));
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
