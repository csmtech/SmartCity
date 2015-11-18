package com.csm.smartcity.ideaRegistration;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
    IconButton solidtext; //For Solid waste management.tag-6
    IconButton streetText;//For Street Light.tag-8
    IconButton watrText;//For Drinking Water.tag-10
    IconButton sewrageText;//For Sewage.tag-11
    IconButton micselnousText;//For Miscellaneous Grievance.tag-12
    //Declaration of type text which shows complaint typeof complaint catagory in orange color background
    TextView typeText;
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
    //TextView roundOr; //or text view use to hide this when complait image is selected/ taken
    // Declaration of auto complete text view for complaint location
  //  AutoCompleteTextView location;
    //Declaration of land mark text view for complaint landmark
   // EditText landmark;
    //Declaration of complaint post button
    IconButton complnpost;
    /*
    * Component of word area pop view
    * */
    Spinner wordspinner; //word spinner/drop down for word data
    Spinner areaspinner; //area spinner/drop down for area data
    Button okBtn;        //Ok button of popup
    IconButton crossareaBtn; //cross button of popup
    /*
    * Declaration of object variables
    * */
    private PopupWindow pwindo; // Declaration of popup window for word area popup
    LayoutInflater inflater; // LayoutInflater to manipulate custom popup
    View layout; // declaring pop view
    ArrayList<String> wordList; //Declaraton of array list for ward data from sqllite
    ArrayList<String> areaList; //Declaraton of array list for area data from sqllite
    JSONArray wordDBData; // declaration of wordDBData to store word data retrieving from sqllite db
    JSONArray areaDBData;// declaration of areaDBData to store area data retrieving from sqllite db
   // GPSTracker gps; //Declaration of gps tracker object
    LoginUserObject objLogin; // LoginUserObject is a pojo object of  citizen details contains getter setter
    List<ControllData> cntrlData; // Declaration of controll db data list
    ArrayAdapter<String> wordadapter; // Declaration of array adapter for ward spinner
    ArrayList<String> autoCompleteArrList; // Declaration of array list for auto complete text view
    ArrayAdapter autoCompleteAdapter;  // Declaration of array adapter for location auto complete text view
    /*
    * Declaration of string variables
    * */
    final String tag_json_obj = "obj_complnRegd"; //variable to store valley queue request name
    String catagoryID="0"; //variable to store catagory id for DB
    String typeID="0"; //variable to store type id for DB
    String typeName="";//variable to store typename return from TypePopupActivity to show in type text
    String encodedImage="";//variable to store byte code of complaint image
    String imgDecodableString; // to store complaint image path in choose from gallery
    String wordID=""; //variable declaration to store word id for DB
    String areaID="";//variable declaration to store area id for DB
    String googleAreaName="";//variable declaration to store google Area
    String deviceBackFlag=""; //variable to store a flag for device back click
    String reopen="0"; //variable to store flag for complaint registration or reopen
    String areaName="";// Variable to store area name to fill into location auto complete text view
    String wardName="";// Variable to store area name to fill into location auto complete text view

    /*
    * Declaration of int variables
    * */
    private static int RESULT_LOAD_IMG = 2; //variable for start activity on result request code to load image from gallery
    private static int REQUEST_CAMERA=1; //variable for start activity on result request code to open camera
    private static int OPEN_TYPEPOPUP=3;//variable for start activity on result request code to open type popup
    int width; //variable to store device width
    int height; //variable to store device height

    DonutProgress pb;
    private long totalSize;
    private Uri fileUri;
    IconButton upload;
    IconButton retry;
    Bitmap bm1;
    long lengthbmp;
    String imgStatus="No";
    RelativeLayout layoutImageUpload;

    private String latitude="";
    private String longitude="";

   // private IconButton btn_edit_loc;
   // private IconButton btn_select_area;
    ViewGroup viewGroup;
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
        viewGroup = ((ViewGroup) this.findViewById(android.R.id.content));
        //Getting all catagory icon button from xml by its id.
        solidtext=(IconButton)findViewById(R.id.solidtext);
        streetText=(IconButton)findViewById(R.id.streetText);
        watrText=(IconButton)findViewById(R.id.watrText);
        sewrageText=(IconButton)findViewById(R.id.sewrageText);
        micselnousText=(IconButton)findViewById(R.id.micselnousText);
        /*
        * Getting typetext text view from xml by its id.
        * Initially it is hidden
        * After selecting complaint type it shown in orange color background text view
        * */
        typeText=(TextView)findViewById(R.id.typeText);
        typeText.setVisibility(View.GONE); //Initially it is hidden
        complnDtl=(EditText)findViewById(R.id.complnDtl); //Getting complaint detail edit text component
        charLength=(TextView)findViewById(R.id.charLength); //Getting complaint detail char length text view component
        /*
        * Components of complaint image
        * */
        complnImage=(ImageView)findViewById(R.id.complnImage); //Getting complaint iamge component
        // complnImage.setVisibility(View.INVISIBLE);//Initially complaint image is hidden
        crossBtn=(IconButton)findViewById(R.id.crossBtn);//Getting cross button component of complaint iamge
        crossBtn.setVisibility(View.INVISIBLE);//Initially it is hidden

        upload=(IconButton)findViewById(R.id.upload);
        retry=(IconButton)findViewById(R.id.retry);
        complnImage.setAlpha((float) 0.0);
        layoutImageUpload=(RelativeLayout) findViewById(R.id.layoutImageUpload);

        tableView=(LinearLayout)findViewById(R.id.tableView); //Getting table view of complaint image
       // roundOr = (TextView)findViewById(R.id.roundOr); //Getting round or text view
       // location= (AutoCompleteTextView) findViewById(R.id.location);// getting location auto complete text view
       // landmark = (EditText)findViewById(R.id.landmark); //Getting landmark
        complnpost=(IconButton)findViewById(R.id.complnpost);//Getting complaint post button from its id


       // btn_edit_loc =(IconButton)findViewById(R.id.btn_edit_loc) ; //Getting Button Edit Location
       // btn_select_area=(IconButton)findViewById(R.id.btn_select_area);  //Getting Button Select Area

       /*
        * Initialization of all object variables
        * */
