package com.csm.smartcity.sqlLiteModel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by arundhati on 9/4/2015.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "SMARTCITY";

    private static final String KEY_ID = "ID";

    private static  String ALLRECORDTABLE_NAME = "ALLCONTROLDATA";
    private static final String DATA_NAME = "DATA_NAME";
    private static final String DATA_VALUE = "DATA_VALUE";



    private static  String COMPLAINTTABLE_NAME = "OFFLINE_COMPLAINT";

    private static final String REOPEN="REOPEN";
    private static final String APPLICANT_NAME="APPLICANT_NAME";
    private static final String PHONE_NO="PHONE_NO";
    private static final String COMP_WARD_ID="COMP_WARD_ID";
    private static final String CATAGORY_ID="CATAGORY_ID";
    private static final String TYPE_ID="TYPE_ID";
    private static final String COMP_AREA_ID="COMP_AREA_ID";
    private static final String LANDMARK="LANDMARK";
    private static final String COMPLAINT_DETAILS="COMPLAINT_DETAILS";
    private static final String COMP_IMAGE="COMP_IMAGE";
    private static final String LAT="LAT";
    private static final String LONG="LONG";
    private static final String COMP_CITIZEN_ID="COMP_CITIZEN_ID";
    private static final String CURRENT_DATE="CURRENT_DATE";





    // Contacts table name
    private static  String TABLE_NAME = "USER_PROFILE";
    // Contacts Table Columns names
    private static final String USER_NAME = "USER_NAME";
    private static final String MOBILENO = "MOBILENO";
    private static final String IMAGE="IMAGE";
    private static final String IMAGE_FLAG="IMAGE_FLAG";
    private static final String ADDRESS="ADDRESS";
    private static final String DOB="DOB";
    private static final String LAND_MARK="LAND_MARK";
    private static final String AREA_ID="AREA_ID";
    private static final String REGD_DATE="REGD_DATE";
    private static final String GENDER="GENDER";
    private static final String PLOT="PLOT";
    private static final String CITIZEN_ID="CITIZEN_ID";
    private static final String WARD_ID="WARD_ID";
    private static final String EMAIL_ID="EMAIL_ID";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
//        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
//                + KEY_ID + " INTEGER PRIMARY KEY," + USER_NAME + " TEXT,"
//                + MOBILENO + " TEXT," +IMAGE+ " TEXT," +IMAGE_FLAG+ " TEXT,"
//                +ADDRESS+ " TEXT," +DOB+ " TEXT," +LAND_MARK+ " TEXT,"
//                +AREA_ID+ " TEXT," +REGD_DATE+ " TEXT," +GENDER+ " TEXT,"
//                +PLOT+ " TEXT," +CITIZEN_ID+ " TEXT," +WARD_ID+ " TEXT,"
//                +EMAIL_ID+ " TEXT" +")";

        String ALLCONTROL_TABLE = "CREATE TABLE " + ALLRECORDTABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + DATA_NAME + " TEXT,"
                + DATA_VALUE + " TEXT"+")";

//        String CREATE_COMPLAINT_TABLE = "CREATE TABLE " + COMPLAINTTABLE_NAME + "("
//                + KEY_ID + " INTEGER PRIMARY KEY," + REOPEN + " TEXT,"
//                + APPLICANT_NAME + " TEXT," +PHONE_NO+ " TEXT," +COMP_WARD_ID+ " TEXT,"
//                +CATAGORY_ID+ " TEXT," +TYPE_ID+ " TEXT,"+COMP_AREA_ID+ " TEXT,"
//                +LANDMARK+ " TEXT," +COMPLAINT_DETAILS+ " TEXT," +COMP_IMAGE+ " TEXT,"
//                +LAT+ " TEXT," + LONG+ " TEXT," +COMP_CITIZEN_ID+ " TEXT," +CURRENT_DATE+ " TEXT"+")";

      //  db.execSQL(CREATE_CONTACTS_TABLE);
        db.execSQL(ALLCONTROL_TABLE);
      //  db.execSQL(CREATE_COMPLAINT_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
       // db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ALLRECORDTABLE_NAME);
       // db.execSQL("DROP TABLE IF EXISTS " + COMPLAINTTABLE_NAME);
        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */


