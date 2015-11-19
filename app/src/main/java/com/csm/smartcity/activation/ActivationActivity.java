package com.csm.smartcity.activation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.csm.smartcity.R;
import com.csm.smartcity.common.AppCommon;
import com.csm.smartcity.common.AppController;
import com.csm.smartcity.common.ColoredSnackbar;
import com.csm.smartcity.common.CommonDialogs;
import com.csm.smartcity.common.UtilityMethods;
import com.csm.smartcity.connection.PostJsonArrayRequest;
import com.csm.smartcity.dashboard.NewDashboardActivity;
import com.csm.smartcity.login.LoginActivity;
import com.csm.smartcity.sqlLiteModel.DatabaseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ActivationActivity extends AppCompatActivity {

    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    MyCountDownTimer myCountDownTimer;
    Button button5;
    Button btnsubmit;
    EditText edtOTP;
    TextView smsTimeText;
    TextView smsText;
    TextView textView4;
    String mobNo;
    String user_data="";
    final String tag_json_obj = "Mob_Act_Code";
    final String mob_reg_otp = "mob_reg_otp";
    private boolean bolBroacastRegistred;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activation);

        button5=(Button)findViewById(R.id.button5);
        btnsubmit=(Button)findViewById(R.id.btnsubmit);

        mobNo = getIntent().getExtras().getString("mobNo");

        user_data=getIntent().getExtras().getString("userdata");

        edtOTP=(EditText)findViewById(R.id.edtOTP);
        textView4=(TextView)findViewById(R.id.textView4);
        textView4.setText("A 4 digit activation code has been send to your mobile No:" + mobNo + " on SMS");
        smsTimeText=(TextView)findViewById(R.id.smsTimeText);
        smsText=(TextView)findViewById(R.id.smsText);
        init();
        btnsubmit.setOnClickListener(new View.OnClickListener() {           //:::::::::::::::::::::::if SMS is Not Comming with in time
            @Override
            public void onClick(View v) {
                if (AppCommon.isNetworkAvailability(getApplicationContext())) {
                    if (edtOTP.getText().toString() != "" && edtOTP.getText().toString().length() > 3)
                        postSMS();
                    else{
                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), CommonDialogs.ONE_TIME_PASS, Snackbar.LENGTH_LONG);
                        ColoredSnackbar.confirm(snackbar).show();
                       // Toast.makeText(ActivationActivity.this, "Please Enter one time password", Toast.LENGTH_SHORT).show();
                    }

                } else{
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),  CommonDialogs.INTERNET_UNAVAILABLE, Snackbar.LENGTH_LONG);
                    ColoredSnackbar.confirm(snackbar).show();
                    //Toast.makeText(ActivationActivity.this, "Please check your internet connectivity.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    public void postSMS() {
        AppCommon.showDialog("Loading...",ActivationActivity.this);
        final SharedPreferences prefs = getSharedPreferences("GCM_ID", Context.MODE_MULTI_PROCESS);
        String registrationId = prefs.getString("regId", "");
        // Toast.makeText(ActivationActivity.this, registrationId, Toast.LENGTH_SHORT).show();
        String OTP=edtOTP.getText().toString();
        Map<String, String> params = new HashMap<String, String>();
        try {
            params.put("Activation_No", OTP);
            params.put("GCM_ID", registrationId);
        } catch (Exception e) { }

        String url = AppCommon.getURL() + "citizenActivation";
        PostJsonArrayRequest jsonObjReq = new PostJsonArrayRequest(
                url,new JSONObject(params),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        AppCommon.hideDialog();
                        try {
                            // Log.i("STAG",response.toString());
                            JSONObject profileData = response.getJSONObject(0);
                            if(response.getJSONObject(0).length()>0){

                                if(user_data.equals("")){

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
                                    DatabaseHandler db = new DatabaseHandler(ActivationActivity.this);
                                    int countData = db.getControldataCount();
                                    if (countData == 0) {
                                        UtilityMethods.getAllData("ACT", ActivationActivity.this);
                                    }else {
                                        Intent i = new Intent(ActivationActivity.this, NewDashboardActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(i);
                                    }
                                }else{
                                    activateUser();
                                }
                            }
                            else{
                                Toast.makeText(ActivationActivity.this, "Please Enter Valiid OTP Or Regenerate OTP", Toast.LENGTH_SHORT).show();
                            }


                        } catch (Exception e) {
                            Toast.makeText(ActivationActivity.this, "Please Enter Valiid OTP Or Regenerate OTP", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AppCommon.hideDialog();
                Toast.makeText(ActivationActivity.this, "Please try after sometimes", Toast.LENGTH_SHORT).show();
            }
        });
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjReq, mob_reg_otp);
    }                                           //::::::::::::::::::::::::Processing after getting SMS

    public void init() {
        smsText.setVisibility(View.GONE);
        button5.setEnabled(false);
        btnsubmit.setEnabled(false);
        IntentFilter filter = new IntentFilter(Intent.ACTION_DEFAULT);
        filter.addAction(SMS_RECEIVED);
        filter.setPriority(1000);
        bolBroacastRegistred=true;
        ActivationActivity.this.registerReceiver(ActivationActivity.this.smsReceiver, filter);
        myCountDownTimer =  new MyCountDownTimer(120000, 1000);
        myCountDownTimer.start();
        smsText.setVisibility(View.VISIBLE);
        smsTimeText.setVisibility(View.VISIBLE);
    }


    public void activateUser(){
        final SharedPreferences prefs = getSharedPreferences("GCM_ID", Context.MODE_MULTI_PROCESS);
        String registrationId = prefs.getString("regId", "");
        AppCommon.showDialog("Activating....",ActivationActivity.this);
        String[] modifiedUserData = user_data.split(",");
        Map<String, String> params = new HashMap<String, String>();

        try {
            params.put("strFlag", "ACTV");
            params.put("strCitizenID", modifiedUserData[1]);
            params.put("strCitizenName", modifiedUserData[2]);
            params.put("strImage", modifiedUserData[3]);
            params.put("strMobileNo", modifiedUserData[4]);
            params.put("strWard", modifiedUserData[5]);
            params.put("strArea", modifiedUserData[6]);
            params.put("strEmail", modifiedUserData[7]);
            params.put("strGender", modifiedUserData[8]);
            params.put("captureFlag", modifiedUserData[9]);
            params.put("gcmCode", registrationId);
            params.put("strDeviceId", "");
            params.put("strDevicePlatform", "");
            params.put("strDeviceModel", "");
            params.put("strAppVersion", "");
            params.put("FBUserID", "");
        } catch (Exception e) {
        }

        String url = AppCommon.getURL() + "citizenRegistrationV2";
        PostJsonArrayRequest jsonObjReq = new PostJsonArrayRequest(
                url, new JSONObject(params),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        AppCommon.hideDialog();
                        try {
                            Log.i("STAG", response.toString());

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
                            finish();
                           /* Intent i = new Intent(ActivationActivity.this, UserProfileActivity.class);
                            startActivity(i);*/

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AppCommon.hideDialog();
                AppCommon.showAlert("Message", "Please try after sometimes",ActivationActivity.this);
            }
        });
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);



    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_type_popup, menu);
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
//        if (id == R.id.crossicon) {
//
//            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
//            //builder.setTitle("Logout confirmation");
//            builder.setMessage("Your activation is in process.Do you want to exit?");
//            // builder.setPositiveButton("Ok",null);
//            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    if(user_data.equals("")){
//                        Intent intent=new Intent(ActivationActivity.this,MainActivity.class);
//                        startActivity(intent);
//                    }else
//                        finish();
//                }
//            });
//
//            builder.setNegativeButton("Cancel", null);
//            builder.show();
//
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
        //builder.setTitle("Logout confirmation");
        builder.setMessage("Your activation is in process.Do you want to exit?");
        // builder.setPositiveButton("Ok",null);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(user_data.equals("")){
                    Intent intent=new Intent(ActivationActivity.this,LoginActivity.class);
                    startActivity(intent);
                }else
                    finish();
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();

    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            AppCommon.dismissDialog();
            if (bolBroacastRegistred) {
                ActivationActivity.this.unregisterReceiver(ActivationActivity.this.smsReceiver);
                bolBroacastRegistred = false;
            }

        }catch (Exception e){

        }
    }
    public void activateBtnClick(View v){
        AppCommon.showDialog("Please wait...",ActivationActivity.this);
        String url = AppCommon.getURL()+"citizenvalidmob/"+mobNo;
        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            if(Integer.parseInt(response.getString("citizenValidMobResult"))>0){
                                AppCommon.hideDialog();
                                init();

                            }
                            Log.d("TAG", response.getString("citizenValidMobResult")+"");
                        }
                        catch(Exception ex){
                            AppCommon.hideDialog();
                            Log.d("TAG", "New Number");
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAG", "Error: " + error);
                // hide the progress dialog
                AppCommon.hideDialog();
            }
        });
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

