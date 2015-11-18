package com.csm.smartcity.userRegistration;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.csm.smartcity.R;
import com.csm.smartcity.activation.ActivationActivity;
import com.csm.smartcity.common.AppCommon;
import com.csm.smartcity.common.AppController;
import com.csm.smartcity.common.CircularNetworkImageView;
import com.csm.smartcity.common.ColoredSnackbar;
import com.csm.smartcity.common.CommonDialogs;
import com.csm.smartcity.common.RoundImage;
import com.csm.smartcity.common.UtilityMethods;
import com.csm.smartcity.connection.CustomVolleyRequestQueue;
import com.csm.smartcity.connection.PostJsonArrayRequest;
import com.csm.smartcity.sqlLiteModel.ControllData;
import com.csm.smartcity.sqlLiteModel.DatabaseHandler;
import com.csm.smartcity.sqlLiteModel.LoginUserObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserRegistrationActivity extends AppCompatActivity {

      //components of registration page
    ImageView regdUserImg;                          //Declaration of user iamge ImageView field
    CircularNetworkImageView regdNetworkUserImg;    //Declaration of user iamge CircularNetworkImageView field
    TextView photoLBL;                              //Declaration of photoLBL TextView field
    EditText regdName;                              //Declaration of  name EditText field
    EditText regdMobNo;                             //Declaration of  mobile no EditText field
    EditText regdEmail;                             //Declaration of  email EditText field
    AutoCompleteTextView regdAreaAutoComplete;      //Declaration of  area AutoCompleteTextView field
    RadioGroup radioGrp;                            //Declaration of gender RadioGroup
    Button registerBtn;                             //Declaration of register button
    /*
    * Declaration of string variables
    * */
    final String tag_json_obj = "json_obj_req";  //Variable for volley request queue
    static int REQUEST_CAMERA=1;         //Request camera request code to check when activityOnResult response
    private static int RESULT_LOAD_IMG = 2; //variable for start activity on result request code to load image from gallery
    String mobNo;            //variable to store mobile no retrieving from bundle extras
    String areaId="0";       //variable to store area id
    String wordId="0";       //variable to store ward id
    String encodedImage="";  //variable to store byte code of image
    String dbImgFlag="";     //variable to store db image flag in case of edit
    String captureFlag="0";  //variable to store flag whether image is captured or not
    String citizenID="";     //variable to store citizen id
    String extraUserData;    //variable to store user data incase of update mob no
    String actvMobNo="";     //Variable to store mob no in update case
    /*
    * Declaration of class variables
    * */
    JSONArray sreaDBData;       //Declaration of JSONArray to store area data
    ImageLoader mImageLoader;   //Declaration of ImageLoader to load network image
    CustomVolleyRequestQueue customvolley;  //Declaration of CustomVolleyRequestQueue to store volley request in a queue
    ArrayList<String> arrList;
    ViewGroup viewGroup;  //Declaration of viewGroup for getting content of the activity to show custom message
    //Life cycle method onCreate of this class which is overridden
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Used to set XML view to this activity/context
        setContentView(R.layout.activity_user_registration);
        //Getting data from extras
        Bundle bundle = getIntent().getExtras(); //Getting extras from bundle
        mobNo = bundle.getString("mobNo");       //getting mobile no from bundle
        regdUserImg=(ImageView)findViewById(R.id.regdUserImg); //Getting user image component
        regdNetworkUserImg=(CircularNetworkImageView)findViewById(R.id.regdNetworkUserImg); //Getting network user image
        regdNetworkUserImg.setVisibility(View.GONE);    //to make it invisible
        photoLBL=(TextView)findViewById(R.id.photoLBL); //getting photo lbl component
        regdName=(EditText)findViewById(R.id.regdName); //getting name from text field
        regdMobNo=(EditText)findViewById(R.id.regdMobNo); //getting mobile number from text field
        regdEmail=(EditText)findViewById(R.id.regdEmail); //Getting email from text field
        regdAreaAutoComplete=(AutoCompleteTextView)findViewById(R.id.regdAreaAutoComplete); //Getting auto complete text view
        radioGrp=(RadioGroup)findViewById(R.id.radioGrp); //getting gender radio group
        registerBtn=(Button)findViewById(R.id.registerBtn); //Getting login button
        // Getting content view group for custom message
        viewGroup = ((ViewGroup) this.findViewById(android.R.id.content));
        //Getting area,ward data from SQLlite DB through DatabaseHandler
        DatabaseHandler db=new DatabaseHandler(this);
        List<ControllData> cntrlData=db.getAllContollData("1,2");
        //Putting areas in string array list
        arrList = new ArrayList<String>();
        try {
            sreaDBData=new JSONArray(cntrlData.get(0).getDataValue());
            for(int i=0;i<sreaDBData.length();i++){
                //areaData.put(sreaDBData.getJSONObject(i).get("ID").toString(), sreaDBData.getJSONObject(i).get("Name").toString()) ;
                arrList.add(sreaDBData.getJSONObject(i).get("Name").toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //adding arraylist to area  AutoCompleteTextView through ArrayAdapter
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,arrList);
        regdAreaAutoComplete.setAdapter(adapter);
        regdAreaAutoComplete.setThreshold(1);// list appear from single letter type
        //:::::::::::::Area auto complete click listener
        regdAreaAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                String areaName = arg0.getItemAtPosition(arg2).toString();
                try {
                    for (int i = 0; i < sreaDBData.length(); i++) {
                        if (areaName == sreaDBData.getJSONObject(i).get("Name").toString()) {
                            areaId = sreaDBData.getJSONObject(i).get("ID").toString();
                            wordId = sreaDBData.getJSONObject(i).get("WARD_ID").toString();
                            break;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        //IF condition for edit button click from profile page
        if(mobNo.equals("edit")) {
            LoginUserObject objLogin= AppCommon.getLoginPrefData(this);
            photoLBL.setText("Change photo");
            registerBtn.setText("Update");
            getSupportActionBar().setTitle("Update Profile");
            citizenID=objLogin.getCITIZEN_ID();
            String imagePath="";
            String userImg=objLogin.getIMAGE();
            String imageFlag=objLogin.getIMAGE_FLAG();
            if(!userImg.equals("") && !imageFlag.equals("FA")){
                imagePath= AppCommon.getUserPhotoURL()+userImg;//{{ImgPath}}Citizen/citizen_{{image}};
                dbImgFlag="U";
                encodedImage="exist";
            }else if(!userImg.equals("") && imageFlag.equals("FA")){
                imagePath=userImg;
                dbImgFlag="FU";
            }else{
                imagePath="";
                dbImgFlag="U";
            }
            if(!imagePath.equals("")) {
                try {
                    customvolley = CustomVolleyRequestQueue.getInstance(this.getApplicationContext());
                    mImageLoader = customvolley.getImageLoader();
                    mImageLoader.get(imagePath, ImageLoader.getImageListener(regdNetworkUserImg, R.drawable.userimg,R.drawable.userimg ));
                    regdNetworkUserImg.setImageUrl(imagePath, mImageLoader);
                    regdUserImg.setVisibility(View.GONE);
                    regdNetworkUserImg.setVisibility(View.VISIBLE);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            actvMobNo=objLogin.getMOBILENO();
            regdName.setText(objLogin.getUSER_NAME());
            regdMobNo.setText(objLogin.getMOBILENO());
            regdEmail.setText(objLogin.getEMAIL_ID());
            wordId=objLogin.getWARD_ID();
            areaId=objLogin.getAREA_ID();
            String areaName="";
            try {

                for (int i = 0; i < sreaDBData.length(); i++) {
                    if (areaId.equals(sreaDBData.getJSONObject(i).get("ID").toString())) {
                        areaName = sreaDBData.getJSONObject(i).get("Name").toString();
                        break;
                    }
                }
            } catch (Exception e){}

            //  final String finalAreaName = areaName;
            regdAreaAutoComplete.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //regdAreaAutoComplete.showDropDown();
                }
            }, 500);
            regdAreaAutoComplete.setText(areaName);
            regdAreaAutoComplete.setSelection(regdAreaAutoComplete.getText().length());
            ((RadioButton)radioGrp.getChildAt(Integer.parseInt(objLogin.getGENDER()))).setChecked(true);


        }else{  //for normal registration
            regdMobNo.setText(mobNo, TextView.BufferType.EDITABLE);
            regdMobNo.setEnabled(false);
        }
        //Setting onClickListener to registration button
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrationClick();
            }
        });

    } //end of on create override method of this activity

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_back, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.regdMenuIcon) {
//            finish();
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
    public void imageClick(View v){

        final CharSequence[] items = {
                "Take photo", "Choose from gallery"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose options");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {

                if (item == 0) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                }else if(item == 1){
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Select File"), RESULT_LOAD_IMG);
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode==1 && resultCode == RESULT_OK && data!=null) {
                Bitmap picture = (Bitmap) data.getExtras().get("data");
                Bitmap bm1= UtilityMethods.getUserResizedBitmap(picture, 80, 80);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bm1.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] b = stream.toByteArray();
                encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                //Log.i("STAG", encodedImage);
                RoundImage ri=new RoundImage(bm1);
                regdUserImg.setImageDrawable(ri);
                captureFlag="1";
                regdNetworkUserImg.setVisibility(View.GONE);
                regdUserImg.setVisibility(View.VISIBLE);

            }  else if(requestCode==2 && resultCode == RESULT_OK && data!=null) {
                //ImageView imgView = (ImageView) findViewById(R.id.complnImage);
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String imgDecodableString = cursor.getString(columnIndex);
                cursor.close();

                Bitmap bm = BitmapFactory.decodeFile(imgDecodableString);
                Bitmap bm1= UtilityMethods.getUserResizedBitmap(bm, 80, 80);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm1.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                byte[] b = baos.toByteArray();
                encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                RoundImage ri=new RoundImage(bm1);
                regdUserImg.setImageDrawable(ri);
                captureFlag="1";
                regdNetworkUserImg.setVisibility(View.GONE);
                regdUserImg.setVisibility(View.VISIBLE);

            }
            else{
              //  new NotificationTipView(getApplicationContext(),viewGroup, 10000, NotificationTipView.IMAGE_PICK);
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),  CommonDialogs.IMAGE_PICK, Snackbar.LENGTH_LONG);
                ColoredSnackbar.confirm(snackbar).show();
            }
        } catch (Exception e) {
            // Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
          //  new NotificationTipView(getApplicationContext(),viewGroup, 10000, NotificationTipView.IMAGE_ERROR);
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),  CommonDialogs.IMAGE_ERROR, Snackbar.LENGTH_LONG);
            ColoredSnackbar.confirm(snackbar).show();

        }

    }
    //Function to validate registration form
    public boolean isValid(){
        if ((regdName.getText().toString()).equals("")) {
           // new NotificationTipView(getApplicationContext(),viewGroup, 10000, NotificationTipView.NAME_VALID);
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),  CommonDialogs.NAME_VALID, Snackbar.LENGTH_LONG);
            ColoredSnackbar.confirm(snackbar).show();
            return false;
        }else if((regdMobNo.getText().toString()).equals("")) {
          //  new NotificationTipView(getApplicationContext(), viewGroup, 10000, NotificationTipView.MOB_VALID);
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),  CommonDialogs.MOB_VALID, Snackbar.LENGTH_LONG);
            ColoredSnackbar.confirm(snackbar).show();
            return false;
        } else if((regdMobNo.getText().toString()).length()<10) { //Condition for invalid mob no
           // new NotificationTipView(getApplicationContext(),viewGroup, 10000, NotificationTipView.MOB_NO_LENGTH);
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),  CommonDialogs.MOB_NO_LENGTH, Snackbar.LENGTH_LONG);
            ColoredSnackbar.confirm(snackbar).show();
            return false;
        } else if ((regdEmail.getText().toString()).equals("")) {
           // new NotificationTipView(getApplicationContext(),viewGroup, 10000, NotificationTipView.EMAIL_VALID);
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),  CommonDialogs.EMAIL_VALID, Snackbar.LENGTH_LONG);
            ColoredSnackbar.confirm(snackbar).show();
            return false;
        }else if(!(Patterns.EMAIL_ADDRESS.matcher(regdEmail.getText().toString()).matches())){
            //new NotificationTipView(getApplicationContext(),viewGroup, 10000, NotificationTipView.ADDRS_VALID);
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),  CommonDialogs.ADDRS_VALID, Snackbar.LENGTH_LONG);
            ColoredSnackbar.confirm(snackbar).show();
            return false;
        }else if (areaId.equals("0") || (regdAreaAutoComplete.getText().toString()).equals("")) {
           // new NotificationTipView(getApplicationContext(),viewGroup, 10000, NotificationTipView.AREA_VALID);
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),  CommonDialogs.AREA_VALID, Snackbar.LENGTH_LONG);
            ColoredSnackbar.confirm(snackbar).show();
            return false;
        }
        return true;
    }

    public void registrationClick(){
        //Condition for network unavailability
        if(AppCommon.isNetworkAvailability(getApplicationContext())) {
            Map<String, String> params = new HashMap<String, String>();
            if (isValid()) {
                //Getting GSM id from SharedPreferences
                final SharedPreferences prefs = getSharedPreferences("GCM_ID", Context.MODE_MULTI_PROCESS);
                String registrationId = prefs.getString("regId", "");
                //Getting checked index of gender radio button
                int radioButtonID = radioGrp.getCheckedRadioButtonId();
                View radioButton = radioGrp.findViewById(radioButtonID);
                int idx = radioGrp.indexOfChild(radioButton);
                //IF condition for update click
                if(mobNo.equals("edit")){
                    AppCommon.showDialog("Updating....",UserRegistrationActivity.this);
                    //In case of mobile no change to update at activation time updation data is stored in variable
                    extraUserData=dbImgFlag+","+citizenID+","+regdName.getText().toString()+","+encodedImage+","
                            +regdMobNo.getText().toString()+","+wordId+","+areaId+","+regdEmail.getText().toString()+","+String.valueOf(idx)+","+captureFlag;

                    try {
                        params.put("strFlag", dbImgFlag);
                        params.put("strCitizenID", citizenID);
                        params.put("strCitizenName", regdName.getText().toString());
                        params.put("strImage", encodedImage);
                        params.put("strMobileNo", regdMobNo.getText().toString());
                        params.put("strWard", wordId);
                        params.put("strArea", areaId);
                        params.put("strEmail", regdEmail.getText().toString());
                        params.put("strGender", String.valueOf(idx));
                        params.put("captureFlag", captureFlag);
                        params.put("gcmCode", registrationId);
                        params.put("strDeviceId", "");
                        params.put("strDevicePlatform", "");
                        params.put("strDeviceModel", "");
                        params.put("strAppVersion", "");
                        params.put("FBUserID", "");
                    } catch (Exception e) {
                    }

                }else { //For normal registration
                    AppCommon.showDialog("Registering....",UserRegistrationActivity.this);

                    try {
                        params.put("strFlag", "A");
                        params.put("strCitizenID", "0");
                        params.put("strCitizenName", regdName.getText().toString());
                        params.put("strImage", encodedImage);
                        params.put("strMobileNo", regdMobNo.getText().toString());
                        params.put("strWard", wordId);
                        params.put("strArea", areaId);
                        params.put("strEmail", regdEmail.getText().toString());
                        params.put("strGender", String.valueOf(idx));
                        params.put("captureFlag", "1");
                        params.put("gcmCode", registrationId);
                        params.put("strDeviceId", "");
                        params.put("strDevicePlatform", "");
                        params.put("strDeviceModel", android.os.Build.DEVICE);
                        params.put("strAppVersion","");
                        params.put("FBUserID", "");
                    } catch (Exception e) {
                    }
                }
                //Server url for citizen registration
                String url = AppCommon.getURL() + "citizenRegistrationV2";
                /*
                * PostJsonArrayRequest to send data to server
                * It is a custom method inside connection package
                * */
                PostJsonArrayRequest jsonObjReq = new PostJsonArrayRequest(url, new JSONObject(params),
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                AppCommon.hideDialog();//Hide progress dialog after data stored in server
                                Log.i("STAG", response.toString());
                                try {
                                    //To catch response data
                                    String res_flag=response.getJSONObject(0).getString("Flag");
                                    //IF condition for update response
                                    if(mobNo.equals("edit")) {
                                        /*
                                        * Condition for whether goto profile page activity or activation activity
                                        * 1- if mobile no is not changed (goto profile page activity)
                                        * else-  for mobile no changed (goto activation activity)
                                        * */
                                        if(res_flag.equals("1")){
                                            JSONObject profileData = response.getJSONObject(0).getJSONArray("UserDetail").getJSONObject(0);
                                            SharedPreferences pref = getSharedPreferences("MyPofile", Context.MODE_PRIVATE);
                                            SharedPreferences.Editor edit = pref.edit();
                                            edit.putString("USER_NAME", profileData.getString("USER_NAME"));
                                            edit.putString("MOBILENO", profileData.getString("MOBILENO"));
                                            edit.putString("IMAGE", profileData.getString("IMAGE"));
                                            edit.putString("IMAGE_FLAG", profileData.getString("IMAGE_FLAG"));
                                            edit.putString("ADDRESS", profileData.getString("ADDRESS"));
                                            edit.putString("DOB", profileData.getString("DOB"));
                                            edit.putString("LAND_MARK", profileData.getString("LAND_MARK"));
                                            edit.putString("AREA_ID", profileData.getString("AREA_ID"));
                                            edit.putString("REGD_DATE", profileData.getString("REGD_DATE"));
                                            edit.putString("GENDER", profileData.getString("GENDER"));
                                            edit.putString("PLOT", profileData.getString("PLOT"));
                                            edit.putString("CITIZEN_ID", profileData.getString("CITIZEN_ID"));
                                            edit.putString("WARD_ID", profileData.getString("WARD_ID"));
                                            edit.putString("EMAIL_ID", profileData.getString("EMAIL_ID"));
                                            edit.commit();

                                            AlertDialog.Builder build = AppCommon.showAlertWithCallBack("", "Profile updated successfully.",UserRegistrationActivity.this);
                                            build.setCancelable(false);
                                            build.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    finish();
                                                }
                                            });
                                            build.show();


                                        }else{
                                            finish();
                                            Intent i = new Intent(UserRegistrationActivity.this, ActivationActivity.class);
                                            i.putExtra("mobNo",regdMobNo.getText().toString());
                                            i.putExtra("userdata", extraUserData);
                                            startActivity(i);
                                        }
                                    }else{//condition for registration response
                                        Intent i = new Intent(UserRegistrationActivity.this, ActivationActivity.class);
                                        i.putExtra("mobNo", regdMobNo.getText().toString());
                                        i.putExtra("userdata","");
                                        startActivity(i);
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        AppCommon.hideDialog();
                        //Custom message for server error
                       // new NotificationTipView(getApplicationContext(),viewGroup, 10000, NotificationTipView.SERVER_ERROR);
                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),  CommonDialogs.SERVER_ERROR, Snackbar.LENGTH_LONG);
                        ColoredSnackbar.confirm(snackbar).show();
                    }
                });
                //Retry policy of request queue
                jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                        30000,
                        -1,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

            }
        }else{
            //Custom message for internet unavailability
           // new NotificationTipView(getApplicationContext(), viewGroup, 10000, NotificationTipView.INTERNET_UNAVAILABLE);
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),  CommonDialogs.INTERNET_UNAVAILABLE, Snackbar.LENGTH_LONG);
            ColoredSnackbar.confirm(snackbar).show();
        }
    }
}