//        inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE); // Initiation of layout inflater
//        layout = inflater.inflate(R.layout.word_area_select,(ViewGroup) findViewById(R.id.popup_element)); // Initiation of view object
         /*
        * Components of word area custom popup retrieved from custom view(layout) object
        * */
//        wordspinner = (Spinner)layout.findViewById(R.id.wordSpnr) ; //ward drop down
//        areaspinner = (Spinner)layout.findViewById(R.id.areaSpnr) ; //area drop down
//        okBtn = (Button)layout.findViewById(R.id.okBtn) ; //ok button of pop up
//        crossareaBtn = (IconButton)layout.findViewById(R.id.crossareaBtn) ; //cross button of pop up


//        objLogin = AppCommon.getLoginPrefData(this);   // Getting login user details
        DatabaseHandler db=new DatabaseHandler(this);  //initiation of DatabaseHandler class
//        //Initialization of array list string type
//        wordList = new ArrayList<String>();// Initiation of ward list
//        areaList = new ArrayList<String>();// Initiation of area list
        //Getting all word area data from SQLLite  from DatabaseHandler class
       // cntrlData = db.getAllContollData("1,2");
        //Handling JSON exception
//        try {
//            wordDBData=new JSONArray(cntrlData.get(1).getDataValue());
//            areaDBData=new JSONArray(cntrlData.get(0).getDataValue());
//            wordList.add("-- Select ward --");
//            for(int i=0;i<wordDBData.length();i++){
//                wordList.add(wordDBData.getJSONObject(i).get("Name").toString());
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        //initialization of array adepter to fill word list in wora drop down
//        wordadapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item,wordList);
//        wordadapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
//        wordspinner.setAdapter(wordadapter); // this will set list of values to spinner
//        wordspinner.setSelection(wordList.indexOf(0));
//        // Adding on item selection listener to ward spinner
//        wordspinner.setOnItemSelectedListener(wordClick());
//        // Adding on item selection listener to area spinner
//        areaspinner.setOnItemSelectedListener(areaClick());
//        //Adding area data to auto complete text view
//        autoCompleteArrList = new ArrayList<String>();
//        try {
//            for(int i=0;i<areaDBData.length();i++){
//                String areaName=areaDBData.getJSONObject(i).get("Name").toString();
//                String wardId=areaDBData.getJSONObject(i).get("WARD_ID").toString();
//
//                for(int j=0;j<wordDBData.length();j++){
//                    if( wardId.equals(wordDBData.getJSONObject(j).get("ID").toString())){
//                        autoCompleteArrList.add(areaName+","+wordDBData.getJSONObject(j).get("Name").toString());
//                    }
//                }
//            }
//            //Adepter for auto complete text view
//            autoCompleteAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,autoCompleteArrList);
//            location.setAdapter(autoCompleteAdapter); //Setting array adapter to auto complete text view
//            location.setThreshold(1);  //setting Threshold to open list by input a single letter
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        setLocation();

        // Adding on item selection listener to area auto complete text view list
