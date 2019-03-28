package advanced.android.ebcm;

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


public class MainActivity extends AppCompatActivity {

    private ArrayList<Profile> profiles = new ArrayList<>();
    LinearLayout myVerticalLayout = null;
    private final int CREATE_PROFILE_ACTIVITY_REQ_CODE = 372;

    private static final String TAG = "MainActivity";

    DatabaseHelper mDatabaseHelper;


    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDatabaseHelper = new DatabaseHelper(this);
        myVerticalLayout = findViewById(R.id.profile_list);

        generateProfileView();

        /**
         * Test
         */
        LinearLayout test = findViewById(R.id.layoutProfile);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Panel Clicked!", Toast.LENGTH_SHORT).show();
            }
        });

        ImageView clipdel = findViewById(R.id.deleteClipArt);
        clipdel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Example Delete Clicked!", Toast.LENGTH_SHORT).show();
            }
        });
        /**
         *  Test end
         */

        FloatingActionButton fab = findViewById(R.id.fabNewProfile);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newProfileActivity = new Intent(MainActivity.this, NewProfileActivity.class);
                startActivityForResult(newProfileActivity,CREATE_PROFILE_ACTIVITY_REQ_CODE);
                overridePendingTransition(R.anim.blink,0);
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        LinearLayout profileLayout = findViewById(R.id.profile_list);
        profileLayout.removeAllViews();
        generateProfileView();
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
            startActivity(devicesListActivity);
            return true;
        }
        else if ( id == R.id.action_help){
            return true;
        }
        else if ( id == R.id.action_devices){
            Intent devicesListActivity = new Intent(MainActivity.this, DevicesListActivity.class);
            devicesListActivity.putExtra("KEY",Constant.FAVOURITE_DEVICE);
            startActivity(devicesListActivity);
            return true;
        }
        else if ( id == R.id.action_help){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CREATE_PROFILE_ACTIVITY_REQ_CODE && resultCode == Activity.RESULT_OK) {
            String profileName = data.getStringExtra("PROFILE_NAME");
            String profilePrice = data.getStringExtra("PROFILE_PRICE");
            String profileDescription = data.getStringExtra("PROFILE_NAME");

//            generateNewProfile(profileName,profileDescription,profilePrice);
        }
    }

    private void generateProfileView(){


        Cursor data = mDatabaseHelper.getProfileData();

        if (data != null) {
            if (data.moveToFirst() && data.getCount() > 1) {
                do {
                    i = Integer.parseInt(data.getString(0));
                    String name = data.getString(1);
                    String description = data.getString(2);
                    String price = String.valueOf(data.getFloat(3));

                    final Profile profile = new Profile(i,name,description,price);
                    profiles.add(profile);
                    profile.generateProfile(getApplicationContext(), myVerticalLayout);
                    ///*        test.clipDelete.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //                Toast.makeText(MainActivity.this, test.getName(), Toast.LENGTH_SHORT).show();
        //                myVerticalLayout.removeView(test.profileForm);
        //                profiles.remove(test);
        //            }
        //              });*/
                    Log.d("profileCREATE", Integer.toString(i));

                } while (data.moveToNext());
            }
        } else {
            Toast.makeText(this, "Database is empty", Toast.LENGTH_SHORT);
        }

    }

    private void generateNewProfile(String name, String description, String price) {


        Cursor data = mDatabaseHelper.getProfileItemID(name, description);

        if (data != null) {

            if (data.moveToFirst() && data.getCount() >= 1) {
                do {
                    i = Integer.parseInt(data.getString(0));
                    Log.d("profile", "got here");

                } while (data.moveToNext());
            }

        } else {
            Toast.makeText(this, "There is no ID found for profile", Toast.LENGTH_SHORT);
        }


        final Profile test = new Profile(i, name, description, price);
        profiles.add(test);
        test.generateProfile(getApplicationContext(), myVerticalLayout);
///*        test.clipDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, test.getName(), Toast.LENGTH_SHORT).show();
//                myVerticalLayout.removeView(test.profileForm);
//                profiles.remove(test);
//            }
//        });*/


    }
}
