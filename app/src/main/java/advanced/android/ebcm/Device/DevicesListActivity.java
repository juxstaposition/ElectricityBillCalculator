package advanced.android.ebcm.Device;

import advanced.android.ebcm.Constant;
import advanced.android.ebcm.DatabaseHelper;
import advanced.android.ebcm.Profile.DeleteProfileActivity;
import advanced.android.ebcm.Profile.EditProfileActivity;
import advanced.android.ebcm.R;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import static advanced.android.ebcm.Constant.DELETE_PROFILE_ACTIVITY_REQ_CODE;
import static advanced.android.ebcm.Constant.EDIT_PROFILE_ACTIVITY_REQ_CODE;

public class DevicesListActivity extends AppCompatActivity implements View.OnClickListener {

    DatabaseHelper mDatabaseHelper;
    int profileId;
    String profileName, profileDescription;
    Number profilePrice;
    boolean deleted = false;
    boolean updated = false;
    boolean situation = true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        profileId  = Integer.parseInt(getIntent().getStringExtra("PROFILE_ID"));

        final String transferredData = getIntent().getStringExtra("KEY");
        System.out.println(transferredData);


        if (transferredData.equals(Constant.PROFILE_DEVICES)){
            getProfile();
            setTitle(profileName);
        }

        FloatingActionButton addDeviceFab = findViewById(R.id.fabNewProfile);

        addDeviceFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newItemActivity = new Intent(DevicesListActivity.this, NewDeviceActivity.class);
                newItemActivity.putExtra("KEY", transferredData);
                startActivity(newItemActivity);
                overridePendingTransition(R.anim.blink,0);
            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile_devices, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if ( id == R.id.edit_profile_menu){
            Intent devicesListActivity = new Intent(DevicesListActivity.this, EditProfileActivity.class);
            devicesListActivity.putExtra("KEY",Constant.EDIT_PROFILE);
            devicesListActivity.putExtra("PROFILE_ID", Integer.toString(profileId));
            devicesListActivity.putExtra("PROFILE_NAME", profileName);
            devicesListActivity.putExtra("PROFILE_DESCRIPTION", profileDescription);
            devicesListActivity.putExtra("PROFILE_PRICE", String.valueOf(profilePrice));
            startActivityForResult(devicesListActivity, EDIT_PROFILE_ACTIVITY_REQ_CODE);
            return true;
        }
        else if ( id == R.id.delete_profile_menu){
            Intent devicesListActivity = new Intent(DevicesListActivity.this, DeleteProfileActivity.class);
            devicesListActivity.putExtra("KEY",Constant.DELETE_PROFILE);
            devicesListActivity.putExtra("PROFILE_ID", Integer.toString(profileId));
            devicesListActivity.putExtra("PROFILE_NAME", profileName);
            startActivityForResult(devicesListActivity, DELETE_PROFILE_ACTIVITY_REQ_CODE);
            return true;
        }
        else if ( id == R.id.action_help){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onBackPressed () {
        Intent returnIntent = new Intent();

        if (deleted) {

            returnIntent.putExtra("ACTION", "DELETE");
            returnIntent.putExtra("PROFILE_ID", profileId);

            setResult(Activity.RESULT_OK, returnIntent);
            super.onBackPressed();

        }
        if (updated) {

            returnIntent.putExtra("ACTION", "UPDATE");
            returnIntent.putExtra("PROFILE_ID", profileId);
            returnIntent.putExtra("PROFILE_NAME", profileName);
            returnIntent.putExtra("PROFILE_DESCRIPTION", profileDescription);
            returnIntent.putExtra("PROFILE_PRICE", profilePrice);

            setResult(Activity.RESULT_OK, returnIntent);
            super.onBackPressed();

        }
        if (situation) {
            setResult(Activity.RESULT_CANCELED);
            super.onBackPressed();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == DELETE_PROFILE_ACTIVITY_REQ_CODE && resultCode == Activity.RESULT_OK) {
            updated = false;
            situation = false;
            deleted = true;
            getProfile();
            onBackPressed();
        }
        if (requestCode == EDIT_PROFILE_ACTIVITY_REQ_CODE && resultCode == Activity.RESULT_OK) {
            situation = false;
            updated = true;
            getProfile();
            setTitle(profileName);
        }

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_right_exit);

    }

    void getProfile() {
        mDatabaseHelper = new DatabaseHelper(this);

        Cursor data = mDatabaseHelper.getProfileItemByID(profileId);

        if (data != null) {
            if (data.moveToFirst() && data.getCount() >= 1) {
                do {

                    profileId = data.getInt(0);
                    profileName = data.getString(1);
                    profileDescription = data.getString(2);
                    profilePrice = data.getFloat(3);

                } while (data.moveToNext());
            }
        } else {
            Log.d("GET_PROFILE", " is empty");
        }



       mDatabaseHelper.close();
    }

}
