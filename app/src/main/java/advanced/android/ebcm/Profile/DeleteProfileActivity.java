package advanced.android.ebcm.Profile;

import advanced.android.ebcm.Constant;
import advanced.android.ebcm.DatabaseHelper;
import advanced.android.ebcm.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class DeleteProfileActivity extends AppCompatActivity implements View.OnClickListener {

    TextView btnCancel, btnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_profile_confirmation_layout);

        btnCancel = findViewById(R.id.buttonDeleteProfileCancel);
        btnConfirm = findViewById(R.id.buttonDeleteProfileConfirm);

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
            deleteProfile();
            finish();

            Log.d("delete", "confirm pressed");
        }
        else if (v.getId() == R.id.buttonDeleteProfileCancel || v.getId() == R.id.back_view_delete_profile){
            setResult(Activity.RESULT_CANCELED, returnIntent);
            finish();

            Log.d("delete", "cancel pressed");
        }
    }

    private void deleteProfile () {
        int id = Integer.parseInt(getIntent().getStringExtra("PROFILE_ID"));

        DatabaseHelper mDatabaseHelper = new DatabaseHelper(this);
        mDatabaseHelper.deleteProfile(id);
        Toast.makeText(this,"removed id "+ id +" from database.", Toast.LENGTH_SHORT).show();
    }
}
