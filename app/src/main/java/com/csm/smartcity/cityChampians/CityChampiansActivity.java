package com.csm.smartcity.cityChampians;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.csm.smartcity.R;
import com.csm.smartcity.userList.UsersActivity;

public class CityChampiansActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout greenBadge;
    LinearLayout blueBadge;
    LinearLayout orangeBadge;
    LinearLayout pinkBadge;
    LinearLayout yellowBadge;
    LinearLayout purpleBadge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_champians);

        greenBadge=(LinearLayout)findViewById(R.id.greenBadge);
        blueBadge=(LinearLayout)findViewById(R.id.blueBadge);
        orangeBadge=(LinearLayout)findViewById(R.id.orangeBadge);
        pinkBadge=(LinearLayout)findViewById(R.id.pinkBadge);
        yellowBadge=(LinearLayout)findViewById(R.id.yellowBadge);
        purpleBadge=(LinearLayout)findViewById(R.id.purpleBadge);

        greenBadge.setOnClickListener(this);
        blueBadge.setOnClickListener(this);
        orangeBadge.setOnClickListener(this);
        pinkBadge.setOnClickListener(this);
        yellowBadge.setOnClickListener(this);
        purpleBadge.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        Intent intent=new Intent(this, UsersActivity.class);
        startActivity(intent);
        overridePendingTransition(R.animator.push_up_in, R.animator.push_up_out);
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
}
