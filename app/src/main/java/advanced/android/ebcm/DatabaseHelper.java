package advanced.android.ebcm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    //database info
    private static final String DATABASE_NAME = "ebcm";
    private static final int DATABASE_VERSION = 1;

    //table names
    private static final String TABLE_PROFILE = "profile_table";
    private static final String TABLE_DEVICE = "device_table";


    //profile table columns
    private static final String PROFILE_COL0 = "profile_ID";
    private static final String PROFILE_COL1 = "profile_name";
    private static final String PROFILE_COL2 = "profile_description";
    private static final String PROFILE_COL3 = "profile_price";

    //device table column
    private static final String DEVICE_COL0 = "device_ID";
    private static final String DEVICE_COL1 = "device_name";
    private static final String DEVICE_COL2 = "device_quantity";
    private static final String DEVICE_COL3 = "device_usage_hours";
    private static final String DEVICE_COL4 = "device_usage_minutes";
    private static final String DEVICE_COL5 = "device_usage_days";
    private static final String DEVICE_COL6 = "device_group";
    private static final String DEVICE_COL7 = "consumption";
    private static final String DEVICE_COL8 = "profile_parent";



    //create table profiles
    private static final String createProfileTable = "CREATE TABLE " + TABLE_PROFILE + " ("+ PROFILE_COL0 +" INTEGER PRIMARY KEY AUTOINCREMENT, " +
            PROFILE_COL1 + " TEXT, " + PROFILE_COL2 + " TEXT, " + PROFILE_COL3 + " NUMERIC "+ ")";

    //create table devices
    private static final String createDeviceTable = "CREATE TABLE " + TABLE_DEVICE + " (" + DEVICE_COL0 +" INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DEVICE_COL1 + " TEXT, " + DEVICE_COL2 +" INTEGER, " + DEVICE_COL3 + " INTEGER, " + DEVICE_COL4 + " INTEGER, " +
            DEVICE_COL5 + " INTEGER, " + DEVICE_COL6 + " TEXT, " + DEVICE_COL7 + " INTEGER, " + DEVICE_COL8 + " INTEGER " + ")";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

