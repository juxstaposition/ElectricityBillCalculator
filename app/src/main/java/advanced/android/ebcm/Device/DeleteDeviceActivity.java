package advanced.android.ebcm.Device;

import advanced.android.ebcm.Constant;
import advanced.android.ebcm.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class DeleteDeviceActivity  extends AppCompatActivity implements View.OnClickListener {

    TextView btnCancel, btnConfirm, deviceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_confirmation_layout);

        deviceName = findViewById(R.id.delete_profile_name);
        btnCancel = findViewById(R.id.buttonDeleteCancel);
        btnConfirm = findViewById(R.id.buttonDeleteConfirm);

        deviceName.setText(getIntent().getStringExtra("DEVICE_NAME"));

        findViewById(R.id.buttonDeleteCancel).setOnClickListener(this);
        findViewById(R.id.buttonDeleteConfirm).setOnClickListener(this);
        findViewById(R.id.back_view_delete).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        Intent returnIntent = new Intent();

        Constant animation = new Constant();
        animation.startAnimation(v,R.anim.blink,getApplicationContext());

        if (v.getId() == R.id.buttonDeleteConfirm){

            returnIntent.putExtra("DEVICE_ID", getIntent().getStringExtra("DEVICE_ID"));
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
        else if (v.getId() == R.id.buttonDeleteCancel || v.getId() == R.id.back_view_delete){

            setResult(Activity.RESULT_CANCELED, returnIntent);
            finish();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,R.anim.fade);
    }

}
