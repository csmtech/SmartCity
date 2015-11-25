package com.csm.smartcity.ideaRegistration;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.csm.smartcity.R;
import com.csm.smartcity.common.AppCommon;
import com.csm.smartcity.common.AppController;
import com.csm.smartcity.common.ColoredSnackbar;
import com.csm.smartcity.common.CommonDialogs;
import com.csm.smartcity.common.UtilityMethods;
import com.csm.smartcity.connection.PostJsonStringRequest;
import com.csm.smartcity.sqlLiteModel.ControllData;
import com.csm.smartcity.sqlLiteModel.DatabaseHandler;
import com.csm.smartcity.sqlLiteModel.LoginUserObject;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.joanzapata.iconify.widget.IconButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class IdeaRegistrationActivity extends AppCompatActivity {

     /*
      * Declaration of all view components
      * */
    //Declaration of all catagory icon buttons
    IconButton txtTransport; //For Solid waste management.tag-6
    IconButton txtWaste;//For Street Light.tag-8
    IconButton txtCitizens;//For Drinking Water.tag-10
    IconButton txtTown;//For Sewage.tag-11
    IconButton txtOthers;//For Miscellaneous Grievance.tag-12
    //Declaration of complaint detail edit text view for complaint registration
    EditText complnDtl;
    //char length for complaint detail character count
    TextView charLength;
    /*
    * Declaration of all complaint used in complaint image
    * */
    ImageView complnImage; //Image view to show image button
    IconButton crossBtn;   //cross btn on right top corner to close image view
    LinearLayout tableView; //Table view used to hide table layout and show complaint image when image is selected/taken.
    //Declaration of complaint post button
    IconButton complnpost;

    LoginUserObject objLogin; // LoginUserObject is a pojo object of  citizen details contains getter setter
    /*
    * Declaration of string variables
    * */
    final String tag_json_obj = "obj_complnRegd"; //variable to store valley queue request name
    String catagoryID="0"; //variable to store catagory id for DB
    String encodedImage="";//variable to store byte code of complaint image
    String imgDecodableString; // to store complaint image path in choose from gallery
    String userId="0";
    /*
    * Declaration of int variables
    * */
    private static int RESULT_LOAD_IMG = 2; //variable for start activity on result request code to load image from gallery
    private static int REQUEST_CAMERA=1; //variable for start activity on result request code to open camera

    DonutProgress pb;
    IconButton upload;
    IconButton retry;
    Bitmap bm1;
    long lengthbmp;
    String imgStatus="No";
    RelativeLayout layoutImageUpload;

    private String uploadStatus="false";
    private boolean uploadCancelStatus=false;
    Bitmap bm;
    Bitmap picture;
    String[] char_spec={".",","," ","`","@","#","%","(",")","-","_",":",";","\"","'","!","?","/","\\","0","1","2","3","4","5","6","7","8","9"};
    //Life cycle method onCreate of this class which is overridden
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Used to set XML view to this activity/context
        setContentView(R.layout.activity_idea_registration);
        pb = (DonutProgress) findViewById(R.id.progressBar);
        //Getting all catagory icon button from xml by its id.
        txtTransport=(IconButton)findViewById(R.id.txtTransport);
        txtWaste=(IconButton)findViewById(R.id.txtWaste);
        txtCitizens=(IconButton)findViewById(R.id.txtCitizens);
        txtTown=(IconButton)findViewById(R.id.txtTown);
        txtOthers=(IconButton)findViewById(R.id.txtOthers);
        /*
        * Getting typetext text view from xml by its id.
        * Initially it is hidden
        * After selecting complaint type it shown in orange color background text view
        * */
        complnDtl=(EditText)findViewById(R.id.complnDtl); //Getting complaint detail edit text component
        charLength=(TextView)findViewById(R.id.charLength); //Getting complaint detail char length text view component
        /*
        * Components of complaint image
        * */
        complnImage=(ImageView)findViewById(R.id.complnImage); //Getting complaint iamge component
        crossBtn=(IconButton)findViewById(R.id.crossBtn);//Getting cross button component of complaint iamge
        crossBtn.setVisibility(View.INVISIBLE);//Initially it is hidden

        upload=(IconButton)findViewById(R.id.upload);
        retry=(IconButton)findViewById(R.id.retry);
        complnImage.setAlpha((float) 0.0);
        layoutImageUpload=(RelativeLayout) findViewById(R.id.layoutImageUpload);

        tableView=(LinearLayout)findViewById(R.id.tableView); //Getting table view of complaint image
        complnpost=(IconButton)findViewById(R.id.complnpost);//Getting complaint post button from its id
        // Getting login user details
        if (AppCommon.isLogin(this)) {
            objLogin = AppCommon.getLoginPrefData(this);
            userId=objLogin.getCITIZEN_ID();
        }else{
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Please login to post your idea.", Snackbar.LENGTH_LONG);
            ColoredSnackbar.confirm(snackbar).show();
        }
        //COMPLAIN DETAIL EDIT TEXT FIELD TEXT CHANGE LISTENER
        complnDtl.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                for (String x : char_spec) {
                    if (s.toString().startsWith(x)) {
                        complnDtl.setText("");
                        charLength.setText("500 characters");
                        break;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String cnt = String.valueOf(500 - s.length());
                charLength.setText(cnt + " characters");
            }
        });
        //Adding on click listener to complaint post button
        complnpost.setOnClickListener(registerComplaint());


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

    public void closeImg(View v){
        encodedImage="";
        imgDecodableString="";
        tableView.setVisibility(View.VISIBLE);
        // roundOr.setVisibility(View.VISIBLE);
        layoutImageUpload.setVisibility(View.GONE);
        crossBtn.setVisibility(View.GONE);
        complnImage.setAlpha((float) 0.0);
        upload.setVisibility(View.GONE);
        retry.setVisibility(View.GONE);
        pb.setVisibility(View.GONE);

        FileInputStream fstrm=null;
        Log.e("EEEE", "EEEE " + uploadStatus);
        uploadStatus="true";
        if(bm!=null)
        {
            bm.recycle();
            bm=null;
        }
        if(picture!=null)
        {
            picture.recycle();
            picture=null;
        }
        Log.i("STAG",imgStatus+" nnnnnnnnnnnnnnnnnnnnnn");
        if(imgStatus.contains("No")){

        }
        else{
            AppCommon.showDialog("Please wait....",IdeaRegistrationActivity.this); //Showing registering dialog
            Map<String, String> params = new HashMap<String, String>();
            try {
                params.put("strFileName", imgStatus);
            } catch (Exception e) {
            }
            String url = AppCommon.getURL() + "deleteFile";
            PostJsonStringRequest jsonObjReq = new PostJsonStringRequest(
                    url, new JSONObject(params),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            AppCommon.hideDialog();
                            try {
                                Log.i("STAG",response.toString());

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    AppCommon.hideDialog();
                }
            });
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    -1,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
        }

        imgStatus="No";

    }

    //Click method of catagory box click
    public void openPopup(View v){
        catagoryID=v.getTag().toString();//Getting catagory id from XML tag attribute

               if(catagoryID.equals("1")){
                   txtTransport.setText("Smart Transport\nSolutions\n\n{fa-bus 30sp #E36C09}");
                   txtWaste.setText("Smart Waste\nManagement.\n\n{fa-trash 30sp #606060}");
                   txtCitizens.setText("Citizen Engagement\nInitiatives\n\n{fa-users 27sp #606060}");
                   txtTown.setText("Bhubaneswar Smart District:\nTown Center\n\n{fa-cubes 27sp #606060}");
                   txtOthers.setText("Others\n\n{fa-envelope 30sp #606060}");

                   txtTransport.setTextColor(Color.parseColor("#E36C09"));
                   txtWaste.setTextColor(Color.parseColor("#606060"));
                   txtCitizens.setTextColor(Color.parseColor("#606060"));
                   txtTown.setTextColor(Color.parseColor("#606060"));
                   txtOthers.setTextColor(Color.parseColor("#606060"));
                }else if(catagoryID.equals("2")){
                   txtTransport.setText("Smart Transport\nSolutions\n\n{fa-bus 30sp #606060}");
                   txtWaste.setText("Smart Waste\nManagement.\n\n{fa-trash 30sp #E36C09}");
                   txtCitizens.setText("Citizen Engagement\nInitiatives\n\n{fa-users 27sp #606060}");
                   txtTown.setText("Bhubaneswar Smart District:\nTown Center\n\n{fa-cubes 27sp #606060}");
                   txtOthers.setText("Others\n\n{fa-envelope 30sp #606060}");

                   txtTransport.setTextColor(Color.parseColor("#606060"));
                   txtWaste.setTextColor(Color.parseColor("#E36C09"));
                   txtCitizens.setTextColor(Color.parseColor("#606060"));
                   txtTown.setTextColor(Color.parseColor("#606060"));
                   txtOthers.setTextColor(Color.parseColor("#606060"));
                }else if(catagoryID.equals("3")){
                   txtTransport.setText("Smart Transport\nSolutions\n\n{fa-bus 30sp #606060}");
                   txtWaste.setText("Smart Waste\nManagement.\n\n{fa-trash 30sp #606060}");
                   txtCitizens.setText("Citizen Engagement\nInitiatives\n\n{fa-users 27sp #E36C09}");
                   txtTown.setText("Bhubaneswar Smart District:\nTown Center\n\n{fa-cubes 27sp #606060}");
                   txtOthers.setText("Others\n\n{fa-envelope 30sp #606060}");

                   txtTransport.setTextColor(Color.parseColor("#606060"));
                   txtWaste.setTextColor(Color.parseColor("#606060"));
                   txtCitizens.setTextColor(Color.parseColor("#E36C09"));
                   txtTown.setTextColor(Color.parseColor("#606060"));
                   txtOthers.setTextColor(Color.parseColor("#606060"));
                }else if(catagoryID.equals("4")){
                   txtTransport.setText("Smart Transport\nSolutions\n\n{fa-bus 30sp #606060}");
                   txtWaste.setText("Smart Waste\nManagement.\n\n{fa-trash 30sp #606060}");
                   txtCitizens.setText("Citizen Engagement\nInitiatives\n\n{fa-users 27sp #606060}");
                   txtTown.setText("Bhubaneswar Smart District:\nTown Center\n\n{fa-cubes 27sp #E36C09}");
                   txtOthers.setText("Others\n\n{fa-envelope 30sp #606060}");

                   txtTransport.setTextColor(Color.parseColor("#606060"));
                   txtWaste.setTextColor(Color.parseColor("#606060"));
                   txtCitizens.setTextColor(Color.parseColor("#606060"));
                   txtTown.setTextColor(Color.parseColor("#E36C09"));
                   txtOthers.setTextColor(Color.parseColor("#606060"));
                }else if(catagoryID.equals("5")){
                   txtTransport.setText("Smart Transport\nSolutions\n\n{fa-bus 30sp #606060}");
                   txtWaste.setText("Smart Waste\nManagement.\n\n{fa-trash 30sp #606060}");
                   txtCitizens.setText("Citizen Engagement\nInitiatives\n\n{fa-users 27sp #606060}");
                   txtTown.setText("Bhubaneswar Smart District:\nTown Center\n\n{fa-cubes 27sp #606060}");
                   txtOthers.setText("Others\n\n{fa-envelope 30sp #E36C09}");

                   txtTransport.setTextColor(Color.parseColor("#606060"));
                   txtWaste.setTextColor(Color.parseColor("#606060"));
                   txtCitizens.setTextColor(Color.parseColor("#606060"));
                   txtTown.setTextColor(Color.parseColor("#606060"));
                   txtOthers.setTextColor(Color.parseColor("#E36C09"));
                }
    }

    public void takePhoto(View v) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }
    public void pickPhoto(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select File"), RESULT_LOAD_IMG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        byte[] b;
        try {
            //Toast.makeText(this,requestCode+":::"+resultCode+":::"+data , Toast.LENGTH_LONG).show();
            if (requestCode==1 && resultCode == RESULT_OK && data!=null) {
                complnImage.setAlpha((float) 0.0);
                uploadStatus="false";
                //ImageView imgView = (ImageView) findViewById(R.id.complnImage);
                layoutImageUpload.setVisibility(View.VISIBLE);
                picture = (Bitmap) data.getExtras().get("data");
                bm1= UtilityMethods.getResizedBitmap(picture, 500);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bm1.compress(Bitmap.CompressFormat.JPEG, 60, stream);
                b = stream.toByteArray();
                lengthbmp = b.length;
                encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                //Log.i("STAG", encodedImage);
                complnImage.setImageBitmap(picture);
                uploadPhoto();
            }
            //gallery image activity result
            else if(requestCode==2 && resultCode == RESULT_OK && data!=null) {
//               try {
                complnImage.setAlpha((float) 0.0);
                uploadStatus = "false";
                layoutImageUpload.setVisibility(View.VISIBLE);
                //ImageView imgView = (ImageView) findViewById(R.id.complnImage);
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();

                // bm = BitmapFactory.decodeFile(imgDecodableString);

                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;

                bm = BitmapFactory.decodeFile(imgDecodableString,options);


                bm1 = UtilityMethods.getResizedBitmap(bm, 500);

                   /* if(bm!=null)
                    {
                        bm.recycle();
                        bm=null;
                    }*/
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm1.compress(Bitmap.CompressFormat.JPEG, 60, baos); //bm is the bitmap object
                b = baos.toByteArray();
                lengthbmp = b.length;
                encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                // Log.i("STAG", encodedImage);

                complnImage.setImageBitmap(bm);
                uploadPhoto();
                /*}catch (OutOfMemoryError err){
                    err.printStackTrace();*/
               /* }
                catch (Exception e){
                    e.printStackTrace();
                }*/

//                tableView.setVisibility(View.INVISIBLE);
//                roundOr.setVisibility(View.INVISIBLE);
//                crossBtn.setVisibility(View.VISIBLE);
//                complnImage.setVisibility(View.VISIBLE);

            }else{
                if(requestCode==3)
                    Toast.makeText(this, "You haven't picked any complaint type. ", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {}

    }

    public void uploadPhoto() {
        retry.setVisibility(View.GONE);
        upload.setVisibility(View.GONE);
        pb.setVisibility(View.VISIBLE);
        pb.setProgress(0);
        crossBtn.setVisibility(View.VISIBLE);
        //imgView.setAlpha((float)0.2);
        //pb.setMax((int)lengthbmp);
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    Uri tempUri = getImageUri(getApplicationContext(), bm1);
                    FileInputStream fstrm = new FileInputStream(getRealPathFromURI(tempUri));

                    new photoUploadTask(AppCommon.getComplaintImgHandlrUrl(), "fgf", "rtrt", fstrm,lengthbmp).execute();

                } catch (Exception e) {
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), CommonDialogs.INTERNET_UNAVAILABLE, Snackbar.LENGTH_LONG);
                    ColoredSnackbar.confirm(snackbar).show();
                    complnImage.setAlpha((float) 1.0);
                    retry.setVisibility(View.VISIBLE);

                }
            }
        });

        thread.start();
        tableView.setVisibility(View.GONE);
        // roundOr.setVisibility(View.INVISIBLE);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }
    public void retry(View v) {
        uploadPhoto();
    }

    // method to validate complaint registration page
    public boolean isValid(){
        String complnDtlText = complnDtl.getText().toString();
        if (imgStatus.equals("Inprocess")) {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),"Please Wait image is uploading", Snackbar.LENGTH_LONG);
            ColoredSnackbar.alert(snackbar).show();
            //Toast.makeText(ComplaintRegdActivity.this, "Please Wait image is uploading", Toast.LENGTH_LONG).show();
            return false;
        } else if (userId.equals("0")) {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),"Please login to post ideas.", Snackbar.LENGTH_LONG);
            ColoredSnackbar.alert(snackbar).show();
            //Toast.makeText(ComplaintRegdActivity.this, "Please Wait image is uploading", Toast.LENGTH_LONG).show();
            return false;
        }else if (catagoryID.equals("0")) {
            // Toast.makeText(ComplaintRegdActivity.this, "Please select complaint catagory", Toast.LENGTH_LONG).show();
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),"Please select idea catagory", Snackbar.LENGTH_LONG);
            ColoredSnackbar.alert(snackbar).show();
            return false;
        } else if (complnDtlText.equals("")) {
            //Toast.makeText(ComplaintRegdActivity.this, "Complaint detail can't be left blank", Toast.LENGTH_LONG).show();
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),"ideas description field can't be left blank", Snackbar.LENGTH_LONG);
            ColoredSnackbar.alert(snackbar).show();
            complnDtl.requestFocus();
            return false;
        }
        return true;
    }
    //registerComplaint method to return OnClickListener for post complaint
    public View.OnClickListener registerComplaint(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {
                    //Condition for network availability
                    if (AppCommon.isNetworkAvailability(IdeaRegistrationActivity.this)) {
                        AppCommon.showDialog("Registering....",v.getContext()); //Showing registering dialog
                        Map<String, String> params = new HashMap<String, String>();
                        try {
                          Log.i("atag",catagoryID+""+complnDtl.getText().toString()+":::::::"+imgStatus+"::::::::"+userId);
                            params.put("strCatId", catagoryID);
                            params.put("streIdeaDesc", complnDtl.getText().toString());
                            if(imgStatus.contains("No"))
                                params.put("strIdeaImg", "");
                            else
                                params.put("strIdeaImg", imgStatus);

                            params.put("strUid", userId);

                        } catch (Exception e) {
                        }

                        String url = AppCommon.getURL() + "insertIdeaData";
                        PostJsonStringRequest jsonObjReq = new PostJsonStringRequest(
                                url, new JSONObject(params),
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        AppCommon.hideDialog();
                                        try {

                                            Log.i("atag",response.toString());
//                                            Intent intent = new Intent(ComplaintRegdActivity.this, ComplaintConfirmation.class);
//                                            intent.putExtra("ticketId", ticketID[1]);
//                                            startActivity(intent);
                                        }
                                        catch(Exception ex){
                                            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "something went wrong.Please try after sometimes. ", Snackbar.LENGTH_LONG);
                                            ColoredSnackbar.confirm(snackbar).show();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                AppCommon.hideDialog();
                                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), CommonDialogs.SERVER_ERROR, Snackbar.LENGTH_LONG);
                                ColoredSnackbar.confirm(snackbar).show();
                            }
                        });
                        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                                90000,
                                -1,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
                    } else { //else condition to store complaint info in draft
                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), CommonDialogs.INTERNET_UNAVAILABLE, Snackbar.LENGTH_LONG);
                        ColoredSnackbar.confirm(snackbar).show();
                    }
                }
            }
        };
    }
    /*Fetch GPS Location ad set for Registration and Location*/
