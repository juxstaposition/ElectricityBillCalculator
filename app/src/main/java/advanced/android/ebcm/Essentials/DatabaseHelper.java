package advanced.android.ebcm.Essentials;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.math.BigDecimal;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    //database info
    private static final String DATABASE_NAME = "ebcm";
    private static final int DATABASE_VERSION = 8;

    //table names
    private static final String TABLE_PROFILE = "profile_table";
    private static final String TABLE_DEVICE = "device_table";


    //profile table columns
    private static final String PROFILE_COL0 = "profile_ID";
    private static final String PROFILE_COL1 = "profile_name";
    private static final String PROFILE_COL2 = "profile_description";
    private static final String PROFILE_COL3 = "profile_price";
    private static final String PROFILE_COL4 = "profile_power";
    private static final String PROFILE_COL5 = "profile_cost";
    private static final String PROFILE_COL6 = "profile_time";

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
            PROFILE_COL1 + " TEXT, " + PROFILE_COL2 + " TEXT, " + PROFILE_COL3 + " NUMERIC, "+ PROFILE_COL4 + " NUMERIC, " + PROFILE_COL5 +
            " NUMERIC, " + PROFILE_COL6 + " TEXT " + ")";

    //create table devices
    private static final String createDeviceTable = "CREATE TABLE " + TABLE_DEVICE + " (" + DEVICE_COL0 +" INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DEVICE_COL1 + " TEXT, " + DEVICE_COL2 +" INTEGER, " + DEVICE_COL3 + " INTEGER, " + DEVICE_COL4 + " INTEGER, " +
            DEVICE_COL5 + " INTEGER, " + DEVICE_COL6 + " TEXT, " + DEVICE_COL7 + " INTEGER, " + DEVICE_COL8 + " INTEGER " + ")";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


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
        contentValues.put(PROFILE_COL4, 0);
        contentValues.put(PROFILE_COL5, 0);
        contentValues.put(PROFILE_COL6, 0);

        Log.d(TAG, "addProfileData: Adding " + newName + " as name " + newDescription + " as description " +
                newPrice + " as price " + "(power/cost/time) " + 0 + "/" + 0 + "/" + 0 + " to " + TABLE_PROFILE);
        long result = db.insert(TABLE_PROFILE, null, contentValues);

        //if data as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    //adding data to Device table
    public boolean addDeviceData(String newDeviceName, int newQuantity, int newHours, int newMinutes, int newDays,
                                 int newConsumption, int newProfileParent) {
        String newGroup = "none";
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
     * @return data
     */

    //profile
    public Cursor getProfileData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_PROFILE;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    //device
    public Cursor getDeviceData(int profileParent){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_DEVICE + " WHERE " + DEVICE_COL8 + " = '" + profileParent + "'" ;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    /**
     * returns only the everything where id
     * id int
     * @return
     *
     * This and below are NOT done yet
     */
    //profile
    public Cursor getProfileItemByID(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_PROFILE +
                " WHERE " + PROFILE_COL0 + " = '" + id + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    //device
    public Cursor getDeviceByID(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_DEVICE + " WHERE " + DEVICE_COL0 +" = " + id;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    /**
     * Updates the name field
     * @param newDescription string
     * @param newPrice string
     * @param newName string
     * @param id int
     */
    //profile
    public void updateProfileMain(String newName, String newDescription, String newPrice, int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "UPDATE " + TABLE_PROFILE + " SET " + PROFILE_COL1 +
                    " = '" + newName + "', " + PROFILE_COL2 + " = '" + newDescription + "', " + PROFILE_COL3 + " = '" + newPrice +
                    "' WHERE " + PROFILE_COL0 + " = " + id;

        Log.d(TAG, "updateProfileMain: query: " + query);
        db.execSQL(query);
    }

    public void updateProfilePowerCost(BigDecimal newCost, BigDecimal newPower, int id ) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "UPDATE " + TABLE_PROFILE + " SET " + PROFILE_COL4 + " = '" + newPower +"', " +
                PROFILE_COL5 + " = '" + newCost + "' WHERE " + PROFILE_COL0 + " = " + id;

        Log.d(TAG, "updateProfilePowerCost: query: " + query);

        db.execSQL(query);
    }

    public void updateProfileTime(String time, int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "UPDATE " + TABLE_PROFILE + " SET " + PROFILE_COL6 + " = '" + time + "' WHERE " + PROFILE_COL0 + " = " + id;
        Log.d(TAG, "updateProfileTime: query: " + query);
        db.execSQL(query);
    }


    //device
    public void updateDevice(String newName, int newConsumption, int newQuantity, int newHours, int newMinutes, int newDays, int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "UPDATE " + TABLE_DEVICE + " SET " + DEVICE_COL1 + " = '" + newName + "', " + DEVICE_COL2 + " = " + newQuantity + ", "
                + DEVICE_COL3 + " = " + newHours + ", " + DEVICE_COL4 + " = " + newMinutes + ", " + DEVICE_COL5 + " = " + newDays + ", "
                + DEVICE_COL7 + " = " + newConsumption + " WHERE " + DEVICE_COL0 + " = " + id;

        Log.d(TAG, "updateDevice: query: " + query);
        Log.d(TAG, "updateDevice: Setting device where ID = " + id + " to - name: " + newName + ", quantity: " + newQuantity + ", consumption: " + newConsumption + ", hours: " + newHours +
                ", minutes:" + newMinutes + ", days: " + newDays );
        db.execSQL(query);
    }


    /**
     * Delete from database
     * @param id integer
     */
    public void deleteProfile(int id) {
        if (id != -1) {
            SQLiteDatabase db = this.getWritableDatabase();
            String query = "DELETE FROM " + TABLE_PROFILE + " WHERE " +
                    PROFILE_COL0 + " = " + id;
            Log.d(TAG, "deleteProfile: query: " +query);
            db.execSQL(query);
        }

    }

    public void deleteDevice(int id) {
        if ( id != -1) {
            SQLiteDatabase db = this.getWritableDatabase();
            String query = "DELETE FROM " + TABLE_DEVICE + " WHERE " +
                    DEVICE_COL0 + " = " + id;
            Log.d(TAG, "deleteDeviceName: query: " +query);
            db.execSQL(query);
        }
    }
}