//        location.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//                String areaName = arg0.getItemAtPosition(arg2).toString();
//                String[] strArr=areaName.split(",");
//                btn_edit_loc.setVisibility(View.VISIBLE);
//                btn_select_area.setVisibility(View.GONE);
//                try {
//                    for (int i = 0; i < areaDBData.length(); i++) {
//                        if (strArr[0].equals(areaDBData.getJSONObject(i).get("Name").toString())) {
//                            areaID = areaDBData.getJSONObject(i).get("ID").toString();
//                            wordID = areaDBData.getJSONObject(i).get("WARD_ID").toString();
//                            break;
//                        }
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        });
        //declaration and intialization of Display object to get window width and height
//        Display display= ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
//        width = display.getWidth();
//        height = display.getHeight();

        //OnClickListener to pop up ok button
//        okBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                deviceBackFlag="";
//                String wordName = wordspinner.getSelectedItem().toString();
//                String areaName = areaspinner.getSelectedItem().toString();
//                if(wordName.equals("-- Select ward --")){
//                    AppCommon.showCustomToast(ComplaintRegdActivity.this, "Please select ward");
//                }else if(areaName.equals("-- Select area --")){
//                    AppCommon.showCustomToast(ComplaintRegdActivity.this,"Please select area");
//                }else {
//                    location.setText(areaName+","+wordName);
//                    location.setSelection(location.getText().length());
//                    btn_edit_loc.setVisibility(View.VISIBLE);
//                    btn_select_area.setVisibility(View.GONE);
//                    deviceBackFlag="";
//                    pwindo.dismiss();
//                }
//            }
//        });
        //OnClickListener to pop up cross button
//        crossareaBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                deviceBackFlag="";
//                pwindo.dismiss();
//            }
//        });
        // Getting login user details
        objLogin = AppCommon.getLoginPrefData(this);


       /*Fetch GPS and Set Location*/
  //      fetchLocation();




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


              /*  if (s.toString().startsWith(" ")) {
                    complnDtl.setText("");
                }else if(s.toString().)
                else {
                    //enableButton(...)
                }*/

            }

            @Override
            public void afterTextChanged(Editable s) {
                String cnt = String.valueOf(500 - s.length());
                charLength.setText(cnt + " characters");
            }
        });


//        landmark.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                for(String x:char_spec){
//                    if (s.toString().startsWith(x)) {
//                        landmark.setText("");
//                        break;
//                    }
//                }
//            }
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//        });
        //Adding on click listener to complaint post button
       // complnpost.setOnClickListener(registerComplaint());


    }