//        Intent i = new Intent(this, DashboardActivity.class);
//        //i.putExtras(sendBundle);
//        startActivity(i);
    }                               //::::::::::::::::::::::::Resend if SMS is not comming

    //region "Broad Cast Receiver"
    private BroadcastReceiver smsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String strMsgBody = null;
            Bundle bundle = intent.getExtras();
            if( bundle != null) {
                Object[] smsextras = (Object[]) bundle.get( "pdus" );
                for ( int i = 0; i < smsextras.length; i++ )
                {
                    SmsMessage smsmsg = SmsMessage.createFromPdu((byte[]) smsextras[i]);
                    strMsgBody = smsmsg.getMessageBody().toString();
                    String[] strArr=strMsgBody.split("-");
                    if(strArr[0].equals("MCMP CODE")) {
                        edtOTP.setText(strArr[1]);
                        Log.v("TAG", "smsReceiver.onReceive, " + strArr[1]);
                        myCountDownTimer.cancel();
                        button5.setText("Resend");
                        button5.setEnabled(true);
                        button5.setTextColor(Color.WHITE);
                        btnsubmit.setEnabled(true);
                        smsText.setVisibility(View.GONE);
                        smsTimeText.setVisibility(View.GONE);
                        ActivationActivity.this.unregisterReceiver(ActivationActivity.this.smsReceiver);
                        bolBroacastRegistred = false;
                        postSMS();
                    }
                }
            }

        }
    };
    //endregion                                                                                                  //::::::::::::::::::::::::Resend if SMS is not comming

    public class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            smsTimeText.setText((millisUntilFinished / 1000) + " Sec.");
        }

        @Override
        public void onFinish() {
            try {
                button5.setEnabled(true);
                btnsubmit.setEnabled(true);
                smsText.setVisibility(View.GONE);
                smsTimeText.setVisibility(View.GONE);
                if (bolBroacastRegistred) {
                    ActivationActivity.this.unregisterReceiver(ActivationActivity.this.smsReceiver);
                    bolBroacastRegistred = false;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }               //::::::::::::::::::::::::Timer Control
}
