package com.csm.smartcity.splashScreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;

import com.csm.smartcity.R;
import com.csm.smartcity.common.AppCommon;
import com.csm.smartcity.dashboard.NewDashboardActivity;
import com.csm.smartcity.login.LoginActivity;

public class MainActivity extends Activity {
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!AppCommon.isLogin(getApplicationContext())) {
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    finish();
                    startActivity(i);
                } else {
                    Intent i = new Intent(MainActivity.this, NewDashboardActivity.class);
                    finish();
                    startActivity(i);
                }
            }
        }, SPLASH_TIME_OUT);



    }
}
