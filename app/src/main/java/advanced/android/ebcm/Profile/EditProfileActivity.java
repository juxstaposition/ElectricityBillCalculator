package advanced.android.ebcm.Profile;

import advanced.android.ebcm.Constant;
import advanced.android.ebcm.DatabaseHelper;
import advanced.android.ebcm.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class EditProfileActivity  extends AppCompatActivity implements View.OnClickListener  {

    TextInputLayout profileNameInput, profileDescriptionInput, profilePriceInput;
    TextInputEditText name, description, price;

    DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);





        profileNameInput = findViewById(R.id.editProfileName);
        name = findViewById(R.id.edit_profile_name);
        name.setText(getIntent().getStringExtra("PROFILE_NAME"));

        profileDescriptionInput = findViewById(R.id.editProfileDescription);
        description = findViewById(R.id.edit_profile_description);
        description.setText(getIntent().getStringExtra("PROFILE_DESCRIPTION"));

        profilePriceInput = findViewById(R.id.editProfilePrice);
        price = findViewById(R.id.edit_profile_price);
        price.setText(getIntent().getStringExtra("PROFILE_PRICE"));


        findViewById(R.id.buttonEditProfileCancel).setOnClickListener(this);
        findViewById(R.id.buttonEditProfileConfirm).setOnClickListener(this);
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

        if (view.getId() == R.id.buttonEditProfileConfirm){



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

            if (validation && (profilePrice.length() == 0 || Float.valueOf(profilePrice) <= 0 )){
                validation = sendWarningToast("Cost must be greater than 0!");
            }

            if (validation){

                returnIntent.putExtra("PROFILE_NAME",profileName);
                returnIntent.putExtra("PROFILE_PRICE",profilePrice);
                returnIntent.putExtra("PROFILE_DESCRIPTION",profileDescription);
                setResult(Activity.RESULT_OK, returnIntent);

                Log.d("PROFILE", profileName +", " + profileDescription + ", " + profilePrice);
                updateProfile();
                finish();
            }
        }
        else if ( view.getId() == R.id.buttonEditProfileCancel || view.getId() == R.id.back_view_edit_profile  ){
            setResult(Activity.RESULT_CANCELED, returnIntent);
            finish();
        }

    }

    private boolean sendWarningToast(String message){
        Toast.makeText(EditProfileActivity.this, message, Toast.LENGTH_SHORT).show();
        return false;
    }


    private void updateProfile () {

        Intent receivedIntent = getIntent();

        TextView name = findViewById(R.id.edit_profile_name);
        TextView description = findViewById(R.id.edit_profile_description);
        TextView price = findViewById(R.id.edit_profile_price);

        int id = Integer.parseInt(receivedIntent.getStringExtra("PROFILE_ID"));

        String newName = name.getText().toString();
        String newDescription = description.getText().toString();
        String newPrice = price.getText().toString();


        DatabaseHelper mDatabaseHelper = new DatabaseHelper(this);

        mDatabaseHelper.updateProfile(newName,newDescription,newPrice, id);

        mDatabaseHelper.close();

        toastMesasge("Profile data updated");
    }

    /**
     * customizable toast
     * @param message string
     */
    private void toastMesasge(String message) {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }


}
