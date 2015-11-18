package com.csm.smartcity.idea;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.csm.smartcity.R;
import com.csm.smartcity.adapter.TabsAdapter;
import com.csm.smartcity.ideaRegistration.IdeaRegistrationActivity;
import com.melnykov.fab.FloatingActionButton;

public class IdeaActivity extends AppCompatActivity implements ActionBar.TabListener {

    private ViewPager viewPager;
    private TabsAdapter mAdapter;
    private ActionBar actionBar;
    FloatingActionButton fab;

    // Tab titles
    private String[] tabs = { "Recent", "Most Liked", "Catagories"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idea);
        // Initilization
        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getSupportActionBar();
        mAdapter = new TabsAdapter(getSupportFragmentManager());

        viewPager.setAdapter(mAdapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Adding Tabs
        for (String tab_name : tabs) {
            actionBar.addTab(
                    actionBar.newTab().setText(tab_name).setTabListener(this)
                    // actionBar.newTab().setCustomView(R.layout.tab_layout).setTabListener(this)

            );
        }

        actionBar.setSelectedNavigationItem(1);
        /**
         * on swiping the viewpager make respective tab selected
         * */
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                actionBar.setSelectedNavigationItem(position);
            }
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {}
            @Override
            public void onPageScrollStateChanged(int arg0) {}
        });
    }


    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    public void ideaRegdClick(View v){
        //Toast.makeText(this,"hello",Toast.LENGTH_LONG).show();
        Intent intent=new Intent(IdeaActivity.this, IdeaRegistrationActivity.class);
        startActivity(intent);

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

//    public void getCatagoryData(View v){
//        Toast.makeText(this,"hello",Toast.LENGTH_LONG).show();
//    }
}
