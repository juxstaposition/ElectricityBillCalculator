package advanced.android.ebcm;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class DevicesListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final String transferredData = getIntent().getStringExtra("KEY");


        FloatingActionButton fab = findViewById(R.id.fabNewProfile);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newItemActivity = new Intent(DevicesListActivity.this, NewDeviceActivity.class);
                newItemActivity.putExtra("KEY", transferredData);
                startActivity(newItemActivity);
            }
        });
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_right_exit);
    }

}
