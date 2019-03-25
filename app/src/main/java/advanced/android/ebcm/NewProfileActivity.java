package advanced.android.ebcm;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

public class NewProfileActivity extends AppCompatActivity implements View.OnClickListener{

    TextInputLayout profileNameInput = null, profileDescriptionInput = null, profileCostInput = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_profile);


        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

//        getWindow().setLayout( width*8/10,height*8/10 );

        profileNameInput = findViewById(R.id.newProfileName);
        profileDescriptionInput = findViewById(R.id.newProfileDescription);
        profileCostInput = findViewById(R.id.newProfileCost);

        findViewById(R.id.buttonProfileAdd).setOnClickListener(this);
        findViewById(R.id.buttonProfileCancel).setOnClickListener(this);
        findViewById(R.id.back_view).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.buttonProfileAdd){
            boolean validation = true;

            String  profileName = profileNameInput.getEditText().getText().toString(),
                    profileDescription = profileDescriptionInput.getEditText().getText().toString(),
                    profileCost = profileCostInput.getEditText().getText().toString();

            if (profileName.length() == 0){
                Toast.makeText(NewProfileActivity.this, "Insert Profile Name!", Toast.LENGTH_SHORT).show();
                validation = false;
            }

            if (validation && profileDescription.length() == 0 ){
                Toast.makeText(NewProfileActivity.this, "Insert Description Name!", Toast.LENGTH_SHORT).show();
                validation = false;
            }

            if (validation && (profileCost.length() == 0 || Integer.parseInt(profileCost) <= 0 )){
                Toast.makeText(NewProfileActivity.this, "Cost must be greater than 0!", Toast.LENGTH_SHORT).show();
                validation = false;
            }

            System.out.println("Profile Name: "        + profileName);
            System.out.println("Profile Description: " + profileDescription);
            System.out.println("Profile Cost: "        + profileCost);

            if (validation){
                Toast.makeText(NewProfileActivity.this, "New Profile Created!", Toast.LENGTH_LONG).show();
                super.onBackPressed();
            }
        }
        else if (view.getId() == R.id.buttonProfileCancel || view.getId() == R.id.back_view){
            super.onBackPressed();
        }

    }

}
