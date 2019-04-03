package advanced.android.ebcm.Profile;

import advanced.android.ebcm.Constant;
import advanced.android.ebcm.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


public class DeleteProfileActivity extends AppCompatActivity implements View.OnClickListener {

    TextView btnCancel, btnConfirm, profileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_profile_confirmation_layout);

        profileName = findViewById(R.id.delete_profile_name);
        btnCancel = findViewById(R.id.buttonDeleteProfileCancel);
        btnConfirm = findViewById(R.id.buttonDeleteProfileConfirm);

        profileName.setText(getIntent().getStringExtra("PROFILE_NAME"));

        findViewById(R.id.buttonDeleteProfileCancel).setOnClickListener(this);
        findViewById(R.id.buttonDeleteProfileConfirm).setOnClickListener(this);
        findViewById(R.id.back_view_delete_profile).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        Intent returnIntent = new Intent();

        Constant animation = new Constant();
        animation.startAnimation(v,R.anim.blink,getApplicationContext());

        if (v.getId() == R.id.buttonDeleteProfileConfirm){
            setResult(Activity.RESULT_OK, returnIntent);
            finish();

            Log.d("delete", "confirm pressed");
        }
        else if (v.getId() == R.id.buttonDeleteProfileCancel || v.getId() == R.id.back_view_delete_profile){
            setResult(Activity.RESULT_CANCELED, returnIntent);
            finish();

            Log.d("delete", "cancel pressed");
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,R.anim.fade);
    }

}
