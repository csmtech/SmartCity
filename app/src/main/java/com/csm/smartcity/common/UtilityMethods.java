package com.csm.smartcity.common;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;

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
        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
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
                                }else if(i==3){
                                    strData.setDataName("TYPEDATA");
                                    strData.setDataValue(response.getJSONArray("allControlsDataResult").getJSONObject(0).getJSONArray("TYPEDATA").toString());
                                } else if (i == 4) {
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
                            actvI.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
                Log.d("TAG", "Error: " + error);
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

}
