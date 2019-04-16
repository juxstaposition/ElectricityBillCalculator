package advanced.android.ebcm;

import advanced.android.ebcm.Device.DevicesListActivity;
import advanced.android.ebcm.Profile.DeleteProfileActivity;
import advanced.android.ebcm.Profile.NewProfileActivity;
import advanced.android.ebcm.Profile.Profile;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.*;

import java.util.ArrayList;

import static advanced.android.ebcm.Constant.*;


public class MainActivity extends AppCompatActivity {

    private ArrayList<Profile> profiles = new ArrayList<>();
    LinearLayout myVerticalLayout = null;
    ArrayList<Integer> profileIds = new ArrayList<>();

    int profileIdToUpdate;

    DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myVerticalLayout = findViewById(R.id.profile_list);

        mDatabaseHelper = new DatabaseHelper(this);

        generateProfileView();

        FloatingActionButton fab = findViewById(R.id.fabNewProfile);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newProfileActivity = new Intent(MainActivity.this, NewProfileActivity.class);
                newProfileActivity.putExtra("KEY",Constant.NEW_PROFILE);
                startActivityForResult(newProfileActivity, CREATE_PROFILE_ACTIVITY_REQ_CODE);
                overridePendingTransition(R.anim.blink,0);
            }
        });

        mDatabaseHelper.close();

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateProfilePowerCostTime(profileIdToUpdate);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if ( id == R.id.action_help) {
            Intent intent = new Intent(getApplicationContext(), HelpInstructions.class);
           startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == DELETE_PROFILE_ACTIVITY_REQ_CODE && resultCode == Activity.RESULT_OK) {
            deleteProfile(profileIdToUpdate);
        }
        if (data != null) {
            Bundle mBundle = data.getExtras();
            
            if (requestCode == CREATE_PROFILE_ACTIVITY_REQ_CODE && resultCode == Activity.RESULT_OK) {
                addNewProfile();
            }
            if (requestCode == UPDATE_PROFILE_ACTIVITY_REQ_CODE && resultCode == Activity.RESULT_OK) {
                if (mBundle.getString("ACTION").equals(EDIT_PROFILE)) {
                    String name = mBundle.getString("PROFILE_NAME");
                    String description = mBundle.getString("PROFILE_DESCRIPTION");
                    Number price = mBundle.getFloat("PROFILE_PRICE");


                    Log.d("updateMAIN", "id " + profileIdToUpdate + ", name " + name + ", description " + description + ", price " + price);
                    updateProfileData(profileIdToUpdate, name, description, price);

                }
                if (mBundle.getString("ACTION").equals(DELETE_PROFILE)) {
                    Log.d("executed", "delete executed");
                    deleteProfile(profileIdToUpdate);
                }
            }
        }
    }

    private void updateProfilePowerCostTime(int id) {

        DatabaseHelper mDatabaseHelper = new DatabaseHelper(this);
        Cursor data = mDatabaseHelper.getProfileItemByID(id);

        if(data != null) {
            float power = -1;
            float cost = -1;
            String time = "00:00";
            if (data.moveToFirst() && data.getCount() != 0) {
                try{
                    power = data.getFloat(4);
                    cost = data.getFloat(5);
                    time = data.getString(6);


                } catch (Exception e){
                    System.out.println("could not update power/cost/time");
                }
                data.close();
            }

            for (Profile profile : profiles){
                if (profile.getId() == id){

                    profile.setPower(String.valueOf(power));
                    profile.setCost(String.valueOf(cost));
                    profile.setTime(time);

                    break;
                }
            }
        } else {
            System.out.println("no data from db");
        }

        mDatabaseHelper.close();
    }

    private void generateProfileView(){


        Cursor data = mDatabaseHelper.getProfileData();

        if (data != null) {
            if (data.moveToFirst() && data.getCount() >= 1) {
                do {

                    createNewProfile(data);

                } while (data.moveToNext());
                data.close();
            }
        } else {
            Toast.makeText(this, "Database is empty", Toast.LENGTH_SHORT).show();
        }

    }

    public void addNewProfile(){

        mDatabaseHelper = new DatabaseHelper(this);

        Cursor data = mDatabaseHelper.getProfileData();

        try {
            if (data != null) {
                if (data.moveToFirst() && data.getCount() >= 1) {
                    do {
                        profileIds.add(data.getInt(0));
                    } while (data.moveToNext());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            data.close();
        } finally {
            if (data != null) {
                data.close();
            }
        }

        int max = 0;

        for ( int i = 0; i < profileIds.size(); i++ ) {

            if ( max < profileIds.get(i) ){
                max = profileIds.get(i);
                System.out.println(max);
            }
        }
        Log.d("MAX_VALUE", Integer.toString(max));

        Cursor profileNEW = mDatabaseHelper.getProfileItemByID(max);

        try {
            if (profileNEW != null) {
                if (profileNEW.moveToFirst() && profileNEW.getCount() >= 1) {
                    do {

                        createNewProfile(profileNEW);

                    } while (profileNEW.moveToNext());
                }
            } else {
                Log.d("addingNewProfile", "profileNEW is empty");
            }
        } catch (Exception e) {
            e.printStackTrace();
            if(profileNEW != null) {
                profileNEW.close();
            }
        } finally {
            if (profileNEW != null) {
                profileNEW.close();

            }
        }



        mDatabaseHelper.close();
    }   // addNewProfile

    private void deleteProfile(int id){

        deleteDevices(id);

        for (Profile profile : profiles){
            if (profile.getId() == id){

                mDatabaseHelper = new DatabaseHelper(getApplicationContext());
                mDatabaseHelper.deleteProfile(profile.getId());
                mDatabaseHelper.close();

                profiles.remove(profile);
                myVerticalLayout.removeView(profile.getLayout());
            }
        }
    }

    private void createNewProfile(Cursor data){

        final int id = Integer.parseInt(data.getString(0));
        final String name = data.getString(1);
        String description = data.getString(2);
        String price = String.valueOf(data.getFloat(3));
        String power = String.valueOf(data.getFloat(4));
        String cost = String.valueOf(data.getFloat(5));
        String time = data.getString(6);



        final Profile profile = new Profile(id, name, description, price, power, cost, time);
        profiles.add(profile);
        profile.generateProfile(getApplicationContext(), myVerticalLayout);

        profile.clipDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DeleteProfileActivity.class);
                intent.putExtra("PROFILE_NAME", name);
                profileIdToUpdate = id;
                Log.d("main_activity", ""+profileIdToUpdate);
                startActivityForResult(intent, DELETE_PROFILE_ACTIVITY_REQ_CODE);
                overridePendingTransition(R.anim.blink,0);
        }});
        profile.profileForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant animation = new Constant();
                animation.startAnimation(v,R.anim.blink,getApplicationContext());
                animation.startAnimation(profile.clipDelete,R.anim.blink,getApplicationContext());

                profileIdToUpdate = profile.getId();
                Intent intent = new Intent(MainActivity.this, DevicesListActivity.class);
                intent.putExtra("KEY",Constant.PROFILE_DEVICES);
                intent.putExtra("PROFILE_ID",Integer.toString(id));
                startActivityForResult(intent, UPDATE_PROFILE_ACTIVITY_REQ_CODE);
            }
        });
        Log.d("profileCREATE", Integer.toString(id));
    }   // createNewProfile

    private void updateProfileData(int id, String name, String description, Number price) {
        for (Profile profile : profiles){
            if (profile.getId() == id){

                profile.setName(name);
                profile.setDescription(description);
                profile.setPrice(String.valueOf(price));

                break;
            }
        }
    }

    private void deleteDevices(int profileParent) {
        DatabaseHelper mDatabaseHelper = new DatabaseHelper(this);
        Cursor data = mDatabaseHelper.getDeviceData(profileParent);

        if (data != null) {
            if (data.moveToFirst() && data.getCount() >= 1) {
                do {

                    mDatabaseHelper.deleteDevice(data.getInt(0));
                    Toast.makeText(this,"Deleted device with id "+ data.getInt(0), Toast.LENGTH_LONG).show();

                } while (data.moveToNext());
                data.close();
            } else {
                data.close();
            }
        }
        mDatabaseHelper.close();
    }

    public Profile getProfiles (int id){
        for (Profile temp : profiles){
            if (temp.getId() == id){
                return temp;
            }
        }
        return null;
    }

}   // MainActivity
