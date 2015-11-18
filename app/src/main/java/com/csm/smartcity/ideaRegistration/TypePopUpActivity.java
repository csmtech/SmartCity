package com.csm.smartcity.ideaRegistration;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.csm.smartcity.R;
import com.csm.smartcity.sqlLiteModel.ControllData;
import com.csm.smartcity.sqlLiteModel.DatabaseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class TypePopUpActivity extends AppCompatActivity {
    String CatagoryID="";
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    JSONArray sreaDBData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_pop_up);


        Bundle bundle = getIntent().getExtras();
        CatagoryID = bundle.getString("CatagoryId");

        DatabaseHandler db=new DatabaseHandler(this);
        List<ControllData> cntrlData=db.getAllContollData("4");
        ArrayList<String> arrList = new ArrayList<String>();
        try {
            sreaDBData=new JSONArray(cntrlData.get(0).getDataValue());
            for(int i=0;i<sreaDBData.length();i++){
                //areaData.put(sreaDBData.getJSONObject(i).get("ID").toString(), sreaDBData.getJSONObject(i).get("Name").toString()) ;
                String sub_id=sreaDBData.getJSONObject(i).get("SUBSCHEME_ID").toString();
                if(sub_id.equals(CatagoryID)) {
                    arrList.add(sreaDBData.getJSONObject(i).get("Name").toString());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.typepopup_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ListPopupPAdapter(arrList,R.layout.type_popup_listitem);
        mRecyclerView.setAdapter(mAdapter);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_catagory_idea, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.crossicon) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public class ListPopupPAdapter extends RecyclerView.Adapter<ListPopupPAdapter.ViewHolder> {
        private ArrayList<String> mDataset;
        private int layout;
        LinearLayout singleCard;
        TextView typeName;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public  class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public View mTextView;
            public ViewHolder(View v) {
                super(v);
                mTextView = v;
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public ListPopupPAdapter(ArrayList<String> myDataset,int dynamiclayout) {
            mDataset = myDataset;
            layout=dynamiclayout;

        }

        // Create new views (invoked by the layout manager)
        @Override
        public ListPopupPAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
            // create a new view
            final   View v = LayoutInflater.from(parent.getContext())
                    .inflate(layout, parent, false);
            // set the view's size, margins, paddings and layout parameters
            ViewHolder vh = new ViewHolder(v);
            typeName=(TextView)v.findViewById(R.id.typeName);
            singleCard=(LinearLayout)v.findViewById(R.id.typeLayoutId);

            singleCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String ID="0";
                    String typeName="";
                    try {
                        TextView tv=(TextView)v.findViewById(R.id.typeName);
                        String typeDataName=tv.getText().toString();
                        for(int i=0;i<sreaDBData.length();i++){
                            String Name= "";
                            Name = sreaDBData.getJSONObject(i).get("Name").toString();
                            if(Name.equals(typeDataName)) {
                                typeName=Name;
                                ID=sreaDBData.getJSONObject(i).get("ID").toString();
                                break;
                            }
                        }


                        Intent intent=new Intent();
                        intent.putExtra("typeId",ID);
                        intent.putExtra("typeName",typeName);
                        setResult(3,intent);
                        finish();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            return vh;
        }

        @Override
        public void onBindViewHolder(ListPopupPAdapter.ViewHolder holder, int position) {
            //typeName.setText();
            typeName.setText(mDataset.get(position));


        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.size();
        }
    }





}

