package advanced.android.ebcm.Profile;

import advanced.android.ebcm.Essentials.Constant;
import advanced.android.ebcm.R;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;


public class DeleteProfileActivity extends AppCompatActivity implements View.OnClickListener {

    TextView btnCancel, btnConfirm, profileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_confirmation_layout);

        profileName = findViewById(R.id.delete_profile_name);
        btnCancel = findViewById(R.id.buttonDeleteCancel);
        btnConfirm = findViewById(R.id.buttonDeleteConfirm);

        profileName.setText(getIntent().getStringExtra("PROFILE_NAME"));

        findViewById(R.id.buttonDeleteCancel).setOnClickListener(this);
        findViewById(R.id.buttonDeleteConfirm).setOnClickListener(this);
        findViewById(R.id.back_view_delete).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {


        Constant animation = new Constant();
        animation.startAnimation(v,R.anim.blink,getApplicationContext());

        if (v.getId() == R.id.buttonDeleteConfirm){

            setResult(Activity.RESULT_OK);
            finish();
        }
        else if (v.getId() == R.id.buttonDeleteCancel || v.getId() == R.id.back_view_delete){

            setResult(Activity.RESULT_CANCELED);
            finish();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,R.anim.fade);
    }
}
