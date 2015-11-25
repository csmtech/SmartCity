package com.csm.smartcity.common;

/**
 * Created by rasmikant on 9/23/2015.
 */


import android.app.Activity;
import android.content.pm.ResolveInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.csm.smartcity.R;


public class ShareIntentListAdapter extends ArrayAdapter {
    Activity context;
    Object[] items;
    boolean[] arrows;
    int layoutId;

    public ShareIntentListAdapter(Activity context, int layoutId, Object[] items) {
        super(context, layoutId, items);

        this.context = context;
        this.items = items;
        this.layoutId = layoutId;
    }

    public View getView(int pos, View convertView, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View row = inflater.inflate(layoutId, null);
        TextView label = (TextView) row.findViewById(R.id.txt_share_medium);
        label.setText(((ResolveInfo) items[pos]).activityInfo.applicationInfo.loadLabel(context.getPackageManager()).toString());
        ImageView image = (ImageView) row.findViewById(R.id.img_share_medium);
        image.setImageDrawable(((ResolveInfo)items[pos]).activityInfo.applicationInfo.loadIcon(context.getPackageManager()));
        return(row);
    }
}
