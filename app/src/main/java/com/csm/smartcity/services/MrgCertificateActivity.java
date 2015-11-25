package com.csm.smartcity.services;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.csm.smartcity.R;

public class MrgCertificateActivity extends AppCompatActivity {
    private  WebView webview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mrg_certificate);
        final ProgressDialog progressDialog=new ProgressDialog(MrgCertificateActivity.this);
        progressDialog.setMessage("Loading...");
        webview = (WebView) findViewById(R.id.webview1);
        webview.getSettings().setJavaScriptEnabled(true);
        //webview.setWebViewClient(new WebViewClient());

        webview.setWebViewClient(new WebViewClient() {


            //If you will not use this method url links are opeen in new brower not in webview
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // TODO Auto-generated method stub
                super.onPageStarted(view, url, favicon);
                progressDialog.show();
            }

//            //Show loader on url load
//            public void onLoadResource (WebView view, String url) {
//                if (progressDialog == null) {
//                    // in standard case YourActivity.this
//
//                    progressDialog.show();
//                }
//            }
            public void onPageFinished(WebView view, String url) {
                try{
//                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();

//                        progressDialog.dismiss();

//                    }
//                    String javascript="javascript: document.getElementById('headerLogo').style.display='none';";
//                    view.loadUrl(javascript);
                    webview.loadUrl("javascript:(function() { " +
                            "document.getElementsByClassName('Header')[0].style.display='none'; })()");

                }catch(Exception exception){
                    exception.printStackTrace();
                    progressDialog.dismiss();
                }
            }


        });

        webview.loadUrl("http://bmc.gov.in/OnlineRegistration_yatriNivas_mobile.aspx");

    }
    @Override
    public void onBackPressed() {
        if(webview.canGoBack()) {
            webview.goBack();
        } else {
            super.onBackPressed();
        }
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
