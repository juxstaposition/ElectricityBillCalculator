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

import static advanced.android.ebcm.Constant.CREATE_PROFILE_ACTIVITY_REQ_CODE;
import static advanced.android.ebcm.Constant.DELETE_PROFILE_ACTIVITY_REQ_CODE;
import static advanced.android.ebcm.Constant.UPDATE_PROFILE_ACTIVITY_REQ_CODE;


public class MainActivity extends AppCompatActivity {

    private ArrayList<Profile> profiles = new ArrayList<>();
    LinearLayout myVerticalLayout = null;
    ArrayList<Integer> profileIds = new ArrayList<>();

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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bundle mBundle = data.getExtras();

        if (requestCode == CREATE_PROFILE_ACTIVITY_REQ_CODE && resultCode == Activity.RESULT_OK) {
            addNewProfile();
        }

        if (requestCode == DELETE_PROFILE_ACTIVITY_REQ_CODE && resultCode == Activity.RESULT_OK) {
            if (mBundle != null) {
                deleteProfile(Integer.parseInt(mBundle.getString("PROFILE_ID")));
                Log.d("MainActivity/Delete", ""+mBundle.getInt("PROFILE_ID"));
            }
        }
        else if ( requestCode == UPDATE_PROFILE_ACTIVITY_REQ_CODE && resultCode == Activity.RESULT_OK ) {

            if (mBundle != null) {
                if (mBundle.getString("ACTION").equals("UPDATE") ) {
                    int id = mBundle.getInt("PROFILE_ID");
                    String name = mBundle.getString("PROFILE_NAME");
                    String description = mBundle.getString("PROFILE_DESCRIPTION");
                    Number price = mBundle.getFloat("PROFILE_PRICE");
                    Log.d("updateMAIN", "id " + id + ", name " +name+", description " + description + ", price " + price );

                    updateProfileData(id, name, description, price);
                }
                if (mBundle.getString("ACTION").equals("DELETE")) {
                    deleteProfile(mBundle.getInt("PROFILE_ID"));
                }
            }
        }
    }

    private void generateProfileView(){


        Cursor data = mDatabaseHelper.getProfileData();

        if (data != null) {
            if (data.moveToFirst() && data.getCount() >= 1) {
                do {

                    createNewProfile(data);

                } while (data.moveToNext());
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



        final Profile profile = new Profile(id,name,description,price);
        profiles.add(profile);
        profile.generateProfile(getApplicationContext(), myVerticalLayout);

        profile.clipDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DeleteProfileActivity.class);
                intent.putExtra("PROFILE_NAME", name);
                intent.putExtra("PROFILE_ID", Integer.toString(id));
                startActivityForResult(intent, DELETE_PROFILE_ACTIVITY_REQ_CODE);
                overridePendingTransition(R.anim.blink,0);
        }});
        profile.profileForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant animation = new Constant();
                animation.startAnimation(v,R.anim.blink,getApplicationContext());
                animation.startAnimation(profile.clipDelete,R.anim.blink,getApplicationContext());

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

                    deleteProfile(data.getInt(0));
                    Toast.makeText(this,"Deleted device with id "+ data.getInt(0), Toast.LENGTH_LONG).show();

                } while (data.moveToNext());
            }
        } else {
            Toast.makeText(this, "No devices to delete with DB", Toast.LENGTH_SHORT).show();
        }
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
