package com.csm.smartcity.services;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.csm.smartcity.R;

import com.csm.smartcity.dashboard.NewDashboardActivity;

public class Services extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);
    }
    public void onHome(View v)

    {
        startActivity(new Intent(this,  NewDashboardActivity.class));
    }
    public void onMarriage(View v)

    {
        startActivity(new Intent(this, MrgCertificateActivity.class));
    }
    public void onWater(View v)

    {
        startActivity(new Intent(this, WaterTankerActivity.class));
    }
    public void onCesspool(View v)

    {
        startActivity(new Intent(this, CesspoolVehicleActivity.class));
    }
    public void onMahayatra(View v)

    {
        startActivity(new Intent(this, MahayatraVehicleActivity.class));
    }
    public void onYatrinivas(View v)

    {
        startActivity(new Intent(this, YatriNivasActivity.class));
    }
    public void onKalyanMandap(View v)

    {
        startActivity(new Intent(this,KalyanMandapActivity.class));
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
