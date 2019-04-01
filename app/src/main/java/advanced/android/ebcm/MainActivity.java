package advanced.android.ebcm;

import advanced.android.ebcm.Device.DevicesListActivity;
import advanced.android.ebcm.Profile.NewProfileActivity;
import advanced.android.ebcm.Profile.Profile;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

        generateProfileView();

        /*
         * Test
         */
        final ImageView clipdel = findViewById(R.id.deleteClipArt);

        LinearLayout test = findViewById(R.id.layoutProfile);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant animation = new Constant();
                animation.startAnimation(v,R.anim.blink,getApplicationContext());
                animation.startAnimation(clipdel,R.anim.blink,getApplicationContext());
            }
        });

        clipdel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Example Delete Clicked!", Toast.LENGTH_SHORT).show();
            }
        });

        /*
         *  Test end
         */

        FloatingActionButton fab = findViewById(R.id.fabNewProfile);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newProfileActivity = new Intent(MainActivity.this, NewProfileActivity.class);
                startActivityForResult(newProfileActivity, CREATE_PROFILE_ACTIVITY_REQ_CODE);
                overridePendingTransition(R.anim.blink,0);
            }
        });

        FloatingActionButton fabCalc = findViewById(R.id.fabCalculateProfile);
        fabCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    addNewProfile();
            }
        });
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

        //noinspection SimplifiableIfStatement
        if ( id == R.id.action_devices){
            Intent devicesListActivity = new Intent(MainActivity.this, DevicesListActivity.class);
            devicesListActivity.putExtra("KEY",Constant.FAVOURITE_DEVICE);
            startActivityForResult(devicesListActivity, UPDATE_PROFILE_ACTIVITY_REQ_CODE);
            return true;
        }
//        else if ( id == R.id.action_devices){
//            Intent devicesListActivity = new Intent(MainActivity.this, DevicesListActivity.class);
//            devicesListActivity.putExtra("KEY",Constant.FAVOURITE_DEVICE);
//            startActivity(devicesListActivity);
//            return true;
//        }
        else if ( id == R.id.action_help){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CREATE_PROFILE_ACTIVITY_REQ_CODE && resultCode == Activity.RESULT_OK) {
            addNewProfile();
        }

        else if ( requestCode == UPDATE_PROFILE_ACTIVITY_REQ_CODE && resultCode == Activity.RESULT_OK ) {

            int id = Integer.parseInt(getIntent().getStringExtra("PROFILE_ID"));
            String name = getIntent().getStringExtra("PROFILE_NAME");
            String description = getIntent().getStringExtra("PROFILE_DESCRIPTION");
            Number price = Float.valueOf("PROFILE_PRICE");
            updateProfileData(id, name, description, price);
            Log.d("update_req", "true");
        }
    }

    private void generateProfileView(){

        mDatabaseHelper = new DatabaseHelper(this);

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
        mDatabaseHelper.close();

    }

    public void addNewProfile(){

        mDatabaseHelper = new DatabaseHelper(this);

        Cursor data = mDatabaseHelper.getProfileData();

        if (data != null) {
            if (data.moveToFirst() && data.getCount() >= 1) {
                do {
                    profileIds.add(data.getInt(0));
                } while (data.moveToNext());
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

        if (profileNEW != null) {
            if (profileNEW.moveToFirst() && profileNEW.getCount() >= 1) {
                do {

                    createNewProfile(profileNEW);

                } while (profileNEW.moveToNext());
            }
        } else {
            Log.d("addingNewProfile", "profileNEW is empty");
        }

        mDatabaseHelper.close();
    }   // addNewProfile

    private void deleteProfile(int id){
        for (Profile profile : profiles){
            if (profile.getId() == id){
                profiles.remove(profile);

                mDatabaseHelper = new DatabaseHelper(getApplicationContext());
                mDatabaseHelper.deleteProfile(profile.getId());
                mDatabaseHelper.close();
                break;
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
                Toast.makeText(MainActivity.this, profile.getName() + " deleted", Toast.LENGTH_SHORT).show();
                myVerticalLayout.removeView(profile.getLayout());

                deleteProfile(profile.getId());

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
                startActivity(intent);
            }
        });
        Log.d("profileCREATE", Integer.toString(id));
    }   // createNewProfile

    private void updateProfileData(int id, String name, String description, Number price) {

    }

}   // MainActivity
