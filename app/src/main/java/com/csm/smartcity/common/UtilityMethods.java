package com.csm.smartcity.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.csm.smartcity.dashboard.NewDashboardActivity;
import com.csm.smartcity.sqlLiteModel.ControllData;
import com.csm.smartcity.sqlLiteModel.DatabaseHandler;
import com.csm.smartcity.userRegistration.UserRegistrationActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

/**
 * Created by arundhati on 11/13/2015.
 */
public class UtilityMethods {
    final static String tag_json_obj = "json_obj_req";

    public static  void getAllData(String msg,Context c){
        final Context context=c;

        final String message=msg;
        AppCommon.showDialog("Loading....",c);
        String url = AppCommon.getURL()+"allControlsData/0/0";
        Log.i("atag",url);
        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            Log.i("atag",response.toString());

                            int loopLength=(response.getJSONArray("allControlsDataResult").getJSONObject(0)).length();
                            ControllData strData=new ControllData();
                            DatabaseHandler db=new DatabaseHandler(context);

                            if(message.equals("upd")){
                                db.deleteControllData();
                            }
                            for (int i = 0; i < loopLength; i++)
                            {
                                if(i==0) {
                                    strData.setDataName("AREADATA");
                                    strData.setDataValue(response.getJSONArray("allControlsDataResult").getJSONObject(0).getJSONArray("AREADATA").toString());
                                }else if(i==1){
                                    strData.setDataName("WORDDATA");
                                    strData.setDataValue(response.getJSONArray("allControlsDataResult").getJSONObject(0).getJSONArray("WORDDATA").toString());
                                }else if(i==2){
                                    strData.setDataName("CATAGORYDATA");
                                    strData.setDataValue(response.getJSONArray("allControlsDataResult").getJSONObject(0).getJSONArray("CATAGORYDATA").toString());
//                                }else if(i==3){
//                                    strData.setDataName("TYPEDATA");
//                                    strData.setDataValue(response.getJSONArray("allControlsDataResult").getJSONObject(0).getJSONArray("TYPEDATA").toString());
                                } else if (i == 3) {
                                    strData.setDataName("STATUS");
                                    strData.setDataValue(response.getJSONArray("allControlsDataResult").getJSONObject(0).getJSONArray("STATUS").toString());
                                }
                                db.addControllData(strData);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        AppCommon.hideDialog();

                        if(message.equals("FB") || message.equals("ACT")){
                            Intent actvI=new Intent(context,NewDashboardActivity.class);
                            actvI.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                           // ActivityCompat.finishAffinity(LoginActivity.class);
                            context.startActivity(actvI);
                        }else if(message.equals("Skip")){
                            Intent actvI=new Intent(context,NewDashboardActivity.class);
                            context.startActivity(actvI);
                        }else if(message.equals("upd")){
                            AppCommon.showAlert("Message", "Application data updated successfully.",context);
                        }else {
                            Intent actvI = new Intent(context, UserRegistrationActivity.class);
                            actvI.putExtra("mobNo", message);
                            context.startActivity(actvI);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AppCommon.hideDialog();
                Snackbar snackbar = Snackbar.make(((Activity)context).findViewById(android.R.id.content), CommonDialogs.SERVER_ERROR, Snackbar.LENGTH_LONG);
                ColoredSnackbar.confirm(snackbar).show();
            }
        });
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    public static Bitmap getUserResizedBitmap(Bitmap image, int bitmapWidth,int bitmapHeight) {
        return Bitmap.createScaledBitmap(image, bitmapWidth, bitmapHeight,true);
    }

    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }


    public static String getSentencecaseString(String name){
        String titleCaseValue=null;
        try {


            String[] words = name.split(" ");
            StringBuilder sb = new StringBuilder();
            if (words[0].length() > 0) {
                sb.append(Character.toUpperCase(words[0].charAt(0)) + words[0].subSequence(1, words[0].length()).toString().toLowerCase());
                for (int i = 1; i < words.length; i++) {
                    sb.append(" ");
                    sb.append(Character.toUpperCase(words[i].charAt(0)) + words[i].subSequence(1, words[i].length()).toString().toLowerCase());
                }
            }
            titleCaseValue= sb.toString();

            //Log.i("STAG",titleCaseValue);

        }catch (StringIndexOutOfBoundsException e){
            titleCaseValue=name;
            e.printStackTrace();
        }catch (Exception e){
            titleCaseValue=name;
            e.printStackTrace();
        }
        return titleCaseValue;
    }


    /**
     * Background Async task to load user profile picture from url
     * */
    public static class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
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



    public static Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte= Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }

}
