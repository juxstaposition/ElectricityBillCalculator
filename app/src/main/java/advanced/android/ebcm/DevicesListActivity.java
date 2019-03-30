package advanced.android.ebcm;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

public class DevicesListActivity extends AppCompatActivity {

    DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final String transferredData = getIntent().getStringExtra("KEY");

        if (transferredData.equals(Constant.PROFILE_DEVICES)){
            mDatabaseHelper = new DatabaseHelper(this);
            System.out.println("______________________________________");
            System.out.println(getIntent().getStringExtra("PROFILE_ID"));
            Cursor profile = mDatabaseHelper.getProfileItemByID(getIntent().getStringExtra("PROFILE_ID"));

            if (profile != null) {
                if (profile.moveToFirst() && profile.getCount() >= 1) {
                    do {

                        String profileName = profile.getString(1);
                        setTitle(profileName);

                    } while (profile.moveToNext());
                }
            } else {
                Log.d("addingNewProfile", "profileNEW is empty");
            }

        }

        FloatingActionButton fab = findViewById(R.id.fabNewProfile);
        fab.setOnClickListener(new View.OnClickListener() {
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
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_right_exit);
    }

}
