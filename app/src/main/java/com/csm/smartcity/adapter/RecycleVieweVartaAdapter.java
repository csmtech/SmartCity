package com.csm.smartcity.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.csm.smartcity.R;
import com.csm.smartcity.common.AppCommon;
import com.csm.smartcity.connection.CustomVolleyRequestQueue;
import com.csm.smartcity.ideaComment.IdeaCommentActivity;
import com.csm.smartcity.model.eVartaModel;
import com.csm.smartcity.utils.OnLoadMoreListener;
import com.joanzapata.iconify.widget.IconTextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
//fa-thumbs-up
//fa-comments

/**
 * Created by samarekha on 11/12/2015.
 */
public class RecycleVieweVartaAdapter extends RecyclerView.Adapter {
    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
    Context c;
    private ProgressDialog mProgressDialog;
    private OnLoadMoreListener onLoadMoreListener;
    CustomVolleyRequestQueue customvolley;
    private List<eVartaModel> items;
    private ArrayList<eVartaModel> mDataset;
    private SparseBooleanArray selectedItems;
    private boolean loading;
    private int visibleThreshold = 10; //number of items remain to the recycler before reaching the end
    private int lastVisibleItem, totalItemCount;
    public RecycleVieweVartaAdapter(ArrayList<eVartaModel>myDataset, RecyclerView recyclerView) {
        mDataset=myDataset;
    }
    public void setLoaded() {
        loading = false;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        c=parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_evarta, parent, false);
        vh = new RecentViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        eVartaModel model = items.get(position);
        ((RecentViewHolder) holder).name.setText(mDataset.get(position).getEVARTA_NAME());
        ((RecentViewHolder) holder).txtDay.setText(mDataset.get(position).getPOST_DATE());
        if(mDataset.get(position).getGeteVartaCommentCount().equals("")) {
            ((RecentViewHolder) holder).txtCmntcount.setVisibility(View.GONE);
        }
        else{
            if(mDataset.get(position).getGeteVartaCommentCount().equals("1")){
                ((RecentViewHolder) holder).txtCmntcount.setText(mDataset.get(position).getGeteVartaCommentCount()+" Comment");
            }else{
                ((RecentViewHolder) holder).txtCmntcount.setText(mDataset.get(position).getGeteVartaCommentCount()+" Comments");
            }

        }
       // File pdfFile = new File(Environment.getExternalStorageDirectory() + "/testthreepdf/" + "cover_letter_template_3.pdf");
        File pdfFile = new File(Environment.getExternalStorageDirectory() + "/pdf/" +mDataset.get(position).getPdf_name());
        Log.i("atag", "exist "+pdfFile.exists());
        if (pdfFile.exists()) {
            Log.i("atag", "file exis");
            ((RecentViewHolder) holder).downloadmg.setVisibility(View.GONE);
            ((RecentViewHolder) holder).open.setVisibility(View.VISIBLE);
        }
        else{
            Log.i("atag", "not exist ");
            ((RecentViewHolder) holder).open.setVisibility(View.GONE);
            ((RecentViewHolder) holder).downloadmg.setVisibility(View.VISIBLE);

        }
        ((RecentViewHolder) holder).downloadmg.setOnClickListener(pdfOnclickListener(mDataset.get(position).getGeteVartaDocument(),mDataset.get(position).getPdf_name()));
        ((RecentViewHolder) holder).open.setOnClickListener(openPdfOnclickListener(mDataset.get(position).getPdf_name()));
        ((RecentViewHolder) holder).txtCmnt.setOnClickListener(cmntOnclickListener(mDataset.get(position).getEVARTA_ID()));
        ((RecentViewHolder) holder).txtShare.setOnClickListener(shareOnclickListener(mDataset.get(position).getEVARTA_ID()));
        ((RecentViewHolder) holder).txtCmntcount.setOnClickListener(cmntOnclickListener(mDataset.get(position).getEVARTA_ID()));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }
    /*COMMENT click*/
    private View.OnClickListener cmntOnclickListener(final String id){

        return  new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(),IdeaCommentActivity.class);
                intent.putExtra("eVartaId",id);
                v.getContext().startActivity(intent);
                ((Activity) v.getContext()).overridePendingTransition(R.animator.push_up_in, R.animator.push_up_out);
            }

        };
    }
    /*SHARE click*/
    private View.OnClickListener shareOnclickListener(final String eVartaID){

        return  new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }

        };
    }
    /*PDF click*/
    private View.OnClickListener pdfOnclickListener(final String pdfDoc,final String pdfNm){

        return  new View.OnClickListener(){
            @Override
            public void onClick(View v) {
               // Log.i("atag", "pdf" + pdfDoc);
                    new DownloadFile().execute(pdfDoc, pdfNm);
                  //  new DownloadFile().execute("http://www.dayjob.com/downloads/cv_examples/cover_letter_template_3.pdf", "cover_letter_template_3.pdf");
            }

        };
    }

    class DownloadFile extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            super.onPreExecute();
            // Create progress dialog
            mProgressDialog = new ProgressDialog(c);

            // Set your progress dialog Message
            mProgressDialog.setMessage("Downloading, Please Wait!");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(100);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setCancelable(false);
           // mProgressDialog.setCanceledOnTouchOutside(false);
            // Show progress dialog
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            int count;

            try {
                Log.i("atag", "try");
                String fileUrl = strings[0];
                String fileName = strings[1];
                Log.i("atag", "fileUrl" + fileUrl);
                Log.i("atag", "fileName" + fileName);
                String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
                File folder = new File(extStorageDirectory, "pdf");
                folder.mkdir();
                File pdfFile = new File(folder, fileName);

                URL url = new URL(fileUrl);
                Log.i("atag", "url" + url);
                HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                FileOutputStream fileOutputStream = new FileOutputStream(pdfFile);
                Log.i("atag", "pdfFile" + pdfFile);
                int totalSize = urlConnection.getContentLength();
                Log.i("atag", "totalSize" + totalSize);
                byte data[] = new byte[1024];
                long total = 0;
                while ((count = inputStream.read(data)) != -1) {
                    total += count;
                    publishProgress("" + (int) ((total * 100) / totalSize));
                    fileOutputStream.write(data, 0, count);

                }

                fileOutputStream.flush();
                fileOutputStream.close();
                inputStream.close();
            } catch (Exception e) {
                Log.i("atag", "catch");
            }
            return null;
        }
        protected void onProgressUpdate(String... progress) {
          /*  if(Integer.parseInt(progress[0])==100){
                Log.i("atag", "comp");
                AppCommon.dismissDialog();
            }*/
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));

        }

        @Override
        protected void onPostExecute(String unused) {
            Log.i("atag", "comp");
            mProgressDialog.hide();
        }
    }
  //Open Click
    private View.OnClickListener openPdfOnclickListener(final String pdfNm){
        return  new View.OnClickListener(){
            @Override
            public void onClick(View v) {
               // File pdfFile = new File(Environment.getExternalStorageDirectory() + "/testthreepdf/" + "cover_letter_template_3.pdf");  // -> filename = maven.pdf
                 File pdfFile = new File(Environment.getExternalStorageDirectory() + "/pdf/" + pdfNm);

                Intent target = new Intent(Intent.ACTION_VIEW);
                target.setDataAndType(Uri.fromFile(pdfFile),"application/pdf");
                target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                Intent intent = Intent.createChooser(target, "Open File");

                try{
                    c.startActivity(intent);
                    Log.i("atag", "pdfIntent "+intent);
                }catch(ActivityNotFoundException e){
                    Log.i("atag", "catch");
                    // Toast.makeText(eVartaActivity.this, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }
    public static class RecentViewHolder extends RecyclerView.ViewHolder
    {
        TextView name;// ei text view achhi hmm okk run
        TextView txtDay;
        TextView txtCmntcount;
        IconTextView downloadmg;
        TextView open;
        IconTextView txtCmnt;
        IconTextView txtShare;
        public RecentViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.txtCompUser);
            txtDay = (TextView) itemView.findViewById(R.id.txtday);
            txtCmntcount=(TextView) itemView.findViewById(R.id.txtCmntcount);
            downloadmg=(IconTextView) itemView.findViewById(R.id.downloadmg);
            txtCmnt=(IconTextView) itemView.findViewById(R.id.txtCmnt);
            txtShare=(IconTextView) itemView.findViewById(R.id.txtShare);
            open=(TextView) itemView.findViewById(R.id.open);
            open.setText(Html.fromHtml("<u><b>Open</b></u>"));
        }
    }

}



