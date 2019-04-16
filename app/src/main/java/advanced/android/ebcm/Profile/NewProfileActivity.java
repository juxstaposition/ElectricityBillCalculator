package advanced.android.ebcm.Profile;

import advanced.android.ebcm.Constant;
import advanced.android.ebcm.DatabaseHelper;
import advanced.android.ebcm.R;
import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class NewProfileActivity extends AppCompatActivity implements View.OnClickListener{

    TextInputLayout profileNameInput, profileDescriptionInput, profilePriceInput;
    TextInputEditText name, description, price;

    boolean newProfile = true;

    DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_profile);


        profileNameInput = findViewById(R.id.editProfileName);
        profileDescriptionInput = findViewById(R.id.editProfileDescription);
        profilePriceInput = findViewById(R.id.editProfilePrice);

        final String transferredData = getIntent().getStringExtra("KEY");
        System.out.print(transferredData);

        if (transferredData.equals(Constant.EDIT_PROFILE)) {
            newProfile = false;

            TextView profileTitle = findViewById(R.id.newProfileTitle);
            profileTitle.setText(R.string.edit_profile);

            TextView buttonAdd = findViewById(R.id.buttonProfileAdd);
            buttonAdd.setText(R.string.confirm);

            name = findViewById(R.id.edit_profile_name);
            name.setText(getIntent().getStringExtra("PROFILE_NAME"));

            description = findViewById(R.id.edit_profile_description);
            description.setText(getIntent().getStringExtra("PROFILE_DESCRIPTION"));

            price = findViewById(R.id.edit_profile_price);
            price.setText(getIntent().getStringExtra("PROFILE_PRICE"));
        }

        findViewById(R.id.buttonProfileAdd).setOnClickListener(this);
        findViewById(R.id.buttonDeleteCancel).setOnClickListener(this);
        findViewById(R.id.back_view_edit_profile).setOnClickListener(this);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,R.anim.fade);
    }

    @Override
    public void onBackPressed(){

        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

    @Override
    public void onClick(View view) {

        Intent returnIntent = new Intent();

        Constant animation = new Constant();
        animation.startAnimation(view,R.anim.blink,getApplicationContext());

        if (view.getId() == R.id.buttonProfileAdd){

            boolean validation = true;

            String  profileName = profileNameInput.getEditText().getText().toString(),
                    profileDescription = profileDescriptionInput.getEditText().getText().toString(),
                    profilePrice = profilePriceInput.getEditText().getText().toString();

            if (profileName.length() == 0){
                validation = sendWarningToast("Please insert profile N=name!");
            }

            if (validation && profileDescription.length() == 0 ){
                profileDescription = "";
            }

            if (validation && (profilePrice.length() == 0 || Float.valueOf(profilePrice.trim()) <= 0 )){
                validation = sendWarningToast("Price per kWh must be greater than 0!");
            }

            if (profilePrice.length() > 6){
                validation = sendWarningToast("Price value is too long!\nUse less characters");
            }

            if (validation){

                returnIntent.putExtra("PROFILE_NAME",profileName);
                returnIntent.putExtra("PROFILE_PRICE",profilePrice);
                returnIntent.putExtra("PROFILE_DESCRIPTION",profileDescription);
                returnIntent.putExtra("PROFILE_POWER", 0);
                returnIntent.putExtra("PROFILE_COST", 0);
                Log.d("PROFILE", profileName +", " + profileDescription + ", " + profilePrice);

                if (newProfile) {
                    mDatabaseHelper = new DatabaseHelper(this);
                    addProfileToDatabase(profileName, profileDescription, profilePrice);
                    mDatabaseHelper.close();
                }
                else {
                    updateProfile();
                }

                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        }
        else if (view.getId() == R.id.buttonDeleteCancel || view.getId() == R.id.back_view_edit_profile){
            setResult(Activity.RESULT_CANCELED, returnIntent);
            finish();
        }
    }


    //      !!!  power and cost added on database side (inside DatabaseHelper)
    private void addProfileToDatabase (String name, String description, String price) {

        mDatabaseHelper.addProfileData(name,description,price);
    }

    private void updateProfile () {

        Intent receivedIntent = getIntent();

        TextView name = findViewById(R.id.edit_profile_name);
        TextView description = findViewById(R.id.edit_profile_description);
        TextView price = findViewById(R.id.edit_profile_price);


        int id = receivedIntent.getIntExtra("PROFILE_ID", -1);


        String newName = name.getText().toString();
        String newDescription = description.getText().toString();
        String newPrice = price.getText().toString();

        DatabaseHelper mDatabaseHelper = new DatabaseHelper(this);
        mDatabaseHelper.updateProfileMain(newName,newDescription,newPrice, id);
        mDatabaseHelper.close();
    }

    private boolean sendWarningToast(String message){
        Toast.makeText(NewProfileActivity.this, message, Toast.LENGTH_SHORT).show();
        return false;
    }
}