package com.csm.smartcity.common;

/**
 * Created by rasmikant on 9/23/2015.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.util.Log;

import com.csm.smartcity.R;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import java.util.ArrayList;
import java.util.List;


public class ShareHelper {
    Context context;
    String subject;
    String body;
    Uri fBImgUri = null;
    Uri imgUri;
    Uri linkUri;

    //Facebook facebook;
    public ShareHelper(Context context, String subject, String body, Uri uri, Uri link, Uri fBImg) {
        this.context = context;
        this.subject = subject;
        this.body = body;
        this.imgUri = uri;
        this.linkUri = link;
        this.fBImgUri = fBImg;
        Log.i("STAG", String.valueOf(this.fBImgUri));
    }

    public void share() {
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        List<ResolveInfo> activities = context.getPackageManager().queryIntentActivities(sendIntent, 0);
        List<ResolveInfo> activitiesSelected = new ArrayList<ResolveInfo>();
        //String packageName = activities.activityInfo.packageName;
        for (int i = 0; i < activities.size(); i++) {
            // Extract the label, append it, and repackage it in a LabeledIntent
            ResolveInfo ri = activities.get(i);
            String packageName = ri.activityInfo.packageName;
            //Log.i("RTAG", packageName + "");
            if (packageName.contains("android.email") || packageName.contains("twitter") || packageName.contains("facebook") || packageName.contains("mms") || packageName.contains("android.gm") || packageName.contains("com.whatsapp")) {
                //emailIntent.setPackage(packageName);
                activitiesSelected.add(ri);
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Share with...");
        final ShareIntentListAdapter adapter = new ShareIntentListAdapter((Activity) context, R.layout.share_row, activitiesSelected.toArray());
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ResolveInfo info = (ResolveInfo) adapter.getItem(which);
                if (info.activityInfo.packageName.contains("facebook")) {

                    ShareLinkContent linkContent = new ShareLinkContent.Builder()
                            .setContentTitle(subject)
                            .setContentUrl(linkUri)
                            .setContentDescription(body)
                            .setImageUrl(fBImgUri)
                            .build();

                    // ShareApi.share(linkContent, null);
                    ShareDialog shareDialog = new ShareDialog((Activity) context);
                    shareDialog.show(linkContent);

                    //new PostToFacebookDialog(context, body).show();
                } else {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setClassName(info.activityInfo.packageName, info.activityInfo.name);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                    intent.putExtra(Intent.EXTRA_TEXT, body);
                    intent.putExtra(Intent.EXTRA_STREAM, imgUri);
                    intent.setType("image/*");
                    ((Activity) context).startActivity(intent);
                }
            }
        });
        builder.create().show();
        // return facebook;
    }
}
