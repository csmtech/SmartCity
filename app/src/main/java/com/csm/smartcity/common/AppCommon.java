package com.csm.smartcity.common;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.csm.smartcity.R;
import com.csm.smartcity.sqlLiteModel.LoginUserObject;

/**
 * Created by arundhati on 11/10/2015.
 */
public class AppCommon {
    static ProgressDialog  pDialog;

  //   private static String baseURL="http://203.129.207.124/bmcsampark/"; ////Staging External IP
   // private static String baseURL="http://192.168.10.42/bmcsampark/"; ////Staging Internal IP server 27
    // private static String baseURL="http://mycitymypride.org/"; ////LIVE
   //  private static String baseURL="http://192.168.8.112/BMCSAMPARKNEW/"; ////Local
   private static String baseURL="http://192.168.8.106/bmcsampark/";//Zafir sir


    public static String getURL(){
        return baseURL+"SmartCitySVC.svc/";
        // return baseURL+"MCMPSVCV2.svc/";
    }
    public static String getUserPhotoURL(){
        return baseURL+"photos/citizen/citizen_";
    }

//    public static String getIdeaPhotoUrl(){
//
//    }

    public static String getComplaintImgHandlrUrl(){
        return baseURL+"web/Uploader.ashx";  //http://192.168.8.113/bmcsampark/web/Uploader.ashx
    }

    public static boolean isNetworkAvailability(Context context ){
        // get Connectivity Manager object to check connection
        ConnectivityManager connec =(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Check for network connections
        if ( connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {
            return true;
        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {
            return false;
        }
        return false;
    }

    public static void showDialog(String str,Context c){

        pDialog = new ProgressDialog(c);
        pDialog.setMessage(str);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();
    }
    public static void hideDialog(){
        pDialog.hide();
    }
    public static  void dismissDialog(){
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
        // pDialog.dismiss();
    }

    public static boolean isLogin(Context c){
        SharedPreferences pref = c.getSharedPreferences("MyPofile", Context.MODE_PRIVATE);
        String username = pref.getString("CITIZEN_ID", "");
        // region "Check the user has already logged in or not"
        if(TextUtils.isEmpty(username))
            return false;
        else
            return true;
    }

    public static AlertDialog.Builder showAlertWithCallBack(String title,String msg,Context c){
        AlertDialog.Builder builder = new AlertDialog.Builder(c, R.style.MyAlertDialogStyle);
        builder.setTitle(title);
        builder.setMessage(msg);
      /*  builder.setPositiveButton("OK", null);
        builder.show();*/

        return builder;
    }

    public static void showAlert(String title,String msg,Context c){
        AlertDialog.Builder builder = new AlertDialog.Builder(c, R.style.MyAlertDialogStyle);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton("OK", null);
        builder.show();
    }

    public static LoginUserObject getLoginPrefData(Context c){
        SharedPreferences pref = c.getSharedPreferences("MyPofile", Context.MODE_PRIVATE);
        LoginUserObject objUser=new LoginUserObject();
        objUser.setUSER_NAME(pref.getString("USER_NAME", ""));
        objUser.setMOBILENO(pref.getString("MOBILENO", ""));
        objUser.setIMAGE(pref.getString("IMAGE", ""));
        objUser.setIMAGE_FLAG(pref.getString("IMAGE_FLAG", ""));
        objUser.setADDRESS(pref.getString("ADDRESS", ""));
        objUser.setDOB(pref.getString("DOB", ""));
        objUser.setLAND_MARK(pref.getString("LAND_MARK", ""));
        objUser.setAREA_ID(pref.getString("AREA_ID", ""));
        objUser.setREGD_DATE(pref.getString("REGD_DATE", ""));
        objUser.setGENDER(pref.getString("GENDER", ""));
        objUser.setPLOT(pref.getString("PLOT", ""));
        objUser.setCITIZEN_ID(pref.getString("CITIZEN_ID", ""));
        objUser.setWARD_ID(pref.getString("WARD_ID", ""));
        objUser.setEMAIL_ID(pref.getString("EMAIL_ID", ""));
        return objUser;
    }


}
