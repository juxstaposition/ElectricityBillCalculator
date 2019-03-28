package advanced.android.ebcm;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myVerticalLayout = findViewById(R.id.profile_list);

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
                overridePendingTransition(R.anim.blink,R.anim.slide_right_exit);
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

            addNewProfile(profileName,profileDescription,profilePrice);
        }
    }

    private void addNewProfile(String name, String description, String price){

        final Profile test = new Profile(i,name,description,price);
        i++;
        profiles.add(test);
        test.generateProfile(getApplicationContext(), myVerticalLayout);
/*        test.clipDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, test.getName(), Toast.LENGTH_SHORT).show();
                myVerticalLayout.removeView(test.profileForm);
                profiles.remove(test);
            }
        });*/

    }


}
