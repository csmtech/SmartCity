package com.csm.smartcity.dashboard;

import android.app.Service;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.csm.smartcity.R;
import com.csm.smartcity.cityChampians.CityChampiansActivity;
import com.csm.smartcity.common.AppCommon;
import com.csm.smartcity.common.CircularNetworkImageView;
import com.csm.smartcity.common.ColoredSnackbar;
import com.csm.smartcity.common.CommonDialogs;
import com.csm.smartcity.common.UtilityMethods;
import com.csm.smartcity.connection.CustomVolleyRequestQueue;
import com.csm.smartcity.idea.IdeaActivity;
import com.csm.smartcity.information.InformationActivity;
import com.csm.smartcity.login.LoginActivity;
import com.csm.smartcity.services.Services;
import com.csm.smartcity.sqlLiteModel.DatabaseHandler;
import com.csm.smartcity.sqlLiteModel.LoginUserObject;
import com.csm.smartcity.userRegistration.UserRegistrationActivity;
import com.facebook.login.LoginManager;

import java.io.File;
import java.io.InputStream;

public class NewDashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    TextView layoutIdea;
    LinearLayout layoutService;
    LinearLayout infoLayout;
    LinearLayout grievanceModule;
    LoginUserObject objLogin; // LoginUserObject is a pojo object of  citizen details contains getter setter
    CircularNetworkImageView imgProfilePic;
    TextView txtuserName;
    CustomVolleyRequestQueue customvolley;
    ImageLoader mImageLoader;
    String skipFlg="";
  //  private Menu menu;
    MenuItem bedMenuItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);




        infoLayout=(LinearLayout)findViewById(R.id.infoLayout);
        layoutIdea=(TextView)findViewById(R.id.layoutIdea);
        grievanceModule=(LinearLayout)findViewById(R.id.grievanceModule);
        layoutService=(LinearLayout)findViewById(R.id.services);
        layoutIdea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewDashboardActivity.this, IdeaActivity.class);
                startActivity(intent);
            }
        });

        infoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewDashboardActivity.this, InformationActivity.class));
            }
        });
        layoutService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewDashboardActivity.this, Services.class));
            }
        });

        grievanceModule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Put the package name here...
                boolean installed = appInstalledOrNot("com.csm.BMCSanjogV2");
                if (installed) {
                    //This intent will help you to launch if the package is already installed
                    Intent LaunchIntent = getPackageManager()
                            .getLaunchIntentForPackage("com.csm.BMCSanjogV2");
                    startActivity(LaunchIntent);
                    String packageName = "com.csm.BMCSanjogV2";
                    Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
                    System.out.println("App is already installed on your phone");
                } else {
                    System.out.println("App is not currently installed on your phone");
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("market://details?id=com.csm.BMCSanjogV2"));
                    startActivity(intent);
                }
            }
        });

        txtuserName=(TextView)findViewById(R.id.txtuserName);

        if (AppCommon.isLogin(getApplicationContext())){

            // ::::::::::GETTING USER DETAILS FROM SQLLITE DB
            objLogin = AppCommon.getLoginPrefData(this);   // Getting login user details

            String userImg=objLogin.getIMAGE();
            String imageFlag=objLogin.getIMAGE_FLAG();
            String userName=objLogin.getUSER_NAME();
            String citizen_id=objLogin.getCITIZEN_ID();

            imgProfilePic = (CircularNetworkImageView) findViewById(R.id.imgProfilePic);
           // txtuserName=(TextView)findViewById(R.id.txtuserName);
            String imagePath;

            if(!userImg.equals("") && !imageFlag.equals("FA")){
                imagePath= AppCommon.getUserPhotoURL()+userImg;//{{ImgPath}}Citizen/citizen_{{image}};
            }else if(!userImg.equals("") && imageFlag.equals("FA")){
                imagePath=userImg;
            }else{
                imagePath="";
            }
            txtuserName.setText(userName);

            Log.i("atag", imagePath);
            // Instantiate the RequestQueue//.
            try {
                customvolley= CustomVolleyRequestQueue.getInstance(NewDashboardActivity.this);
                mImageLoader =customvolley.getImageLoader();
                mImageLoader.get(imagePath, ImageLoader.getImageListener(imgProfilePic, R.drawable.userimg, R.drawable.userimg));
                imgProfilePic.setImageUrl(imagePath, mImageLoader);

            } catch (Exception e) {
                Log.i("atag","hello");
                e.printStackTrace();
            }

        //    new LoadProfileImage(imgProfilePic).execute("http://192.168.10.42/bmcsampark/Photos/Citizen/citizen_330.jpg");

        }
        else{
            Log.i("atag","2");
            txtuserName.setText("Please login");
            navigationView.getMenu().findItem(R.id.logout).setTitle("Login");
        }



    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        if (AppCommon.isLogin(getApplicationContext())) {
            this.moveTaskToBack(true);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.new_dashboard, menu);
       //bedMenuItem = menu.findItem(R.id.logout);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if(id == R.id.editProfile){
            Intent actvI = new Intent(NewDashboardActivity.this, UserRegistrationActivity.class);
            actvI.putExtra("mobNo","edit");
            startActivity(actvI);
        }else if (id == R.id.champians) {
            Intent i = new Intent(NewDashboardActivity.this, CityChampiansActivity.class);
            startActivity(i);
        }else if (id == R.id.appData) {
            if(AppCommon.isNetworkAvailability(getApplicationContext())==true) {
                UtilityMethods.getAllData("upd", NewDashboardActivity.this);
            }else{
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), CommonDialogs.INTERNET_UNAVAILABLE, Snackbar.LENGTH_LONG);
                ColoredSnackbar.confirm(snackbar).show();
            }
            // Handle the camera action
        } else if (id == R.id.logout) {
           Log.i("atag", item.getTitle().toString());
            if(item.getTitle().equals("Login")){
                finish();
            }else{

                AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
                builder.setTitle("Logout confirmation");
                builder.setMessage("Do you want to logout ?");
                // builder.setPositiveButton("Ok",null);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        trimCache(NewDashboardActivity.this);
                        //ClearDataApplication.getInstance().clearApplicationData();
                        DatabaseHandler db = new DatabaseHandler(NewDashboardActivity.this);
                        db.deleteControllData();
                        SharedPreferences preferences = getSharedPreferences("MyPofile", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.commit();
                        try{
                            LoginManager.getInstance().logOut();
                        }catch (Exception e){

                        }

                        NewDashboardActivity.this.finish();
                        Intent i = new Intent(NewDashboardActivity.this, LoginActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    }
                });

                builder.setNegativeButton("Cancel", null);
                builder.show();

            }



        }
// else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }
        catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    public static void trimCache(Context context) {
        try {
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);

            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }


    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    /**
     * Background Async task to load user profile picture from url
     * */
    private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public LoadProfileImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }




}
