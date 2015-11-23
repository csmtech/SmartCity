package com.csm.smartcity.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.csm.smartcity.ideaComment.IdeaCommentActivity;
import com.csm.smartcity.model.IdeaDataObject;
import com.csm.smartcity.userList.UsersActivity;
import com.joanzapata.iconify.widget.IconTextView;

import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
//fa-thumbs-up
//fa-comments
/**
 * Created by arundhati on 11/12/2015.
 */
public class RecycleViewCardAdapter extends RecyclerView.Adapter {
    private ArrayList<IdeaDataObject> mDataset;

    public RecycleViewCardAdapter(ArrayList<IdeaDataObject> myDataset, RecyclerView recyclerView) {
        mDataset=myDataset;
        Log.i("atag",mDataset.toString()+":::::::in adapter");

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_complaint, parent, false);
        vh = new RecentViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RecentViewHolder) {

            ((RecentViewHolder)holder).txtCompUser.setText(UtilityMethods.getSentencecaseString(mDataset.get(position).getCITIZEN_NAME()));
            ((RecentViewHolder)holder).txtTime.setText(mDataset.get(position).getDURATION());
            ((RecentViewHolder)holder).txtCompArea.setText(mDataset.get(position).getCITIZEN_AREA_NAME());
            ((RecentViewHolder)holder).txtCompDetail.setText(mDataset.get(position).getIDEA_DETAIL());
            ((RecentViewHolder)holder).txtLikeCount.setText(mDataset.get(position).getLIKE_COUNT()+" Like");
            ((RecentViewHolder)holder).txt_comment_count.setText(mDataset.get(position).getCOMMENT_COUNT() + " Comment");

            String userUrl=AppCommon.getUserPhotoURL()+mDataset.get(position).getCITIZEN_IMAGE();
            String ideaImgUrl=AppCommon.getUserPhotoURL()+mDataset.get(position).getCITIZEN_IMAGE();
            Log.i("atag", userUrl);
            new UtilityMethods.LoadProfileImage(((RecentViewHolder)holder).imgUserimage).execute(userUrl);
            new UtilityMethods.LoadProfileImage(((RecentViewHolder)holder).imgCompImage).execute(userUrl);

           /*Support Complaint Click*/
            ((RecentViewHolder) holder).layoutLike.setOnClickListener(likeOnclickListener(mDataset.get(position).getIDEA_ID()));
            ((RecentViewHolder) holder).layoutComment.setOnClickListener(commentListOnclickListener(mDataset.get(position).getIDEA_ID()));
            ((RecentViewHolder) holder).layoutShare.setOnClickListener(shareOnclickListener());
            ((RecentViewHolder) holder).txtLikeCount.setOnClickListener(userListOnclickListener(mDataset.get(position).getIDEA_ID()));
            ((RecentViewHolder) holder).txt_comment_count.setOnClickListener(commentListOnclickListener(mDataset.get(position).getIDEA_ID()));


        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    public static class RecentViewHolder extends RecyclerView.ViewHolder
    {
        IconTextView txtCompDetail;
        TextView txtCompUser;
        TextView txtTime;
        TextView txtCompArea;
        TextView txtLikeCount;
        TextView txt_comment_count;
        TextView txtSupport;
        TextView txtSupportIcon;
        TextView txtShareIcon;
        TextView txtInviteIcon;
        ImageView imgUserimage;
        ImageView imgCompImage;
        LinearLayout layoutShare;
        LinearLayout layoutLike;
        LinearLayout layoutComment;
        LinearLayout layoutSupportShare;
        TextView txtClapIcon;
        TextView txtClap;
        LinearLayout layoutClap;
        LinearLayout layoutResolveAction;
        TextView txtUpdatedBy;
        TextView txtUpdatedOn;
        TextView txtResolvedRemark;

        public RecentViewHolder(View itemView) {
            super(itemView);
            txtCompUser = (TextView) itemView.findViewById(R.id.txtCompUser);
            txtCompDetail = (IconTextView) itemView.findViewById(R.id.txtCompDetail);
            txtTime = (TextView) itemView.findViewById(R.id.txtTime);
            txtCompArea = (TextView) itemView.findViewById(R.id.txtCompArea);
            txtLikeCount = (TextView) itemView.findViewById(R.id.txtLikeCount);
            txt_comment_count= (TextView) itemView.findViewById(R.id.txt_comment_count);
            layoutShare = (LinearLayout) itemView.findViewById(R.id.layoutShare);
            layoutLike = (LinearLayout) itemView.findViewById(R.id.layoutLike);
            txtSupport = (TextView) itemView.findViewById(R.id.txtSupport);
            layoutComment = (LinearLayout) itemView.findViewById(R.id.layoutComment);
            layoutSupportShare = (LinearLayout) itemView.findViewById(R.id.layout_support_share_bar);
            layoutClap = (LinearLayout) itemView.findViewById(R.id.layout_clap);
            txtClapIcon = (TextView) itemView.findViewById(R.id.txt_clap_icon);
            txtClap = (TextView) itemView.findViewById(R.id.txt_clap);
            layoutResolveAction = (LinearLayout) itemView.findViewById(R.id.layout_resolve_action);
            txtUpdatedBy = (TextView) itemView.findViewById(R.id.txt_updated_by);
            txtUpdatedOn = (TextView) itemView.findViewById(R.id.txt_updated_on);
            txtResolvedRemark = (TextView) itemView.findViewById(R.id.txt_resolve_remark);
            imgUserimage=(ImageView)itemView.findViewById(R.id.imgUserimage);
            imgCompImage=(ImageView)itemView.findViewById(R.id.imgCompImage);
        }

    }

    private View.OnClickListener likeOnclickListener(final String id){
        return new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                if (AppCommon.isNetworkAvailability(v.getContext())) {
                    if (AppCommon.isLogin(v.getContext())) {
//                        if ((AppCommon.isSoundEnable(v.getContext())).equals("true")) {
//                            support_mp.start();
//                        }

                        //final String strSupportFlag = (complaint.getSUPPORT_STATUS().equals("N")) ? "0" : "1";
                        String url = AppCommon.getURL() + "IdeaLikeCall/" + AppCommon.getLoginPrefData(v.getContext()).getCITIZEN_ID() + "/" +id;
                        Log.i("atag",url);
                        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                                url, null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
//                                        String str = complaint.getSUPPORT_STATUS();
//                                        String supprt_cnt = complaint.getSUPPORT_COUNT();
//                                        if (complaint.getSUPPORT_STATUS().equals("N")) {
//                                            tv1.setTypeface(null, Typeface.BOLD);
//                                            tv.setTextColor(Color.rgb(65, 160, 119));
//                                            tv1.setTextColor(Color.rgb(65, 160, 119));
//                                            complaint.setSUPPORT_STATUS("Y");
//                                            supprt_cnt = String.valueOf(Integer.parseInt(supprt_cnt) + 1);
//                                            complaint.setSUPPORT_COUNT(supprt_cnt);
//                                            supportCnt.setText(supprt_cnt + " Support");
//                                        } else {
//                                            tv1.setTypeface(null, Typeface.NORMAL);
//                                            tv.setTextColor(Color.rgb(181, 175, 174)); //Grey Color
//                                            tv1.setTextColor(Color.rgb(181, 175, 174));
//                                            complaint.setSUPPORT_STATUS("N");
//                                            supprt_cnt = String.valueOf(Integer.parseInt(supprt_cnt) - 1);
//                                            complaint.setSUPPORT_COUNT(supprt_cnt);
//                                            supportCnt.setText(supprt_cnt + " Support");
//                                        }

                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Snackbar snackbar = Snackbar.make(v, "something went wrong.Please try after sometimes. ", Snackbar.LENGTH_LONG);
                                ColoredSnackbar.confirm(snackbar).show();
                            }
                        });
                        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(30000, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        AppController.getInstance().addToRequestQueue(jsonObjReq, "like_click_request");

                    } else {
                        //AppCommon.showCustomToast((Activity)v.getContext(),AppCommon.LOGIN_FOR_SUPPORT);
                        //Custom message for internet unavailability
                        //new NotificationTipView((Activity)v.getContext(),(ViewGroup)v, 10000, NotificationTipView.LOGIN_FOR_SUPPORT);
                        Snackbar snackbar = Snackbar.make(v, "Please login to support this issue", Snackbar.LENGTH_LONG);
                        ColoredSnackbar.confirm(snackbar).show();
                    }

                } else {
                    /*If Network is not Available*/
                    //AppCommon.showCustomToast((Activity)v.getContext(),AppCommon.NETWORK_UNAVAILABLE);
                    Snackbar snackbar = Snackbar.make(v, CommonDialogs.INTERNET_UNAVAILABLE, Snackbar.LENGTH_LONG);
                    ColoredSnackbar.confirm(snackbar).show();
                }
            }



        };
    }

    private View.OnClickListener shareOnclickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };
    }

    private View.OnClickListener userListOnclickListener(final String id){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(), UsersActivity.class);
                intent.putExtra("ideaId",id);
                v.getContext().startActivity(intent);
            }
        };
    }

    private View.OnClickListener commentListOnclickListener(final String id){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent intent=new Intent(v.getContext(),IdeaCommentActivity.class);
                intent.putExtra("ideaId",id);
                v.getContext().startActivity(intent);
                ((Activity) v.getContext()).overridePendingTransition(R.animator.push_up_in, R.animator.push_up_out);
            }
        };
    }

}