//    public void setLocation(){
//        wordID=objLogin.getWARD_ID(); //Getting ward id of login user
//        areaID= objLogin.getAREA_ID(); //Getting area id of login user
//        try {
//            for (int i = 0; i < areaDBData.length(); i++) {
//                if (areaID.equals(areaDBData.getJSONObject(i).get("ID").toString())) {
//                    areaName = areaDBData.getJSONObject(i).get("Name").toString();
//                    break;
//                }
//            }
//            for (int i = 0; i < wordDBData.length(); i++) {
//                if (wordID.equals(wordDBData.getJSONObject(i).get("ID").toString())) {
//                    wardName = wordDBData.getJSONObject(i).get("Name").toString();
//                    break;
//                }
//            }
//            location.setText(areaName+","+wardName);
//            location.setSelection(location.getText().length());
//            btn_edit_loc.setVisibility(View.VISIBLE);
//            btn_select_area.setVisibility(View.GONE);
//
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

//    @Override
//    public void onBackPressed() {
//        if(deviceBackFlag.equals("popup")){
//            deviceBackFlag="";
//            pwindo.dismiss();
//        }else{
//            super.onBackPressed();
//        }
//    }
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






//        if(uploadStatus.equals("true"))
//            new photoUploadTask(AppCommon.getComplaintImgHandlrUrl(), "fgf", "rtrt", fstrm,lengthbmp).cancel(true);
    }

    //Click method of catagory box click
    public void openPopup(View v){
        catagoryID=v.getTag().toString();//Getting catagory id from XML tag attribute

        //Opening type pop up activity
        Intent popup=new Intent(this, TypePopUpActivity.class);
        popup.putExtra("CatagoryId", catagoryID);
        startActivityForResult(popup, OPEN_TYPEPOPUP);
        //Adding transition animation to open the page
        overridePendingTransition(R.animator.push_up_in, R.animator.push_up_out);
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

                    // Set your server page url (and the file title/description)
                    // HttpFileUpload hfu = new HttpFileUpload(URL2, "my file title","my file description");

                    //hfu.Send_Now(fstrm);
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        byte[] b;
        try {
            //capture activity result
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

            }
            else if(requestCode==3 && data!=null) {
                typeName=data.getStringExtra("typeName");
                typeID=data.getStringExtra("typeId");
                typeText.setText(typeName);
                typeText.setVisibility(View.VISIBLE);
                if(catagoryID.equals("6")){
                    solidtext.setText("Solid Waste Mgmt.\n{fa-trash 30sp #f9810e}");
                    streetText.setText("Street Light\n{fa-lightbulb-o 30sp #606060}");
                    watrText.setText("Drinking Water\n{ic-drinkingwater 27sp #606060}");
                    sewrageText.setText("Sewage\n{ic-sewage 27sp #606060}");
                    micselnousText.setText("Misc. Grievance\n{fa-envelope-o 30sp #606060}");
                }else if(catagoryID.equals("8")){
                    streetText.setText("Street Light\n{fa-lightbulb-o 30sp #f9810e}");
                    solidtext.setText("Solid Waste Mgmt.\n{fa-trash 30sp #606060}");
                    watrText.setText("Drinking Water\n{ic-drinkingwater 27sp #606060}");
                    sewrageText.setText("Sewage\n{ic-sewage 27sp #606060}");
                    micselnousText.setText("Misc. Grievance\n{fa-envelope-o 30sp #606060}");
                }else if(catagoryID.equals("10")){
                    watrText.setText("Drinking Water\n{ic-drinkingwater 27sp #f9810e}");
                    solidtext.setText("Solid Waste Mgmt.\n{fa-trash 30sp #606060}");
                    streetText.setText("Street Light\n{fa-lightbulb-o 30sp #606060}");
                    sewrageText.setText("Sewage\n{ic-sewage 27sp #606060}");
                    micselnousText.setText("Misc. Grievance\n{fa-envelope-o 30sp #606060}");
                }else if(catagoryID.equals("11")){
                    sewrageText.setText("Sewage\n{ic-sewage 27sp #f9810e}");
                    solidtext.setText("Solid Waste Mgmt.\n{fa-trash 30sp #606060}");
                    streetText.setText("Street Light\n{fa-lightbulb-o 30sp #606060}");
                    watrText.setText("Drinking Water\n{ic-drinkingwater 27sp #606060}");
                    micselnousText.setText("Misc. Grievance\n{fa-envelope-o 30sp #606060}");
                }else if(catagoryID.equals("12")){
                    micselnousText.setText("Misc. Grievance\n{fa-envelope-o 30sp #f9810e}");
                    solidtext.setText("Solid Waste Mgmt.\n{fa-trash 30sp #606060}");
                    streetText.setText("Street Light\n{fa-lightbulb-o 30sp #606060}");
                    watrText.setText("Drinking Water\n{ic-drinkingwater 27sp #606060}");
                    sewrageText.setText("Sewage\n{ic-sewage 27sp #606060}");
                }

            }

            /*Fetch Location After Back from the Setting Window*/
//            else if(requestCode==11){
//                fetchLocation();
//            }

            else{
                if(requestCode==3)
                    Toast.makeText(this, "You haven't picked any complaint type. ", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {}

    }


    //wordClick method to return OnItemSelectedListener
//    public AdapterView.OnItemSelectedListener wordClick(){
//        return new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                wordID = "";
//                String wordName = wordspinner.getSelectedItem().toString();
//                try {
//                    for (int i = 0; i < wordDBData.length(); i++) {
//                        String wordData = wordDBData.getJSONObject(i).get("Name").toString();
//                        if (wordData.equals(wordName)) {
//                            wordID = wordDBData.getJSONObject(i).get("ID").toString();
//                            break;
//                        }
//                    }
//                    //adding area list of corresponding word to area spinner
//                    areaList.clear();
//                    areaList.add("-- Select area --");
//                    areaspinner.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, areaList));
//                    for (int i = 0; i < areaDBData.length(); i++) {
//                        String areawordID = areaDBData.getJSONObject(i).get("WARD_ID").toString();
//                        if (areawordID.equals(wordID)) {
//                            areaList.add(areaDBData.getJSONObject(i).get("Name").toString());
//                        }
//                    }
//
//                    ArrayAdapter<String> areaadapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, areaList);
//                    areaadapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
//                    areaspinner.setAdapter(areaadapter); // this will set list of values to spinner
//                    areaspinner.setSelection(areaList.indexOf(0));
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        };
//    }
    //areaClick method to return OnItemSelectedListener
//    public AdapterView.OnItemSelectedListener areaClick(){
//        return new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String areaName = areaspinner.getSelectedItem().toString();
//
//                try {
//                    for (int i = 0; i < areaDBData.length(); i++) {
//                        String areaData = areaDBData.getJSONObject(i).get("Name").toString();
//                        if (areaData.equals(areaName)) {
//                            areaID = areaDBData.getJSONObject(i).get("ID").toString();
//                            break;
//                        }
//                    }
//                } catch (Exception e) {
//
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        };
//    }

    //area selection green button click to select word and area
