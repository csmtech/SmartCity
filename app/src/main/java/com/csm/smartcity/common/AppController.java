package com.csm.smartcity.common;


import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;


public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();
    private static AppController mInstance;
    private RequestQueue mRequestQueue;


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
       // Iconics.init(getApplicationContext());
        Iconify.with(new FontAwesomeModule());
        Iconify.with(new IcoMoonModule());
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        Log.i("STAG", "1");
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
        //  getRequestQueue().ca
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

}