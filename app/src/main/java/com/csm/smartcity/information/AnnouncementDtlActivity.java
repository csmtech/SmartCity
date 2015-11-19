package com.csm.smartcity.information;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.csm.smartcity.R;

import java.util.ArrayList;
import java.util.List;

public class AnnouncementDtlActivity extends AppCompatActivity {
    RecyclerViewAnnouncementDtlAdapter adapter;
    RecyclerView recyclerView;
    private static List<Model> demoData;
    private final String htmlText = "<body><p><img src=\"http://developer.android.com/assets/images/dac_logo.png\"/>"+
            "</p>" +
            "Geek Snack\n"+"Android M and Mediatek's Helio P10 team up for Elephone</body>";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement_dtl);
        recyclerView =  (RecyclerView)findViewById(R.id.announcementListDtl);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        demoData = new ArrayList<Model>();
        //TextVie tvText = (TextView) findViewById(R.id.text);
        for (byte i = 0; i < 20; i++) {
            Model model = new Model();
            //model.txtNews = Html.fromHtml("<p>This is the first line</p>\n" + "<p>This is the second line</p><p>Third line...</p>").toString();
            //model.txtAnnouncement= Html.fromHtml(htmlText, imgGetter, null).toString();
          /* Spanned spanned = Html.fromHtml(htmlText, new Html.ImageGetter() {
               @Override
               public Drawable getDrawable(final String source) {
                   Drawable d = null;
                   try {
                       d = getResources().getDrawable(Integer.parseInt(source));
                       d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
                   } catch (Resources.NotFoundException e) {
                      // Log.e("log_tag", "Image not found. Check the ID.", e);
                   } catch (NumberFormatException e) {
                      // Log.e("log_tag", "Source string not a valid resource ID.", e);
                   }

                   return d;
               }
           }, null);*/
            model.txtAnnouncement=htmlText;
            demoData.add(model);
        }
        adapter = new RecyclerViewAnnouncementDtlAdapter(demoData);
        recyclerView.setAdapter(adapter);
    }

    public void onHome(View v)

    {
        startActivity(new Intent(this, AnnouncementActivity.class));
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