//    private void fetchLocation(){
//        gps = new GPSTracker(ComplaintRegdActivity.this);
//        if(!gps.canGetLocation()){
//            gps.showSettingsAlert();
//        }
//        else{
//            latitude = String.valueOf(gps.getLatitude());
//            longitude = String.valueOf(gps.getLongitude());
//
//            //21.004578, 81.443769
//            //latitude = "21.004578";
//            //longitude = "81.443769";
//
//            String url=AppCommon.getURL()+"getMyLocation";
//            autoLocationProgress.setVisibility(View.VISIBLE);
//            if(AppCommon.isNetworkAvailability(getApplicationContext())==true) {
//
//                Map<String, String> params = new HashMap<String, String>();
//                try {
//                    params.put("strlat", latitude);
//                    params.put("strlong", longitude);
//                } catch (Exception e) {
//                }
//
//                PostJsonArrayRequest jsonObjReq = new PostJsonArrayRequest(url, new JSONObject(params),
//                        new Response.Listener<JSONArray>() {
//                            @Override
//                            public void onResponse(JSONArray response) {
//                                try {
//                                    final String wardName=response.getJSONObject(0).getString("NAME");
//                                    wordID=response.getJSONObject(0).getString("ID");
//                                    if(wordID.equals("99")){
//                                        Log.i("RTAG", "local address!");
//                                        //new NotificationTipView(getApplicationContext(),viewGroup, 10000, NotificationTipView.OUT_SIDE);
//                                        setLocation();
//                                        autoLocationProgress.setVisibility(View.GONE);
//                                    }
//                                    else{
//                                        final Geocoder geocoder;
//                                        geocoder = new Geocoder(getApplicationContext(), Locale.ENGLISH);
//                                        if(geocoder.isPresent()) {
//
//                                            new AsyncTask<Void, Void, String>()
//                                            {
//                                                @Override
//                                                protected String doInBackground(Void... str)
//                                                {
//                                                    List<Address> addresses;
//                                                    String firstAddress="";
//                                                    try {
//                                                        addresses = geocoder.getFromLocation(gps.getLatitude(), gps.getLongitude(), 1);
//                                                        //addresses = geocoder.getFromLocation(21.004578, 81.443769, 1);
//
//                                                        //String address = addresses.get(0).getAddressLine(0);
//                                                        if (addresses != null) {
//                                                            Address returnedAddress = addresses.get(0);
//                                                            firstAddress = returnedAddress.getAddressLine(0);
//                                                            return firstAddress;
//                                                        } else {
//                                                            Log.i("RTAG", "No Address returned!");
//                                                        }
//
//                                                    } catch (IOException e) {
//                                                        // e.printStackTrace();
//                                                        Log.i("RTAG", "Canont get Address!");
//                                                    }
//                                                    return "";
//                                                }
//
//                                                @Override
//                                                protected void onPostExecute(String address)
//                                                {
//                                                    // do whatever you want/need to do with the address found
//                                                    // remember to check first that it's not null
//                                                    //Log.i("RTAG",address);
//                                                    autoLocationProgress.setVisibility(View.GONE);
//                                                    if(address!="") {
//                                                        googleAreaName=address;
//                                                        areaID="0";
//                                                        location.setText(address+", "+wardName);
//                                                        btn_edit_loc.setVisibility(View.VISIBLE);
//                                                        btn_select_area.setVisibility(View.GONE);
//                                                    }
//                                                }
//                                            }.execute();
//
//                                        }
//                                    }
//                                } catch (Exception e) {
//                                    autoLocationProgress.setVisibility(View.GONE);
//                                }
//
//                            }
//                        }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.i("RTAG", "Error: " + error);
//                        autoLocationProgress.setVisibility(View.GONE);
//                    }
//                });
//                jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(30000,-1,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//                AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
//            }
//            else{
//                autoLocationProgress.setVisibility(View.GONE);
//                new NotificationTipView(getApplicationContext(), (ViewGroup)findViewById(android.R.id.content), 10000, NotificationTipView.INTERNET_UNAVAILABLE_DRAFT);
//            }
//        }
//
//
//    }
    class photoUploadTask extends AsyncTask<Void, Integer, String> implements DialogInterface.OnCancelListener {

        URL connectURL;
        String responseString;
        String Title;
        String Description;
        byte[ ] dataToServer;
        FileInputStream fileInputStream = null;
        Long lengthbmp;
        public photoUploadTask(String urlString, String vTitle, String vDesc,FileInputStream fStream,Long lengthbmp){
            try{
                this.connectURL = new URL(urlString);
                this.Title= vTitle;
                this.Description = vDesc;
                this.fileInputStream = fStream;
                this.lengthbmp = lengthbmp;
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
        @Override
        protected void onPreExecute() {


            // pb.setVisibility(View.VISIBLE);

            // updating progress bar value

        }

        @Override
        protected String doInBackground(Void... v) {

            imgStatus="Inprocess";
            String iFileName = "ovicam_temp_vid.mp4";
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            String Tag="fSnd";
            try
            {
                Log.e(Tag,"Starting Http File Sending to URL");

                // Open a HTTP connection to the URL
                HttpURLConnection conn = (HttpURLConnection)connectURL.openConnection();

                // Allow Inputs
                conn.setDoInput(true);

                // Allow Outputs
                conn.setDoOutput(true);

                // Don't use a cached copy.
                conn.setUseCaches(false);

                // Use a post method.
                conn.setRequestMethod("POST");

                conn.setRequestProperty("Connection", "Keep-Alive");

                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);

                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"title\""+ lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(Title);
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + lineEnd);

                dos.writeBytes("Content-Disposition: form-data; name=\"description\""+ lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(Description);
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + lineEnd);

                dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + iFileName +"\"" + lineEnd);
                dos.writeBytes(lineEnd);

                Log.e(Tag,"Headers are written");

                // create a buffer of maximum size
                int bytesAvailable = fileInputStream.available();

                int maxBufferSize = 1024;
                int bufferSize = Math.min(bytesAvailable, maxBufferSize);
                byte[ ] buffer = new byte[bufferSize];

                // read file and write it into form...
                int bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                Log.e(Tag,"start"+lengthbmp);
                int progress = 0;
                double progress2=0.0;
                Log.e("TAG", "gggggg" + progress2);
                while (bytesRead > 0)
                {
                    Log.e("EEEE", "EEEE " + isCancelled());
                    if (uploadStatus.equals("true"))
                    {
                        uploadCancelStatus=true;
                        break;
                    }
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    Log.e(Tag,"continue"+bytesRead);
                    progress += bytesRead;

                    progress2=Math.round(((double)100/(double)lengthbmp)*progress);
                    // update progress bar
                    SystemClock.sleep(1000);
                    Log.e("TAG", "tttttkkk" + progress2);
                    publishProgress((int)progress2);
                }
                progress=0;
                if(!uploadCancelStatus){
                    Log.e(Tag,"end"+bytesRead);
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                    // close streams
                    fileInputStream.close();
                    dos.flush();
                    Log.e(Tag,"File Sent, Response: "+String.valueOf(conn.getResponseCode()));
                    InputStream is = conn.getInputStream();
                    // retrieve the response from server
                    int ch;
                    StringBuffer b =new StringBuffer();
                    while( ( ch = is.read() ) != -1 ){ b.append( (char)ch ); }
                    String s=b.toString();
                    imgStatus=s;
                    Log.i("Response", s);
                    dos.close();
                    publishProgress(100);
                }


            }
            catch (Exception ex)
            {
                Log.e(Tag, "URL error: " + ex.getMessage(), ex);
                return "error";
                // retry.setVisibility(View.VISIBLE);
            }


            return "success";
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            pb.setProgress((int) (progress[0]));
            String num;
            if(progress[0].toString().length()>1)
                num="0."+progress[0];
            else
                num="0.0"+progress[0];
            float f1 = Float.parseFloat(num);
            Log.e("TAG", "xxxxxxxxxxxxxxxxxxx" + f1);
            complnImage.setAlpha((float) f1);
        }

        @Override
        protected void onPostExecute(String v) {
            //progressDialog.dismiss();
            complnImage.setAlpha((float) 1.0);
            Log.e("Tag", "5555555555555555555555" + v);
            if(v.contains("error")) {
                retry.setVisibility(View.VISIBLE);
                imgStatus="No";
            }
            else{
                retry.setVisibility(View.GONE);
                upload.setVisibility(View.VISIBLE);
            }

            pb.setVisibility(View.GONE);
            uploadStatus="false";
            uploadCancelStatus=false;

        }


        @Override
        public void onCancel(DialogInterface dialog) {
            cancel(true);
            //dialog.dismiss();
        }
        @Override
        protected void onCancelled(String result) {
            super.onCancelled(result);
        }
    }


}