//    public void areaPopup(View v){
//        deviceBackFlag="popup";
//        initiatePopupWindow();
//    }

    /*Edit Location Option */
//    public void editLocation(View v){
//        btn_edit_loc.setVisibility(View.GONE);
//        btn_select_area.setVisibility(View.VISIBLE);
//        location.setText("");
//    }


    //initiatePopupWindow to open inflate layout popup
//    private void initiatePopupWindow() {
//        try {
//            pwindo = new PopupWindow(layout, width, height, false);
//            pwindo.showAtLocation(layout, Gravity.NO_GRAVITY, 0, 0);
//        } catch (Exception e) { }
//    }
    // method to validate complaint registration page
    public boolean isValid(){
      //  String landMarkText = landmark.getText().toString();
        String complnDtlText = complnDtl.getText().toString();
       // String loctn=location.getText().toString();
        if (imgStatus.equals("Inprocess")) {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),"Please Wait image is uploading", Snackbar.LENGTH_LONG);
            ColoredSnackbar.alert(snackbar).show();
            //Toast.makeText(ComplaintRegdActivity.this, "Please Wait image is uploading", Toast.LENGTH_LONG).show();
            return false;
        }else if (catagoryID.equals("0") || catagoryID.equals("")) {
            // Toast.makeText(ComplaintRegdActivity.this, "Please select complaint catagory", Toast.LENGTH_LONG).show();
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),"Please select complaint catagory", Snackbar.LENGTH_LONG);
            ColoredSnackbar.alert(snackbar).show();
            return false;
        }
       /* else if (imgStatus.equals("Inprocess")) {
            //Toast.makeText(ComplaintRegdActivity.this, "Please select complaint type", Toast.LENGTH_LONG).show();
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),"Please select complaint type", Snackbar.LENGTH_LONG);
            ColoredSnackbar.alert(snackbar).show();
            return false;
        }*/
        else if (typeID.equals("0") || typeID.equals("")) {
            // Toast.makeText(ComplaintRegdActivity.this, "Please select complaint type", Toast.LENGTH_LONG).show();
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),"Please select complaint category", Snackbar.LENGTH_LONG);
            ColoredSnackbar.alert(snackbar).show();
            return false;
        } else if (complnDtlText.equals("")) {
            //Toast.makeText(ComplaintRegdActivity.this, "Complaint detail can't be left blank", Toast.LENGTH_LONG).show();
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),"Complaint detail can't be left blank", Snackbar.LENGTH_LONG);
            ColoredSnackbar.alert(snackbar).show();
            complnDtl.requestFocus();
            return false;
        }
