package com.csm.smartcity.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
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
import com.csm.smartcity.connection.PostJsonArrayRequest;
import com.csm.smartcity.dashboard.NewDashboardActivity;
import com.csm.smartcity.mobLogin.MobileValidationActivity;
import com.csm.smartcity.sqlLiteModel.DatabaseHandler;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.joanzapata.iconify.widget.IconButton;
import com.joanzapata.iconify.widget.IconTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends Activity {

    TextView skip;
    LinearLayout btnFacebook;
    LinearLayout btnGmail;
    LinearLayout btnMobile;
    private CallbackManager callbackManager;      //Declaration of CallbackManager used for facebook login callback
    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;
    private boolean mSignInClicked;
    private boolean mIntentInProgress;
    private ConnectionResult mConnectionResult;
    private static final int RC_SIGN_IN = 0;
    final String tag_json_obj = "json_obj_req";   //Variable for volley request queue
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        try{
            skip=(TextView)findViewById(R.id.skip);
            skip.setText(Html.fromHtml("<u><b><i>Skip</i></b></u>"));
            btnFacebook=(LinearLayout)findViewById(R.id.btnFacebook);
            btnGmail=(LinearLayout)findViewById(R.id.btnGmail);
            btnMobile=(LinearLayout)findViewById(R.id.btnMobile);

            btnFacebook.setOnClickListener(facebookBtnClick());
            btnGmail.setOnClickListener(gmailBtnClick());
            btnMobile.setOnClickListener(mobileBtnClick());
            skip.setOnClickListener(skipClick());
             /*
            * Initilization of facebook sdk on activity create.
            * Used for facebook login
            * */
            FacebookSdk.sdkInitialize(getApplicationContext());

            //Getting callback manager from factory
            callbackManager = CallbackManager.Factory.create();
            /*
            * After facebook login to manage callback
            * It has two override methods i.e onSuccess and onError
            * */
            LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {

                    //AccessToken to get access token from facebook login result
                    final AccessToken accessToken = loginResult.getAccessToken();
                    GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject user, GraphResponse graphResponse) {
                            String profilePic = "https://graph.facebook.com/" + user.optString("id") + "/picture?width=80&height=80";
                            loginCheckInDB(user.optString("name"),user.optString("gender"),user.optString("id"),user.optString("email"),profilePic,"FA");
                        }
                    }).executeAsync();
//                        }
//                    }).executeAsync();
                }//end of facebook login success

                //For facebook login cancel by user
                @Override
                public void onCancel() {
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), CommonDialogs.LOGIN_CANCEL, Snackbar.LENGTH_LONG);
                    ColoredSnackbar.confirm(snackbar).show();
                }

                //for facebook login error
                @Override
                public void onError(FacebookException e) {
                    Log.i("atag","hello::::::::::::"+e.getMessage());
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), CommonDialogs.FACEBOOK_ERROR, Snackbar.LENGTH_LONG);
                    ColoredSnackbar.confirm(snackbar).show();
                }
            });


            // Initializing google plus api client
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks(){

                        @Override
                        public void onConnected(Bundle bundle) {
                            mSignInClicked = false;
                            Log.i("atag", "User is connected!");
                            // Get user's information
                            getProfileInformation();
                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            Log.i("atag", "connection is suspended!");
                            mGoogleApiClient.connect();
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {
                            if (!connectionResult.hasResolution()) {
                                GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), LoginActivity.this,0).show();
                                return;
                            }

                            if (!mIntentInProgress) {
                                // Store the ConnectionResult for later usage
                                mConnectionResult = connectionResult;

                                if (mSignInClicked) {
                                    // The user has already clicked 'sign-in' so we attempt to
                                    // resolve all
                                    // errors until the user is signed in, or they cancel.
                                    resolveSignInError();
                                }
                            }
                        }
                    }).addApi(Plus.API)
                    .addScope(Plus.SCOPE_PLUS_LOGIN).build();

        }catch (Exception e){
            Log.i("atag", "exception!");
            e.printStackTrace();
        }

    }

    //Override metod of activity used for facebook login callback
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       Log.i("atag",requestCode+"::::::::::::"+resultCode);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode != RESULT_OK) {
                mSignInClicked = false;
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), CommonDialogs.GMAIL_LOGIN_FAILED, Snackbar.LENGTH_LONG);
                ColoredSnackbar.confirm(snackbar).show();
            }
            mIntentInProgress = false;

//            if (!mGoogleApiClient.isConnecting()) {
//                mGoogleApiClient.connect();
//            }
        }else {

            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }




