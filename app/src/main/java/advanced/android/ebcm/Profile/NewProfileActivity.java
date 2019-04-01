package advanced.android.ebcm.Profile;

import advanced.android.ebcm.Constant;
import advanced.android.ebcm.DatabaseHelper;
import advanced.android.ebcm.R;
import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class NewProfileActivity extends AppCompatActivity implements View.OnClickListener{

    TextInputLayout profileNameInput, profileDescriptionInput, profilePriceInput;

    DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_profile);


        profileNameInput = findViewById(R.id.editProfileName);
        profileDescriptionInput = findViewById(R.id.editProfileDescription);
        profilePriceInput = findViewById(R.id.editProfilePrice);

        //final String transferredData = getIntent().getStringExtra("KEY");
        //System.out.print(transferredData);

//        if (transferredData.equals(Constant.EDIT_PROFILE)) {
//            TextView profileTitle = findViewById(R.id.newProfileTitle);
//            profileTitle.setText(R.string.edit_profile);
//            //
//        }

        findViewById(R.id.buttonProfileAdd).setOnClickListener(this);
        findViewById(R.id.buttonDeleteProfileCancel).setOnClickListener(this);
        findViewById(R.id.back_view_edit_profile).setOnClickListener(this);

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
                TextInputLayout newProfileName = findViewById(R.id.editProfileName);
                newProfileName.setErrorEnabled(true);
                newProfileName.setError(getString(R.string.add));
            }

            if (validation && profileDescription.length() == 0 ){
                validation = sendWarningToast("Insert Description Name!");
            }

            if (validation && (profilePrice.length() == 0 || Float.valueOf(profilePrice.trim()) <= 0 )){
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
        else if (view.getId() == R.id.buttonDeleteProfileCancel || view.getId() == R.id.back_view_edit_profile){
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
            toastMessage("Profile Successfully Created");
        } else {
            toastMessage("Something went wrong");
        }
    }

    /**
     * customizable toast
     * @param message string
     */
    private void toastMessage(String message) {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}