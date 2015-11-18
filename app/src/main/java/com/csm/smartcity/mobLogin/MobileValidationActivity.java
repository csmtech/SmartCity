package com.csm.smartcity.mobLogin;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.csm.smartcity.R;
import com.csm.smartcity.activation.ActivationActivity;
import com.csm.smartcity.common.AppCommon;
import com.csm.smartcity.common.AppController;
import com.csm.smartcity.common.ColoredSnackbar;
import com.csm.smartcity.common.CommonDialogs;
import com.csm.smartcity.common.UtilityMethods;
import com.csm.smartcity.sqlLiteModel.DatabaseHandler;
import com.csm.smartcity.userRegistration.UserRegistrationActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class MobileValidationActivity extends AppCompatActivity {
    EditText mobNoField;  //declaration of mobile no edit text field componenet
    Button loginBtn;     //declaration of mobile no edit text field componenet
    final String tag_json_obj = "json_obj_req"; //Variable for volley request queue
    ViewGroup viewGroup;  //Declaration of viewGroup for getting content of the activity to show custom message

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Used to set XML view to this activity/context
        setContentView(R.layout.activity_mobile_validation);
        // Getting content view group for custom message
        viewGroup = ((ViewGroup) this.findViewById(android.R.id.content));
        mobNoField=(EditText)findViewById(R.id.mobNoEditText); //Getting mob no field component
        loginBtn=(Button)findViewById(R.id.loginBtn);  //Getting login button component
        //Setting on click listener to login button
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkMobValidation();
            }
        });
    }//end of on create override method of this activity

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
    // function invoked fir login button onclick method
    public void checkMobValidation(){
        //Getting mobile no field data
        final String mobNo = mobNoField.getText().toString();
        //condition for network unavailability
        if(!AppCommon.isNetworkAvailability(getApplicationContext())) {
            //Custom message for internet unavailability
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), CommonDialogs.INTERNET_UNAVAILABLE, Snackbar.LENGTH_LONG);
            ColoredSnackbar.confirm(snackbar).show();
        }else if(mobNo.equals("")) {  //Condition for blank mob no
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), CommonDialogs.MOB_NO_VALIDATION, Snackbar.LENGTH_LONG);
            ColoredSnackbar.confirm(snackbar).show();
        }else if(mobNo.length()<10) { //Condition for invalid mob no
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), CommonDialogs.MOB_NO_LENGTH, Snackbar.LENGTH_LONG);
            ColoredSnackbar.confirm(snackbar).show();
        }else{
            //Showing in progress dialog
            AppCommon.showDialog("Loading...",MobileValidationActivity.this);
            //User profile pic url to store into app database
            String url = AppCommon.getURL() + "citizenvalidmob/" + mobNo;
            Log.i("STAG", url);
             /*
            * JsonObjectRequest to check whether mob no is present in db or not
            * It is a custom method inside connection package
            * */
            final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,url, new JSONObject(),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String res = response.getString("citizenValidMobResult");
                                Log.i("atag",res);
                                switch (res) {
                                    case "0":    //  condition for unregisterd mobile no
                                        AppCommon.hideDialog();
                                        AlertDialog.Builder build = AppCommon.showAlertWithCallBack("", "Please register your mobile number.",MobileValidationActivity.this);
                                        build.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                DatabaseHandler db = new DatabaseHandler(MobileValidationActivity.this);
                                                int countData = db.getControldataCount();
                                                if (countData == 0) {
                                                    UtilityMethods.getAllData(mobNo, MobileValidationActivity.this);
                                                } else {
                                                    Intent actvI = new Intent(MobileValidationActivity.this, UserRegistrationActivity.class);
                                                    actvI.putExtra("mobNo", mobNo);
                                                    startActivity(actvI);
                                                }

                                            }
                                        });
                                        build.show();
                                        break;
                                    case "1":  //condition for any server side error
                                        AppCommon.hideDialog();
                                       // AppCommon.showCustomToast(LoginMobActivity.this, AppCommon.SERVER_NO_RESPONSE);
                                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), CommonDialogs.SERVER_ERROR, Snackbar.LENGTH_LONG);
                                        ColoredSnackbar.confirm(snackbar).show();
                                        break;
                                    default:  //condition for registered mobile no
                                        AppCommon.hideDialog();
                                        Intent i = new Intent(MobileValidationActivity.this, ActivationActivity.class);
                                        i.putExtra("mobNo", mobNoField.getText().toString());
                                        i.putExtra("userdata", "");
                                        startActivity(i);
                                        break;
                                }


                            } catch (JSONException e) {
                                AppCommon.hideDialog();
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    AppCommon.hideDialog();//Hidding dialog on PostJsonArrayRequest server side error
                    //Custom message for server error
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), CommonDialogs.SERVER_ERROR, Snackbar.LENGTH_LONG);
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
    }


}