//    public void addComplaintData(ComplaintData comp_data){
//
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//
//        values.put(REOPEN, comp_data.getReopen()); // Contact Name
//        values.put(APPLICANT_NAME, comp_data.getApplicant_name()); // Contact Phone
//        values.put(PHONE_NO, comp_data.getPhone_no());
//        values.put(COMP_WARD_ID, comp_data.getCopm_ward_id());
//        values.put(CATAGORY_ID, comp_data.getCatagory_id());
//        values.put(TYPE_ID, comp_data.getType_id());
//        values.put(COMP_AREA_ID, comp_data.getComp_area_id());
//        values.put(LANDMARK, comp_data.getLandmark());
//        values.put(COMPLAINT_DETAILS, comp_data.getComplaint_details());
//        values.put(COMP_IMAGE, comp_data.getComp_image());
//        values.put(LAT, comp_data.getLat());
//        values.put(LONG, comp_data.getLongitude());
//        values.put(COMP_CITIZEN_ID, comp_data.getComp_citizen_id());
//        values.put(CURRENT_DATE, comp_data.getCurrent_date());
//
//
//        // Inserting Row
//        db.insert(COMPLAINTTABLE_NAME, null, values);
//        db.close(); // Closing database connection
//
//
//
//    }


    public void addControllData(ControllData data){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DATA_NAME,data.getDataName());
        values.put(DATA_VALUE,data.getDataValue());

        // Inserting Row
        db.insert(ALLRECORDTABLE_NAME, null, values);
        db.close(); // Closing database connection

    }



    // Getting single contact
    public ControllData getContollData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(ALLRECORDTABLE_NAME, new String[] { KEY_ID,
                        DATA_NAME, DATA_NAME }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        ControllData contrlData = new ControllData();
        contrlData.setDataName(cursor.getString(0));
        contrlData.setDataValue(cursor.getString(1));
        // return contact
        return contrlData;
    }




    // Getting All Contacts
    public List<ControllData> getAllContollData(String data) {
        List<ControllData> contactList = new ArrayList<ControllData>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + ALLRECORDTABLE_NAME+" WHERE "+KEY_ID+" IN("+data+")";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ControllData conData = new ControllData();
                conData.setDataName(cursor.getString(1));
                conData.setDataValue(cursor.getString(2));
                // Adding contact to list
                contactList.add(conData);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }


//    public List<ComplaintData> getAllComplaintData(){
//        List<ComplaintData> complaintList=new ArrayList<ComplaintData>();
//
//        String selectQuery="SELECT "+KEY_ID+","+COMPLAINT_DETAILS+","+CURRENT_DATE+" FROM " + COMPLAINTTABLE_NAME +" ORDER BY "+ KEY_ID +" DESC";
//
//        SQLiteDatabase db=getWritableDatabase();
//        Cursor cursor=db.rawQuery(selectQuery,null);
//
//
//        // looping through all rows and adding to list
//        if (cursor.moveToFirst()) {
//            do {
//                ComplaintData data=new ComplaintData();
//                data.setComp_citizen_id(cursor.getString(0));
//                 data.setComplaint_details(cursor.getString(1));
//                 data.setCurrent_date(cursor.getString(2));
//                 complaintList.add(data);
//            } while (cursor.moveToNext());
//        }
//
//        return complaintList;
//    }


//    public ComplaintData getComplaintData(String id){
//
//        ComplaintData data=new ComplaintData();
//        String selectQuery="SELECT * FROM "+COMPLAINTTABLE_NAME+" where "+KEY_ID+"="+id;
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
//
//        if (cursor != null)
//            cursor.moveToFirst();
//
//        data.setReopen(cursor.getString(1));
//        data.setApplicant_name(cursor.getString(2));
//        data.setPhone_no(cursor.getString(3));
//        data.setCopm_ward_id(cursor.getString(4));
//        data.setCatagory_id(cursor.getString(5));
//        data.setType_id(cursor.getString(6));
//        data.setComp_area_id(cursor.getString(7));
//        data.setLandmark(cursor.getString(8));
//        data.setComplaint_details(cursor.getString(9));
//        data.setComp_image(cursor.getString(10));
//        data.setLat(cursor.getString(11));
//        data.setLongitude(cursor.getString(12));
//        data.setComp_citizen_id(cursor.getString(13));
//        data.setCurrent_date(cursor.getString(14));
//
//        return data;
//    }


    // Getting All Contacts
/*    public List<LoginUserObject>  getAllContacts() {
        List<LoginUserObject> profileList = new ArrayList<LoginUserObject>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setPhoneNumber(cursor.getString(2));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }*/



    // Deleting single contact
//    public void deleteContact(Contact contact) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
//                new String[] { String.valueOf(contact.getID()) });
//        db.close();
//    }


    // Getting contacts Count
    public int getContactsCount() {
        int count=0;
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        if(cursor != null && !cursor.isClosed()){
            count = cursor.getCount();
            cursor.close();
        }
        // return count
        return count;
    }

    public int getControldataCount(){
        int count=0;
        String countQuery = "SELECT  * FROM " + ALLRECORDTABLE_NAME;
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cur=db.rawQuery(countQuery,null);
        if(cur != null && !cur.isClosed()){
            count = cur.getCount();
            cur.close();
        }
        return count;
    }


    public int getComplaintdataCount(){
        int count=0;
        String countQuery = "SELECT  * FROM " + COMPLAINTTABLE_NAME;
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cur=db.rawQuery(countQuery,null);
        if(cur != null && !cur.isClosed()){
            count = cur.getCount();
            cur.close();
        }
        return count;
    }


    public void deleteComplaint(String id) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(COMPLAINTTABLE_NAME, KEY_ID + "=?",
                new String[] { id }); // KEY_ID= id of row and third parameter is argument.
        db.close();
    }

    // Deleting single contact
    public void deleteControllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ALLRECORDTABLE_NAME, null, null);
        db.close();
    }

}