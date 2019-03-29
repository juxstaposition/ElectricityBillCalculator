package advanced.android.ebcm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class NewProfileActivity extends AppCompatActivity implements View.OnClickListener{

    TextInputLayout profileNameInput, profileDescriptionInput, profilePriceInput;

    DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_profile);


        profileNameInput = findViewById(R.id.newProfileName);
        profileDescriptionInput = findViewById(R.id.newProfileDescription);
        profilePriceInput = findViewById(R.id.newProfilePrice);

        final String transferredData = getIntent().getStringExtra("KEY");


//        if (transferredData.equals(Constant.EDIT_PROFILE)) {
//            TextView profileTitle = findViewById(R.id.newProfileTitle);
//            profileTitle.setText(R.string.edit_profile);
//            //
//        }

        findViewById(R.id.buttonProfileAdd).setOnClickListener(this);
        findViewById(R.id.buttonProfileCancel).setOnClickListener(this);
        findViewById(R.id.back_view).setOnClickListener(this);

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,R.anim.fade);
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
                validation = sendWarningToast("Insert Profile Name!");
            }

            if (validation && profileDescription.length() == 0 ){
                validation = sendWarningToast("Insert Description Name!");
            }
            String checkPriceValue = profilePrice;
            if (validation && (profilePrice.length() == 0 || Float.valueOf(checkPriceValue.trim()).floatValue() <= 0 )){
                validation = sendWarningToast("Cost must be greater than 0!");
            }

            if (validation){

                returnIntent.putExtra("PROFILE_NAME",profileName);
                returnIntent.putExtra("PROFILE_PRICE",profilePrice);
                returnIntent.putExtra("PROFILE_DESCRIPTION",profileDescription);
                setResult(Activity.RESULT_OK, returnIntent);

                Log.d("PROFILE", profileName +", " + profileDescription + ", " + profilePrice);

                mDatabaseHelper = new DatabaseHelper(this);
                addProfileToDatabase(profileName, profileDescription, profilePrice);
                mDatabaseHelper.close();

                finish();
            }
        }
        else if (view.getId() == R.id.buttonProfileCancel || view.getId() == R.id.back_view){
            setResult(Activity.RESULT_CANCELED, returnIntent);
            finish();
        }
    }

    private boolean sendWarningToast(String message){
        Toast.makeText(NewProfileActivity.this, message, Toast.LENGTH_SHORT).show();
        return false;
    }

    private void addProfileToDatabase (String name, String description, String price) {
        boolean insertData = mDatabaseHelper.addProfileData(name,description,price);

        if (insertData) {
            toastMesasge("Profile Successfully Created");
        } else {
            toastMesasge("Something went wrong");
        }
    }

    private void updateProfile () {
        //update & delete SQLite test
//        DatabaseHelper mDatabaseHelper = new DatabaseHelper(context);
//
//        //update
//        mDatabaseHelper.updateProfile(getName(),getDescription(),getPrice(), getId(), getName());
//
//        //delete
//        mDatabaseHelper.deleteProfile(getId(),getName());
//        Toast.makeText(context,"removed " + getName() + " from database.", Toast.LENGTH_SHORT).show();
    }

    /**
     * customizable toast
     * @param message
     */
    private void toastMesasge(String message) {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}