package advanced.android.ebcm.Device;

import advanced.android.ebcm.Constant;
import advanced.android.ebcm.DatabaseHelper;
import advanced.android.ebcm.Profile.DeleteProfileActivity;
import advanced.android.ebcm.Profile.NewProfileActivity;
import advanced.android.ebcm.Profile.Profile;
import advanced.android.ebcm.R;
import advanced.android.ebcm.ResultGraph;
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
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

import static advanced.android.ebcm.Constant.*;

public class DevicesListActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<Device> devices = new ArrayList<>();
    private ArrayList<Integer> deviceIds = new ArrayList<>();

    DatabaseHelper mDatabaseHelper;
    int profileId, deviceId, deviceQuantity, deviceConsumption, deviceUsageDays, deviceUsageHours, deviceUsageMinutes ;
    Profile profile;
    LinearLayout deviceLayout;
    String profileName, profileDescription, deviceName;
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

        deviceLayout = findViewById(R.id.device_list);

        mDatabaseHelper = new DatabaseHelper(this);

        generateDeviceView();


        getProfile();
        setTitle(profileName);


        FloatingActionButton addDeviceFab = findViewById(R.id.fabNewProfile);
        FloatingActionButton fabCalc = findViewById(R.id.fabCalculateProfile);


        addDeviceFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newItemActivity = new Intent(DevicesListActivity.this, NewDeviceActivity.class);
                newItemActivity.putExtra("KEY",Constant.NEW_DEVICE);
                newItemActivity.putExtra("PROFILE_ID", String.valueOf(profileId));
                Log.d("PROFILE_ID", String.valueOf(profileId));
                startActivityForResult(newItemActivity, ADD_DEVICE_TO_PROFILE_REQ_CODE);
                overridePendingTransition(R.anim.blink,0);
            }
        });

        fabCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DevicesListActivity.this, ResultGraph.class);
                intent.putExtra("PROFILE_PARENT",String.valueOf(profileId));
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_enter,R.anim.slide_left_exit);
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
            Intent editProfile = new Intent(DevicesListActivity.this, NewProfileActivity.class);
            editProfile.putExtra("KEY",Constant.EDIT_PROFILE);
            editProfile.putExtra("PROFILE_ID", Integer.toString(profileId));
            editProfile.putExtra("PROFILE_NAME", profileName);
            editProfile.putExtra("PROFILE_DESCRIPTION", profileDescription);
            editProfile.putExtra("PROFILE_PRICE", String.valueOf(profilePrice));
            startActivityForResult(editProfile, EDIT_PROFILE_ACTIVITY_REQ_CODE);
            overridePendingTransition(R.anim.blink,0);
            return true;
        }
        else if ( id == R.id.delete_profile_menu){
            Intent deleteProfile = new Intent(DevicesListActivity.this, DeleteProfileActivity.class);
            deleteProfile.putExtra("KEY",Constant.DELETE_PROFILE);
            deleteProfile.putExtra("PROFILE_ID", Integer.toString(profileId));
            deleteProfile.putExtra("PROFILE_NAME", profileName);
            startActivityForResult(deleteProfile, DELETE_PROFILE_ACTIVITY_REQ_CODE);
            overridePendingTransition(R.anim.blink,0);
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
        else if (situation) {
            setResult(Activity.RESULT_CANCELED, returnIntent);
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
            onBackPressed();
        }
        if (requestCode == EDIT_PROFILE_ACTIVITY_REQ_CODE && resultCode == Activity.RESULT_OK) {
            situation = false;
            updated = true;
            getProfile();
            setTitle(profileName);

        }
        if (requestCode == ADD_DEVICE_TO_PROFILE_REQ_CODE && resultCode == Activity.RESULT_OK) {
          addDeviceView(data);
        }
        if (requestCode == EDIT_DEVICE_REQ_CODE && resultCode == Activity.RESULT_OK) {
            updateExistingDevice(data);
        }
        if (requestCode == DELETE_DEVICE_REQ_CODE && resultCode == Activity.RESULT_OK) {
            deleteDevice(data);
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

    void getUpdatedDevice(int deviceId) {
        mDatabaseHelper = new DatabaseHelper(this);

        Cursor data = mDatabaseHelper.getDeviceByID(profileId);

        if (data != null) {
            if (data.moveToFirst() && data.getCount() >= 1) {
                do {
                    System.out.println("__________________________");

                    System.out.println(deviceId);
                    deviceName = data.getString(1);
                    deviceQuantity = data.getInt(2);
                    deviceConsumption = data.getInt(7);
                    deviceUsageHours = data.getInt(3);
                    deviceUsageMinutes = data.getInt(4);
                    deviceUsageDays = data.getInt(5);

                } while (data.moveToNext());
            }
        } else {
            Log.d("GET_PROFILE", " is empty");
        }

        mDatabaseHelper.close();
    }

    private void generateDeviceView(){

        Cursor data = mDatabaseHelper.getDeviceData(profileId);

        if (data != null) {
            if (data.moveToFirst() && data.getCount() >= 1) {
                do {

                    createNewDevice(data);

                } while (data.moveToNext());
            }
        } else {
            Toast.makeText(this, "Database is empty", Toast.LENGTH_SHORT).show();
        }

    }

    private void createNewDevice(Cursor data) {
        final int id = Integer.parseInt(data.getString(0));
        final String name = data.getString(1);
        int quantity = data.getInt(2);
        int hours = data.getInt(3);
        int minutes = data.getInt(4);
        int days = data.getInt(5);
        int consumption = data.getInt(7);
        int profileParent = profileId;


        final Device device = new Device(id,name,quantity,hours,minutes,days, consumption, profileParent );
        device.generateDevice(getApplicationContext(),deviceLayout);
        device.deviceForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant animation = new Constant();
                animation.startAnimation(v,R.anim.blink,getApplicationContext());
                animation.startAnimation(device.deviceForm,R.anim.blink,getApplicationContext());

//                Intent intent = new Intent(DevicesListActivity.this, NewDeviceActivity.class);
//                intent.putExtra("KEY", EDIT_DEVICE);
//                intent.putExtra("DEVICE_ID", String.valueOf(device.getId()));
//                intent.putExtra("DEVICE_NAME", String.valueOf(device.getName()));
//                intent.putExtra("DEVICE_QUANTITY", String.valueOf(device.getQuantity()));
//                intent.putExtra("DEVICE_CONSUMPTION", String.valueOf(device.getPower()));
//                intent.putExtra("DEVICE_USAGE_HOURS", String.valueOf(device.getHours()));
//                intent.putExtra("DEVICE_USAGE_MINUTES", String.valueOf(device.getMinutes()));
//                intent.putExtra("DEVICE_USAGE_DAYS", String.valueOf(device.getDays()));
//
//                startActivityForResult(intent, EDIT_DEVICE_REQ_CODE);
//                overridePendingTransition(R.anim.blink, 0);

                Intent intent = new Intent(DevicesListActivity.this, DeleteDeviceActivity.class);
                intent.putExtra("KEY", DELETE_DEVICE);
                intent.putExtra("DEVICE_ID", String.valueOf(device.getId()));
                intent.putExtra("DEVICE_NAME", device.getName());
                startActivityForResult(intent, DELETE_DEVICE_REQ_CODE);
                overridePendingTransition(R.anim.blink, 0);
            }
        });

        devices.add(device);
