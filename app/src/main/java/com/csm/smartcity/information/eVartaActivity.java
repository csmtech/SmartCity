package com.csm.smartcity.information;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.csm.smartcity.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class eVartaActivity extends AppCompatActivity {
    RecyclerVieweVartaAdapter adapter;
    RecyclerView recyclerView;
    private static List<Model> demoData;
   /* LinearLayout layoutShare;
    TextView txt_icon_share;*/
   public static final int DIALOG_DOWNLOAD_PROGRESS = 0;

    private ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e_varta);
       /* layoutShare=(LinearLayout)findViewById(R.id.layoutShare);
        txt_icon_share=(TextView)findViewById(R.id.txt_icon_share);*/
        //layoutShare.setOnClickListener(layoutShareClick());
        recyclerView =  (RecyclerView)findViewById(R.id.evarta_recycle_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        demoData = new ArrayList<Model>();

        for (byte i = 0; i < 20; i++) {
            Model model = new Model();
            if (i % 2 == 0) {
                model.name = "e-varta Oct-2015";
                model.txtDay="1 month ago";
            } else {
                model.name = "e-varta Sept-2015";
                model.txtDay="2 months ago";
            }


            demoData.add(model);
        }
        adapter = new RecyclerVieweVartaAdapter(demoData);
        recyclerView.setAdapter(adapter);

    }
    public void onHome(View v)

    {
        startActivity(new Intent(this, InformationActivity.class));
    }

    public void onDownload(View v)

    {
        File pdfFile = new File(Environment.getExternalStorageDirectory() + "/testthreepdf/" + "cover_letter_template_3.pdf");
        if (pdfFile.exists()) {
            showPdf();
        }else{
            new DownloadFile().execute("http://www.dayjob.com/downloads/cv_examples/cover_letter_template_3.pdf", "cover_letter_template_3.pdf");
        }
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_DOWNLOAD_PROGRESS:
                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setMessage("Downloading file..");
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                return mProgressDialog;
            default:
                return null;
        }
    }

    class DownloadFile extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(DIALOG_DOWNLOAD_PROGRESS);
        }

        @Override
        protected String doInBackground(String... strings) {
            int count;

            try {
                String fileUrl = strings[0];
                String fileName = strings[1];
                String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
                File folder = new File(extStorageDirectory, "testthreepdf");
                folder.mkdir();

                File pdfFile = new File(folder, fileName);

                URL url = new URL(fileUrl);
                HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                //urlConnection.setRequestMethod("GET");
                //urlConnection.setDoOutput(true);
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                FileOutputStream fileOutputStream = new FileOutputStream(pdfFile);

                int totalSize = urlConnection.getContentLength();
               // Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);
              System.out.println(pdfFile);


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
                showPdf();
            } catch (Exception e) {}
            return null;

        }
        protected void onProgressUpdate(String... progress) {
           // Log.d("ANDRO_ASYNC",progress[0]);
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String unused) {
            dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
        }
    }

    public void showPdf()
    {
        File pdfFile = new File(Environment.getExternalStorageDirectory() + "/testthreepdf/" + "cover_letter_template_3.pdf");  // -> filename = maven.pdf
        Uri path = Uri.fromFile(pdfFile);
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try{
            startActivity(pdfIntent);
        }catch(ActivityNotFoundException e){
            Toast.makeText(eVartaActivity.this, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
        }
    }
   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
       // Inflate the menu; this adds items to the action bar if it is present.
       getMenuInflater().inflate(R.menu.menu_back, menu);
       return true;
   }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.regdMenuIcon) {
            finish();
           /* Intent i = new Intent(UserProfileActivity.this, SettingActivity.class);
            startActivity(i);*/
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