//    tutorial simple database creating query
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        String createTable = "CREATE TABLE " + TABLE_PROFILE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                PROFILE_COL1 + " TEXT)";
//        db.execSQL(createTable);
//    }

    //actually creating tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createProfileTable);
        db.execSQL(createDeviceTable);
    }

    //re create tables if there where changes in database version
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEVICE);
        onCreate(db);
    }

    //adding data to Profile table
    public boolean addProfileData(String newName, String newDescription, String newPrice) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PROFILE_COL1, newName);
        contentValues.put(PROFILE_COL2, newDescription);
        contentValues.put(PROFILE_COL3, newPrice);

        Log.d(TAG, "addProfileData: Adding " + newName + " as name " + newDescription + " as description " + newPrice + " as price " + " to " + TABLE_PROFILE);
        long result = db.insert(TABLE_PROFILE, null, contentValues);

        //if data as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }

    }

    //adding data to Device table
    public boolean addDeviceData(String newDeviceName, String newQuantity, String newHours, String newMinutes, String newDays, String newGroup, String newConsumption, String newProfileParent) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DEVICE_COL1, newDeviceName);
        contentValues.put(DEVICE_COL2, newQuantity);
        contentValues.put(DEVICE_COL3, newHours);
        contentValues.put(DEVICE_COL4, newMinutes);
        contentValues.put(DEVICE_COL5, newDays);
        contentValues.put(DEVICE_COL6, newGroup);
        contentValues.put(DEVICE_COL7, newConsumption);
        contentValues.put(DEVICE_COL8, newProfileParent);


        Log.d(TAG, "addDeviceData: Adding " + DEVICE_COL1 + ": " + newDeviceName + ", " + DEVICE_COL2 + ": " + newQuantity + ", " + DEVICE_COL3 + ": " + newHours +
                ", " + DEVICE_COL4 + ": " + newMinutes + ", " + DEVICE_COL5 + ": " + newDays + ", " + DEVICE_COL6 + ": " + newGroup +
                ", " + DEVICE_COL7 + newConsumption + ", " + DEVICE_COL8 + newProfileParent + " to " + TABLE_DEVICE);
        long result = db.insert(TABLE_DEVICE, null, contentValues);

        //if data as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }

    }


    /**
     * Returns all the data from database
     * @return
     */

    //profile
    public Cursor getProfileData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_PROFILE;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    //device
    public Cursor getDeviceData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_DEVICE;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    /**
     * returns only the ID that matches the name passed in
     * @params name
     * @return
     *
     * This and below are NOT done yet
     */
    //profile
    public Cursor getProfileItemID(String name, String description){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_PROFILE +
                " WHERE " + PROFILE_COL1 + " = '" + name + "' AND " + PROFILE_COL2 + " = '" + description + "'" ;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    //device
    public Cursor getDeviceItemID(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + DEVICE_COL0 + " FROM " + TABLE_DEVICE +
                " WHERE " + DEVICE_COL1 + " = '" + name + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }


    public Cursor getDeviceByID(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_DEVICE + " WHERE " + DEVICE_COL0 +" = " + id;
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    /**
     * Updates the name field
     * @param newName
     * @param id
     * @param oldName
     */

    //profile
    public void updateProfile(String newName, String newDescription, String newPrice, int id, String oldName) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_PROFILE + " SET " + PROFILE_COL1 +
                " = '" + newName + "', " + PROFILE_COL2 + " = '" + newDescription + "', " + PROFILE_COL3 + " = '" + newPrice + "' WHERE " + PROFILE_COL0 + " = '" + id +"'" +
                " AND " + PROFILE_COL1 + " ='" + oldName + "'";

        Log.d(TAG, "updateProfile: query: " + query);
        Log.d(TAG, "updateProfile: Setting name to " + newName);
        db.execSQL(query);
    }

    //device
    public void updateDevice(String newName, String newConsumption, String newQuantity, String newHours, String newMinutes, String newDays, String newGroup, String newProfileParent,  int id, String oldName) {
        SQLiteDatabase db = this.getWritableDatabase();
//        String query = "UPDATE " + TABLE_DEVICE + " SET " + DEVICE_COL1 +
//                " = '" + newName + "' WHERE " + DEVICE_COL0 + " = '" + id +"'" +
//                " AND " + DEVICE_COL1 + " ='" + oldName + "'";

        String query = "UPDATE " + TABLE_DEVICE + " SET " + DEVICE_COL1 +
                " = '" + newName + "', " + DEVICE_COL2 + " = " + newQuantity + ", " + DEVICE_COL3 + " = " + newHours + ", " + DEVICE_COL4 + " = " + newMinutes
                + ", " + DEVICE_COL5 + " = " + newDays +", " + DEVICE_COL6 + " = '" + newGroup + "', " + DEVICE_COL7 + " = " + newConsumption + ", " + DEVICE_COL8 + " = " + newProfileParent + " WHERE "
                + DEVICE_COL0 + " = " + id + " AND " + DEVICE_COL1 + " = '" + oldName + "'";

        Log.d(TAG, "updateDevice: query: " + query);
        Log.d(TAG, "updateDevice: Setting device where ID = " + id + " to - name: " + newName + ", quantity: " + newQuantity + ", consumption: " + newConsumption + ", hours: " + newHours +
                ", minutes:" + newMinutes + ", days: " + newDays + ", group: " + newGroup + ", profile_parent:" + newProfileParent);
        db.execSQL(query);
    }


    /**
     * Delete from database
     * @param name
     * @param id
     */
    public void deleteProfile(int id, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_PROFILE + " WHERE " +
                PROFILE_COL0 + " = '" + id + "'" + " AND " + PROFILE_COL1 + " = '" + name + "'";
        Log.d(TAG, "deleteProfile: query: " +query);
        Log.d(TAG, "deleteProfile: Deleting " + name + " from database.");
        db.execSQL(query);
    }

    public void deleteDeviceName(int id, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_DEVICE + " WHERE " +
                DEVICE_COL0 + " = '" + id + "'" + " AND " + DEVICE_COL1 + " = '" +
                name + "'";
        Log.d(TAG, "deleteDeviceName: query: " +query);
        Log.d(TAG, "deleteDeviceName: Deleting " + name + " from database.");
        db.execSQL(query);
    }
}
