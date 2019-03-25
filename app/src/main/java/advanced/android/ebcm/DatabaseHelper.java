package advanced.android.ebcm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ebcm";
    
    private static final String TABLE_PROFILES = "profiles";
    private static final String KEY_PROFILE_ID = "id";
    private static final String KEY_PROFILE_NAME = "profileName";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_POWER_COST = "powerCost";


    private static final String TABLE_DEVICES = "devices";
    private static final String KEY_DEV_ID = "id";
    private static final String KEY_DEVICE_NAME = "id";
    private static final String KEY_CONSUMPTION = "consumption";
    private static final String KEY_QUANTITY = "quantity";
    private static final String KEY_USAGE_HOURS = "usageHours";
    private static final String KEY_USAGE_MINUTES = "usageMinutes";
    private static final String KEY_USAGE_DAYS = "usageDays";
    private static final String KEY_RESULT = "result";




    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PROFILES_TABLE = "CREATE TABLE " + TABLE_PROFILES + "("
                + KEY_PROFILE_ID + "INTEGER PRIMARY KEY," + KEY_PROFILE_NAME + " TEXT,"
                + KEY_DESCRIPTION + " TEXT," + KEY_POWER_COST+ " TEXT" + ")";
        db.execSQL(CREATE_PROFILES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILES);

        onCreate(db);
    }

    void addProfile(Profile profile) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("KEY_PROFILE_NAME", profile.getName());
        values.put("KEY_PROFILE_DESCRIPTION", profile.getDescription());

        db.insert(TABLE_PROFILES, null, values);
        db.close();
    }

    Profile getProfile(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PROFILES, new String[] {KEY_PROFILE_ID, KEY_PROFILE_NAME, KEY_DESCRIPTION}, KEY_PROFILE_ID
        + "=?", new String[]{String.valueOf(id)}, null, null,null,null);

        if(cursor != null) {
            cursor.moveToFirst();
        }

        Profile profile = new Profile(cursor.getString(1), cursor.getString(2), cursor.getString(3));

        return profile;
    }

}