//        devices.add(device);
//        device.generateDevice(getApplicationContext(), myVerticalLayout);
//
//        device.clipDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Intent intent = new Intent(DevicesListActivity.this, DeleteProfileActivity.class);
////                intent.putExtra("PROFILE_NAME", name);
////                intent.putExtra("PROFILE_ID", Integer.toString(id));
////                startActivityForResult(intent, DELETE_PROFILE_ACTIVITY_REQ_CODE);
//                Toast.makeText(DevicesListActivity.this, "Delete Device Clicked!!", Toast.LENGTH_SHORT).show();
//            }});
//        device.deviceForm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Constant animation = new Constant();
//                animation.startAnimation(v,R.anim.blink,getApplicationContext());
//                animation.startAnimation(device.clipDelete,R.anim.blink,getApplicationContext());
//
////                Intent intent = new Intent(DevicesListActivity.this, DevicesListActivity.class);
////                intent.putExtra("KEY",Constant.PROFILE_DEVICES);
////                intent.putExtra("PROFILE_ID",Integer.toString(id));
////                startActivityForResult(intent, UPDATE_PROFILE_ACTIVITY_REQ_CODE);
//
//                Toast.makeText(DevicesListActivity.this, "Device clicked!", Toast.LENGTH_SHORT).show();
//
//            }
//        });
        Log.d("deviceCREATE", Integer.toString(device.getId()));
    }

    private void addDeviceView(Intent data) {
        mDatabaseHelper = new DatabaseHelper(this);

        Cursor deviceIdData = mDatabaseHelper.getDeviceData(profileId);

        try {
            if (data != null) {
                if (deviceIdData.moveToFirst() && deviceIdData.getCount() >= 1) {
                    do {
                        deviceIds.add(deviceIdData.getInt(0));
                    } while (deviceIdData.moveToNext());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            deviceIdData.close();
        } finally {
            if (deviceIdData != null) {
                deviceIdData.close();
            }
        }

        int max = 0;

        for ( int i = 0; i < deviceIds.size(); i++ ) {

            if ( max < deviceIds.get(i) ){
                max = deviceIds.get(i);
                System.out.println(max);
            }
        }
        Log.d("MAX_VALUE", Integer.toString(max));

        Bundle mBundle = data.getExtras();
        final String deviceName = mBundle.getString("DEVICE_NAME");
        final String deviceConsumption = mBundle.getString("DEVICE_CONSUMPTION");
        final String deviceQuantity = mBundle.getString("DEVICE_QUANTITY");
        final String deviceUsageHours = mBundle.getString("DEVICE_USAGE_HOURS");
        final String deviceUsageMinutes = mBundle.getString("DEVICE_USAGE_MINUTES");
        final String deviceUsageDays = mBundle.getString("DEVICE_USAGE_DAYS");


        Log.d("DEVICE_LIST", "name: "+ deviceName +", consumption: "+ deviceConsumption + ", quantity: "+ deviceQuantity +", usageHours: "+
                deviceUsageHours +", usageMinutes: "+ deviceUsageMinutes +", usageDays: "+ deviceUsageDays);


        final Device device = new Device(max,deviceName,Integer.parseInt(deviceQuantity),
                Integer.parseInt(deviceUsageHours),Integer.parseInt(deviceUsageMinutes),
                Integer.parseInt(deviceUsageDays),Integer.parseInt(deviceConsumption), profileId );
        device.generateDevice(getApplicationContext(),deviceLayout);
        devices.add(device);
        device.deviceForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant animation = new Constant();
                animation.startAnimation(v,R.anim.blink,getApplicationContext());
                animation.startAnimation(device.deviceForm,R.anim.blink,getApplicationContext());

                Intent intent = new Intent(DevicesListActivity.this, NewDeviceActivity.class);
                intent.putExtra("KEY", EDIT_DEVICE);
                intent.putExtra("DEVICE_ID", String.valueOf(device.getId()));
                intent.putExtra("DEVICE_NAME", String.valueOf(device.getName()));
                intent.putExtra("DEVICE_QUANTITY", String.valueOf(device.getQuantity()));
                intent.putExtra("DEVICE_CONSUMPTION", String.valueOf(device.getPower()));
                intent.putExtra("DEVICE_USAGE_HOURS", String.valueOf(device.getHours()));
                intent.putExtra("DEVICE_USAGE_MINUTES", String.valueOf(device.getMinutes()));
                intent.putExtra("DEVICE_USAGE_DAYS", String.valueOf(device.getDays()));

                startActivityForResult(intent, EDIT_DEVICE_REQ_CODE);
                overridePendingTransition(R.anim.blink,0);
                Toast.makeText(DevicesListActivity.this, String.valueOf(device.getId()) + " " + device.getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateExistingDevice(Intent data) {

        if (data != null) {
            for (Device device: devices){
                if (device.getId() == data.getIntExtra("DEVICE_ID", -1)){
                    device.setName(data.getStringExtra("DEVICE_NAME"), data.getIntExtra("DEVICE_QUANTITY",-1) );
                    device.setPower(data.getIntExtra("DEVICE_CONSUMPTION", -1));
                    device.setTime(data.getIntExtra("DEVICE_USAGE_HOURS",-1),data.getIntExtra("DEVICE_USAGE_MINUTES",-1));
                    device.setDays(data.getIntExtra("DEVICE_USAGE_DAYS",-1));
                    break;
                }
            }
        } else {
            Toast.makeText(this,"Could not update the list pleas re-open Profile", Toast.LENGTH_LONG).show();
        }

    }

    public void deleteDevice(Intent receivedIntent) {
        for (Device device: devices){
            if (device.getId() == Integer.parseInt(receivedIntent.getStringExtra("DEVICE_ID"))){

                mDatabaseHelper = new DatabaseHelper(getApplicationContext());
                mDatabaseHelper.deleteDevice(device.getId());
                mDatabaseHelper.close();

                devices.remove(device);
                deviceLayout.removeView(device.getLayout());

            }
        }
    }

}