//    protected void onStart() {
//        super.onStart();
//        mGoogleApiClient.connect();
//    }

    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }


    public View.OnClickListener facebookBtnClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //condition to check network availability
                if(AppCommon.isNetworkAvailability(getApplicationContext())){
                    //For facebook login and to access user's public profile and friends
                    LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "user_friends"));
                }
                else{
                    //Custom message for internet unavailability
                    Snackbar snackbar = Snackbar.make(v, CommonDialogs.INTERNET_UNAVAILABLE, Snackbar.LENGTH_LONG);
                    ColoredSnackbar.confirm(snackbar).show();
                }

            }
        };
    }

    public View.OnClickListener gmailBtnClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("atag", "gmail click");
                Log.i("atag", String.valueOf(mGoogleApiClient.isConnecting()));
                Log.i("atag", String.valueOf(mGoogleApiClient.isConnected()));
               // mGoogleApiClient.connect();

                if (!mGoogleApiClient.isConnecting()) {
                    mSignInClicked = true;
                    resolveSignInError();
                    //Refer site
                    //http://www.androidhive.info/2014/02/android-login-with-google-plus-account-1/
                }

            //    mGoogleApiClient.connect();




            }
        };
    }

    public View.OnClickListener mobileBtnClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("atag", "mobile click");
                Intent intent=new Intent(LoginActivity.this, MobileValidationActivity.class);
                startActivity(intent);
            }
        };
    }

    public View.OnClickListener skipClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("atag", "skip click");
            try {
                DatabaseHandler db = new DatabaseHandler(LoginActivity.this);
                int countData = db.getControldataCount();
                //If countData == 0, getAllData to download all contallControlData and then goto dashboard page
                //Else goto dashboard page
                if (countData == 0) {
                    UtilityMethods.getAllData("Skip", LoginActivity.this);
                }else {
                    Intent intent = new Intent(LoginActivity.this, NewDashboardActivity.class);
                    startActivity(intent);
                }

             }catch (Exception e){}

            }
        };
    }


    /**
     * Fetching user's information name, email, profile pic from gmail
     * */
    private void getProfileInformation() {
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
                String personName = currentPerson.getDisplayName();
                String personPhotoUrl = currentPerson.getImage().getUrl();
                personPhotoUrl = personPhotoUrl.substring(0,personPhotoUrl.length() - 2)+ "80";
               // String personGooglePlusProfile = currentPerson.getUrl();
                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
                String gender=(currentPerson.getGender()== 1) ? "female" : "male";
                String id=currentPerson.getId();

               // int gender=currentPerson.getGender();

//                Log.e("atag", "Name: " + personName + ", gender: "
//                        + gender + ", email: " + email
//                        + ", Image: " + personPhotoUrl);
//
//                Log.e("atag", "Image: " + personPhotoUrl);
//                Log.e("atag", "id: " + id);

                loginCheckInDB(personName, gender, id, email, personPhotoUrl, "GM");
//                Intent intent=new Intent(LoginActivity.this, DashboardActivity.class);
//                    startActivity(intent);


               /* new LoadProfileImage(imgProfilePic).execute(personPhotoUrl);*/

            } else {
                Toast.makeText(getApplicationContext(),"Person information is null", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to resolve any signin errors
     * */
    private void resolveSignInError() {
        try{

        Log.i("atag",":::::::::"+String.valueOf(mConnectionResult.hasResolution()));
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
        }catch (NullPointerException e){
            mIntentInProgress = false;
            mGoogleApiClient.connect();
        }
    }


    public void loginCheckInDB(String name,String gender,String fbID,String email,String profilepic,String flag){
        //Getting GSM id from SharedPreferences
        final SharedPreferences prefs = getSharedPreferences("GCM_ID", Context.MODE_MULTI_PROCESS);
        final String registrationId = prefs.getString("regId", "");
        //Showing in progress dialog
        AppCommon.showDialog("Loading....",LoginActivity.this);
                            /*
                            * Gender of login user
                            * 0-male
                            * 1-female
                            * */
        String gen = "0";
        if (gender.equals("female")) {
            gen = "1";
        }
        //User profile pic url to store into app database
      //  String profilePic = "https://graph.facebook.com/" + user.optString("id") + "/picture?width=80&height=80";
        //Map object used to send post data to server
        Map<String, String> params = new HashMap<String, String>();
        //putting key value of login user in Map object
        Log.i("atag", "::::::::::"+registrationId);
        try {
            params.put("strFlag", flag);
            params.put("strCitizenID", "0");
            params.put("strCitizenName", name);
            params.put("strImage", profilepic);
            params.put("strMobileNo", "");
            params.put("strWard", "0");
            params.put("strArea", "0");
            params.put("strEmail", email);
            params.put("strGender", gen);
            params.put("captureFlag", "1");
            params.put("gcmCode", registrationId);
            params.put("strDeviceId", "");
            params.put("strDevicePlatform", "");
            params.put("strDeviceModel", android.os.Build.DEVICE);
            params.put("strAppVersion", "");
            params.put("FBUserID", fbID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Server url for facebook login user
        String url = AppCommon.getURL() + "citizenRegistrationV2";
                            /*
                            * PostJsonArrayRequest to send data to server
                            * It is a custom method inside connection package
                            * */
        PostJsonArrayRequest jsonObjReq = new PostJsonArrayRequest(
                url, new JSONObject(params),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        AppCommon.hideDialog(); //Hide progress dialog after data stored in server
                        Log.i("atag","success");
                        try {
                            //To store response data in JSONObject
                            JSONObject profileData = response.getJSONObject(0).getJSONArray("UserDetail").getJSONObject(0);

                            Log.i("atag",profileData.toString());

                            //To store login user's profile details in SharedPreferences
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
                            edit.commit(); //To commit SharedPreferences data
                            /*
                            * DatabaseHandler class to check whether allControlData(ward,area,complaint catagory,complaint type)
                            * is in SQLLite database or not
                            * */
                            DatabaseHandler db = new DatabaseHandler(LoginActivity.this);
                            int countData = db.getControldataCount();
                            //If countData == 0, getAllData to download all contallControlData and then goto dashboard page
                            //Else goto dashboard page
                            if (countData == 0) {
                                UtilityMethods.getAllData("FB", LoginActivity.this);
                            }else {
                                Intent intent = new Intent(LoginActivity.this, NewDashboardActivity.class);
                                finish();
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AppCommon.hideDialog(); //Hidding dialog on PostJsonArrayRequest server side error
                Log.i("atag", "error");
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), CommonDialogs.SERVER_ERROR, Snackbar.LENGTH_LONG);
                ColoredSnackbar.confirm(snackbar).show();
                //Custom message for server error
                // new NotificationTipView(getApplicationContext(),viewGroup, 10000, NotificationTipView.SERVER_ERROR);
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