//        else if (landMarkText.equals("")) {
//            //Toast.makeText(ComplaintRegdActivity.this, "Landmark can't be left blank", Toast.LENGTH_LONG).show();
//            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),"Landmark can't be left blank", Snackbar.LENGTH_LONG);
//            ColoredSnackbar.alert(snackbar).show();
//            landmark.requestFocus();
//            return false;
//        } else if(loctn.equals("")){
//            // Toast.makeText(ComplaintRegdActivity.this, "Please enter your location.", Toast.LENGTH_LONG).show();
//            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),"Please enter your location.", Snackbar.LENGTH_LONG);
//            ColoredSnackbar.alert(snackbar).show();
//            return false;
//        }

        return true;
    }
    //registerComplaint method to return OnClickListener for post complaint
//    public View.OnClickListener registerComplaint(){
//        return new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isValid()) {
//                    String dbWordID;
//                    String dbAreaID;
//                    String name = objLogin.getUSER_NAME();
//                    String mobNo = objLogin.getMOBILENO();
//                    String citizenid = objLogin.getCITIZEN_ID();
//                    String landMarkText = landmark.getText().toString(); //Getting landmark from landmark edit text field
//                    String complnDtlText = complnDtl.getText().toString();//Getting complaint detail from landmark edit text field
//                    /*
//                    * Condition if complaint from user's own area
//                    * */
//                    //condition for getting lat long for complaint location
//                   /* if(gps.canGetLocation()){
//                        latitude = String.valueOf(gps.getLatitude());
//                        longitude = String.valueOf(gps.getLongitude());
//                        // Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
//                    }*/
//                   /* Log.i("STAG", "file name==="+imgStatus+"::::::::reopen=" + reopen + ":::name=" + name + ":::mob no=" + mobNo + ":::word id=" + wordID + ":::catagory id="
//                            + catagoryID + ":::type id=" + typeID + ":::area id=" + areaID + ":::landmark=" + landMarkText + ":::complaint text="
//                            + complnDtlText + ":::encoded img=" + encodedImage + ":::citizen id=" + citizenid + ":::Lat=" + latitude + ":::Long=" + longitude);*/
//
//                    //Condition for network availability
//                    if (AppCommon.isNetworkAvailability(ComplaintRegdActivity.this)) {
//                        common.showDialog("Registering...."); //Showing registering dialog
//                        Map<String, String> params = new HashMap<String, String>();
//                        try {
//
//                            params.put("strReopenFlag", reopen);
//                            params.put("strApplicantName", name);
//                            params.put("strPhoneNo", mobNo);
//                            params.put("strWardValue", wordID);
//                            params.put("strCatagory", catagoryID);
//                            params.put("strType", typeID);
//                            params.put("strAreaValue", areaID);
//                            params.put("strLandMark", landMarkText);
//                            params.put("strCompDetails", complnDtlText);
//                            params.put("strAddress", "");
//                            // params.put("image", encodedImage);
//                            if(imgStatus.contains("No"))
//                                params.put("image", "");
//                            else
//                                params.put("image", imgStatus);
//
//                            params.put("strLat", latitude);
//                            params.put("strLong", longitude);
//                            params.put("strLastUpdateBy", citizenid);
//                            params.put("strDeviceId", "");
//                            params.put("strDevicePlatform", "");
//                            params.put("strDeviceModel", "");
//                            params.put("strAppVersion", "");
//                            params.put("strGoogleArea", googleAreaName);
//
//                        } catch (Exception e) {
//                        }
//
//                        String url = AppCommon.getURL() + "complainRegistrationV2";
//                        PostJsonStringRequest jsonObjReq = new PostJsonStringRequest(
//                                url, new JSONObject(params),
//                                new Response.Listener<String>() {
//                                    @Override
//                                    public void onResponse(String response) {
//                                        try {
//                                            common.hideDialog();
//                                            catagoryID = "0";
//                                            typeID = "0";
//                                            googleAreaName="";
//                                            typeText.setVisibility(View.GONE);
//                                            encodedImage = "";
//                                            crossBtn.setVisibility(View.INVISIBLE);
//                                            complnImage.setVisibility(View.INVISIBLE);
//                                            landmark.setText("");
//                                            complnDtl.setText("");
//                                            String[] ticketID = response.split(",");
//                                            // Log.i("STAG", response);
//                                            //common.showAlert("Message", "Complain is registered successfully - " + ticketID[1]);
//                                            Intent intent = new Intent(ComplaintRegdActivity.this, ComplaintConfirmation.class);
//                                            intent.putExtra("ticketId", ticketID[1]);
//                                            startActivity(intent);
//                                        }
//                                        catch(Exception ex){
//                                            common.showAlert("Message", "Please try after sometimes");
//                                        }
//                                    }
//                                }, new Response.ErrorListener() {
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//                                common.hideDialog();
//                                common.showAlert("Message","Please try after sometimes");
//                            }
//                        });
//                        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
//                                90000,
//                                -1,
//                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//                        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
//                    } else { //else condition to store complaint info in draft
//                        DatabaseHandler db = new DatabaseHandler(ComplaintRegdActivity.this);
//                        Calendar c = Calendar.getInstance();
//                        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy hh:mm aa");
//                        String formattedDate = df.format(c.getTime());
//                        Log.i("STAG", formattedDate);
//                        ComplaintData comp_data = new ComplaintData();
//                        comp_data.setReopen(reopen);
//                        comp_data.setApplicant_name(name);
//                        comp_data.setPhone_no(mobNo);
//                        comp_data.setCopm_ward_id(wordID);
//                        comp_data.setCatagory_id(catagoryID);
//                        comp_data.setType_id(typeID);
//                        comp_data.setComp_area_id(areaID);
//                        comp_data.setLandmark(landMarkText);
//                        comp_data.setComplaint_details(complnDtlText);
//                        comp_data.setComp_image(encodedImage);
//                        comp_data.setLat(latitude);
//                        comp_data.setLongitude(longitude);
//                        comp_data.setComp_citizen_id(citizenid);
//                        comp_data.setCurrent_date(formattedDate);
//                        db.addComplaintData(comp_data);
//                        Intent intent = new Intent(ComplaintRegdActivity.this, ComplaintConfirmation.class);
//                        intent.putExtra("ticketId", "0");
//                        startActivity(intent);
//                        //common.showAlert("Message", "Connection is not available.Your complaint is stored in draft.");
//                    }
//                }
//            }
//        };
//    }
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
                Log.i("HttpFileUpload","URL Malformatted");
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
